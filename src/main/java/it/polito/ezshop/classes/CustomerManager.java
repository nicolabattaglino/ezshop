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


public class CustomerManager {
    //Todo vedi che fare per le rollback
    public static final String CARD_PATH = "data/loyaltyCards.json";
    public static final String CUSTOMER_PATH = "data/customers.json";

    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private HashMap<Integer, Customer> customerMap;

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
        TypeReference<HashMap<Integer, Customer>> typeRef1 = new TypeReference<HashMap<Integer, Customer>>() {
        };
        try {
            customers.createNewFile();
            customerMap = mapper.readValue(customers, typeRef1);
        } catch (IOException e) {
            customers.delete();
            try {
                customers.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                customerMap = new HashMap<>();
            }

        }
        this.shop = shop;
    }


    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException {
        if (customerName == null || customerName.equals(""))
            throw new InvalidCustomerNameException();

        Integer id = customerIdGen + 1;
        Customer customer = new CustomerObj(id, customerName);
        if (customerMap.put(id, customer) != null)
            return -1;

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
        if (id < 0) {
            throw new InvalidCustomerIdException();
        } else if (newCustomerName == null || newCustomerName.trim().equals("")) {
            throw new InvalidCustomerNameException();
        } else if (newCustomerCard == null || newCustomerCard.trim().equals("") || !newCustomerCard.matches("^[0-9]{10}$")) {
            //todo da rivedere
            throw new InvalidCustomerCardException();
        } else if (newCustomerCard.equals("")) {
            //todo da rivedere
            //cardMap.get(customer.getCustomerCard()).setIsAttached(false);
            customer.setCustomerCard(null);
        } else if (cardMap.get(newCustomerCard).getIsAttached()) {
            return false;
        } else if (newCustomerCard == null) {

        } else {
            customer.setCustomerCard(newCustomerCard);
            customer.setCustomerName(newCustomerName);

        }
        return false;
    }
    
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (id < 0) {
            throw new InvalidCustomerIdException();
        } else if (customerMap.get(id) == null) {
            return false;
        } else {
            String cardId = customerMap.get(id).getCustomerCard();
            cardMap.get(id).setPoints(0); // la carta la elimino?
            cardMap.get(id).setIsAttached(false);
            customerMap.remove(id);
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
            return customerMap.get(id);
        }
    }
    
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        return (ArrayList<Customer>) customerMap.values();
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
        Customer customer = customerMap.get(customerId);
        if (customer == null || target.getIsAttached())
            return false;
        target.setIsAttached(true);
        customer.setCustomerCard(customerCard);
        try {
            persistCards();
            persistCustomers();
        } catch (IOException e) {          //Todo vedi che fare per le rollback
            return false;
        }

        /*try {
            persistCards();
        } catch (IOException e) {
            target.setIsAttached(false);
            return false;
        }
        try {
            customer.setCustomerCard(customerCard);
            persistCustomers();
        } catch (IOException e) {
            target.setIsAttached(false);
            customer.setCustomerCard(null);
            try {
                persistCards();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return false;
        } */

        return true;

    }
    
    
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        if (customerCard == null || customerCard.trim().equals("") || !customerCard.matches("^([0-9]{10}$)"))
            throw new InvalidCustomerCardException();
        if (cardMap.get(customerCard) == null || pointsToBeAdded < 0)
        LoyaltyCardObj target = cardMap.get(customerCard);
        if (target == null || target.getPoints() + pointsToBeAdded < 0)
            // false   if there is no card with given code,
            // if pointsToBeAdded is negative and there were not enough points on that card before this operation, ?????
            //if we cannot reach the db.
            return false;


            /*for (Map.Entry<Integer, Customer> entry : customerMap.entrySet()) {
                if(entry.getValue().getCustomerCard().equals(customerCard)){
                    entry.getValue().setPoints(pointsToBeAdded);
                    return true;
                }*/
            int points = cardMap.get(customerCard).getPoints();
            points += pointsToBeAdded;
            cardMap.get(customerCard).setPoints(points);
            return true;


        int points = target.getPoints();
        points += pointsToBeAdded;
        target.setPoints(points);
        try {
            persistCards();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;


    }

    private void persistCards() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(CARD_PATH), cardMap);
    }

    private void persistCustomers() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(CUSTOMER_PATH), customerMap);
    }

}
