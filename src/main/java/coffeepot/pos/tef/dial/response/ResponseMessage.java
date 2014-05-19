/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef.dial.response;

import coffeepot.pos.tef.misc.Parcel;
import coffeepot.pos.tef.dial.TefDial;
import coffeepot.pos.tef.misc.ParcelingType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/*
 Código do Campo	Tipo de Informação			ATV	ADM	CHQ	CRT	CNC
 000-000	HEADER						-	M	M	M	M
 001-000	IDENTIFICAÇÃO					-	M	M	M	M
 002-000	DOCUMENTO FISCAL VINCULADO			-	O	O(3)	O(3)	O(3)
 003-000	VALOR TOTAL					-	O	M	M	M
 004-000	MOEDA						-	O	-	M(3)	M(3)
 005-000	CMC-7						-	O	M(3)	-	O(2)
 006-000	TIPO DE PESSOA					-	-	M	-	-
 007-000	DOCUMENTO DA PESSOA				-	-	M	-	-
 008-000	DATA DO CHEQUE					-	-	M	-	-
 009-000	STATUS DA TRANSAÇÃO				-	M	M	M	M
 010-000	NOME DA REDE					-	M	M	M	M
 011-000	TIPO DA TRANSAÇÃO				-	M	M	M	M
 012-000	NÚMERO DA TRANSAÇÃO - NSU			-	O	M(1)	M(1)	M(1)
 013-000	CÓDIGO DE AUTORIZAÇÃO DA TRANSAÇÃO		-	O	O	O	O
 014-000	NÚMERO DO LOTE DA TRANSAÇÃO			-	O	-	O	O
 015-000	TIMESTAMP DA TRANSAÇÃO - HOST			-	O	M(1)	M(1)	M(1)
 016-000	TIMESTAMP DA TRANSAÇÃO - LOCAL			-	O	O	O	O
 017-000	TIPO PARCELAMENTO				-	-	-	O	-
 018-000	QUANTIDADE DE PARCELAS				-	-	-	O	-
 019-yyy	DATA VENCIMENTO DA PARCELA			-	-	-	O	-
 020-yyy	VALOR DA PARCELA				-	-	-	O	-
 021-yyy	NÚMERO DA TRANSAÇÃO – NSU DA PARCELA		-	-	-	O	-
 022-000	DATA DA TRANSAÇÃO - COMPROVANTE			-	O	O	M(1)	M(1)
 023-000	HORA DA TRANSAÇÃO - COMPROVANTE			-	O	O	M(1)	M(1)
 024-000	DATA PRÉ-DATADO					-	-	-	O	-
 025-000	NÚMERO DA TRANSAÇÃO CANCELADA - NSU		-	O	-	-	M
 026-000	TIMESTAMP DA TRANSAÇÃO CANCELADA		-	O	-	-	M
 027-000	FINALIZAÇÃO					-	O	M(1)	M(1)	M(1)
 028-000	QUANTIDADE DE LINHAS DO COMPROVANTE (*)		-	M	M	M	M
 029-yyy	IMAGEM DE CADA LINHA DO COMPROVANTE		-	O	O	O	O
 030-000	TEXTO ESPECIAL OPERADOR				-	O	O	O	O
 031-000	TEXTO ESPECIAL CLIENTE				-	O	O	O	O
 032-000	AUTENTICAÇÃO					-	-	O	-	-
 033-000	BANCO						-	O	M(4)	-	O
 034-000	AGÊNCIA						-	O	M(4)	-	O
 035-000	AGÊNCIA - DC					-	O	O	-	O
 036-000	CONTA CORRENTE					-	O	M(4)	-	O
 037-000	CONTA CORRENTE - DC				-	O	O	-	O
 038-000	NÚMERO DO CHEQUE				-	O	O(4)	-	O
 039-000	NÚMERO DO CHEQUE  - DC				-	O	O	-	O
 040-000	NOME DA ADMINISTRADORA				-	O	O	O	O
 999-999	TRAILER - REGISTRO FINAL			-	M	M	M	M
 */
