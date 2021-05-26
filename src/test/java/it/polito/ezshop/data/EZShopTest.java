package it.polito.ezshop.data;

import org.junit.Test;
import it.polito.ezshop.exceptions.*;

import static org.junit.Assert.*;

public class EZShopTest {
    
    @Test
    public void getTransactionManager() {
    }
    
    @Test
    public void getCustomerManager() {
    }
    
    @Test
    public void getUserManager() {
    }
    
    @Test
    public void reset() {
    }
    
    @Test
    public void createUser() {
    }
    
    @Test
    public void deleteUser() throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException {
        EZShop s = new EZShop();
        assertThrows(UnauthorizedException.class, () -> s.deleteUser(0));
        s.createUser("SimAdmin","12345","ADMINISTRATOR");
        int id = s.createUser("JohnB","123321","Cashier");
        s.login("SimAdmin","12345");
        assertTrue(s.deleteUser(id));
    }
    
    @Test
    public void getAllUsers() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
        EZShop s = new EZShop();
        assertThrows(UnauthorizedException.class, () -> s.getAllUsers());
        s.createUser("SimAdmin","12345","ADMINISTRATOR");
        int id = s.createUser("JohnB","123321","Cashier");
        s.login("SimAdmin","12345");
        assertEquals(id, (int) s.getAllUsers().get(id).getId());
    }
    
    @Test
    public void getUser() {
    }
    
    @Test
    public void updateUserRights() {
    }
    
    @Test
    public void login() {
    }
    
    @Test
    public void getProductOrderManager() {
    }
    
    @Test
    public void logout() {
    }
    
    @Test
    public void createProductType() {
    }
    
    @Test
    public void updateProduct() {
    }
    
    @Test
    public void deleteProductType() {
    }
    
    @Test
    public void getAllProductTypes() {
    }
    
    @Test
    public void getProductTypeByBarCode() {
    }
    
    @Test
    public void getProductTypesByDescription() {
    }
    
    @Test
    public void updateQuantity() {
    }
    
    @Test
    public void updatePosition() {
    }
    
    @Test
    public void issueOrder() {
    }
    
    @Test
    public void payOrderFor() {
    }
    
    @Test
    public void payOrder() {
    }
    
    @Test
    public void recordOrderArrival() {
    }
    
    @Test
    public void getAllOrders() {
    }
    
    @Test
    public void defineCustomer() {
    }
    
    @Test
    public void modifyCustomer() {
    }
    
    @Test
    public void deleteCustomer() {
    }
    
    @Test
    public void getCustomer() {
    }
    
    @Test
    public void getAllCustomers() {
    }
    
    @Test
    public void createCard() {
    }
    
    @Test
    public void attachCardToCustomer() {
    }
    
    @Test
    public void modifyPointsOnCard() {
    }
    
    @Test
    public void startSaleTransaction() {
    }
    
    @Test
    public void addProductToSale() {
    }
    
    @Test
    public void deleteProductFromSale() {
    }
    
    @Test
    public void applyDiscountRateToProduct() {
    }
    
    @Test
    public void applyDiscountRateToSale() {
    }
    
    @Test
    public void computePointsForSale() {
    }
    
    @Test
    public void endSaleTransaction() {
    }
    
    @Test
    public void deleteSaleTransaction() {
    }
    
    @Test
    public void getSaleTransaction() {
    }
    
    @Test
    public void startReturnTransaction() {
    }
    
    @Test
    public void returnProduct() {
    }
    
    @Test
    public void endReturnTransaction() {
    }
    
    @Test
    public void deleteReturnTransaction() {
    }
    
    @Test
    public void receiveCashPayment() {
    }
    
    @Test
    public void receiveCreditCardPayment() {
    }
    
    @Test
    public void returnCashPayment() {
    }
    
    @Test
    public void returnCreditCardPayment() {
    }
    
    @Test
    public void recordBalanceUpdate() {
    }
    
    @Test
    public void getCreditsAndDebits() {
    }
    
    @Test
    public void computeBalance() {
    }
    
    @Test
    public void addOrder() {
    }
}