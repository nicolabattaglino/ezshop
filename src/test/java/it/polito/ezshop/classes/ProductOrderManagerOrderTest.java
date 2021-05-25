package it.polito.ezshop.classes;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductOrderManagerOrderTest {
    
    private static ProductOrderManager p;
    private static EZShop ezShop = new EZShop();
    
    @BeforeClass
    public static void initManager() {
        p = ezShop.getProductOrderManager();
    }
    
    @Test
    public void testIssueOrderInvalid() {
        assertThrows(InvalidProductCodeException.class, () -> p.issueOrder("133456789012", 10, 25.0));
        assertThrows(InvalidQuantityException.class, () -> p.issueOrder("123456789012", 0, 25.0));
        assertThrows(InvalidQuantityException.class, () -> p.issueOrder("123456789012", -10, 25.0));
        assertThrows(InvalidPricePerUnitException.class, () -> p.issueOrder("123456789012", 10, 0.0));
        assertThrows(InvalidPricePerUnitException.class, () -> p.issueOrder("123456789012", 10, -1.0));
    }
    
    @Test
    public void testIssueOrderOk() {
        try {
            assertEquals(-1, (int) p.issueOrder("123456789012", 20, 25.0));
            Integer id = p.createProductType("hello test", "123456789012", 22.0, "note");
            assertNotEquals(-1, (int) p.issueOrder("123456789012", 20, 25.0));
        } catch (InvalidQuantityException | InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidProductCodeException e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    @Test
    public void testPayOrderForInvalid() {
        assertThrows(InvalidProductCodeException.class, () -> p.payOrderFor("00000", 10, 10));
        assertThrows(InvalidQuantityException.class, () -> p.payOrderFor("123456789012", 0, 10));
        assertThrows(InvalidQuantityException.class, () -> p.payOrderFor("123456789012", -10, 10));
        assertThrows(InvalidPricePerUnitException.class, () -> p.payOrderFor("0123456789012", 10, 0));
        assertThrows(InvalidPricePerUnitException.class, () -> p.payOrderFor("0123456789012", 10, -10));
    }
    
    @Test
    public void testPayOrderForNoProduct() {
        try {
            assertEquals(-1, (int) p.payOrderFor("0123456789012", 10, 10));
        } catch (InvalidProductCodeException | InvalidQuantityException | InvalidPricePerUnitException e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    @Test
    public void testPayOrderForNotEnoughBalance() {
        try {
            Integer id = p.createProductType("hello test", "123456789012", 22.0, "note");
            assertEquals(-1, (int) p.payOrderFor("0123456789012", 10, 10));
            
        } catch (InvalidProductCodeException | InvalidQuantityException | InvalidPricePerUnitException | InvalidProductDescriptionException e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    @Test
    public void testPayOrderForOk() {
        try {
            Integer id = p.createProductType("hello test", "123456789012", 22.0, "note");
            ezShop.getTransactionManager().recordBalanceUpdate(100);
            assertNotEquals(-1, (int) p.payOrderFor("123456789012", 10, 10));
            
        } catch (InvalidProductCodeException | InvalidQuantityException | InvalidPricePerUnitException | InvalidProductDescriptionException e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    @Test
    public void testPayOrderFor() {
        assertThrows(InvalidOrderIdException.class, () -> p.payOrder(null));
        assertThrows(InvalidOrderIdException.class, () -> p.payOrder(0));
        assertThrows(InvalidOrderIdException.class, () -> p.payOrder(-1));
    }
    
    @Test
    public void testPayOrderNotPresent() {
        try {
            assertFalse(p.payOrder(5));
        } catch (UnauthorizedException | InvalidOrderIdException e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    @Test
    public void testPayOrderNotEnoughBalance() {
        try {
            p.createProductType("hello test", "123456789012", 22.0, "note");
            Integer id = p.issueOrder("123456789012", 20, 10.0);
            assertFalse(p.payOrder(id));
        } catch (InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidProductCodeException | InvalidQuantityException | InvalidOrderIdException | UnauthorizedException e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    @Test
    public void testPayOrderOk() {
        try {
            p.createProductType("hello test", "123456789012", 22.0, "note");
            Integer id = p.issueOrder("123456789012", 2, 10.0);
            ezShop.getTransactionManager().recordBalanceUpdate(100);
            assertTrue(p.payOrder(id));
        } catch (InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidProductCodeException | InvalidQuantityException | InvalidOrderIdException | UnauthorizedException e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    @Test
    public void testPayOrderPayedAndCompleted() {
        try {
            p.createProductType("hello test", "123456789012", 22.0, "note");
            Integer id = p.issueOrder("123456789012", 2, 10.0);
            ezShop.getTransactionManager().recordBalanceUpdate(100);
            assertTrue(p.payOrder(id));
            //the order is already payed
            assertTrue(p.payOrder(id));
            ezShop.getTransactionManager().addCompletedOrder(id);
            //the order is now completed
            assertFalse(p.payOrder(id));
        } catch (InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidProductCodeException | InvalidQuantityException | InvalidOrderIdException | UnauthorizedException e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    @Test
    public void testRecordOrderArrivalInvalidOrderId() {
        assertThrows(InvalidOrderIdException.class, () -> p.recordOrderArrival(null));
        assertThrows(InvalidOrderIdException.class, () -> p.recordOrderArrival(0));
        assertThrows(InvalidOrderIdException.class, () -> p.recordOrderArrival(-1));
    }
    
    @Test
    public void testRecordOrderArrivalInvalidProductLocation() {
        Integer id = null;
        try {
            p.createProductType("hello test", "123456789012", 22.0, "note");
            id = p.issueOrder("123456789012", 2, 10.0);
            ezShop.getTransactionManager().recordBalanceUpdate(100);
            p.payOrder(id);
        } catch (InvalidProductCodeException | InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidQuantityException | InvalidOrderIdException | UnauthorizedException e) {
            fail("Unexpected exception: " + e);
        }
        Integer finalId = id;
        assertThrows(InvalidLocationException.class, () -> p.recordOrderArrival(finalId));
    }
    
    @Test
    public void testRecordOrderArrivalNotFound() {
        try {
            p.recordOrderArrival(10);
        } catch (InvalidOrderIdException | InvalidLocationException e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    @Test
    public void testRecordOrderArrivalOk() {
        try {
            Integer prodId = p.createProductType("hello test", "123456789012", 22.0, "note");
            Integer id = p.issueOrder("123456789012", 2, 10.0);
            ezShop.getTransactionManager().recordBalanceUpdate(100);
            p.payOrder(id);
            p.updatePosition(prodId, "10-10-10");
            p.recordOrderArrival(id);
            assertTrue(p.payOrder(id));
            assertEquals(2, (int) p.getProductTypeByBarCode("123456789012").getQuantity());
        } catch (InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidProductCodeException | InvalidQuantityException | InvalidOrderIdException | UnauthorizedException | InvalidLocationException | InvalidProductIdException e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    //TODO VERIFY ALL FILES (PERSISTENCE)
    @After
    public void clear() {
        ezShop.reset();
    }
    
    
}
