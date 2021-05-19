package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;

public class TicketEntryObj implements TicketEntry {
    public static final String getBarCode = null;
    private String barCode;
    private String productDescription;
    private int amount;
    private double pricePerUnit;
    private double discountRate;
    //TODO costruttore di copia
    
    public TicketEntryObj(int amount, String barcode, String productDescription, double pricePerUnit) {
        this.amount = amount;
        this.barCode = barcode;
        this.productDescription = productDescription;
        this.pricePerUnit = pricePerUnit;
    }
    
    
    public String getBarCode() {
        return barCode;
    }
    
    public void setBarCode(String i) {
        this.barCode = i;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    @Override
    public void setProductDescription(String productDescription) {
    
    }
    
    public void setProductDescription() {
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
    

