package it.polito.ezshop.classes;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.*;
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
        EZShop shop = new EZShop();
        TransactionManager tManager = shop.getTransactionManager();
        int saleId= tManager.startSaleTransaction();
        try {
            assertTrue(tManager.startReturnTransaction(saleId)>=0);
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
        }
        try {
            assertTrue(-1== tManager.startReturnTransaction(saleId+1));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void returnProduct() throws InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException {
        EZShop shop = new EZShop();
        TransactionManager tManager = shop.getTransactionManager();
        int saleId= tManager.startSaleTransaction();
        ProductOrderManager poManager= shop.getProductOrderManager();

        try {
             poManager.createProductType("test", "123456789012", 5.0, "note");
        } catch (InvalidProductCodeException e) {
            e.printStackTrace();
        } catch (InvalidProductDescriptionException e) {
            e.printStackTrace();
        } catch (InvalidPricePerUnitException e) {
            e.printStackTrace();
        }
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        tManager.addProductToSale(saleId, "123456789012", 1);
        int retCode=tManager.startReturnTransaction(saleId);
        String pBarCode= poManager.getProductTypesByDescription("test").get(0).getBarCode();
        String fakeCode = "12345678901286";
        poManager.createProductType("test2", "1234567890128", 5.0, "note");
        assertTrue(tManager.returnProduct(retCode,pBarCode , 1));
        assertFalse(tManager.returnProduct(retCode,pBarCode , 2));
        assertFalse(tManager.returnProduct(retCode,fakeCode , 1));
        assertFalse(tManager.returnProduct(retCode,"1234567890128" , 1));
        assertFalse(tManager.returnProduct(retCode+1,pBarCode , 1));


    }

    @Test
    public void endReturnTransaction() throws InvalidTransactionIdException, InvalidQuantityException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, UnauthorizedException {
        EZShop shop = new EZShop();
        TransactionManager tManager = shop.getTransactionManager();
        int saleId= tManager.startSaleTransaction();
        ProductOrderManager poManager= shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        tManager.addProductToSale(saleId, "123456789012", 1);
        int retCode=tManager.startReturnTransaction(saleId);
        String pBarCode= poManager.getProductTypesByDescription("test").get(0).getBarCode();
        tManager.returnProduct(retCode,pBarCode , 1);
        assertTrue(tManager.endReturnTransaction(retCode, false));
        retCode=tManager.startReturnTransaction(saleId);
        pBarCode= poManager.getProductTypesByDescription("test").get(0).getBarCode();
        tManager.returnProduct(retCode,pBarCode , 1);
        assertTrue(tManager.endReturnTransaction(retCode, true));

        assertFalse(tManager.endReturnTransaction(retCode, true));
        assertFalse(tManager.endReturnTransaction(retCode+1, true));
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
    public void getCreditsAndDebits() {
    }


    @Test
    public void clear() {
    }


    @Test
    public void addCompletedOrder() {
    }

    @Test
    public void addOrder() {
    }
}
