package it.polito.ezshop.classes;

import java.time.LocalDate;

import it.polito.ezshop.data.TicketEntry;

public class TicketEntryObj implements TicketEntry {
    public static final String getBarCode = null;
    private String barCode;
    private String productDescription;
    private int amount;
    private double pricePerUnit;
    private double discountRate;
    //TODO costruttore di copia
    
    public TicketEntryObj(int amount, String string, String string2, double d) {
        this.amount = amount;
        this.barCode = string;
        this.productDescription = string2;
        this.pricePerUnit = d;
    }
    
    public TicketEntryObj(int amount2, LocalDate now, double d, String string) {
    }

    public TicketEntryObj(int amount2, LocalDate now, double d, String string) {
    }

    public TicketEntryObj(int amount2, LocalDate now, double d, String string) {
    }

    public String getBarCode() {
        return barCode;
    }
    
    public void setBarCode(int i) {
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
    

