package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    private Integer RFID;
    @JsonIgnore
    private ProductTypeObj productType;
    private String barcode;
    
    @JsonCreator
    public Product(@JsonProperty("RFID") Integer rfid, @JsonProperty("barcode") String barcode) {
        RFID = rfid;
    }
    
    public ProductTypeObj getProductType() {
        return productType;
    }
    
    public void setProductType(ProductTypeObj productType) {
        if (productType == null) return;
        this.barcode = productType.getBarCode();
        this.productType = productType;
    }
    
    public Integer getRFID() {
        return RFID;
    }
    
    public void setRFID(Integer RFID) {
        this.RFID = RFID;
    }
    
    public String getBarcode() {
        return barcode;
    }
}
