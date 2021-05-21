package it.polito.ezshop.classes;

import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionManagerTest {

	@Test
	public void testBalanceUpdate() {

		TransactionManager t = new TransactionManager(null);
		assertTrue(t.recordBalanceUpdate(5.0));
		assertFalse(t.recordBalanceUpdate(-20.0));
		assertTrue(t.recordBalanceUpdate(-2.0));
	}
	
	@Test
	public void testLuhn() {
		it.polito.ezshop.data.EZShop shop = new it.polito.ezshop.data.EZShop();
		TransactionManager t = new TransactionManager(shop);
		assertTrue(t.luhn("79927398713"));
		assertFalse(t.luhn("-79927398713"));
		assertFalse(t.luhn("5"));
		assertFalse(t.luhn(""));
		assertFalse(t.luhn("iduhsidh"));
		assertFalse(t.luhn(null));
		
	}
	
	@Test
	public void testComputeBalance() {
		it.polito.ezshop.data.EZShop shop = new it.polito.ezshop.data.EZShop();
		TransactionManager t = new TransactionManager(shop);
		assertEquals(0, t.computeBalance(), 0.0);
	}

    @Test
    public void startSaleTransaction() {
    }

    @Test
    public void addProductToSale() {
    }

    @Test
    public void deleteProductFromSale() {
    }

    @Test
    public void applyDiscountRateToProduct() {
    }

    @Test
    public void applyDiscountRateToSale() {
    }

    @Test
    public void computePointsForSale() {
    }

    @Test
    public void endSaleTransaction() {
    }

    @Test
    public void deleteSaleTransaction() {
    }

    @Test
    public void getSaleTransaction() {
    }

    @Test
    public void getAllOrders() {
    }

    @Test
    public void startReturnTransaction() {
    }

    @Test
    public void returnProduct() {
    }

    @Test
    public void endReturnTransaction() {
    }

    @Test
    public void deleteReturnTransaction() {
    }

    @Test
    public void receiveCashPayment() {
    }

    @Test
    public void receiveCreditCardPayment() {
    }

    @Test
    public void returnCashPayment() {
    }

    @Test
    public void returnCreditCardPayment() {
    }

    @Test
    public void recordBalanceUpdate() {
    }

    @Test
    public void getCreditsAndDebits() {
    }

    @Test
    public void computeBalance() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void luhn() {
    }

    @Test
    public void addCompletedOrder() {
    }

    @Test
    public void addOrder() {
    }
}
