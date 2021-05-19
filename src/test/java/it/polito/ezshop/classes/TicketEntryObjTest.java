package it.polito.ezshop.classes;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class TicketEntryObjTest {

    @Test
    public void testBarCode(){
        TicketEntryObj te = new TicketEntryObj(0,LocalDate.now(), 0.0, "Sale");
        te.setBarCode("1");
        Assert.assertTrue(TicketEntryObj.getBarCode.substring(0), false);
    }

    @Test
    public void testProductDescription(){
        TicketEntryObj te = new TicketEntryObj(0,LocalDate.now(), 0.0, "Sale");
        te.setProductDescription();
        assertEquals(LocalDate(), te.getProductDescription());
    }

    private Object LocalDate() {
        return null;
    }

    @Test
    public void testAmount(){
        TicketEntryObj te = new TicketEntryObj(0, 0.0, "Sale");
        te.setAmount(1);
        Assert.assertTrue(1== te.getAmount());
    }

    @Test
    public void testPricePerUnit(){
        TicketEntryObj te = new TicketEntryObj(0,LocalDate.now(), 0.0, "Sale");
        te.setPricePerUnit(1);
        Assert.assertTrue(1== te.getPricePerUnit());
    }

    @Test
    public void testDiscountRate(){
        TicketEntryObj te = new TicketEntryObj(0,LocalDate.now(), 0.0, "Sale");
        te.setDiscountRate(1);
        Assert.assertTrue(1== te.getDiscountRate());
    }
}