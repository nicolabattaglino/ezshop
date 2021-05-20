package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidLocationException;

public class ProductTypeObj implements ProductType {
    
    private Integer amount, id;
    private String description, barCode, notes;
    private double selPrice, discountRate;
    private Position position;
    
    
    public ProductTypeObj(Integer amount, Integer id, String description, String barCode, String notes, double selPrice, double discountRate) {
        this.amount = amount;
        this.id = id;
        this.description = description;
        this.barCode = barCode;
        this.notes = notes;
        this.selPrice = selPrice;
        this.discountRate = discountRate;
        this.position = new Position();
    }
    
    @JsonCreator
    public ProductTypeObj(
            @JsonProperty("amount") Integer amount,
            @JsonProperty("id") Integer id,
            @JsonProperty("description") String description,
            @JsonProperty("barCode") String barCode,
            @JsonProperty("notes") String notes,
            @JsonProperty("selPrice") double selPrice,
            @JsonProperty("discountRate") double discountRate,
            @JsonProperty("position") Position position) {
        this.amount = amount;
        this.id = id;
        this.description = description;
        this.barCode = barCode;
        this.notes = notes;
        this.selPrice = selPrice;
        this.discountRate = discountRate;
        this.position = position;
    }
    
    public ProductTypeObj(ProductType product) {
        this.amount = product.getQuantity();
        this.id = product.getId();
        this.description = product.getProductDescription();
        this.barCode = product.getBarCode();
        this.notes = product.getNote();
        this.selPrice = product.getPricePerUnit();
        try {
            String location = product.getLocation();
            this.position = location.equals("") ? new Position() : new Position(location);
        } catch (InvalidLocationException e) {
            e.printStackTrace();
        }
        
    }
    
    public ProductTypeObj(ProductTypeObj product) {
        this.amount = product.amount;
        this.id = product.id;
        this.description = product.description;
        this.barCode = product.barCode;
        this.notes = product.notes;
        this.selPrice = product.selPrice;
        this.discountRate = product.discountRate;
        this.position = product.position;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public double getDiscountRate() {
        return discountRate;
    }
    
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
    
    @Override
    public Integer getQuantity() {
        return amount;
    }
    
    @Override
    public void setQuantity(Integer quantity) {
        amount = quantity;
    }
    
    @Override
    public String getLocation() {
        return position.toString();
    }
    
    @Override
    public void setLocation(String location) {
        if (location == null || location.equals("")) this.position = new Position();
        else
            try {
                this.position = new Position(location);
            } catch (InvalidLocationException e) {
                e.printStackTrace();
            }
    }
    
    @Override
    public String getNote() {
        return notes;
    }
    
    @Override
    public void setNote(String note) {
        notes = note;
    }
    
    @Override
    public String getProductDescription() {
        return description;
    }
    
    @Override
    public void setProductDescription(String productDescription) {
        description = productDescription;
    }
    
    @Override
    public String getBarCode() {
        return barCode;
    }
    
    @Override
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    
    @Override
    public Double getPricePerUnit() {
        return selPrice;
    }
    
    @Override
    public void setPricePerUnit(Double pricePerUnit) {
        selPrice = pricePerUnit;
    }
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductTypeObj)) return false;
        
        ProductTypeObj that = (ProductTypeObj) o;
        
        if (Double.compare(that.selPrice, selPrice) != 0) return false;
        if (Double.compare(that.discountRate, discountRate) != 0) return false;
        if (!amount.equals(that.amount)) return false;
        if (!id.equals(that.id)) return false;
        if (!description.equals(that.description)) return false;
        if (!barCode.equals(that.barCode)) return false;
        if (!notes.equals(that.notes)) return false;
        return position.equals(that.position);
    }
    
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = amount.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + barCode.hashCode();
        result = 31 * result + notes.hashCode();
        temp = Double.doubleToLongBits(selPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(discountRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + position.hashCode();
        return result;
    }
}
