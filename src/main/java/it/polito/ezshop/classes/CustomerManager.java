package it.polito.ezshop.classes;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

import java.io.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CustomerManager  {
    HashMap<Integer, Customer> customerMap = new HashMap<Integer, Customer>();
    HashMap<String, LoyaltyCardObj> cardMap = new HashMap<>();
    private Integer customerIdGen = 0;
    private String loyaltyCardIdGen = "";
    //private long loyaltyCardIdGen;
    private EZShop shop;
    
    public CustomerManager(EZShop shop) {
        this.shop = shop;
    }



    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        if (customerName == null || customerName.equals("")) {  //????? empty or null
            throw new InvalidCustomerNameException();
        } else {
            Integer id = customerIdGen + 1;
            Customer customer = new CustomerObj(id, customerName);
            customerMap.put(id, customer);
            return id;
        }
    }
    
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {

        Customer customer = customerMap.get(id);
        if (id < 0) {
            throw new InvalidCustomerIdException();
        } else if (newCustomerName == null || newCustomerName.equals("") ) {
            throw new InvalidCustomerNameException();
        } else if (newCustomerCard == null || newCustomerCard.trim().equals("")  || !newCustomerCard.matches("^[0-9]{10}$")) { //?????????
            throw new InvalidCustomerCardException();
        } else if (newCustomerCard.equals("")) {
            cardMap.get(customer.getCustomerCard()).setIsAttached(false);
            customer.setCustomerCard(null);
        } else if (cardMap.get(newCustomerCard).getIsAttached() == true) {
            return false;
        } else if (newCustomerCard == null){

        } else {
            customer.setCustomerCard(newCustomerCard);
            customer.setCustomerName(newCustomerName);
        }
        return false;
    }
    
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (id < 0) {
            throw new InvalidCustomerIdException();
        } else if (customerMap.get(id) == null){
            return false;
        } else {
            String cardId = customerMap.get(id).getCustomerCard();
            cardMap.get(id).setPoints(0); // la carta la elimino?
            cardMap.get(id).setIsAttached(false);
            //cardMap.remove(cardId); ???
            customerMap.remove(id);
            return true;
        }
    }
    
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (id < 0) {
            throw new InvalidCustomerIdException();
        } else if (customerMap.get(id) == null){
            return null;
        } else {
            return customerMap.get(id);
        }
    }
    
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        return (ArrayList<Customer>) customerMap.values();
    }
    
    public String createCard() throws UnauthorizedException {
        BufferedReader brTest = null;
        String id = null;

        try {
            brTest = new BufferedReader(new FileReader("cardgen.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            id = brTest.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        id = id + 1;
        try (PrintStream out = new PrintStream(new FileOutputStream("cardgen.txt"))) {
            out.print(id);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ///////////////////////////////
        loyaltyCardIdGen = loyaltyCardIdGen + 1;
        LoyaltyCardObj l = new LoyaltyCardObj(loyaltyCardIdGen);
        return loyaltyCardIdGen;
    }
    
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        if (customerId < 0) {
            throw new InvalidCustomerIdException();
        } else if (customerCard == null || customerCard.equals("") || !customerCard.matches("^([0-9]{10}$)")) {
            throw new InvalidCustomerCardException();
        } else if (customerMap.get(customerId) == null || cardMap.get(customerCard).getIsAttached() == false)  {
            return false;
        } else {
            Customer customer = customerMap.get(customerId);
            customer.setCustomerCard(customerCard); //  come passo la card al customer?
            return true;
        }
    }
    
    
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        if (customerCard.equals("") || customerCard == null || !customerCard.matches("^([0-9]{10}$)")) {
            throw new InvalidCustomerCardException();
        } else if (cardMap.get(customerCard) == null || pointsToBeAdded < 0) {
            // false   if there is no card with given code,
            // if pointsToBeAdded is negative and there were not enough points on that card before this operation, ?????
            //if we cannot reach the db.
            return false;
        } else {
            /*for (Map.Entry<Integer, Customer> entry : customerMap.entrySet()) {
                if(entry.getValue().getCustomerCard().equals(customerCard)){
                    entry.getValue().setPoints(pointsToBeAdded);
                    return true;
                }*/
            int points = cardMap.get(customerCard).getPoints();
            points += pointsToBeAdded;
            cardMap.get(customerCard).setPoints(points);
            return true;
        }

    }


    private static void parseCustomerObject(JSONObject customer) {
        //UserRole r;

        //String password = (String) customer.get("password");
        //System.out.println(password);

        String card = (String) customer.get("card");
        //System.out.println(role);
        Integer id = Integer.valueOf(customer.get("id").toString());
        //System.out.println(id);

        String name = (String) customer.get("name");
        //System.out.println(username);

        Customer c = new CustomerObj(id,name);


    }

    private void writeToFile() {
        int i = 0;
        JSONArray customerListJSON = new JSONArray();
        ArrayList<Customer> customerList = new ArrayList<Customer>(customerMap.values());
        for (i = 0; i < customerList.size(); i++) {
            JSONObject userDetails = new JSONObject();
            userDetails.put("id", customerList.get(i).getId());
            userDetails.put("name", customerList.get(i).getCustomerName());
            userDetails.put("card", customerList.get(i).getCustomerCard());
            customerListJSON.add(userDetails);
        }
        //Write JSON file
        try (FileWriter file = new FileWriter("customer.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(customerListJSON.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile() {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("customer.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray customerList = (JSONArray) obj;
            //System.out.println(employeeList);

            //Iterate over employee array
            customerList.forEach(usr -> parseCustomerObject((JSONObject) usr));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
}
