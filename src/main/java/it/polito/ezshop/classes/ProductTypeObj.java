package it.polito.ezshop.classes;

import it.polito.ezshop.data.ProductType;

public class ProductTypeObj implements ProductType {
    
    private Integer amount, id;
    private String description, barCode, notes;
    private double selPrice, discountRate;
    
    //TODO add position
    public ProductTypeObj(Integer amount, Integer id, String description, String barCode, String notes, double selPrice, double discountRate) {
        this.amount = amount;
        this.id = id;
        this.description = description;
        this.barCode = barCode;
        this.notes = notes;
        this.selPrice = selPrice;
        this.discountRate = discountRate;
    }
    
    @Override
    public Integer getQuantity() {
        return null;
    }
    
    @Override
    public void setQuantity(Integer quantity) {
    
    }
    
    @Override
    public String getLocation() {
        return null;
    }
    
    @Override
    public void setLocation(String location) {
    
    }
    
    @Override
    public String getNote() {
        return null;
    }
    
    @Override
    public void setNote(String note) {
    
    }
    
    @Override
    public String getProductDescription() {
        return null;
    }
    
    @Override
    public void setProductDescription(String productDescription) {
    
    }
    
    @Override
    public String getBarCode() {
        return null;
    }
    
    @Override
    public void setBarCode(String barCode) {
    
    }
    
    @Override
    public Double getPricePerUnit() {
        return null;
    }
    
    @Override
    public void setPricePerUnit(Double pricePerUnit) {
    
    }
    
    @Override
    public Integer getId() {
        return null;
    }
    
    @Override
    public void setId(Integer id) {
    
    }
}
