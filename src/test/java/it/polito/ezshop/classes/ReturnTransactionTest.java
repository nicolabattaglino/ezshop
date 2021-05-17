package it.polito.ezshop.classes;

import static org.junit.Assert.*;
import java.util.*;

import it.polito.ezshop.data.TicketEntry;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReturnTransactionTest {

	@Test
	public void testBalanceId(){
		ReturnTransaction r = new ReturnTransaction(LocalDate.now(), 0.0, "Return", 0);
		r.setBalanceId(1);
		assertTrue(1== r.getBalanceId());
	}

	@Test
	public void testLocalDate(){
		ReturnTransaction r = new ReturnTransaction(LocalDate.now(), 0.0, "Return", 0);
		r.setDate(LocalDate.now());
		assertEquals(LocalDate.now() , r.getDate());
	}

	@Test
	public void testMoney(){
		ReturnTransaction r = new ReturnTransaction(LocalDate.now(), 0.0, "Return", 0);
		r.setMoney(1.0);
		assertTrue(1.0== r.getMoney());
	}

	@Test
	public void testReturningId(){
		ReturnTransaction r = new ReturnTransaction(LocalDate.now(), 0.0, "Return", 0);
		assertTrue(0== r.getTransactionID());
	}

	@Test
	public void testTicketNumber(){
		ReturnTransaction r = new ReturnTransaction(LocalDate.now(), 0.0, "Return", 0);
		r.setTicketNumber(1);
		assertTrue(1== r.getTicketNumber());
	}

	@Test
	public void testEntries(){
		ReturnTransaction r = new ReturnTransaction(LocalDate.now(), 0.0, "Return", 0);
		List<TicketEntry> lt= new ArrayList<TicketEntry>();
		r.setEntries(lt);
		assertEquals(lt, r.getEntries());
		TicketEntry t =null;

		r.addEntry(t);
		lt.add(t);
		assertEquals(lt, r.getEntries());
	}

	@Test
	public void testPrice(){
		ReturnTransaction r = new ReturnTransaction(LocalDate.now(), 0.0, "Return", 0);
		r.setPrice(1);
		assertTrue(1== r.getPrice());
	}

	@Test
	public void testStatus(){
		ReturnTransaction r = new ReturnTransaction(LocalDate.now(), 0.0, "Return", 0);
		r.setStatus("closed");
		assertEquals("closed", r.getStatus());
	}

	@Test
	public void testType(){
		ReturnTransaction r = new ReturnTransaction(LocalDate.now(), 0.0, "Return", 0);
		r.setType("Sale");
		assertEquals("Sale", r.getType());
	}

}
