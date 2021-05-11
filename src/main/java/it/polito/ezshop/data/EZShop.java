package it.polito.ezshop.data;

import it.polito.ezshop.classes.CustomerManager;
import it.polito.ezshop.classes.ProductOrderManager;
import it.polito.ezshop.classes.TransactionManager;
import it.polito.ezshop.classes.UserManager;
import it.polito.ezshop.classes.UserRole;
import it.polito.ezshop.exceptions.*;

import java.time.LocalDate;
import java.util.List;


public class EZShop implements EZShopInterface {
    TransactionManager transactionManager;
    CustomerManager customerManager;
    UserManager userManager;
    ProductOrderManager productOrderManager;

    @Override
    public void reset() {

    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        if(userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString())){
            return userManager.createUser(username,password,role);

        } else {
            return null;
        }
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

        if (!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                userManager.getUserLogged() == null) {
            throw new UnauthorizedException();
        }else {
            return userManager.deleteUser(id);
        }
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        return userManager.getAllUsers();
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        if (!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                userManager.getUserLogged() == null){
            throw new UnauthorizedException();
        }else {
            return userManager.getUser(id);
        }
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        if (!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                userManager.getUserLogged() == null) {
            throw new UnauthorizedException();
        } else {
            return userManager.updateUserRights(id,role);
        }
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        if(username == null || username.equals("")){
            throw  new InvalidUsernameException();
        } else if (password == null || password.equals("")){
            throw  new InvalidPasswordException();
        } else {
            return (User) userManager.login(username, password);
        }
    }
    public ProductOrderManager getProductOrderManager(){
        return this.productOrderManager;
    }
    @Override
    public boolean logout() {
        return userManager.logout();
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        return null;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        return false;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        return null;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        if(!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString())) {
            throw new UnauthorizedException();
        } else {
           return customerManager.defineCustomer(customerName);
        }
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        if(!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString())) {
            throw new UnauthorizedException();
        } else {
           return customerManager.modifyCustomer(id,newCustomerName,newCustomerCard);
        }
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if(!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString())) {
            throw new UnauthorizedException();
        } else {
            return customerManager.deleteCustomer(id);
        }
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if(!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString())) {
            throw new UnauthorizedException();
        } else {
            return customerManager.getCustomer(id);
        }
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        if(!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString())) {
            throw new UnauthorizedException();
        } else {
            return customerManager.getAllCustomers();
        }
    }

    @Override
    public String createCard() throws UnauthorizedException {
        if(!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString())) {
            throw new UnauthorizedException();
        } else {
            return customerManager.createCard();
        }
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {

        if(!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString())) {
            throw new UnauthorizedException();
        } else {
            return customerManager.attachCardToCustomer(customerCard, customerId);
        }
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        if(!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString())) {
            throw new UnauthorizedException();
        } else {
            return customerManager.modifyPointsOnCard(customerCard, pointsToBeAdded);
        }    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        if(!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString())) {
            throw new UnauthorizedException();
        } else {
           return transactionManager.startSaleTransaction();
        }
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if(!userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString()) ||
                !userManager.getUserLogged().getRole().equals(UserRole.ADMINISTRATOR.toString())) {
            throw new UnauthorizedException();
        } else {
           return transactionManager.addProductToSale(transactionId, productCode, amount);
        }
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        try {
            return transactionManager.endReturnTransaction(returnId, commit);
        } catch (InvalidProductIdException | InvalidProductCodeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        return false;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        return null;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        return 0;
    }
}
