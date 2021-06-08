package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    private String RFID;
    @JsonIgnore
    private ProductTypeObj productType;
    private String barcode;
    
    public Product(String rfid) {
        RFID = rfid;
    }
    
    @JsonCreator
    public Product(@JsonProperty("RFID") String rfid, @JsonProperty("barcode") String barcode) {
        RFID = rfid;
        this.barcode = barcode;
    }
    
    public ProductTypeObj getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeObj productType) {
        if (productType == null) return;
        this.barcode = productType.getBarCode();
        this.productType = productType;
    }

    public String getRFID() {
        return RFID;
    }
    
    public void setRFID(String RFID) {
        this.RFID = RFID;
    }
    
    public String getBarcode() {
        return barcode;
    }
}
