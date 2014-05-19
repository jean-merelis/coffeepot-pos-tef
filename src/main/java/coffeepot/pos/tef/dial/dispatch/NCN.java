/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef.dial.dispatch;

import coffeepot.pos.tef.dial.Constants;
import coffeepot.pos.tef.dial.TefDial;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class NCN extends DispatchMessage {

    protected Integer taxDocumentNumberLinked;  //002-000	DOCUMENTO FISCAL VINCULADO
    protected String network;                   //010-000	NOME DA REDE
    protected Long nsu;                         //012-000	NÚMERO DA TRANSAÇÃO - NSU
    protected String controlCode;              //027-000	FINALIZAÇÃO

    public NCN() {
        operation = TefDial.Operation.NCN;
    }
    
    @Override
    protected void writeFields(StringBuilder sb) {
        if (taxDocumentNumberLinked != null) {
            sb.append("002-000").append(Constants.EQUAL).append(taxDocumentNumberLinked.toString()).append(Constants.CR_LF);
        }
        sb.append("010-000").append(Constants.EQUAL).append(network).append(Constants.CR_LF);
        sb.append("012-000").append(Constants.EQUAL).append(nsu.toString()).append(Constants.CR_LF);
        sb.append("027-000").append(Constants.EQUAL).append(controlCode).append(Constants.CR_LF);
    }

    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    public Integer getTaxDocumentNumberLinked() {
        return taxDocumentNumberLinked;
    }
    
    public void setTaxDocumentNumberLinked(Integer taxDocumentNumberLinked) {
        this.taxDocumentNumberLinked = taxDocumentNumberLinked;
    }
    
    public String getNetwork() {
        return network;
    }
    
    public void setNetwork(String network) {
        this.network = network;
    }
    
    public Long getNsu() {
        return nsu;
    }
    
    public void setNsu(Long nsu) {
        this.nsu = nsu;
    }
    
    public String getControlCode() {
        return controlCode;
    }
    
    public void setControlCode(String finalization) {
        this.controlCode = finalization;
    }
    //</editor-fold>
        
}
