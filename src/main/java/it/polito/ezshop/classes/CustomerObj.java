package it.polito.ezshop.classes;

import it.polito.ezshop.data.Customer;

import java.io.Serializable;
import java.util.HashMap;


public class CustomerObj implements Customer {
    
    private Integer id;
    private String name;
   // private String surname;

    private LoyaltyCardObj loyaltyCard;

    public CustomerObj() {}

    public CustomerObj(Integer id, String customerName) {
        this.id = id;
        this.name = customerName;
        this.loyaltyCard = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoyaltyCardObj getLoyaltyCard() {
        return loyaltyCard;
    }

    public void setLoyaltyCard(LoyaltyCardObj loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
    }

    public String getCustomerName() {
        return name;
    }
    
    public void setCustomerName(String customerName) {
        this.name = customerName;
    }
    
    @JsonIgnore
    public String getCustomerCard() {
        return loyaltyCard.getCardCode();
    }
    
    @JsonIgnore
    public void setCustomerCard(String customerCard) {

        if (customerCard == null) {
            loyaltyCard = null;
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, LoyaltyCardObj> cardMap;
        TypeReference<HashMap<String, LoyaltyCardObj>> typeRef = new TypeReference<HashMap<String, LoyaltyCardObj>>() {};
        try {
            cardMap = mapper.readValue(CustomerManager.CARD_PATH, typeRef );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }
        loyaltyCard = cardMap.get(customerCard);
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