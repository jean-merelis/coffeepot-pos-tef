/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef.dial.dispatch;

import coffeepot.pos.tef.dial.Constants;
import coffeepot.pos.tef.dial.TefDial;
import java.math.BigDecimal;
import org.joda.time.DateTime;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class CHQ extends DispatchMessage {

    protected Integer taxDocumentNumberLinked;  //002-000	DOCUMENTO FISCAL VINCULADO
    protected BigDecimal totalValue;            //003-000	VALOR TOTAL
    protected String cmc7;                      //005-000	CMC-7
    protected String doc;                       //007-000	DOCUMENTO DA PESSOA
    protected DateTime checkDate;               //008-000	DATA DO CHEQUE    
    protected String bankCode;                  //033-000	BANCO
    protected String bankingAgency;             //034-000	AGÊNCIA	
    protected String bankingAgencyDC;           //035-000	AGÊNCIA - DC
    protected String checkingAccount;           //036-000	CONTA CORRENTE
    protected String checkingAccountDC;         //037-000	CONTA CORRENTE - DC
    protected String checkNumber;               //038-000	NÚMERO DO CHEQUE
    protected String checkNumberDC;             //039-000	NÚMERO DO CHEQUE  - DC

    public CHQ() {
        operation = TefDial.Operation.CHQ;
    }

    @Override
    protected void writeFields(StringBuilder sb) {
        if (taxDocumentNumberLinked != null) {
            sb.append("002-000").append(Constants.EQUAL).append(taxDocumentNumberLinked.toString()).append(Constants.CR_LF);
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


        if (cmc7 != null) {
            sb.append("005-000").append(Constants.EQUAL).append(cmc7).append(Constants.CR_LF);
        }
        if (doc != null) {
            sb.append("006-000").append(Constants.EQUAL).append(doc.length() == 11 ? "F" : "J").append(Constants.CR_LF);
            sb.append("007-000").append(Constants.EQUAL).append(doc).append(Constants.CR_LF);
        }
        if (checkDate != null) {
            sb.append("008-000").append(Constants.EQUAL).append(checkDate.toString("ddMMyyyy")).append(Constants.CR_LF);
        }
        if (bankCode != null) {
            sb.append("033-000").append(Constants.EQUAL).append(bankCode).append(Constants.CR_LF);
        }
        if (bankingAgency != null) {
            sb.append("034-000").append(Constants.EQUAL).append(bankingAgency).append(Constants.CR_LF);
        }
        if (bankingAgencyDC != null) {
            sb.append("035-000").append(Constants.EQUAL).append(bankingAgencyDC).append(Constants.CR_LF);
        }
        if (checkingAccount != null) {
            sb.append("036-000").append(Constants.EQUAL).append(checkingAccount).append(Constants.CR_LF);
        }
        if (checkingAccountDC != null) {
            sb.append("037-000").append(Constants.EQUAL).append(checkingAccountDC).append(Constants.CR_LF);
        }
        if (checkNumber != null) {
            sb.append("038-000").append(Constants.EQUAL).append(checkNumber).append(Constants.CR_LF);
        }
        if (checkNumberDC != null) {
            sb.append("039-000").append(Constants.EQUAL).append(checkNumberDC).append(Constants.CR_LF);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    public Integer getTaxDocumentNumberLinked() {
        return taxDocumentNumberLinked;
    }

    public void setTaxDocumentNumberLinked(Integer taxDocumentNumberLinked) {
        this.taxDocumentNumberLinked = taxDocumentNumberLinked;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public String getCmc7() {
        return cmc7;
    }

    public void setCmc7(String cmc7) {
        this.cmc7 = cmc7;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public DateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(DateTime checkDate) {
        this.checkDate = checkDate;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankingAgency() {
        return bankingAgency;
    }

    public void setBankingAgency(String bankingAgency) {
        this.bankingAgency = bankingAgency;
    }

    public String getBankingAgencyDC() {
        return bankingAgencyDC;
    }

    public void setBankingAgencyDC(String bankingAgencyDC) {
        this.bankingAgencyDC = bankingAgencyDC;
    }

    public String getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(String checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    public String getCheckingAccountDC() {
        return checkingAccountDC;
    }

    public void setCheckingAccountDC(String checkingAccountDC) {
        this.checkingAccountDC = checkingAccountDC;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getCheckNumberDC() {
        return checkNumberDC;
    }

    public void setCheckNumberDC(String checkNumberDC) {
        this.checkNumberDC = checkNumberDC;
    }
    //</editor-fold>
}
