/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef;

import coffeepot.pos.tef.dial.dispatch.CRT;
import coffeepot.pos.tef.dial.dispatch.CNF;
import coffeepot.pos.tef.dial.TefDial;
import coffeepot.pos.tef.dial.dispatch.CHQ;
import coffeepot.pos.tef.dial.dispatch.NCN;
import coffeepot.pos.tef.dial.response.StatusResponse;
import coffeepot.pos.tef.dial.response.ResponseMessage;
import java.io.File;
import java.math.BigDecimal;
import java.util.Currency;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * //@author Jeandeson O. Merelis
 */
public class TefTest {

    public TefTest() {
    }

    //@BeforeClass
    public static void setUpClass() {
    }

    //@AfterClass
    public static void tearDownClass() {
    }

    //@Before
    public void setUp() {
    }

    //@After
    public void tearDown() {
    }

    //@Test
    public void testActive() throws Exception {
        System.out.println("");
        System.out.println("");
        System.out.println("active");
        TefDial instance = new TefDial();
        instance.setRequestDirectory(new File("C:\\tef_dial\\req"));
        instance.setResponseDirectory(new File("C:\\tef_dial\\resp"));
        System.out.println("isActive: " + instance.isActive());
    }

    //@Test
    public void testAdm() throws Exception {
        System.out.println("");
        System.out.println("");
        System.out.println("adm");
        TefDial instance = new TefDial();
        instance.setRequestDirectory(new File("C:\\tef_dial\\req"));
        instance.setResponseDirectory(new File("C:\\tef_dial\\resp"));
        ResponseMessage adm = instance.adm();
        System.out.println("conteúdo do arquivo:\n" + adm.getOriginalFile());
    }

    //@Test
    public void testChq() throws Exception {
        System.out.println("");
        System.out.println("");
        System.out.println("testChq");
        TefDial instance = new TefDial();
        instance.setRequestDirectory(new File("C:\\tef_dial\\req"));
        instance.setResponseDirectory(new File("C:\\tef_dial\\resp"));

        CHQ chq = new CHQ();
        chq.setTotalValue(new BigDecimal("151.88"));
        chq.setTaxDocumentNumberLinked(123456);
        chq.setBankCode("001");
        chq.setCheckDate(DateTime.parse("2013-12-01"));
        chq.setDoc("11111111111");
        chq.setBankingAgency("0112");        
        
        ResponseMessage resp = instance.chq(chq);
        if (resp.isTransactionOk()) {
            CNF cnf = new CNF();
            cnf.setIdentifier(resp.getIdentifier());
            cnf.setControlCode(resp.getControlCode());
            cnf.setNetwork(resp.getNetwork());
            cnf.setNsu(resp.getNsu());
            cnf.setFiscalDocumentNumberLinked(resp.getFiscalDocumentNumberLinked());
            StatusResponse sts = instance.cnf(cnf);
            System.out.println("cnf:\n\n");
            System.out.println(sts.getOperation());
            System.out.println(sts.getIdentifier());
            System.out.println(sts.getFields());
        }
        System.out.println("conteúdo do arquivo:\n" + resp.getOriginalFile());
    }

    //@Test
    public void testCrt() throws Exception {
        System.out.println("");
        System.out.println("");
        System.out.println("crt");
        TefDial instance = new TefDial();
        instance.setRequestDirectory(new File("C:\\tef_dial\\req"));
        instance.setResponseDirectory(new File("C:\\tef_dial\\resp"));

        CRT crt = new CRT();
        crt.setCurrency(Currency.getInstance("BRL"));
        crt.setTotalValue(new BigDecimal("19.99"));
        crt.setFiscalDocumentNumberLinked(123456);

        ResponseMessage resp = instance.crt(crt);
        if (resp.isTransactionOk()) {
            CNF cnf = new CNF();
            cnf.setIdentifier(resp.getIdentifier());
            cnf.setControlCode(resp.getControlCode());
            cnf.setNetwork(resp.getNetwork());
            cnf.setNsu(resp.getNsu());
            cnf.setFiscalDocumentNumberLinked(resp.getFiscalDocumentNumberLinked());
            StatusResponse sts = instance.cnf(cnf);
            System.out.println("cnf:\n\n");
            System.out.println(sts.getOperation());
            System.out.println(sts.getIdentifier());
            System.out.println(sts.getFields());
        }
        System.out.println("conteúdo do arquivo:\n" + resp.getOriginalFile());
    }

    //@Test
    public void testNCN() throws Exception {
        System.out.println("");
        System.out.println("");
        System.out.println("testNCN");
        TefDial instance = new TefDial();
        instance.setRequestDirectory(new File("C:\\tef_dial\\req"));
        instance.setResponseDirectory(new File("C:\\tef_dial\\resp"));

        CRT crt = new CRT();
        crt.setCurrency(Currency.getInstance("BRL"));
        crt.setTotalValue(new BigDecimal("19.99"));
        crt.setFiscalDocumentNumberLinked(123456);

        ResponseMessage resp = instance.crt(crt);
        if (resp.isTransactionOk()) {
            NCN ncn = new NCN();
            ncn.setIdentifier(resp.getIdentifier());
            ncn.setControlCode(resp.getControlCode());
            ncn.setNetwork(resp.getNetwork());
            ncn.setNsu(resp.getNsu());
            ncn.setTaxDocumentNumberLinked(resp.getFiscalDocumentNumberLinked());
            StatusResponse sts = instance.ncn(ncn);
            System.out.println("ncn:\n\n");
            System.out.println(sts.getOperation());
            System.out.println(sts.getIdentifier());
            System.out.println(sts.getFields());
        }
        System.out.println("conteúdo do arquivo:\n" + resp.getOriginalFile());
    }
}