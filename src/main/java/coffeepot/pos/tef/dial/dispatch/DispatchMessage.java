/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef.dial.dispatch;

import coffeepot.pos.tef.dial.Constants;
import coffeepot.pos.tef.dial.TefDial;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.joda.time.DateTime;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DispatchMessage {

    protected TefDial.Operation operation;
    protected int identifier = 0;
    protected Map<String, String> mappedFields;
    //CIELO
    protected String automationVersion;             //701-000 Versão da Automação
    protected Integer storeIndex;                   //702-000 Índice do Estabelecimento
    protected Set<TefDial.Feature> features;        //706-000 Capacidades da Automação
    protected String softwareHouse;                 //716-000 Empresa da Automação
    protected DateTime taxDateTime;                 //717-000 Data/hora fiscal

    public TefDial.Operation getOperation() {
        return operation;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getAutomationVersion() {
        return automationVersion;
    }

    public void setAutomationVersion(String automationVersion) {
        this.automationVersion = automationVersion;
    }

    public Integer getStoreIndex() {
        return storeIndex;
    }

    public void setStoreIndex(Integer storeIndex) {
        this.storeIndex = storeIndex;
    }

    public Set<TefDial.Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<TefDial.Feature> features) {
        this.features = features;
    }

    public String getSoftwareHouse() {
        return softwareHouse;
    }

    public void setSoftwareHouse(String softwareHouse) {
        this.softwareHouse = softwareHouse;
    }

    public DateTime getTaxDateTime() {
        return taxDateTime;
    }

    public void setFiscalDateTime(DateTime taxDateTime) {
        this.taxDateTime = taxDateTime;
    }
        
    private void writeHeaderAndIdentifier(StringBuilder sb) {
        sb.append(Constants.HEADER).append(Constants.EQUAL).append(operation.toString()).append(Constants.CR_LF)
                .append(Constants.IDENTIFIER).append(Constants.EQUAL).append(identifier).append(Constants.CR_LF);
    }

    /**
     * Escreve os campos regulares de cada tipo de transação. Este método deve
     * ser sobrescrito pelas classes especialistas.
     *
     * @param sb
     */
    protected void writeFields(StringBuilder sb) {
    }

    /**
     * Escreve os campos extras informado ao objeto. Estes são campos que não
     * fazem parte do manual do gerenciador padrão oficial, mas podem ser usados
     * para diversos fins.
     *
     * @param sb
     */
    private void writeMappedFields(StringBuilder sb) {
        if (mappedFields != null) {
            Set<Map.Entry<String, String>> entrySet = mappedFields.entrySet();
            for (Map.Entry<String, String> e : entrySet) {
                sb.append(e.getKey()).append(Constants.EQUAL).append(e.getValue()).append(Constants.CR_LF);
            }
        }
    }

    private void writeTrailer(StringBuilder sb) {
        //sb.append("777-777 = REDECARD");
        sb.append(Constants.TRAILER).append(Constants.CR_LF);
    }

    private void writeCieloFields(StringBuilder sb) {
        if (operation.equals(TefDial.Operation.ATV)) {
            return;
        }
        if (automationVersion != null){
            sb.append("701-000").append(Constants.EQUAL).append(automationVersion).append(Constants.CR_LF);
        }
        if (storeIndex != null){
            sb.append("702-000").append(Constants.EQUAL).append(storeIndex).append(Constants.CR_LF);
        }
        if (features != null){
            int i = 0;
            for (TefDial.Feature f: features){
                i += f.getIntValue();
            }
            sb.append("706-000").append(Constants.EQUAL).append(i).append(Constants.CR_LF);
        }
        if (softwareHouse != null){
            sb.append("716-000").append(Constants.EQUAL).append(softwareHouse).append(Constants.CR_LF);
        }
        if (taxDateTime != null){
            sb.append("717-000").append(Constants.EQUAL).append(taxDateTime.toString("yyMMddHHmmss")).append(Constants.CR_LF);
        }
    }

    public void write(OutputStream out) throws IOException {
        StringBuilder sb = new StringBuilder();
        writeHeaderAndIdentifier(sb);
        writeFields(sb);
        writeMappedFields(sb);
        writeCieloFields(sb);
        writeTrailer(sb);
        out.write(sb.toString().getBytes());
    }

    /**
     * Adiciona campos especiais que serão inclusos no arquivo de requisição.
     *
     * @param key
     * @param value
     */
    public final void addField(String key, String value) {
        if (mappedFields == null) {
            mappedFields = new LinkedHashMap<>();
        }
        mappedFields.put(key, value);
    }

    public Map<String, String> getMappedFields() {
        return mappedFields;
    }
}