/**
 * @author Jeandeson O. Merelis
 */
public class ResponseMessage implements java.io.Serializable {

    protected TefDial.Operation operation;      //000-000	HEADER
    protected int identifier = 0;               //001-000	IDENTIFICAÇÃO
    protected Integer taxDocumentNumberLinked;  //002-000	DOCUMENTO FISCAL VINCULADO
    protected BigDecimal totalValue;            //003-000	VALOR TOTAL
    protected Currency currency;                //004-000	MOEDA
    protected String cmc7;                      //005-000	CMC-7
    protected String doc;                       //007-000	DOCUMENTO DA PESSOA
    protected DateTime checkDate;               //008-000	DATA DO CHEQUE
    protected boolean transactionOk;            //009-000	STATUS DA TRANSAÇÃO
    protected String errorCode;
    protected String network;                   //010-000	NOME DA REDE
    protected Integer transactionType;          //011-000	TIPO DA TRANSAÇÃO
    protected Long nsu;                         //012-000	NÚMERO DA TRANSAÇÃO - NSU
    protected Long authorizationCode;           //013-000	CÓDIGO DE AUTORIZAÇÃO DA TRANSAÇÃO
    protected Long batchNumber;                 //014-000	NÚMERO DO LOTE DA TRANSAÇÃO
    protected DateTime hostTimestamp;           //015-000	TIMESTAMP DA TRANSAÇÃO - HOST
    protected DateTime localTimestamp;          //016-000	TIMESTAMP DA TRANSAÇÃO - LOCAL
    protected ParcelingType parcelingType;      //017-000	TIPO PARCELAMENTO 
    protected int parcelsCount = 0;             //018-000	QUANTIDADE DE PARCELAS
    protected List<Parcel> parcels;             //019-yyy	DATA VENCIMENTO DA PARCELA - 020-yyy VALOR DA PARCELA - 021-yyy	NÚMERO DA TRANSAÇÃO – NSU DA PARCELA
    protected DateTime transactionTimestamp;    //022-000	DATA DA TRANSAÇÃO - COMPROVANTE - 023-000 HORA DA TRANSAÇÃO - COMPROVANTE
    protected DateTime preDated;                //024-000	DATA PRÉ-DATADO
    protected Long canceledNsu;                 //025-000	NÚMERO DA TRANSAÇÃO CANCELADA - NSU
    protected DateTime canceledHostTimestamp;   //026-000	TIMESTAMP DA TRANSAÇÃO CANCELADA
    protected String controlCode;               //027-000	FINALIZAÇÃO / Código de controle
    protected int voucherLinesCount = 0;        //028-000	QUANTIDADE DE LINHAS DO COMPROVANTE (*)	
    protected String voucherImage;              //029-yyy	IMAGEM DE CADA LINHA DO COMPROVANTE
    protected String operatorMessage;           //030-000	TEXTO ESPECIAL OPERADOR
    protected String customerMessage;           //031-000	TEXTO ESPECIAL CLIENTE
    protected String authentication;            //032-000	AUTENTICAÇÃO
    protected String bankCode;                  //033-000	BANCO
    protected String bankingAgency;             //034-000	AGÊNCIA	
    protected String bankingAgencyDC;           //035-000	AGÊNCIA - DC
    protected String checkingAccount;           //036-000	CONTA CORRENTE
    protected String checkingAccountDC;         //037-000	CONTA CORRENTE - DC
    protected String checkNumber;               //038-000	NÚMERO DO CHEQUE
    protected String checkNumberDC;             //039-000	NÚMERO DO CHEQUE  - DC
    protected String administratorName;         //040-000	NOME DA ADMINISTRADORA
    //CIELO
    protected BigDecimal originalValue;         //707-000 Valor original
    protected BigDecimal changeValue;           //708-000 Valor do troco
    protected BigDecimal discountValue;         //709-000 Valor do desconto   
    protected String couponReduced;             //711-xxx Cupom reduzido    
    protected String voucherOfCustomer;         //713-xxx Via Cliente do comprovante
    protected String voucherOfStore;            //715-xxx Via Estabelecimento do comprovante
    protected String terminalNumber;            //718-000 Número lógico do terminal
    protected String storeCode;                 //719-000 Código do estabelecimento
    //
    protected String originalFile;
    //
    protected int sequence;
    protected boolean cnfSent = false;
//    protected boolean printed = false;

