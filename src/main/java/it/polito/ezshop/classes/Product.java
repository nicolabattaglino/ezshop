package it.polito.ezshop.classes;

import java.util.Objects;

public class Product {
    private String RFID;
    private ProductTypeObj productType;

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    public Product(String rfid) {
        RFID = rfid;
    }

    public ProductTypeObj getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeObj productType) {
        this.productType = productType;
    }

    public String getRFID() {
        return RFID;
    }
}
