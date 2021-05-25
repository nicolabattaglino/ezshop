package it.polito.ezshop.classes;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductOrderManagerOrderTest {
    
    private static ProductOrderManager p;
    
    @BeforeClass
    public static void initManager() {
        p = (new EZShop()).getProductOrderManager();
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
            assertEquals(-1, (int) p.issueOrder("123456789012", 0, 25.0));
            Integer id = p.createProductType("hello test", "123456789012", 22.0, "note");
            assertEquals(-1, (int) p.issueOrder("123456789012", 0, 25.0));
            
            
        } catch (InvalidQuantityException | InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidProductCodeException e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    @Test
    public void testPayOrderFor() {
    }
    
    @Test
    public void testPayOrder() {
    }
    
    @Test
    public void testRecordOrderArrival() {
    }
    
    @After
    public void clear() {
        p.clear();
    }
    
    
}
