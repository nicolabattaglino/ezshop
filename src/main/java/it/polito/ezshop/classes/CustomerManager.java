package it.polito.ezshop.classes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerManager {
    //Todo vedi che fare per le rollback
    public static final String CARD_PATH = "data/loyaltyCards.json";
    public static final String CARD_ID_PATH = "data/loyaltyCardsIdGen.json";
    public static final String CUSTOMER_ID_PATH = "data/customersIdGen.json";
    public static final String CUSTOMER_PATH = "data/customers.json";

    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private HashMap<Integer, CustomerObj> customerMap;

    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private HashMap<String, LoyaltyCardObj> cardMap;

    private Integer customerIdGen = 0;
    private long loyaltyCardIdGen = 1000000000;
    private EZShop shop;
    
    public CustomerManager(EZShop shop) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, LoyaltyCardObj>> typeRef = new TypeReference<HashMap<String, LoyaltyCardObj>>() {
        };
        File cards = new File(CARD_PATH);
        try {
            cards.createNewFile();
            cardMap = mapper.readValue(cards, typeRef);
        } catch (IOException e) {
            cards.delete();
            try {
                cards.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                cardMap = new HashMap<>();
            }
        }
        File customers = new File(CUSTOMER_PATH);
        TypeReference<HashMap<Integer, CustomerObj>> typeRef1 = new TypeReference<HashMap<Integer, CustomerObj>>() {
        };
        try {
            customers.createNewFile();
            customerMap = mapper.readValue(customers, typeRef1);
        } catch (IOException e) {
            e.printStackTrace();
            customers.delete();
            try {
                customers.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                customerMap = new HashMap<>();
            }
        }
        TypeReference<Integer> typeRef2 = new TypeReference<Integer>() {};
        File customersId = new File(CUSTOMER_ID_PATH);
        try {
            customersId.createNewFile();
            customerIdGen = mapper.readValue(customersId, typeRef2);
        } catch (IOException e) {
            customersId.delete();
            try {
                cards.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                customerIdGen = 0;
            }
        }
        TypeReference<Long> typeRef3 = new TypeReference<Long>() {};
        File cardsId = new File(CARD_ID_PATH);
        try {
            cardsId.createNewFile();
            loyaltyCardIdGen = mapper.readValue(customersId, typeRef3);
        } catch (IOException e) {
            cardsId.delete();
            try {
                cardsId.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                loyaltyCardIdGen = 1000000000;
            }
        }
        this.shop = shop;
    }



    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException {
        if (customerName == null || customerName.equals(""))
            throw new InvalidCustomerNameException();

        Integer id = ++customerIdGen;
        System.out.println(customerIdGen);
        CustomerObj customer = new CustomerObj(id, customerName);
        customerMap.put(id, customer);
        if (customerMap.get(id) == null){
            return -1;
        }
        try {
            persistCustomers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;

    }


    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {

        // todo ricontrolla bene ci sono tanti difetti
        Customer customer = customerMap.get(id);
        LoyaltyCardObj card = null;
        if (id < 0)
            throw new InvalidCustomerIdException();
         if (newCustomerName == null || newCustomerName.trim().equals(""))
            throw new InvalidCustomerNameException();
         if (!newCustomerCard.matches("^$|^[0-9]{10}$"))
            //todo da rivedere
            throw new InvalidCustomerCardException();
         if (!newCustomerCard.equals("")) {

             // todo controllo se carta esiste?
             if (cardMap.get(newCustomerCard) == null || cardMap.get(newCustomerCard).getIsAttached()) {
                 return false;
             }
        } //else {
        if (newCustomerCard.trim().equals("")) {
            card = cardMap.get(customer.getCustomerCard());
            card.setIsAttached(false);
            card.setPoints(0);
            customerMap.get(id).setCustomerCard("");
        } else {
            customer.setCustomerCard(newCustomerCard);
            cardMap.get(newCustomerCard).setIsAttached(true);
        }
             customer.setCustomerName(newCustomerName);

             try {
                 // todo se una delle due persist fallisce?
                 persistCustomers();
                 persistCards();
             } catch (IOException e) {
                 e.printStackTrace();
             }
             return true;

         //}
    }
    
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (id < 0) {
            throw new InvalidCustomerIdException();
        } else if (customerMap.get(id) == null) {
            return false;
        } else {
            CustomerObj c = customerMap.get(id);
            if (c.getLoyaltyCard() != null) {
                String cardId = c.getCustomerCard();
                cardMap.get(cardId).setPoints(0);
                cardMap.get(cardId).setIsAttached(false);
            }
            customerMap.remove(id);
            try {
                // todo se una delle due persist fallisce?
                persistCustomers();
                persistCards();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
    
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (id < 0) {
            throw new InvalidCustomerIdException();
        } else if (customerMap.get(id) == null) {
            return null;
        } else {
            //todo vedi che fare per la copia
            //CustomerObj c =
            return customerMap.get(id);
        }
    }
    
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        return  new ArrayList<Customer>(customerMap.values());
    }

    public String createCard() {
        loyaltyCardIdGen = loyaltyCardIdGen + 1;
        LoyaltyCardObj l = new LoyaltyCardObj(String.valueOf(loyaltyCardIdGen));
        cardMap.put(l.getCardCode(), l);
        try {
            persistCards();
        } catch (IOException e) {
            //Todo vedi che fare per le rollback
            return "";
        }
        return l.getCardCode();
    }
    
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        if (customerId <= 0)
            throw new InvalidCustomerIdException();
        if (customerCard == null || customerCard.equals("") || !customerCard.matches("^([0-9]{10}$)"))
            throw new InvalidCustomerCardException();
        LoyaltyCardObj target = cardMap.get(customerCard);
        CustomerObj customer = customerMap.get(customerId);
        if (customer == null || target.getIsAttached())
            return false;

        customer.setCustomerCard(customerCard);
        customer.getLoyaltyCard().setIsAttached(true);
        target.setIsAttached(true);

        try {
            persistCards();
            persistCustomers();
        } catch (IOException e) {          //Todo vedi che fare per le rollback
            return false;
        }
        return true;

    }
    
    
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        if (customerCard == null || customerCard.trim().equals("") || !customerCard.matches("^([0-9]{10}$)"))
                throw new InvalidCustomerCardException();

        LoyaltyCardObj target = cardMap.get(customerCard);
        if (target == null || target.getPoints() + pointsToBeAdded < 0)
            return false;
        int points = target.getPoints();
        points += pointsToBeAdded;
        for (Map.Entry<Integer,CustomerObj> cu: customerMap.entrySet()
             ) {
            CustomerObj c = cu.getValue();
            if (c.getLoyaltyCard() != null && c.getCustomerCard().equals(customerCard)){
                c.setPoints(points);
                target.setPoints(points);
            }
        }
        try {
            persistCards();
            persistCustomers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;


    }

    private void persistCards() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(CARD_PATH), cardMap);
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(CARD_ID_PATH), loyaltyCardIdGen);
    }

    private void persistCustomers() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(CUSTOMER_PATH), customerMap);
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(CUSTOMER_ID_PATH), customerIdGen);
    }
}
