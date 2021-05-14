package it.polito.ezshop.classes;

import it.polito.ezshop.exceptions.*;

public class TestClass {
    public static void main(String[] args) throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidUserIdException, InvalidCustomerNameException, InvalidCustomerIdException {

        //UserManager um = new UserManager();
        CustomerManager cm = new CustomerManager(null);

        cm.defineCustomer("Paolo");
        //cm.defineCustomer("Carla");
        //System.out.println(cm.getAllCustomers());
        //cm.createCard();
        //cm.defineCustomer("Antonio");
        //System.out.println(cm.getCustomer(0).getCustomerName());
        //cm.createCard();
        //um.createUser("John","1234","Administrator");
        //um.createUser("Paul","3333","ShopManager");
        //UserManager um1 = new UserManager();

        //System.out.println(um.login("John", "1234").getUsername());
        //um.createUser("Paul","3344","ShopManager");
        //System.out.println("User logged: " + um.getUserLogged().getUsername());
        //System.out.println("User role "+ um.getUserLogged().getRole());
        //System.out.println(cm.defineCustomer("aaa"));
        //um.updateUserRights(2,"shopmanager");
        //um.deleteUser(1);

    }
}
