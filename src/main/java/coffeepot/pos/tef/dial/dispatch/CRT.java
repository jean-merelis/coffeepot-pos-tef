/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef.dial.dispatch;

import coffeepot.pos.tef.dial.Constants;
import coffeepot.pos.tef.dial.dispatch.DispatchMessage;
import coffeepot.pos.tef.dial.TefDial;
import java.math.BigDecimal;
import java.util.Currency;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class CRT extends DispatchMessage {

    protected Integer fiscalDocumentNumberLinked;  //002-000	DOCUMENTO FISCAL VINCULADO
    protected BigDecimal totalValue;            //003-000	VALOR TOTAL
    protected Currency currency;                //004-000	MOEDA

    public CRT() {
        operation = TefDial.Operation.CRT;
    }

    @Override
    protected void writeFields(StringBuilder sb) {
        if (fiscalDocumentNumberLinked != null) {
            sb.append("002-000").append(Constants.EQUAL).append(fiscalDocumentNumberLinked.toString()).append(Constants.CR_LF);
        }
        
        if (totalValue == null) {
            throw new IllegalArgumentException("Valor não informado");
        }

        if (totalValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor informado é inválido");
        }

        totalValue = totalValue.setScale(2, BigDecimal.ROUND_FLOOR);
        totalValue = totalValue.movePointRight(2);
        String s = totalValue.toPlainString();
        s = s.replace(".", "");

        sb.append("003-000").append(Constants.EQUAL).append(s).append(Constants.CR_LF);
        if (currency != null) {
            if (currency.getCurrencyCode().equals("BRL")) {
                sb.append("004-000").append(Constants.EQUAL).append("0").append(Constants.CR_LF);
            } else if (currency.getCurrencyCode().equals("USD")) {
                sb.append("004-000").append(Constants.EQUAL).append("1").append(Constants.CR_LF);
            }
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="getters and setters">    
    public Integer getFiscalDocumentNumberLinked() {
        return fiscalDocumentNumberLinked;
    }
    
    public void setFiscalDocumentNumberLinked(Integer fiscalDocumentNumberLinked) {
        this.fiscalDocumentNumberLinked = fiscalDocumentNumberLinked;
    }
    
    public BigDecimal getTotalValue() {
        return totalValue;
    }
    
    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
    
    public Currency getCurrency() {
        return currency;
    }
    
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    //</editor-fold>
}
