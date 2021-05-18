package it.polito.ezshop.classes;

import static org.junit.Assert.*;
import java.util.*;
import it.polito.ezshop.data.TicketEntry;
import java.time.LocalDate;
import org.junit.Test;
import java.util.ArrayList;

public class SaleTransactionObjTest {
	@Test
	public void testEntries() {
		//TODO fix
		SaleTransactionObj s = new SaleTransactionObj(0,LocalDate.now(), 0.0, "Sale");
		List<TicketEntry> lt = new ArrayList<TicketEntry>();
		s.setEntries(lt);
		assertEquals(lt, s.getEntries());
		TicketEntry t = new TicketEntryObj(5,"barCode","description", 2.0);

		s.addEntry(t);
		lt.add(t);
		assertEquals(lt, s.getEntries());
	}

	@Test
	public void testPrice() {
		SaleTransactionObj s = new SaleTransactionObj(0,LocalDate.now(), 0.0, "Sale");
		s.setPrice(1);
		assertTrue(1 == s.getPrice());
	}

	@Test
	public void testDiscountRate() {
		SaleTransactionObj s = new SaleTransactionObj(0,LocalDate.now(), 0.0, "Sale");
		s.setDiscountRate(1.0);
		assertEquals(1.0, s.getDiscountRate(), 0.0);
	}

	@Test
	public void testBalanceId() {
		SaleTransactionObj s = new SaleTransactionObj(0,LocalDate.now(), 0.0, "Sale");
		s.setBalanceId(1);
		assertEquals(1, s.getBalanceId());
	}

	@Test
	public void testLocalDate() {
		SaleTransactionObj s = new SaleTransactionObj(0,LocalDate.now(), 0.0, "Sale");
		s.setDate(LocalDate.now());
		assertEquals(LocalDate.now(), s.getDate());
	}

	@Test
	public void testMoney() {
		SaleTransactionObj s = new SaleTransactionObj(0,LocalDate.now(), 0.0, "Sale");
		s.setMoney(1.0);
		assertEquals(1.0, s.getMoney(), 0.0);
	}

}

