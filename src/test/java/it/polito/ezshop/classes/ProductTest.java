package it.polito.ezshop.classes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProductTest {
    
    @Test
    public void testGetRFID() {
        final String RFID = "000000000100";
        final Product p = new Product(RFID);
        assertEquals(RFID, p.getRFID());
    }
    
    @Test
    public void testGetSetBarcodeProductType() throws IOException {
        final String rfid = "000000000100";
        final String barcode = "123456789012";
        Product p = new Product(rfid, barcode);
        assertEquals(barcode, p.getBarcode());
        final String barCode = "11111111111111";
        ProductTypeObj pObj = new ProductTypeObj(20, 0, "descr", barCode, "notes", 10.0, 0.25, new Position());
        p.setProductType(pObj);
        assertEquals(barCode, p.getBarcode());
        assertEquals(pObj, p.getProductType());
        ObjectMapper mapper = new ObjectMapper();
        assertNotNull(p.getProductType());
    }
}