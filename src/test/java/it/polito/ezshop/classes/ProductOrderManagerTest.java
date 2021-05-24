package it.polito.ezshop.classes;

import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProductOrderManagerTest {
    
    ProductOrderManager p = new ProductOrderManager(null);
    
    @Test
    public void testCheckBarcodeNull() {
        assertFalse(p.checkBarcode(null));
    }
    
    @Test
    public void testCheckBarcodeNonNum() {
        assertFalse(p.checkBarcode("a2bcde!"));
    }
    
    @Test
    public void testCheckBarcodeLenghtLessThan12() {
        assertFalse(p.checkBarcode("1234567"));
    }
    
    @Test
    public void testCheckBarcodeLenghtGreaterThan14() {
        assertFalse(p.checkBarcode("123456789012345"));
    }
    
    @Test
    public void testCheckBarcodeCheckDigitNotValid() {
        assertFalse(p.checkBarcode("123456789013"));
        assertFalse(p.checkBarcode("1234567890137"));
        assertFalse(p.checkBarcode("12345678901377"));
    }
    
    @Test
    public void testCheckBarcodeCheckDigitValid() {
        assertTrue(p.checkBarcode("123456789012"));
        assertTrue(p.checkBarcode("1234567890128"));
        assertTrue(p.checkBarcode("12345678901286"));
    }
    
    @Test
    public void testCreateProductTypeOk() {
        try {
            Integer code = p.createProductType("test", "123456789012", 25.0, "note");
            assertEquals(code, p.getProductTypeByBarCode("123456789012").getId());
            code = p.createProductType("test", "123456789012", 25.0, "note");
            assertEquals(-1, (int) code);
        } catch (InvalidProductCodeException | InvalidProductDescriptionException | InvalidPricePerUnitException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCreateProductTypeInvalidCode() {
        assertThrows(InvalidProductCodeException.class, () -> p.createProductType("test", "123456789112", 25.0, "note"));
    }
    
    @Test
    public void testCreateProductTypeInvalidDescription() {
        assertThrows(InvalidProductDescriptionException.class, () -> p.createProductType("", "123456789012", 25.0, "note"));
        assertThrows(InvalidProductDescriptionException.class, () -> p.createProductType(null, "123456789012", 25.0, "note"));
    }
    
    @Test
    public void testUpdateProductInvalidID() {
        assertThrows(InvalidProductIdException.class, () -> p.updateProduct(-1, "test", "123456789012", 25.0, "note"));
        assertThrows(InvalidProductIdException.class, () -> p.updateProduct(null, "test", "123456789012", 25.0, "note"));
    }
    
    @Test
    public void testUpdateProductPriceInvalidPrice() {
        assertThrows(InvalidPricePerUnitException.class, () -> p.updateProduct(1, "test", "123456789012", 0.0, "note"));
        assertThrows(InvalidPricePerUnitException.class, () -> p.updateProduct(1, "test", "123456789012", -25.0, "note"));
    }
    
    @Test
    public void testUpdateProductInvalidBarcode() {
        assertThrows(InvalidProductCodeException.class, () -> p.updateProduct(1, "test", "123456789112", 25.0, "note"));
    }
    
    @Test
    public void testUpdateProductPriceCodeAlreadyExist() {
        try {
            Integer id = p.createProductType("test", "123456789012", 25.0, "note");
            assertFalse(p.updateProduct(id + 1, "testa", "123456789012", 22.0, "nota"));
        } catch (InvalidProductCodeException | InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidProductIdException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUpdateProductIdNotPresent() {
        try {
            assertFalse(p.updateProduct(1, "test1", "123456789012", 22.0, "nota"));
        } catch (InvalidProductCodeException | InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidProductIdException e) {
            e.printStackTrace();
        }
    }
    
    
    @Test
    public void testUpdateProductOK() {
        try {
            Integer id = p.createProductType("test", "123456789012", 25.0, "note");
            assertTrue(p.updateProduct(id, "test1", "123456789012", 22.0, "nota"));
            assertEquals(new ProductTypeObj(0, id, "test1", "123456789012", "nota", 22.0, 0, new Position()),
                    p.getProductTypeByBarCode("123456789012"));
        } catch (InvalidProductCodeException | InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidProductIdException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDeleteProductTypeInvalidId() {
        assertThrows(InvalidProductIdException.class, () -> p.deleteProductType(-1));
        assertThrows(InvalidProductIdException.class, () -> p.deleteProductType(0));
        assertThrows(InvalidProductIdException.class, () -> p.deleteProductType(null));
    }
    
    @Test
    public void testDeleteProductTypeNotFound() {
        try {
            assertFalse(p.deleteProductType(1));
        } catch (InvalidProductIdException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDeleteProductTypeOk() {
        try {
            Integer id = p.createProductType("test", "123456789012", 25.0, "note");
            assertTrue(p.deleteProductType(id));
            assertNull(p.getProductTypeByBarCode("123456789012"));
        } catch (InvalidProductCodeException | InvalidProductIdException | InvalidPricePerUnitException | InvalidProductDescriptionException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void getAllProductTypesEmpty() {
        assertTrue(p.getAllProductTypes().isEmpty());
    }
    
    @Test
    public void getAllProductTypes() {
        ArrayList<ProductTypeObj> testSet = new ArrayList<>();
        try {
            Integer id = p.createProductType("test", "123456789012", 22.0, "note");
            testSet.add(new ProductTypeObj(0, id, "test", "123456789012", "note", 22.0, 0, new Position()));
            id = p.createProductType("test1", "1234567890128", 22.0, "note");
            testSet.add(new ProductTypeObj(0, id, "test1", "1234567890128", "note", 22.0, 0, new Position()));
            id = p.createProductType("test2", "12345678901286", 22.0, "note");
            testSet.add(new ProductTypeObj(0, id, "test2", "12345678901286", "note", 22.0, 0, new Position()));
            assertTrue(p.getAllProductTypes().containsAll(testSet));
        } catch (InvalidProductCodeException | InvalidProductDescriptionException | InvalidPricePerUnitException e) {
            e.printStackTrace();
        }
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
    
    @After
    public void clear() {
        p.clear();
    }
    
    
}