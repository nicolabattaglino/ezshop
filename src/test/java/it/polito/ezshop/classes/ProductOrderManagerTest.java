package it.polito.ezshop.classes;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProductOrderManagerTest {
    
    @Test
    public void testCheckBarcodeNull() {
        ProductOrderManager p = new ProductOrderManager(null);
        assertFalse(p.checkBarcode(null));
    }
    
    @Test
    public void testCheckBarcodeNonNum() {
        ProductOrderManager p = new ProductOrderManager(null);
        assertFalse(p.checkBarcode("a2bcde!"));
    }
    
    @Test
    public void testCheckBarcodeLenghtLessThan12() {
        ProductOrderManager p = new ProductOrderManager(null);
        assertFalse(p.checkBarcode("1234567"));
    }
    
    @Test
    public void testCheckBarcodeLenghtGreaterThan14() {
        ProductOrderManager p = new ProductOrderManager(null);
        assertFalse(p.checkBarcode("123456789012345"));
    }
    
    @Test
    public void testCheckBarcodeCheckDigitNotValid() {
        ProductOrderManager p = new ProductOrderManager(null);
        assertFalse(p.checkBarcode("123456789013"));
        assertFalse(p.checkBarcode("1234567890137"));
        assertFalse(p.checkBarcode("12345678901377"));
    }
    
    @Test
    public void testCheckBarcodeCheckDigitValid() {
        ProductOrderManager p = new ProductOrderManager(null);
        assertTrue(p.checkBarcode("123456789012"));
        assertTrue(p.checkBarcode("1234567890128"));
        assertTrue(p.checkBarcode("12345678901286"));
    }

    @Test
    public void checkBarcode() {
    }

    @Test
    public void createProductType() {
    }

    @Test
    public void updateProduct() {
    }

    @Test
    public void deleteProductType() {
    }

    @Test
    public void getAllProductTypes() {
    }

    @Test
    public void getProductTypeByBarCode() {
    }

    @Test
    public void getProductTypesByDescription() {
    }

    @Test
    public void updateQuantity() {
    }

    @Test
    public void updatePosition() {
    }

    @Test
    public void issueOrder() {
    }

    @Test
    public void payOrderFor() {
    }

    @Test
    public void payOrder() {
    }

    @Test
    public void recordOrderArrival() {
    }

    @Test
    public void clear() {
    }
}