    public ResponseMessage(TefDial.Operation operation) {
        this.operation = operation;
    }

    //<editor-fold defaultstate="collapsed" desc="getters">
    public String getFirstImageOfVoucher() {
        if (operation.equals(TefDial.Operation.CRT) && couponReduced != null && !couponReduced.isEmpty()) {
            return couponReduced;
        }

        if (voucherOfCustomer != null && !voucherOfCustomer.isEmpty()) {
            return voucherOfCustomer;
        }

        return voucherImage;
    }

    public String getSecondImageOfVoucher() {
        if (voucherOfStore != null && !voucherOfStore.isEmpty()) {
            return voucherOfStore;
        }

        return voucherImage;
    }

    /**
     * 000-000	HEADER.
     * <p/>
     * Indica o início do arquivo e o tipo de operação relacionada ao arquivo.
     * <br/>
     * Conteúdos válidos: <br/>
     * ATV- Verifica se o Gerenciador Padrão está ativo <br/>
     * ADM- Administrativa <br/>
     * CHQ- Cheque <br/>
     * CRT- Cartão <br/>
     * CNC- Cancelamento <br/>
     * CNF- Confirmação de finalização da venda e impressão do cupom <br/>
     * NCN- Não confirmação de finalização da venda por desistência do cliente
     * ou erro na impressão do cupom.
     */
    public TefDial.Operation getOperation() {
        return operation;
    }

    /**
     * 001-000	IDENTIFICAÇÃO.
     * <p/>
     * Indica o número de controle da solicitação que está sendo feita. Este
     * número é gerado pelo aplicativo de automação comercial, o qual deverá
     * colocar um conteúdo diferente a cada nova solicitação. Este mesmo
     * conteúdo é devolvido nos arquivos de resposta.
     */
    public int getIdentifier() {
        return identifier;
    }

    /**
     * 002-000	DOCUMENTO FISCAL VINCULADO.
     * <p/>
     * Número do documento fiscal vinculado à forma de pagamento ou finalização.
     * Nota relativa a legislação ECF “…A emissão do comprovante de pagamento de
     * operação ou prestação, efetuado com cartão de crédito ou débito
     * automático em conta corrente somente poderá ser feita por meio de ECF,
     * devendo o comprovante estar vinculado ao documento fiscal emitido na
     * operação ou prestação respectiva, conforme disposto em legislação
     * pertinente.”.
     */
    public Integer getFiscalDocumentNumberLinked() {
        return taxDocumentNumberLinked;
    }

    /**
     * 003-000	VALOR TOTAL.
     * <p/>
     * Valor total desta forma de pagamento.
     */
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    /**
     * 004-000	MOEDA.
     * <p/>
     * Indica a moeda utilizada na operação.
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * 005-000	CMC-7.
     * <p/>
     * Contém os dados do cheque no mesmo formato obtido por uma leitura do
     * CMC-7.
     */
    public String getCmc7() {
        return cmc7;
    }

    /**
     * 007-000	DOCUMENTO DA PESSOA.
     * <p/>
     * Indica a pessoa que esta sendo atendida na operação por meio do CNPJ ou
     * CPF.
     */
    public String getDoc() {
        return doc;
    }

    /**
     * 008-000	DATA DO CHEQUE.
     * <p/>
     * Data de vencimento do Cheque
     */
    public DateTime getCheckDate() {
        return checkDate;
    }

    /**
     * 009-000	STATUS DA TRANSAÇÃO.
     * <p/>
     * Indica se a transação foi aprovada ou recusada e qual o motivo da recusa.
     */
    public boolean isTransactionOk() {
        return transactionOk;
    }

    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 010-000	NOME DA REDE.
     * <p/>
     * Nome da rede que tratou a transação.
     */
    public String getNetwork() {
        return network;
    }

