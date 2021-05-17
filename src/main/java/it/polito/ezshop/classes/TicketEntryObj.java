package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TicketEntryObj implements TicketEntry {
    private String barCode;
    private String productDescription;
    private int amount;
    private double pricePerUnit;
    private double discountRate;
    //TODO costruttore di copia
    
    public TicketEntryObj(int amount, String barcode, String description, double pricePerUnit) {
        this.amount = amount;
        this.barCode = barcode;
        this.productDescription = description;
        this.pricePerUnit = pricePerUnit;
    }
    
    public String getBarCode() {
        return barCode;
    }
    
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public double getPricePerUnit() {
        return pricePerUnit;
    }
    
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
    
    public double getDiscountRate() {
        return discountRate;
    }
    
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
}
    

