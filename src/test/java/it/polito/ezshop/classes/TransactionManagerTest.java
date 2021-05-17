package it.polito.ezshop.classes;

import static org.junit.Assert.*;

import it.polito.ezshop.EZShop;
import org.junit.Test;
import it.polito.ezshop.data.*;

public class TransactionManagerTest {

	@Test
	public void testBalanceUpdate() {
		it.polito.ezshop.data.EZShop shop= new it.polito.ezshop.data.EZShop();
		TransactionManager t = new TransactionManager(shop);
		assertTrue( t.recordBalanceUpdate(5.0)== true);
		assertFalse( t.recordBalanceUpdate(-20.0));
		assertTrue(t.recordBalanceUpdate(-2.0));
	}

	@Test
	public  void testLuhn (){
		it.polito.ezshop.data.EZShop shop= new it.polito.ezshop.data.EZShop();
		TransactionManager t = new TransactionManager(shop);
		assertTrue( t.luhn("79927398713"));
	}
	//TODO test compute balace
}