    /**
     * 011-000	TIPO DA TRANSAÇÃO.
     * <p/>
     * Código identificando o tipo da transação executada.
     * <p/>
     * Conteúdos Válidos:<br/>
     * 0- Administrativas – Outras (Reimpressão, Iniciação de Terminal
     * etc.)<br/>
     * 1- Administrativa – Fechamento/Transmissão de Lote<br/>
     * 10- Cartão de Crédito à Vista<br/>
     * 11- Cartão de Crédito Parcelado pelo Estabelecimento<br/>
     * 12- Cartão de Crédito Parcelado pela Administradora<br/>
     * 13- Pré-Autorização com Cartão de Crédito<br/>
     * 20- Cartão de Débito à Vista<br/>
     * 21- Cartão de Débito Pré-Datado<br/>
     * 22- Cartão de Débito Parcelada<br/>
     * 23- Cartão de Débito à Vista Forçada<br/>
     * 24- Cartão de Débito Pré-Datado Forçada<br/>
     * 25- Cartão de Débito Pré-Datado sem Garantia<br/>
     * 30- Outros Cartões<br/>
     * 40- CDC<br/>
     * 41- Consulta CDC<br/>
     * 50- Convênio<br/>
     * 60- Voucher<br/>
     * 70- Consulta Cheque<br/>
     * 71- Garantia de Cheque<br/>
     * 99-Outras<br/><br/>
     *
     * Obs.: No cancelamento de venda feito através da operação ADM
     * (Administrativa) este campo conterá o tipo da transação de venda que foi
     * cancelada.<br/>
     *
     * Exemplo-1: <br/>
     * 000-000 = ADM<br/>
     * 011-000 = 00 (Administrativas: Reimpressão, Iniciação de Terminal
     * etc.)<br/><br/>
     *
     * Exemplo-2: <br/>
     * 000-000 = ADM<br/>
     * 011-000 = 01 (Administrativa: Fechamento/Transmissão de Lote)<br/><br/>
     *
     * Exemplo-3:<br/>
     * 000-000 = ADM<br/>
     * 011-000 = 10 (Cancelamento de Cartão de Crédito, qualquer que tenha sido
     * a modalidade: à Vista, Parcelado pelo Estabelecimento ou pela
     * Administradora)<br/><br/>
     *
     * Exemplo-4:<br/>
     * 000-000 = ADM<br/>
     * 011-000 = 20 (Cancelamento de Cartão de Débito, qualquer que tenha sido a
     * modalidade: à Vista, Pré-Datado, Parcelado etc.)<br/><br/>
     *
     * Exemplo-5:<br/>
     * 000-000 = ADM<br/>
     * 011-000 = 71 (Cancelamento de Garantia de Cheque)
     */
    public Integer getTransactionType() {
        return transactionType;
    }

    /**
     * 012-000 NÚMERO DA TRANSAÇÃO - NSU.
     * <p/>
     * Indica o número de seqüência (NSU – Número Sequencial Único) da transação
     * atribuído pelo Host (Sistema das Redes de Cartão que recebe e trata as
     * solicitações das transações TEF).
     * <p/>
     * Quando este campo é enviado do Gerenciador Padrão para o Aplicativo de
     * Automação Comercial, ele representa o NSU do Host estabelecido para a
     * transação.
     * <p/>
     * Quando este campo é enviado do Aplicativo de Automação Comercial para o
     * Gerenciador Padrão, ele representa o NSU da transação a ser tratada
     * (cancelada, confirmada etc.)
     */
    public Long getNsu() {
        return nsu;
    }

    /**
     * 013-000 CÓDIGO DE AUTORIZAÇÃO DA TRANSAÇÃO.
     * <p/>
     * Indica o número de autorização da transação atribuída pelo Host. Cada
     * transação TEF possui um número de autorização.
     *
     * @return
     */
    public Long getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * 014-000 NÚMERO DO LOTE DA TRANSAÇÃO.
     * <p/>
     * Indica o número de lote da transação.
     *
     * @return
     */
    public Long getBatchNumber() {
        return batchNumber;
    }

