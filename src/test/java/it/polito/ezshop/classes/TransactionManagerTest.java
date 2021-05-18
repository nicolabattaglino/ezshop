package it.polito.ezshop.classes;

import static org.junit.Assert.*;


import org.junit.Test;
import it.polito.ezshop.data.*;

public class TransactionManagerTest {

	@Test
	public void testBalanceUpdate() {
		it.polito.ezshop.data.EZShop shop= new it.polito.ezshop.data.EZShop();
		TransactionManager t = new TransactionManager(shop);
		assertEquals(true, t.recordBalanceUpdate(5.0));
		assertFalse( t.recordBalanceUpdate(-20.0));
		assertTrue(t.recordBalanceUpdate(-2.0));
	}

	@Test
	public  void testLuhn (){
		it.polito.ezshop.data.EZShop shop= new it.polito.ezshop.data.EZShop();
		TransactionManager t = new TransactionManager(shop);
		assertTrue( t.luhn("79927398713"));
		assertFalse( t.luhn("-79927398713"));
		assertFalse(t.luhn("5"));
		assertFalse(t.luhn(""));
		assertFalse(t.luhn("iduhsidh"));

	}

	@Test
	public  void testComputeBalance (){
		it.polito.ezshop.data.EZShop shop= new it.polito.ezshop.data.EZShop();
		TransactionManager t = new TransactionManager(shop);
		assertEquals(0, t.computeBalance(), 0.0);
	}
}
