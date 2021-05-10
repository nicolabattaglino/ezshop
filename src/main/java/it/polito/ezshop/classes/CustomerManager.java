package it.polito.ezshop.classes;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerManager {
    HashMap<Integer, Customer> customerMap = new HashMap<Integer, Customer>();
    HashMap<String, LoyaltyCardObj> cardMap = new HashMap<>();
    private Integer userIdGen = 0;
    private long loyaltyCardIdGen;
    private EZShop shop;
    
    public CustomerManager(EZShop shop) {
        this.shop = shop;
    }
    
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        if (customerName == null || customerName.equals("")) {
            throw new InvalidCustomerNameException();
        } else {
            Integer id = customerMap.size();
            Customer customer = new CustomerObj(id, customerName);
            customerMap.put(id, customer);
            return id;
        }
    }
    
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        Customer customer = customerMap.get(id);
        customer.setCustomerName(newCustomerName);
        
        if (id < 0) {
            throw new InvalidCustomerIdException();
        } else if (newCustomerName.equals("") || newCustomerName == null) {
            throw new InvalidCustomerNameException();
        } else if (newCustomerCard.equals("") || newCustomerCard == null || !newCustomerCard.matches("^([0-9]{10}$)")) {
            throw new InvalidCustomerCardException();
        } else if (newCustomerCard == null || newCustomerCard.equals("")) {
            customer.setCustomerCard(null);
        } else {
            customer.setCustomerCard(newCustomerCard);
        }
        return false;
    }
    
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (id < 0) {
            throw new InvalidCustomerIdException();
        } else {
            customerMap.remove(id);
            return true;
        }
    }
    
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (id < 0) {
            throw new InvalidCustomerIdException();
        } else {
            return customerMap.get(id);
        }
    }
    
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        return (ArrayList<Customer>) customerMap.values();
    }
    
    public String createCard() throws UnauthorizedException {
        String id = ""; // TODO add id
        LoyaltyCardObj card = new LoyaltyCardObj(id);
        cardMap.put(id, card);
        return id;
    }
    
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        if (customerId < 0) {
            throw new InvalidCustomerIdException();
        } else if (customerCard.equals("") || customerCard == null || !customerCard.matches("^([0-9]{10}$)")) {
            throw new InvalidCustomerCardException();
        } else {
            customerMap.get(customerId).setCustomerCard(customerCard);
            return true;
        }
    }
    
    
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        if (customerCard.equals("") || customerCard == null || !customerCard.matches("^([0-9]{10}$)")) {
            throw new InvalidCustomerCardException();
        } else {
            customerMap.get(customerCard).setPoints(pointsToBeAdded);
            return true;
        }
    }
    
    
}