    /**
     * 015-000 TIMESTAMP DA TRANSAÇÃO HOST.
     * <p/>
     * Indica a data e hora da transação no Host.
     *
     * @return
     */
    public DateTime getHostTimestamp() {
        return hostTimestamp;
    }

    /**
     * 016-000 TIMESTAMP DA TRANSAÇÃO LOCAL.
     * <p/>
     * Indica a data e hora da transação no ponto de venda.
     *
     * @return
     */
    public DateTime getLocalTimestamp() {
        return localTimestamp;
    }

    /**
     * 017-000 TIPO PARCELAMENTO.
     * <p/>
     * Indica o tipo de parcelamento aplicado à operação.
     *
     * @return
     */
    public ParcelingType getParcelingType() {
        return parcelingType;
    }

    /**
     * 018-000 QUANTIDADE DE PARCELAS.
     * <p/>
     * Indica o número de parcelas no caso de transações Parceladas (Crédito ou
     * Débito).
     *
     * @return
     */
    public int getParcelsCount() {
        return parcelsCount;
    }

    /**
     * 018-000/019-yyy/020-yyy/021-yyy <br/>
     * Contém as parcelas no caso de transações Parceladas (Crédito ou Débito).
     * <p/>
     * Importante: Em testes realizados, os dados das parcelas não são
     * fornecidos no arquivo, dito isto, este método normalmente retornará null,
     * então para saber se houve parcelamento, sempre verificar o método
     * {@link #getParcelsCount()}, se o mesmo for maior que 0 então teve
     * parcelamento.
     *
     * @return
     */
    public List<Parcel> getParcels() {
        return parcels;
    }

    /**
     * 022-000/023-000 DATA E HORA DA TRANSAÇÃO - COMPROVANTE.
     * <p/>
     * Indica a data da transação.
     *
     * @return
     */
    public DateTime getTransactionTimestamp() {
        return transactionTimestamp;
    }

    /**
     * 024-000 DATA PRÉ-DATADO.
     * <p/>
     * Contém data de agendamento para pré-datado.
     *
     * @return
     */
    public DateTime getPreDated() {
        return preDated;
    }

    /**
     * 025-000 NÚMERO DA TRANSAÇÃO CANCELADA - NSU.
     * <p/>
     * Número de sequência (NSU) da transação cancelada.
     *
     * @return
     */
    public Long getCanceledNsu() {
        return canceledNsu;
    }

    /**
     * 026-000 TIMESTAMP DA TRANSAÇÃO CANCELADA - Host.
     * <p/>
     * Contém a data e hora da transação cancelada no Host.
     *
     * @return
     */
    public DateTime getCanceledHostTimestamp() {
        return canceledHostTimestamp;
    }

    /**
     * 027-000 FINALIZAÇÃO.
     * <p/>
     * Dados recebidos do Módulo TEF que executou a transação e que devem ser
     * devolvidos no comando de finalização de uma venda.
     *
     * @return
     */
    public String getControlCode() {
        return controlCode;
    }

    /**
     * 028-000	QUANTIDADE DE LINHAS DO COMPROVANTE.
     * <p/>
     * Quantidade de linhas da via única do comprovante.
     *
     * @return
     */
    public int getVoucherLinesCount() {
        return voucherLinesCount;
    }

    /**
     * 029-yyy IMAGEM DO COMPROVANTE.
     * <p/>
     * Apresenta a imagem a ser impressa de cada uma das linhas do comprovante.
     * Obs: As linhas serão separadas pelo caractere "\n".
     *
     * @return
     */
    public String getVoucherImage() {
        return voucherImage;
    }

    /**
     * 030-000 TEXTO ESPECIAL OPERADOR.
     * <p/>
     * Texto que, quando presente, deve ser apresentado pelo aplicativo de
     * Automação Comercial ao operador.
     *
     * @return
     */
    public String getOperatorMessage() {
        return operatorMessage;
    }

