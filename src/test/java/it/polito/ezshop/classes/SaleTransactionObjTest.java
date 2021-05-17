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
