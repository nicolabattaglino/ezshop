package it.polito.ezshop.classes;

import java.time.LocalDate;

import it.polito.ezshop.data.TicketEntry;

public class TicketEntryObj implements TicketEntry {

    private String barCode;
    private String productDescription;
    private int amount;
    private double pricePerUnit;
    private double discountRate;

    
    public TicketEntryObj(int amount, String string, String string2, double d) {
        this.amount = amount;
        this.barCode = string;
        this.productDescription = string2;
        this.pricePerUnit = d;
    }
    
    public  TicketEntryObj (TicketEntry t){
        this.barCode= t.getBarCode();
        this.productDescription = t.getProductDescription();
        this.amount = t.getAmount();
        this.pricePerUnit = t.getPricePerUnit();
        this.discountRate = t.getDiscountRate();
    }
    public void setProductDescription(String description){
        this.productDescription = description;
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

    public void setBarCode(int i) {
    }
}
    

