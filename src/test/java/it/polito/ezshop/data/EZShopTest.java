package it.polito.ezshop.data;

import it.polito.ezshop.classes.UserRole;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EZShopTest {
    
    private static final EZShop shop = new EZShop();
    
    @Before
    public void addUsers() {
        try {
            shop.getUserManager().createUser("Hossain", "123", UserRole.SHOPMANAGER.name());
            shop.getUserManager().createUser("Mattia", "123", UserRole.ADMINISTRATOR.name());
            shop.getUserManager().createUser("Stefano", "123", UserRole.CASHIER.name());
        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }
    
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
    public void createUser() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        int id = shop.getUserManager().createUser("JohnB","123321","Cashier");
        shop.login("Mattia","123");
        assertEquals(id, (int) shop.getUser(id).getId());
    }
    
    @Test
    public void deleteUser() throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException {

        assertThrows(UnauthorizedException.class, () -> shop.deleteUser(0));
        shop.login("Hossein","123");
        assertThrows(UnauthorizedException.class, () -> shop.deleteUser(0));
        shop.login("Mattia","123");
        int id = shop.getUserManager().createUser("JohnB","123321","Cashier");
        assertTrue(shop.deleteUser(id));
    }
    
    @Test
    public void getAllUsers() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
        assertThrows(UnauthorizedException.class, () -> shop.getAllUsers());
        shop.login("Hossein","123");
        assertThrows(UnauthorizedException.class, () -> shop.getAllUsers());
        shop.login("Mattia","123");
        assertEquals(0, (int) shop.getAllUsers().get(0).getId());
        shop.reset();
        assertTrue(shop.getAllUsers().isEmpty());
    }
    
    @Test
    public void getUser() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
        assertThrows(UnauthorizedException.class, () -> shop.getUser(0));
        shop.login("Hossein","123");
        assertThrows(UnauthorizedException.class, () -> shop.deleteUser(0));
        shop.login("Mattia","123");
        int id = shop.getUserManager().createUser("JohnB","123321","Cashier");
        assertEquals("JohnB", shop.getUser(id).getUsername());
        assertNull(shop.getUser(100));
    }
    
    @Test
    public void updateUserRights() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
        assertThrows(UnauthorizedException.class, () -> shop.updateUserRights(0,"Cashier"));
        shop.login("Hossein","123");
        assertThrows(UnauthorizedException.class, () -> shop.deleteUser(0));
        shop.login("Mattia","123");
        int id = shop.getUserManager().createUser("JohnB","123321","Cashier");
        assertTrue(shop.updateUserRights(id,"ShopManager"));
        assertFalse(shop.updateUserRights(10,"ShopManager"));
    }
    
    @Test
    public void login() throws InvalidPasswordException, InvalidUsernameException {
        User u = shop.login("Mattia","123");
        assertEquals("Mattia",u.getUsername());
    }
    
    @Test
    public void getProductOrderManager() {
    }
    
    @Test
    public void logout() throws InvalidPasswordException, InvalidUsernameException {
        assertFalse(shop.logout());
        shop.login("Mattia","123");
        assertTrue(shop.logout());
    }
    
    @Test
    public void testCreateProductType() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, () -> shop.createProductType("test", "123456789012", 25.0, "note"));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.createProductType("test", "123456789012", 25.0, "note"));
        shop.login("Hossain", "123");
        assertNotEquals(-1, (int) shop.createProductType("test", "123456789012", 25.0, "note"));
        shop.login("Mattia", "123");
        assertNotEquals(-1, (int) shop.createProductType("test1", "1234567890128", 22.0, "note"));
    }
    
    @Test
    public void testUpdateProduct() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductIdException {
        Integer id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.updateProduct(id, "test1", "123456789012", 22.0, "nota"));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.updateProduct(id, "test1", "123456789012", 22.0, "nota"));
        shop.login("Hossain", "123");
        assertTrue(shop.updateProduct(id, "test1", "123456789012", 22.0, "nota"));
        shop.login("Mattia", "123");
        assertTrue(shop.updateProduct(id, "test1", "123456789012", 24.0, "nota"));
    }
    
    @Test
    public void deleteProductType() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        Integer id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        Integer finalId = id;
        assertThrows(UnauthorizedException.class, () -> shop.updateProduct(finalId, "test1", "123456789012", 22.0, "nota"));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.updateProduct(finalId, "test1", "123456789012", 22.0, "nota"));
        shop.login("Hossain", "123");
        assertTrue(shop.deleteProductType(id));
        id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.login("Mattia", "123");
        assertTrue(shop.deleteProductType(id));
    }
    
    @Test
    public void getAllProductTypes() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        Integer id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, shop::getAllProductTypes);
        shop.login("Stefano", "123");
        assertFalse(shop.getAllProductTypes().isEmpty());
        shop.login("Hossain", "123");
        assertFalse(shop.getAllProductTypes().isEmpty());
        shop.login("Mattia", "123");
        assertFalse(shop.getAllProductTypes().isEmpty());
    }
    
    @Test
    public void getProductTypeByBarCode() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        Integer id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypeByBarCode("123456789012"));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypeByBarCode("123456789012"));
        shop.login("Hossain", "123");
        assertNotNull(shop.getProductTypeByBarCode("123456789012"));
        shop.login("Mattia", "123");
        assertNotNull(shop.getProductTypeByBarCode("123456789012"));
    }
    
    @Test
    public void getProductTypesByDescription() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        Integer id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypesByDescription("test"));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypesByDescription("test"));
        shop.login("Hossain", "123");
        assertFalse(shop.getProductTypesByDescription("test").isEmpty());
        shop.login("Mattia", "123");
        assertFalse(shop.getProductTypesByDescription("test").isEmpty());
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
    public void defineCustomer() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
        assertThrows(UnauthorizedException.class, () -> shop.defineCustomer("JohnB"));
        shop.login("Mattia","123");
        Integer id = shop.defineCustomer("JohnB");
        assertEquals(id, shop.getCustomer(id).getId());
    }
    
    @Test
    public void modifyCustomer() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        assertThrows(UnauthorizedException.class, () -> shop.modifyCustomer(0,"JonhC","1000000002"));
        shop.login("Mattia","123");
        Integer id = shop.defineCustomer("JohnB");
        String cardId = shop.createCard();
        shop.modifyCustomer(id,"JohnC",cardId);
        assertEquals("JohnC", shop.getCustomer(id).getCustomerName());
    }
    
    @Test
    public void deleteCustomer() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException {
        assertThrows(UnauthorizedException.class, () -> shop.deleteCustomer(1));
        shop.login("Mattia","123");
        Integer id = shop.defineCustomer("JohnB");
    }
    
    @Test
    public void getCustomer() throws InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, () -> shop.defineCustomer("JohnB"));
        shop.login("Mattia","123");
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
    
    @After()
    public void clear() {
        shop.reset();
    }
}