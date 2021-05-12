package it.polito.ezshop.classes;

import it.polito.ezshop.data.Customer;

import java.io.Serializable;

public class CustomerObj implements Customer, Serializable {
    
    private Integer id;
    private String name;
    private String surname;
    private LoyaltyCardObj loyaltyCard;

    public CustomerObj(Integer id, String customerName) {
        this.id = id;
        this.name = customerName;
        this.loyaltyCard = null;
    }
    
    public String getCustomerName() {
        return name;
    }
    
    public void setCustomerName(String customerName) {
        this.name = customerName;
    }
    
    public String getCustomerCard() {
        return loyaltyCard.getCardCode();
    }
    
    public void setCustomerCard(String customerCard) {
        loyaltyCard.setCardCode(customerCard);
    }

    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getPoints() {
        return loyaltyCard.getPoints();
    }
    
    public void setPoints(Integer points) {
        loyaltyCard.setPoints(points);
    }
    
}
