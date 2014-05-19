/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef.dial;

import coffeepot.pos.tef.dial.dispatch.ADM;
import coffeepot.pos.tef.dial.dispatch.ATV;
import coffeepot.pos.tef.dial.dispatch.CHQ;
import coffeepot.pos.tef.dial.dispatch.CNC;
import coffeepot.pos.tef.dial.dispatch.CNF;
import coffeepot.pos.tef.dial.dispatch.CRT;
import coffeepot.pos.tef.dial.dispatch.DispatchMessage;
import coffeepot.pos.tef.dial.dispatch.NCN;
import coffeepot.pos.tef.dial.response.ResponseMessage;
import coffeepot.pos.tef.dial.response.ResponseReader;
import coffeepot.pos.tef.dial.response.StatusResponse;
import coffeepot.pos.tef.misc.NotRespondingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class TefDial {

    private File requestDirectory;
    private File responseDirectory;
    private File statusResponseFile;
    private File responseFile;
    private static boolean waitingResponse = false;
    private String automationVersion;
    private String softwareHouse;

    private int getNewIdentifier() {
        return DateTime.now().getMillisOfDay();
    }

    private StatusResponse getStatusResponse() throws IOException {
        StatusResponse result = null;
        if (getStatusResponseFile().exists()) {
            result = ResponseReader.getStatusResponse(getStatusResponseFile());
            statusResponseFile.delete();
        } else {
            // verificar a cada 250 milissegundos. durante 7 segundos.
            for (int i = 0; i < 28; i++) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TefDial.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (statusResponseFile.exists()) {
                    result = ResponseReader.getStatusResponse(getStatusResponseFile());
                    statusResponseFile.delete();
                    break;
                }
            }
        }
        return result;
    }

    private void checkIfIsActive() throws IOException {
        if (!isActive()) {
            throw new IllegalStateException("TEF não responde");
        }
    }

    private void checkIfWaitingResponse() {
        if (waitingResponse) {
            throw new IllegalStateException("Requisição anterior não concluida");
        }
    }

    /**
     * Verifica se o gerenciador padrão está ativo.
     */
    public boolean isActive() throws IOException {
        ATV atv = new ATV();
        atv.setIdentifier(getNewIdentifier());
        StatusResponse sts;
        try {
            sts = dispatchMessage2(atv);
        } catch (NotRespondingException ex) {
            return false;
        }
        return sts != null;
    }

    /**
     * Aciona a Solução TEF Discado para execução das funções administrativas.
     *
     * @return
     * @throws IOException
     * @throws NotRespondingException
     */
    public ResponseMessage adm() throws IOException, NotRespondingException {
        ADM msg = new ADM();
        return dispatchMessage(msg);
    }

    /**
     * Pedido de autorização para transação por meio de cheque.
     *
     * @param msg Mensagem com instruções para o Gerenciador Padrão.
     * @return
     * @throws IOException
     * @throws NotRespondingException
     */
    public ResponseMessage chq(CHQ msg) throws IOException, NotRespondingException {
        return dispatchMessage(msg);
    }

    /**
     * Pedido de autorização para transação por meio de cartão.
     *
     * @param msg Mensagem com instruções para o Gerenciador Padrão.
     * @return
     * @throws IOException
     * @throws NotRespondingException
     */
    public ResponseMessage crt(CRT msg) throws IOException, NotRespondingException {
        return dispatchMessage(msg);
    }

    /**
     * Confirmação da venda e impressão de cupom.
     *
     * @param msg Mensagem com instruções para o Gerenciador Padrão.
     * @return
     * @throws IOException
     * @throws NotRespondingException
     */
    public StatusResponse cnf(CNF msg) throws IOException, NotRespondingException {
        return dispatchMessage2(msg);
    }

    /**
     * Não confirmação da venda e/ou da impressão.
     *
     * @param msg Mensagem com instruções para o Gerenciador Padrão.
     * @return
     * @throws IOException
     * @throws NotRespondingException
     */
    public StatusResponse ncn(NCN msg) throws IOException, NotRespondingException {
        return dispatchMessage2(msg);
    }

    /**
     * Cancelamento de venda efetuada por qualquer meio de pagamento.
     *
     * @param msg Mensagem com instruções para o Gerenciador Padrão.
     * @return
     * @throws IOException
     * @throws NotRespondingException
     */
    public StatusResponse cnc(CNC msg) throws IOException, NotRespondingException {
        return dispatchMessage2(msg);
    }

    /**
     * Envia mensagem para o GP, verifica o recebimento da solicitação pelo GP e
     * aguarda a mensagem de resposta do GP.
     *
     * @param msg
     * @return
     * @throws IOException
     * @throws NotRespondingException
     */
    private ResponseMessage dispatchMessage(DispatchMessage msg) throws IOException, NotRespondingException {
        checkIfWaitingResponse();
        msg.setAutomationVersion(automationVersion);
        msg.setSoftwareHouse(softwareHouse);

        // VisaNET exige um ATV antes de cada transação 
        if (!msg.getOperation().equals(Operation.ATV)) {
            checkIfIsActive();
        }

        if (msg.getIdentifier() == 0) {
            msg.setIdentifier(getNewIdentifier());
        }

        deleteFiles();

        File file = new File(requestDirectory, Constants.FILE_NAME_REQUEST_TEMP);
        file.createNewFile();
        try (FileOutputStream out = new FileOutputStream(file)) {
            msg.write(out);
            out.flush();
        }
        file.renameTo(new File(requestDirectory, Constants.FILE_NAME_REQUEST));

        StatusResponse sts = getStatusResponse();
        if (sts == null) {
            throw new NotRespondingException("TEF não responde");
        }
        if (sts.getIdentifier() != msg.getIdentifier()) {
            throw new NotRespondingException("Inconsistência no campo 001-000 do arquivo 'initpos.sts' gerado pelo TEF");
        }

        waitingResponse = true;
        try {
            // verificar a cada 250 milissegundos. sem tempo limite.            
            for (;;) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TefDial.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (getResponseFile().exists()) {
                    ResponseMessage response = ResponseReader.getResponseMessage(getResponseFile());
                    if (msg.getIdentifier() == response.getIdentifier()) {
                        // getResponseFile().delete();
                        return response;
                    } else {
                        getResponseFile().delete();
                    }
                }
            }

        } finally {
            waitingResponse = false;
        }
    }

    /**
     * Envia mensagem para o GP, verifica o recebimento da solicitação pelo GP.
     *
     * @param msg
     * @return
     * @throws IOException
     * @throws NotRespondingException
     */
    private StatusResponse dispatchMessage2(DispatchMessage msg) throws IOException, NotRespondingException {
        checkIfWaitingResponse();
        
        msg.setAutomationVersion(automationVersion);
        msg.setSoftwareHouse(softwareHouse);
        
        deleteFiles();

        File file = new File(requestDirectory, Constants.FILE_NAME_REQUEST_TEMP);
        file.createNewFile();
        try (FileOutputStream out = new FileOutputStream(file)) {
            msg.write(out);
            out.flush();
        }
        file.renameTo(new File(requestDirectory, Constants.FILE_NAME_REQUEST));
        waitingResponse = true;
        try {
            StatusResponse sts = getStatusResponse();
            if (sts == null) {
                throw new NotRespondingException("TEF não responde");
            }
            if (sts.getIdentifier() != msg.getIdentifier()) {
                throw new NotRespondingException("Inconsistência no campo 001-000 do arquivo 'initpos.sts' gerado pelo TEF");
            }
            return sts;
        } finally {
            waitingResponse = false;
        }
    }

    private void deleteFiles() {
        File f = new File(requestDirectory, Constants.FILE_NAME_REQUEST_TEMP);
        if (f.exists()) {
            if (!f.delete()) {
                throw new RuntimeException("Erro ao apagar o arquivo " + f.toString());
            }
        }

        f = new File(requestDirectory, Constants.FILE_NAME_REQUEST);
        if (f.exists()) {
            if (!f.delete()) {
                throw new RuntimeException("Erro ao apagar o arquivo " + f.toString());
            }
        }

        f = new File(responseDirectory, Constants.FILE_NAME_RESPONSE);
        if (f.exists()) {
            if (!f.delete()) {
                throw new RuntimeException("Erro ao apagar o arquivo " + f.toString());
            }
        }
        f = new File(responseDirectory, Constants.FILE_NAME_STATUS);
        if (f.exists()) {
            if (!f.delete()) {
                throw new RuntimeException("Erro ao apagar o arquivo " + f.toString());
            }
        }
    }

    public void initialize() {
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    public File getRequestDirectory() {
        return requestDirectory;
    }

    public void setRequestDirectory(File requestDirectory) {
        this.requestDirectory = requestDirectory;
    }

    public File getResponseDirectory() {
        return responseDirectory;
    }

    public void setResponseDirectory(File responseDirectory) {
        this.responseDirectory = responseDirectory;
        statusResponseFile = null;
        responseFile = null;
    }

    private File getStatusResponseFile() {
        if (statusResponseFile == null) {
            statusResponseFile = new File(getResponseDirectory(), Constants.FILE_NAME_STATUS);
        }
        return statusResponseFile;
    }

    private File getResponseFile() {
        if (responseFile == null) {
            responseFile = new File(getResponseDirectory(), Constants.FILE_NAME_RESPONSE);
        }
        return responseFile;
    }

    public String getAutomationVersion() {
        return automationVersion;
    }

    public void setAutomationVersion(String automationVersion) {
        this.automationVersion = automationVersion;
    }

    public String getSoftwareHouse() {
        return softwareHouse;
    }

    public void setSoftwareHouse(String softwareHouse) {
        this.softwareHouse = softwareHouse;
    }
    //</editor-fold>

    /**
     * Tipos de operações possíveis para tef discado.
     */
    public enum Operation {

        /**
         * Verifica se o Gerenciador Padrão está ativo.
         */
        ATV,
        /**
         * Permite o acionamento da Solução TEF Discado para execução das
         * funções administrativas.
         */
        ADM,
        /**
         * Pedido de autorização para transação por meio de cheque.
         */
        CHQ,
        /**
         * Pedido de autorização para transação por meio de cartão.
         */
        CRT,
        /**
         * Cancelamento de venda efetuada por qualquer meio de pagamento
         */
        CNC,
        /**
         * Confirmação da venda e impressão de cupom.
         */
        CNF,
        /**
         * Não confirmação da venda e/ou da impressão.
         */
        NCN
    }

    public enum Feature {

        CHANGE(1),
        DISCOUNT(2);
        private int intValue;

        private Feature(int intValue) {
            this.intValue = intValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public static Feature fromInt(int value) {
            if (value == 1) {
                return CHANGE;
            } else if (value == 2) {
                return DISCOUNT;
            } else {
                return null;
            }

        }
    }
}