    /**
     * 031-000 TEXTO ESPECIAL CLIENTE.
     * <p/>
     * Texto que, quando presente, deve ser apresentado pelo aplicativo de
     * Automação Comercial ao cliente.
     *
     * @return
     */
    public String getCustomerMessage() {
        return customerMessage;
    }

    /**
     * 032-000 AUTENTICAÇÃO.
     * <p/>
     * Mensagem de autenticação a ser impressa no cheque.
     *
     * @return
     */
    public String getAuthentication() {
        return authentication;
    }

    /**
     * 033-000 BANCO.
     * <p/>
     * Código do Banco no padrão do Banco Central.
     *
     * @return
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * 034-000 AGÊNCIA.
     * <p/>
     * Código da agência do cheque.
     *
     * @return
     */
    public String getBankingAgency() {
        return bankingAgency;
    }

    /**
     * 035-000 AGÊNCIA - DC.
     * <p/>
     * Dígito de controle da agência do cheque.
     *
     * @return
     */
    public String getBankingAgencyDC() {
        return bankingAgencyDC;
    }

    /**
     * 036-000 CONTA CORRENTE.
     * <p/>
     * Código da conta corrente do cheque.
     *
     * @return
     */
    public String getCheckingAccount() {
        return checkingAccount;
    }

    /**
     * 037-000 CONTA CORRENTE - DC.
     * <p/>
     * Dígito de controle da conta corrente do cheque.
     *
     * @return
     */
    public String getCheckingAccountDC() {
        return checkingAccountDC;
    }

    /**
     * 038-000 NÚMERO DO CHEQUE.
     * <p/>
     * Número do cheque.
     *
     * @return
     */
    public String getCheckNumber() {
        return checkNumber;
    }

    /**
     * 039-000 NÚMERO DO CHEQUE - DC.
     * <p/>
     * Dígito de controle do Número do cheque.
     *
     * @return
     */
    public String getCheckNumberDC() {
        return checkNumberDC;
    }

    /**
     * 040-000 NOME DA ADMINISTRADORA.
     * <p/>
     * Nome do cartão ou do Emissor. O mesmo cartão pode ter nomes diferentes de
     * acordo com a Rede Adquirente utilizada. Por motivo de compatibilidade, a
     * Automação Comercial não deve consistir este campo, somente armazená-lo
     * para consulta ou agrupamento de transações.
     *
     * @return
     */
    public String getCardName() {
        return administratorName;
    }

    /**
     * 707-000 Valor original.
     * <p/>
     * Valor original da transação informado pela Automação Comercial no campo
     * 003-000 do arquivo de solicitação, em centavos da moeda identificada no
     * campo 004-000. Este campo é informado pelo Pay&Go / Plug&Pay caso seja
     * diferente do valor final da transação informado no campo 003-000 do
     * arquivo de resposta, sempre respeitando a regra abaixo: Valor total
     * (003-000) = Valor original (707-000) + Valor do troco (708-000) – Valor
     * do desconto (709-000)
     *
     * @return
     */
    public BigDecimal getOriginalValue() {
        if (originalValue == null) {
            return getTotalValue();
        }
        return originalValue;
    }

    /**
     * 708-000 Valor do troco.
     * <p/>
     * Valor de retirada em dinheiro (saque no cartão) realizada em conjunto com
     * a transação de venda. Este valor é acrescido ao valor original da
     * transação, e deve ser registrado na Impressora Fiscal como "troco".
     *
     * @return
     */
    public BigDecimal getChangeValue() {
        return changeValue;
    }

    /**
     * 709-000 Valor do desconto.
     * <p/>
     * Valor do desconto concedido ao Cliente pela Rede Adquirente ou pelo
     * Emissor para uma transação de venda. Este valor é retirado do valor
     * original da transação, e deve ser registrado na Impressora Fiscal como
     * "desconto".
     *
     * @return
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * 711-xxx Cupom reduzido
     *
     * @return
     */
    public String getCouponReduced() {
        return couponReduced;
    }

