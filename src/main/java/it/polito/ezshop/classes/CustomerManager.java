package it.polito.ezshop.classes;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;

import java.util.HashMap;
import java.util.List;

public class CustomerManager {
    private Integer userIdGen = 0;
    private long loyaltyCardIdGen;
    private EZShop shop;

    HashMap<Integer,CustomerObj> customerMap = new HashMap<Integer,CustomerObj>();
    HashMap<String,LoyaltyCardObj> cardMap = new HashMap<>();

    public CustomerManager(EZShop shop) {
        this.shop = shop;
    }

    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        /*if(customerName == null || customerName.equals("")){
            throw  new InvalidCustomerNameException();
        } else if ((um.getUserLogged().getRole() != UserRole.ADMINISTRATOR &&
                    um.getUserLogged().getRole() != UserRole.CASHIER &&
                    um.getUserLogged().getRole() != UserRole.SHOP_MANAGER)  ||
                    um.getUserLogged().getRole() == null){ // manage role enum
            throw new UnauthorizedException();

        } else {
        }*/
        Integer id = customerMap.size();
        CustomerObj customer = new CustomerObj(id,customerName);
        customerMap.put(id,customer);
        return id;

    }
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        CustomerObj customer = customerMap.get(id);
        customer.setCustomerName(newCustomerName);
        if (newCustomerCard == null || newCustomerCard.equals("")){
            customer.setCustomerCard(null);
        } else {
            customer.setCustomerCard(newCustomerCard);
        }
        return false;
    }
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        customerMap.remove(id);
        return true;
    }
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

        return (Customer) customerMap.get(id);
    }

    public List<Customer> getAllCustomers() throws UnauthorizedException {
        // ?
        return null;
    }

    public String createCard() throws UnauthorizedException {

        return null;
    }

    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        return false;
    }


    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        return false;
    }



}
