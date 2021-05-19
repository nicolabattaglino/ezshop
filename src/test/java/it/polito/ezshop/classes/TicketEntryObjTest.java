package it.polito.ezshop.classes;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TicketEntryObjTest {

    @Test
    public void testBarCode(){
        TicketEntryObj te = new TicketEntryObj(0, "a", "b",0.0);
        te.setBarCode("1");
        Assert.assertTrue(te.getBarCode()== "1");
    }

    @Test
    public void testProductDescription(){
        TicketEntryObj te = new TicketEntryObj(0, "a", "b",0.0);
        te.setProductDescription("description");
        assertEquals("description", te.getProductDescription());
    }



    @Test
    public void testAmount(){
        TicketEntryObj te = new TicketEntryObj(0, "a", "b",0.0);
        te.setAmount(1);
        Assert.assertTrue(1== te.getAmount());
    }

    @Test
    public void testPricePerUnit(){
        TicketEntryObj te = new TicketEntryObj(0, "a", "b",0.0);
        te.setPricePerUnit(1.0);
        Assert.assertTrue(1.0== te.getPricePerUnit());
    }

    @Test
    public void testDiscountRate(){
        TicketEntryObj te = new TicketEntryObj(0, "a", "b",0.0);
        te.setDiscountRate(1.0);
        Assert.assertTrue(1.0== te.getDiscountRate());
    }
}