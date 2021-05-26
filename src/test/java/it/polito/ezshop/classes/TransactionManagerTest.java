package it.polito.ezshop.classes;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class TransactionManagerTest {
    EZShop shop;
    TransactionManager tManager;
    @Before
    public void initTransactionManagerTests() {
        shop = new EZShop();
        tManager = shop.getTransactionManager();
        tManager.defineCreditCards();
    }
    
    @Test
    public void testBalanceUpdate() {
        assertTrue(tManager.recordBalanceUpdate(5.0));
        assertFalse(tManager.recordBalanceUpdate(-20.0));
        assertTrue(tManager.recordBalanceUpdate(-2.0));
    }
    
    @Test
    public void testLuhn() {
        assertTrue(tManager.luhn("79927398713"));
        assertFalse(tManager.luhn("-79927398713"));
        assertFalse(tManager.luhn("5"));
        assertFalse(tManager.luhn(""));
        assertFalse(tManager.luhn("iduhsidh"));
        assertFalse(tManager.luhn(null));
        
    }
    
    @Test
    public void testComputeBalance() {
        assertEquals(0, tManager.computeBalance(), 0.0);
    }
    
    @Test
    public void testStartSaleTransaction() {
        assertTrue(tManager.startSaleTransaction() >= 0);
    }
    
    @Test
    public void testAddProductToSale() {
    }
    
    @Test
    public void testDeleteProductFromSale() {
    }
    
    @Test
    public void testApplyDiscountRateToProduct() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException, InvalidDiscountRateException {
    int saleId= tManager.startSaleTransaction();
    ProductOrderManager poManager= shop.getProductOrderManager();
    poManager.createProductType("test", "123456789012", 5.0, "note");
    poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
    poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
    poManager.createProductType("test2", "12345678901286", 5.0, "note");
    poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
    poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
    tManager.addProductToSale(saleId, "12345678901286", 2);
    tManager.addProductToSale(saleId, "123456789012", 1);

    assertTrue(tManager.applyDiscountRateToProduct(saleId, "pr123cat12", 0.5));
    assertTrue(tManager.applyDiscountRateToProduct(saleId, "pr123cat12", 0));
    assertFalse(tManager.applyDiscountRateToProduct(saleId+1, "pr123cat12", 0.5));
    assertThrows(InvalidTransactionIdException.class, ()->tManager.applyDiscountRateToProduct(null, "pr123cat12", 0.5));
    assertThrows(InvalidTransactionIdException.class, ()->tManager.applyDiscountRateToProduct(0, "pr123cat12", 0.5));
    assertThrows(InvalidTransactionIdException.class, ()->tManager.applyDiscountRateToProduct(-1, "pr123cat12", 0.5));
    }

    @Test
    public void testApplyDiscountRateToSale() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException, InvalidDiscountRateException {
        int saleId= tManager.startSaleTransaction();
        ProductOrderManager poManager= shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "12345678901286", 2);
        tManager.addProductToSale(saleId, "123456789012", 1);


        assertTrue(tManager.applyDiscountRateToSale(saleId, 0.5));
        assertTrue(tManager.applyDiscountRateToSale(saleId, 0));
        assertFalse(tManager.applyDiscountRateToSale(saleId+1, 0.5));
        assertThrows(InvalidTransactionIdException.class, ()->tManager.applyDiscountRateToSale(null, 0.5));
        assertThrows(InvalidTransactionIdException.class, ()->tManager.applyDiscountRateToSale(0, 0.5));
        assertThrows(InvalidTransactionIdException.class, ()->tManager.applyDiscountRateToSale(-1, 0.5));
        assertThrows(InvalidDiscountRateException.class, ()->tManager.applyDiscountRateToSale(saleId, 1.5));
        assertThrows(InvalidDiscountRateException.class, ()->tManager.applyDiscountRateToSale(saleId, -0.5));
        assertThrows(InvalidDiscountRateException.class, ()->tManager.applyDiscountRateToSale(saleId, 1));

    }
    
    @Test
    public void testComputePointsForSale() throws InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidCreditCardException {
        int saleId= tManager.startSaleTransaction();
        ProductOrderManager poManager= shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "12345678901286", 2);
        tManager.addProductToSale(saleId, "123456789012", 1);
        assertTrue(tManager.computePointsForSale(saleId)>0);
        assertFalse(tManager.computePointsForSale(saleId+1)>0);
        assertThrows(InvalidTransactionIdException.class, ()->tManager.computePointsForSale(null));
        assertThrows(InvalidTransactionIdException.class, ()->tManager.computePointsForSale(0));
        assertThrows(InvalidTransactionIdException.class, ()->tManager.computePointsForSale(-1));
    }
    
    @Test
    public void testEndSaleTransaction() throws InvalidTransactionIdException, InvalidQuantityException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException {

        int saleId= tManager.startSaleTransaction();
        ProductOrderManager poManager= shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "12345678901286", 2);
        tManager.addProductToSale(saleId, "123456789012", 1);
        assertTrue(tManager.endSaleTransaction(saleId));
        tManager.receiveCashPayment(saleId, 50);
        assertFalse(tManager.endSaleTransaction(saleId));
        assertFalse(tManager.endSaleTransaction(saleId+1));
        assertThrows(InvalidTransactionIdException.class, ()->tManager.endSaleTransaction(null));
        assertThrows(InvalidTransactionIdException.class, ()->tManager.endSaleTransaction(-1));
    }
    
    @Test
    public void testDeleteSaleTransaction() throws InvalidTransactionIdException, InvalidQuantityException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException {

        int saleId= tManager.startSaleTransaction();
        ProductOrderManager poManager= shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "12345678901286", 1);
        tManager.addProductToSale(saleId, "123456789012", 1);
        int retCode=tManager.startReturnTransaction(saleId);
        String pBarCode= poManager.getProductTypesByDescription("test").get(0).getBarCode();
        tManager.returnProduct(retCode,pBarCode , 1);
        assertTrue(tManager.deleteSaleTransaction(retCode));
        tManager.endSaleTransaction(saleId);
        tManager.receiveCashPayment(saleId, 100);
        assertFalse(tManager.deleteSaleTransaction(saleId));
        assertFalse(tManager.deleteSaleTransaction(saleId+1));
        assertThrows(InvalidTransactionIdException.class, ()-> tManager.deleteSaleTransaction(null));
        assertThrows(InvalidTransactionIdException.class, ()-> tManager.deleteSaleTransaction(0));
        assertThrows(InvalidTransactionIdException.class, ()-> tManager.deleteSaleTransaction(-1));
    }
    
    @Test
    public void testGetSaleTransaction() throws InvalidTransactionIdException {
        assertThrows(InvalidTransactionIdException.class, () -> tManager.getSaleTransaction(null));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.getSaleTransaction(0));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.getSaleTransaction(-1));
        assertNull(tManager.getSaleTransaction(2));
        tManager.endSaleTransaction(tManager.startSaleTransaction());
        
    }

    @Test
    public void testGetAllOrders() {
        assertNotNull(tManager.getAllOrders());
    }
    
    @Test
    public void testStartReturnTransaction() throws InvalidTransactionIdException {
        int saleId = tManager.startSaleTransaction();
        try {
            assertTrue(tManager.startReturnTransaction(saleId) >= 0);
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
        }
        try {
            assertTrue(-1 == tManager.startReturnTransaction(saleId + 1));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTransactionIdException.class, () -> tManager.startReturnTransaction(null));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.startReturnTransaction(0));
    }
    
    @Test
    public void testReturnProduct() throws InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
        int saleId = tManager.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "12345678901286", 1);
        tManager.addProductToSale(saleId, "123456789012", 1);
        int retCode = tManager.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        String fakeCode = "1234567890128";
        poManager.createProductType("test2", "1234567890128", 5.0, "note");
        assertTrue(tManager.returnProduct(retCode, pBarCode, 1));
        assertFalse(tManager.returnProduct(retCode, pBarCode, 2));
        assertFalse(tManager.returnProduct(retCode, fakeCode, 1));
        assertFalse(tManager.returnProduct(retCode, "1234567890128", 1));
        assertFalse(tManager.returnProduct(retCode + 1, pBarCode, 1));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.returnProduct(null, pBarCode, 1));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.returnProduct(0, pBarCode, 1));
        assertThrows(InvalidProductCodeException.class, () -> tManager.returnProduct(retCode, null, 1));
        assertThrows(InvalidProductCodeException.class, () -> tManager.returnProduct(retCode, "", 1));
        assertThrows(InvalidProductCodeException.class, () -> tManager.returnProduct(retCode, "abc", 1));
        assertThrows(InvalidQuantityException.class, () -> tManager.returnProduct(retCode, pBarCode, 0));
        assertThrows(InvalidQuantityException.class, () -> tManager.returnProduct(retCode, pBarCode, -1));
    }
    
    @Test
    public void testEndReturnTransaction() throws InvalidTransactionIdException, InvalidQuantityException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, UnauthorizedException, InvalidLocationException {
        int saleId = tManager.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "12345678901286", 1);
        tManager.addProductToSale(saleId, "123456789012", 1);
        int retCode = tManager.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        tManager.returnProduct(retCode, pBarCode, 1);
        assertTrue(tManager.endReturnTransaction(retCode, false));
        retCode = tManager.startReturnTransaction(saleId);
        pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        tManager.returnProduct(retCode, pBarCode, 1);
        assertTrue(tManager.endReturnTransaction(retCode, true));
        
        assertFalse(tManager.endReturnTransaction(retCode, true));
        assertFalse(tManager.endReturnTransaction(retCode + 1, true));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.endReturnTransaction(null, true));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.endReturnTransaction(0, true));
    }
    
    @Test
    public void testDeleteReturnTransaction() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException {
        int saleId = tManager.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "12345678901286", 1);
        tManager.addProductToSale(saleId, "123456789012", 1);
        
        int retCode = tManager.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        tManager.returnProduct(retCode, pBarCode, 1);
        assertFalse(tManager.deleteReturnTransaction(retCode + 1));
        assertTrue(tManager.deleteReturnTransaction(retCode));
        assertFalse(tManager.deleteReturnTransaction(retCode));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.deleteReturnTransaction(0));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.deleteReturnTransaction(null));
    }
    
    @Test
    public void testReceiveCashPayment() throws InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException {
        int saleId = tManager.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "123456789012", 1);
        assertTrue(tManager.receiveCashPayment(saleId, 10) > 0);
        assertFalse(tManager.receiveCashPayment(saleId + 1, 10) > 0);
        assertFalse(tManager.receiveCashPayment(saleId, 1.0) > 0);
        assertThrows(InvalidTransactionIdException.class, () -> tManager.receiveCashPayment(null, 10));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.receiveCashPayment(0, 10));
    }
    
    @Test
    public void testReceiveCreditCardPayment() throws InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidCreditCardException {
        int saleId = tManager.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "123456789012", 1);
        String ccNumber = "79927398713";
        assertTrue(tManager.receiveCreditCardPayment(saleId, ccNumber));//correct
        assertFalse(tManager.receiveCreditCardPayment(saleId + 1, ccNumber));//wrong sale
        assertFalse(tManager.receiveCreditCardPayment(saleId, "59"));//not registered card
        int saleId2= tManager.startSaleTransaction();
        tManager.addProductToSale(saleId2, "123456789012", 1);
        tManager.addProductToSale(saleId2, "123456789012", 20);
        assertFalse(tManager.receiveCreditCardPayment(saleId2, ccNumber));//not enough money
        assertThrows(InvalidTransactionIdException.class, () -> tManager.receiveCreditCardPayment(null, ccNumber));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.receiveCreditCardPayment(0, ccNumber));
        assertThrows(InvalidCreditCardException.class, () -> tManager.receiveCreditCardPayment(saleId, null));
        assertThrows(InvalidCreditCardException.class, () -> tManager.receiveCreditCardPayment(saleId, ""));
        assertThrows(InvalidCreditCardException.class, () -> tManager.receiveCreditCardPayment(saleId, "11"));
    }
    
    @Test
    public void testReturnCashPayment() throws InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidCreditCardException {
        int saleId = tManager.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "12345678901286", 2);
        tManager.addProductToSale(saleId, "123456789012", 1);
        
        int retCode = tManager.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        tManager.returnProduct(retCode, pBarCode, 1);
        String ccNumber = "79927398713";
        tManager.receiveCreditCardPayment(saleId, ccNumber);
        assertFalse(tManager.returnCashPayment(retCode) > 0);// not ended
        tManager.endReturnTransaction(retCode, true);
        assertTrue(tManager.returnCashPayment(retCode) > 0);
        assertFalse(tManager.returnCashPayment(retCode + 1) > 0);
        assertThrows(InvalidTransactionIdException.class, () -> tManager.returnCashPayment(null));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.returnCashPayment(0));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.returnCashPayment(-1));
    }
    
    @Test
    public void testReturnCreditCardPayment() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException {
        int saleId = tManager.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "12345678901286", 2);
        tManager.addProductToSale(saleId, "123456789012", 1);
        
        int retCode = tManager.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        tManager.returnProduct(retCode, pBarCode, 1);
        String ccNumber = "79927398713";
        tManager.receiveCreditCardPayment(saleId, ccNumber);
        assertFalse(tManager.returnCreditCardPayment(retCode, ccNumber) > 0);// not ended
        tManager.endReturnTransaction(retCode, true);
        assertTrue(tManager.returnCreditCardPayment(retCode, ccNumber) > 0);
        assertFalse(tManager.returnCreditCardPayment(retCode + 1, ccNumber) > 0);
        assertFalse(tManager.returnCreditCardPayment(retCode, "59") > 0);//card doesn't exist
        assertThrows(InvalidTransactionIdException.class, () -> tManager.returnCreditCardPayment(null, ccNumber));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.returnCreditCardPayment(0, ccNumber));
        assertThrows(InvalidTransactionIdException.class, () -> tManager.returnCreditCardPayment(-1, ccNumber));
        assertThrows(InvalidCreditCardException.class, () -> tManager.returnCreditCardPayment(retCode, "11"));
        assertThrows(InvalidCreditCardException.class, () -> tManager.returnCreditCardPayment(retCode, ""));
        assertThrows(InvalidCreditCardException.class, () -> tManager.returnCreditCardPayment(retCode, null));
    }
    
    @Test
    public void testGetCreditsAndDebits() throws InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidCreditCardException {
        int saleId = tManager.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-11-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-12-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        tManager.addProductToSale(saleId, "12345678901286", 2);
        tManager.addProductToSale(saleId, "123456789012", 1);

        int retCode = tManager.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        tManager.returnProduct(retCode, pBarCode, 1);
        String ccNumber = "79927398713";
        tManager.receiveCreditCardPayment(saleId, ccNumber);
        tManager.endReturnTransaction(retCode, true);
        tManager.returnCreditCardPayment(retCode, ccNumber);
        assertFalse(tManager.getCreditsAndDebits(LocalDate.now().minusDays(1), LocalDate.now().minusDays(-1)).isEmpty()); //list exists
        assertTrue(tManager.getCreditsAndDebits(LocalDate.now().minusDays(5), LocalDate.now().minusDays(1)).isEmpty()); //list is empty


    }
    
    @Test
    public void testAddCompletedOrderInvalid() {
        OrderObj order = new OrderObj(1,
                new ProductTypeObj(0, 1, "hello test", "123456789012", "note", 22.0, 0, new Position()),
                25, 2);
        tManager.addOrder(order);
        assertNull(tManager.addCompletedOrder(order.getOrderId()));
        assertNull(tManager.addCompletedOrder(order.getOrderId() + 1));
    }
    
    @Test
    public void testAddCompletedOrderOk() {
        final int orderId = 1;
        OrderObj order = new OrderObj(orderId,
                new ProductTypeObj(0, 1, "hello test", "123456789012", "note", 22.0, 0, new Position()),
                25, 2);
        order.setStatus("PAYED");
        tManager.addOrder(order);
        final OrderObj payed = tManager.addCompletedOrder(order.getOrderId());
        assertEquals("COMPLETED", payed.getStatus());
        order.setStatus("COMPLETED");
        assertEquals(1, tManager.getAllOrders()
                .stream()
                .filter(o -> o.getOrderId() == orderId && o.getStatus().equals("COMPLETED"))
                .count());
        final OrderObj completed = tManager.addCompletedOrder(order.getOrderId());
        assertEquals("COMPLETED", completed.getStatus());
        assertEquals(1, tManager.getAllOrders()
                .stream()
                .filter(o -> o.getOrderId() == orderId && o.getStatus().equals("COMPLETED"))
                .count());
        
    }

    @Test
    public void testAddIssuedOrder() {
        OrderObj order = new OrderObj(1,
                new ProductTypeObj(0, 1, "hello test", "123456789012", "note", 22.0, 0, new Position()),
                25, 2);
        final double balance = tManager.computeBalance();
        assertTrue(tManager.addOrder(order));
        assertTrue(tManager.getAllOrders().contains(order));
        assertEquals(balance, tManager.computeBalance(), 0);
        assertEquals(1, tManager.getAllOrders()
                .stream()
                .filter(o -> o.getOrderId().equals(order.getOrderId()) && o.getStatus().equals("ISSUED"))
                .count());
    }
    
    @Test
    public void testAddPayedOrderFalse() {
        OrderObj order = new OrderObj(1,
                new ProductTypeObj(0, 1, "hello test", "123456789012", "note", 22.0, 0, new Position()),
                25, 2);
        assertTrue(tManager.addOrder(new OrderObj(order)));
        order.setStatus("PAYED");
        final double balance = tManager.computeBalance();
        assertFalse(tManager.addOrder(order));
        assertFalse(tManager.getAllOrders().contains(order));
        assertEquals(balance, tManager.computeBalance(), 0);
    }
    
    @Test
    public void testAddPayedOrderOk() {
        OrderObj order = new OrderObj(1,
                new ProductTypeObj(0, 1, "hello test", "123456789012", "note", 22.0, 0, new Position()),
                25, 2);
        assertTrue(tManager.addOrder(new OrderObj(order)));
        order.setStatus("PAYED");
        tManager.recordBalanceUpdate(100);
        final double balance = tManager.computeBalance();
        assertTrue(tManager.addOrder(order));
        assertEquals(1, tManager.getAllOrders()
                .stream()
                .filter(o -> o.getOrderId().equals(order.getOrderId()) && o.getStatus().equals("PAYED"))
                .count());
        assertEquals(balance - order.getQuantity() * order.getPricePerUnit(), tManager.computeBalance(), 0);
    }
    
    @After
    public void clearTransactionManagerTests() {
        shop.reset();
    }
}
