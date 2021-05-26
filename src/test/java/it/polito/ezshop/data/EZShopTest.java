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
    public void createUser() {
    }
    
    @Test
    public void deleteUser() {
    }
    
    @Test
    public void getAllUsers() {
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
    public void testDeleteProductType() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
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
    public void testGetAllProductTypes() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, shop::getAllProductTypes);
        shop.login("Stefano", "123");
        assertFalse(shop.getAllProductTypes().isEmpty());
        shop.login("Hossain", "123");
        assertFalse(shop.getAllProductTypes().isEmpty());
        shop.login("Mattia", "123");
        assertFalse(shop.getAllProductTypes().isEmpty());
    }
    
    @Test
    public void testGetProductTypeByBarCode() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypeByBarCode("123456789012"));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypeByBarCode("123456789012"));
        shop.login("Hossain", "123");
        assertNotNull(shop.getProductTypeByBarCode("123456789012"));
        shop.login("Mattia", "123");
        assertNotNull(shop.getProductTypeByBarCode("123456789012"));
    }
    
    @Test
    public void testGetProductTypesByDescription() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypesByDescription("test"));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypesByDescription("test"));
        shop.login("Hossain", "123");
        assertFalse(shop.getProductTypesByDescription("test").isEmpty());
        shop.login("Mattia", "123");
        assertFalse(shop.getProductTypesByDescription("test").isEmpty());
    }
    
    @Test
    public void testUpdateQuantity() throws InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
        Integer id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getProductOrderManager().updatePosition(id, "10-10-10");
        assertThrows(UnauthorizedException.class, () -> shop.updateQuantity(id, 10));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.updateQuantity(id, 10));
        shop.login("Hossain", "123");
        assertTrue(shop.updateQuantity(id, 10));
        shop.login("Mattia", "123");
        assertTrue(shop.updateQuantity(id, 10));
    }
    
    @Test
    public void testUpdatePosition() throws InvalidPasswordException, InvalidUsernameException, InvalidLocationException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        Integer id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.updatePosition(id, "10-10-10"));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.updatePosition(id, "10-10-10"));
        shop.login("Hossain", "123");
        assertTrue(shop.updatePosition(id, "10-10-10"));
        shop.login("Mattia", "123");
        assertTrue(shop.updatePosition(id, "10-20-10"));
    }
    
    @Test
    public void testIssueOrder() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.issueOrder("123456789012", 20, 25.0));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.issueOrder("123456789012", 20, 25.0));
        shop.login("Hossain", "123");
        assertNotEquals(-1, (int) shop.issueOrder("123456789012", 20, 25.0));
        shop.login("Mattia", "123");
        assertNotEquals(-1, (int) shop.issueOrder("123456789012", 20, 25.0));
    }
    
    @Test
    public void testPayOrderForInvalid() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        assertThrows(UnauthorizedException.class, () -> shop.payOrderFor("123456789012", 10, 10));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.payOrderFor("123456789012", 10, 10));
    }
    
    @Test
    public void testPayOrderForAdmin() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.login("Mattia", "123");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        assertNotEquals(-1, (int) shop.payOrderFor("123456789012", 10, 10));
    }
    
    @Test
    public void testPayOrderForShopManager() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.login("Hossain", "123");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        assertNotEquals(-1, (int) shop.payOrderFor("123456789012", 10, 10));
    }
    
    @Test
    public void testPayOrderInvalid() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        Integer id = shop.getProductOrderManager().issueOrder("123456789012", 20, 25.0);
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        assertThrows(UnauthorizedException.class, () -> shop.payOrder(id));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.payOrder(id));
    }
    
    @Test
    public void testPayOrderAdmin() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidOrderIdException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        Integer id = shop.getProductOrderManager().issueOrder("123456789012", 20, 25.0);
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        shop.login("Mattia", "123");
        assertTrue(shop.payOrder(id));
    }
    
    @Test
    public void testPayOrderShopManager() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidOrderIdException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        Integer id = shop.getProductOrderManager().issueOrder("123456789012", 20, 25.0);
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        shop.login("Hossain", "123");
        assertTrue(shop.payOrder(id));
    }
    
    @Test
    public void recordOrderArrivalInvalid() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        final int id = shop.getProductOrderManager().payOrderFor("123456789012", 10, 10);
        assertNotEquals(-1, id);
        assertThrows(UnauthorizedException.class, () -> shop.recordOrderArrival(id));
        shop.login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.recordOrderArrival(id));
    }
    
    @Test
    public void recordOrderAdmin() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidLocationException, InvalidOrderIdException, InvalidProductIdException {
        Integer prodId = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getProductOrderManager().updatePosition(prodId, "10-10-10");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        final int id = shop.getProductOrderManager().payOrderFor("123456789012", 10, 10);
        assertNotEquals(-1, id);
        shop.login("Mattia", "123");
        assertTrue(shop.recordOrderArrival(id));
    }
    
    @Test
    public void recordOrderShopManager() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidLocationException, InvalidOrderIdException, InvalidProductIdException {
        Integer prodId = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getProductOrderManager().updatePosition(prodId, "10-10-10");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        final int id = shop.getProductOrderManager().payOrderFor("123456789012", 10, 10);
        assertNotEquals(-1, id);
        shop.login("Hossain", "123");
        assertTrue(shop.recordOrderArrival(id));
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
    
    @After()
    public void clear() {
        shop.reset();
    }
}