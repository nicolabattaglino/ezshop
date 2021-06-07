package it.polito.ezshop.classes;

import java.util.Objects;

public class Product {
    private Integer RFID;
    private ProductTypeObj productType;
    
    public void setRFID(Integer RFID) {
        this.RFID = RFID;
    }
    
    public Product(Integer rfid) {
        RFID = rfid;
    }
    
    public ProductTypeObj getProductType() {
        return productType;
    }
    
    public void setProductType(ProductTypeObj productType) {
        this.productType = productType;
    }
    
    public Integer getRFID() {
        return RFID;
    }
}
