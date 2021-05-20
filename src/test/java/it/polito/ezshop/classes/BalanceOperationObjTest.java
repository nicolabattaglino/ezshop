package it.polito.ezshop.classes;

import static org.junit.Assert.*;

import org.junit.Test;

import java.time.LocalDate;

public class BalanceOperationObjTest {

	@Test
	public void testCredit() {
		Credit c = new Credit(0, LocalDate.now(), "credit");
		c.setMoney(0.0);
		assertTrue(c.getMoney()==0.0);
		c.setMoney(-1.0);
		assertTrue(c.getMoney()==-1.0);
		c.setDate(LocalDate.now());
		c.setBalanceId(1);
		c.setType("Credit");
		assertEquals(c.getDate(),LocalDate.now());
		assertEquals(c.getBalanceId(),1);
		assertEquals(c.getType(),"Credit");
	}
	@Test
	public void testDebit() {
		Debit c = new Debit(0, LocalDate.now(), "debit");
		c.setMoney(0.0);
		assertTrue(c.getMoney()==0.0);
		c.setMoney(-1.0);
		assertTrue(c.getMoney()==-1.0);
		assertEquals(c.getDate(),LocalDate.now());
		assertEquals(c.getBalanceId(),0);
		assertEquals(c.getType(),"debit");
	}

}
