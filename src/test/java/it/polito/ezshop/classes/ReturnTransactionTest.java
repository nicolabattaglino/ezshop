package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReturnTransactionTest {
    
    @Test
    public void testBalanceId() {
        ReturnTransaction r = new ReturnTransaction(0, LocalDate.now(), 0.0, "Return", 0);
        r.setBalanceId(1);
        assertTrue(1 == r.getBalanceId());
    }
    
    @Test
    public void testLocalDate() {
        ReturnTransaction r = new ReturnTransaction(0, LocalDate.now(), 0.0, "Return", 0);
        r.setDate(LocalDate.now());
        assertEquals(LocalDate.now(), r.getDate());
    }
    
    @Test
    public void testMoney() {
        ReturnTransaction r = new ReturnTransaction(0, LocalDate.now(), 0.0, "Return", 0);
        r.setMoney(1.0);
        assertTrue(1.0 == r.getMoney());
    }
    
    @Test
    public void testReturningId() {
        ReturnTransaction r = new ReturnTransaction(0, LocalDate.now(), 0.0, "Return", 0);
        assertTrue(0 == r.getTransactionID());
    }
    
    @Test
    public void testEntries() {
        ReturnTransaction r = new ReturnTransaction(0, LocalDate.now(), 0.0, "Return", 0);
        List<TicketEntry> lt = new ArrayList<TicketEntry>();
        r.setEntries(lt);
        assertEquals(lt, r.getEntries());
        TicketEntryObj t = new TicketEntryObj(1, "a", "b", 1.0);
        
        r.addEntry(t);
        lt.add(t);
        assertEquals(lt, r.getEntries());
    }
    
    @Test
    public void testPrice() {
        ReturnTransaction r = new ReturnTransaction(0, LocalDate.now(), 0.0, "Return", 0);
        r.setPrice(1);
        assertTrue(1 == r.getPrice());
    }
    
    @Test
    public void testStatus() {
        ReturnTransaction r = new ReturnTransaction(0, LocalDate.now(), 0.0, "Return", 0);
        r.setStatus(ReturnStatus.CLOSED);
        assertEquals(ReturnStatus.CLOSED, r.getStatus());
    }
    
    @Test
    public void testType() {
        ReturnTransaction r = new ReturnTransaction(0, LocalDate.now(), 0.0, "Return", 0);
        r.setType("Sale");
        assertEquals("Sale", r.getType());
    }
    
    @Test
    public void testCopyContructor() {
        ReturnTransaction r = new ReturnTransaction(0, LocalDate.now(), 0.0, "Return", 0);
        List<TicketEntry> lt = new ArrayList<TicketEntry>();
        TicketEntry t1 = new TicketEntryObj(1, "a", "b", 1.0);
        TicketEntry t2 = new TicketEntryObj(2, "a", "b", 1.0);
        lt.add(t1);
        lt.add(t2);
        r.setEntries(lt);
        ReturnTransaction r2 = new ReturnTransaction(r);
        assertEquals(r2.getEntries(), r.getEntries());
    }
    
}
