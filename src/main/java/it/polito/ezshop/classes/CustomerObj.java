package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.ezshop.data.Customer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class CustomerObj implements Customer {
    
    private Integer id;
    private String customerName;
    private LoyaltyCardObj loyaltyCard;
    
    public CustomerObj() {
    }
    
    public CustomerObj(Integer id, String customerName) {
        this.id = id;
        this.customerName = customerName;
        this.loyaltyCard = null;
    }
    
    public CustomerObj(CustomerObj customerObj) {
        this.id = customerObj.id;
        this.customerName = customerObj.customerName;
        this.loyaltyCard = customerObj.loyaltyCard;
    }
    
    
    public LoyaltyCardObj getLoyaltyCard() {
        return loyaltyCard;
    }
    
    public void setLoyaltyCard(LoyaltyCardObj loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    @JsonIgnore
    public String getCustomerCard() {
        if (loyaltyCard != null) {
            return loyaltyCard.getCardCode();
        } else {
            return null;
        }
    }
    
    @JsonIgnore
    public void setCustomerCard(String customerCard) {
        if (customerCard == null || customerCard.equals("")) {
            loyaltyCard = null;
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, LoyaltyCardObj> cardMap = null;
        TypeReference<HashMap<String, LoyaltyCardObj>> typeRef = new TypeReference<HashMap<String, LoyaltyCardObj>>() {
        };
        File cards = new File(CustomerManager.CARD_PATH);
        try {
            cards.createNewFile();
            cardMap = mapper.readValue(cards, typeRef);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if (cardMap != null) {
            loyaltyCard = cardMap.get(customerCard);
            loyaltyCard.setIsAttached(true);
        }
        
    }
    
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    @JsonIgnore
    public Integer getPoints() {
        return loyaltyCard.getPoints();
    }
    
    @JsonIgnore
    public void setPoints(Integer points) {
        loyaltyCard.setPoints(points);
    }
    
}