    /**
     * 713-xxx Via Cliente do comprovante.
     *
     * @return
     */
    public String getVoucherOfCustomer() {
        return voucherOfCustomer;
    }

    /**
     * 715-xxx Via Estabelecimento do comprovante.
     *
     * @return
     */
    public String getVoucherOfStore() {
        return voucherOfStore;
    }

    /**
     * 718-000 Número lógico do terminal.
     * <p/>
     * Identificação do terminal.
     *
     * @return
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * 719-000 Código do estabelecimento.
     * <p/>
     * Identificação do estabelecimento.
     *
     * @return
     */
    public String getStoreCode() {
        return storeCode;
    }

    /**
     * Obtém o conteúdo original do arquivo de resposta.
     *
     * @return
     */
    public String getOriginalFile() {
        return originalFile;
    }

    //</editor-fold>
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public boolean isCnfSent() {
        return cnfSent;
    }

    public void setCnfSent(boolean cnfSent) {
        this.cnfSent = cnfSent;
    }

    protected void doSetValue(String field, String value) {
    }

    protected final void setValue(String field, String value) {
        if (value == null) {
            return;
        }
        String fieldSeq = field.substring(4, 7);
        field = field.substring(0, 3);
        value = value.trim();
        switch (field) {
            case "001":
                identifier = Integer.valueOf(value);
                break;

            case "002":
                taxDocumentNumberLinked = Integer.valueOf(value);
                break;

            case "003":
                totalValue = new BigDecimal(value);
                totalValue = totalValue.setScale(0);
                totalValue = totalValue.movePointLeft(2);
                break;

            case "004":
                if ("0".equals(value)) {
                    currency = Currency.getInstance("BRL");
                } else {
                    currency = Currency.getInstance("USD");
                }
                break;

            case "005":
                cmc7 = value;
                break;

            case "007":
                doc = value;
                break;
            case "008":
                try {
                    checkDate = DateTime.parse(value, DateTimeFormat.forPattern("ddMMyyyy"));
                } catch (Exception ex) {
                    Logger.getLogger(ResponseMessage.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "009":
                transactionOk = "0".equals(value);
                if (!transactionOk) {
                    errorCode = value;
                }
                break;

            case "010":
                network = value;
                break;
            case "011":
                transactionType = Integer.valueOf(value);
                break;
            case "012":
                nsu = Long.valueOf(value);
                break;
            case "013":
                authorizationCode = Long.valueOf(value);
                break;
            case "014":
                batchNumber = Long.valueOf(value);
                break;
            case "015":
                try {
                    hostTimestamp = DateTime.parse(value, DateTimeFormat.forPattern("ddMMHHmmss"));
                } catch (Exception ex) {
                    Logger.getLogger(ResponseMessage.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "016":
                try {
                    localTimestamp = DateTime.parse(value, DateTimeFormat.forPattern("ddMMHHmmss"));
                } catch (Exception ex) {
                    Logger.getLogger(ResponseMessage.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "017":
                parcelingType = ParcelingType.fromInt(Integer.valueOf(value));
                break;
            case "018":
                parcelsCount = Integer.valueOf(value);
                break;
            case "019": {
                if (parcels == null) {
                    parcels = new ArrayList<>();
                }
                DateTime d = DateTime.parse(value, DateTimeFormat.forPattern("ddMMyyyy"));

                Integer seq = Integer.valueOf(fieldSeq);
                Parcel p = null;
                try {
                    p = parcels.get(--seq);
                } catch (Exception ex) {
                }
                if (p == null) {
                    p = new Parcel();
                    p.setDate(d);
                    parcels.add(p);
                } else {
                    p.setDate(d);
                }
            }
            break;
            case "020": {
                if (parcels == null) {
                    parcels = new ArrayList<>();
                }

                BigDecimal v = new BigDecimal(value);
                v = v.setScale(0);
                v = v.movePointLeft(2);

                Integer seq = Integer.valueOf(fieldSeq);
                Parcel p = null;
                try {
                    p = parcels.get(--seq);
                } catch (Exception ex) {
                }
                if (p == null) {
                    p = new Parcel();
                    p.setValue(v);
                    parcels.add(p);
                } else {
                    p.setValue(v);
                }
            }
            break;
            case "021": {
                if (parcels == null) {
                    parcels = new ArrayList<>();
                }

                Long l = Long.valueOf(value);

                Integer seq = Integer.valueOf(fieldSeq);
                Parcel p = null;
                try {
                    p = parcels.get(--seq);
                } catch (Exception ex) {
                }
                if (p == null) {
                    p = new Parcel();
                    p.setNsu(l);
                    parcels.add(p);
                } else {
                    p.setNsu(l);
                }
            }
            break;
            case "022":
                try {
                    transactionTimestamp = DateTime.parse(value, DateTimeFormat.forPattern("ddMMyyyy"));
                } catch (Exception ex) {
                    Logger.getLogger(ResponseMessage.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "023": {
                try {
                    if (transactionTimestamp == null) {
                        transactionTimestamp = DateTime.parse(value, DateTimeFormat.forPattern("HHmmss"));
                    } else {
                        DateTime d = DateTime.parse(value, DateTimeFormat.forPattern("HHmmss"));
                        transactionTimestamp = transactionTimestamp.withTime(d.getHourOfDay(), d.getMinuteOfHour(), d.getSecondOfMinute(), 0);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ResponseMessage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case "024":
                try {
                    preDated = DateTime.parse(value, DateTimeFormat.forPattern("ddMMyyyy"));
                } catch (Exception ex) {
                    Logger.getLogger(ResponseMessage.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "025":
                canceledNsu = Long.valueOf(value);
                break;
            case "026":
                try {
                    canceledHostTimestamp = DateTime.parse(value, DateTimeFormat.forPattern("ddMMHHmmss"));
                } catch (Exception ex) {
                    Logger.getLogger(ResponseMessage.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "027":
                controlCode = value;
                break;
            case "028":
                voucherLinesCount = Integer.valueOf(value);
                break;
            case "029":
                if (voucherImage == null) {
                    voucherImage = "";
                }
                value = value.substring(1, value.length() - 1); // remover aspas
                voucherImage += value + "\n";
                break;
            case "030":
                operatorMessage = value;
                break;
            case "031":
                customerMessage = value;
                break;
            case "032":
                authentication = value;
                break;
            case "033":
                bankCode = value;
                break;
            case "034":
                bankingAgency = value;
                break;
            case "035":
                bankingAgencyDC = value;
                break;
            case "036":
                checkingAccount = value;
                break;
            case "037":
                checkingAccountDC = value;
                break;
            case "038":
                checkNumber = value;
                break;
            case "039":
                checkNumberDC = value;
                break;
            case "040":
                administratorName = value;
                break;
            case "707": {
                originalValue = new BigDecimal(value);
                originalValue = originalValue.setScale(0);
                originalValue = originalValue.movePointLeft(2);
                break;
            }
            case "708": {
                changeValue = new BigDecimal(value);
                changeValue = changeValue.setScale(0);
                changeValue = changeValue.movePointLeft(2);
                break;
            }
            case "709": {
                discountValue = new BigDecimal(value);
                discountValue = discountValue.setScale(0);
                discountValue = discountValue.movePointLeft(2);
                break;
            }
            case "711":
                if (couponReduced == null) {
                    couponReduced = "";
                }
                value = value.substring(1, value.length() - 1); // remover aspas
                couponReduced += value + "\n";
                break;

            case "713":
                if (voucherOfCustomer == null) {
                    voucherOfCustomer = "";
                }
                value = value.substring(1, value.length() - 1); // remover aspas
                voucherOfCustomer += value + "\n";
                break;
            case "715":
                if (voucherOfStore == null) {
                    voucherOfStore = "";
                }
                value = value.substring(1, value.length() - 1); // remover aspas
                voucherOfStore += value + "\n";
                break;
            case "718":
                terminalNumber = value;
                break;
            case "719":
                storeCode = value;
                break;
            default:
                doSetValue(field, value);
                break;
        }
    }
}
