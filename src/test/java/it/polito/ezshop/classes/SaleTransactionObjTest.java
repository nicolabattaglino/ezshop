package it.polito.ezshop.classes;

import static org.junit.Assert.*;
import java.util.*;
import it.polito.ezshop.data.TicketEntry;
import java.time.LocalDate;
import org.junit.Test;
import java.util.ArrayList;

public class SaleTransactionObjTest {

	@Test
	public void testEntries(){
		SaleTransactionObjTest s = new SaleTransactionObjTest();
		List<TicketEntry> lt= new ArrayList<TicketEntry>();
		s.setEntries(lt);
		assertEquals(lt, s.getEntries());
		TicketEntry t =null;

		s.addEntry(t);
		lt.add(t);
		assertEquals(lt, s.getEntries());
	}

	@Test
	public void testPrice(){
		SaleTransactionObj s = new SaleTransactionObj();
		s.setPrice(1);
		assertTrue(1== s.getPrice());
	}

	@Test
	public void testDiscountRate(){
		SaleTransactionObj s = new SaleTransactionObj();
		s.setDiscountRate(1.0);
		assertTrue(1.0== s.getDiscountRate());
	}

	@Test
	public void testBalanceId(){
		SaleTransactionObj s = new SaleTransactionObj();
		s.setBalanceId(1);
		assertTrue(1== s.getBalanceId());
	}

	@Test
	public void testLocalDate(){
		SaleTransactionObj s = new SaleTransactionObj();
		s.setDate(LocalDate.now());
		assertEquals(LocalDate.now() , s.getDate());
	}

	@Test
	public void testMoney(){
		SaleTransactionObj s = new SaleTransactionObj();
		s.setMoney(1.0);
		assertTrue(1.0== s.getMoney());
	}
