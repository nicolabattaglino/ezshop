package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SaleTransactionObjTest {
    @Test
    public void testEntries() {
        SaleTransactionObj s = new SaleTransactionObj(0, LocalDate.now(), 0.0, "Sale");
        List<TicketEntry> lt = new ArrayList<TicketEntry>();
        s.setEntries(lt);
        assertEquals(lt, s.getEntries());
        TicketEntry t = new TicketEntryObj(5, "barCode", "description", 2.0);
        
        s.addEntry(t);
        lt.add(t);
        assertEquals(lt, s.getEntries());
    }
    
    @Test
    public void testPrice() {
        SaleTransactionObj s = new SaleTransactionObj(0, LocalDate.now(), 0.0, "Sale");
        s.setPrice(0);
        assertTrue(0 == s.getPrice());
    }
    
    @Test
    public void testDiscountRate() {
        SaleTransactionObj s = new SaleTransactionObj(0, LocalDate.now(), 0.0, "Sale");
        s.setDiscountRate(1.0);
        assertEquals(1.0, s.getDiscountRate(), 0.0);
    }
    
    @Test
    public void testBalanceId() {
        SaleTransactionObj s = new SaleTransactionObj(0, LocalDate.now(), 0.0, "Sale");
        s.setBalanceId(1);
        assertEquals(1, s.getBalanceId());
    }
    
    @Test
    public void testLocalDate() {
        SaleTransactionObj s = new SaleTransactionObj(0, LocalDate.now(), 0.0, "Sale");
        s.setDate(LocalDate.now());
        assertEquals(LocalDate.now(), s.getDate());
    }
    
    @Test
    public void testMoney() {
        SaleTransactionObj s = new SaleTransactionObj(0, LocalDate.now(), 0.0, "Sale");
        s.setMoney(1.0);
        assertEquals(1.0, s.getMoney(), 0.0);
    }
    
    @Test
    public void testType() {
        SaleTransactionObj s = new SaleTransactionObj(0, LocalDate.now(), 0.0, "Sale");
        s.setType("Sale");
        assertEquals("Sale", s.getType());
    }
    
    @Test
    public void testTicketNumber() {
        SaleTransactionObj s = new SaleTransactionObj(0, LocalDate.now(), 0.0, "Sale");
        s.setTicketNumber(1);
        assertTrue(1 == s.getTicketNumber());
    }
    
    @Test
    public void testStatus() {
        SaleTransactionObj s = new SaleTransactionObj(0, LocalDate.now(), 0.0, "Sale");
        s.setStatus(SaleStatus.CLOSED);
        assertEquals(SaleStatus.CLOSED, s.getStatus());
    }
    
    @Test
    public void testCopyContructor() {
        SaleTransactionObj s = new SaleTransactionObj(0, LocalDate.now(), 0.0, "Sale");
        List<TicketEntry> lt = new ArrayList<TicketEntry>();
        TicketEntry t1 = new TicketEntryObj(1, "a", "b", 1.0);
        TicketEntry t2 = new TicketEntryObj(2, "a", "b", 1.0);
        lt.add(t1);
        lt.add(t2);
        s.setEntries(lt);
        s.deleteEntry(t1);
        SaleTransactionObj s2 = new SaleTransactionObj(s);
        assertEquals(s2.getEntries(), s.getEntries());
    }
}