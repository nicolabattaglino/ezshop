package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;

import java.util.Objects;

public class TicketEntryObj implements TicketEntry {
    
    private String barCode;
    private String productDescription;
    private int amount;
    private double pricePerUnit;
    private double discountRate;
    
    public TicketEntryObj(int amount, String barcode, String productDescription, double pricePerUnit) {
        this.amount = amount;
        this.barCode = barcode;
        this.productDescription = productDescription;
        this.pricePerUnit = pricePerUnit;
    }
    
    
    public TicketEntryObj(TicketEntry t) {
        this.barCode = t.getBarCode();
        this.productDescription = t.getProductDescription();
        this.amount = t.getAmount();
        this.pricePerUnit = t.getPricePerUnit();
        this.discountRate = t.getDiscountRate();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketEntryObj that = (TicketEntryObj) o;
        return amount == that.amount && Double.compare(that.pricePerUnit, pricePerUnit) == 0 && Double.compare(that.discountRate, discountRate) == 0 && Objects.equals(barCode, that.barCode) && Objects.equals(productDescription, that.productDescription);
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
    
    public void setProductDescription(String description) {
        this.productDescription = description;
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
    

