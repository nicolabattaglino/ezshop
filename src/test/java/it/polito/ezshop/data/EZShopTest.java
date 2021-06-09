package it.polito.ezshop.data;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class EZShopTest {
    
    private static final EZShop shop = new EZShop();
    
    @Before
    public void addUsers() {
        try {
            shop.getUserManager().createUser("Hossain", "123", UserRole.ShopManager.name());
            shop.getUserManager().createUser("Mattia", "123", UserRole.Administrator.name());
            shop.getUserManager().createUser("Stefano", "123", UserRole.Cashier.name());
            
        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetTransactionManager() {
        assertNotNull(shop.getTransactionManager());
    }
    
    @Test
    public void testGetCustomerManager() {
        assertNotNull(shop.getCustomerManager());
    }
    
    @Test
    public void testGetUserManager() {
        assertNotNull(shop.getUserManager());
    }
    
    @Test
    public void testCreateUser() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        int id = shop.getUserManager().createUser("JohnB", "123321", "Cashier");
        shop.getUserManager().login("Mattia", "123");
        assertEquals(id, (int) shop.getUser(id).getId());
    }
    
    @Test
    public void testDeleteUser() throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException {
        
        assertThrows(UnauthorizedException.class, () -> shop.deleteUser(0));
        shop.getUserManager().login("Hossein", "123");
        assertThrows(UnauthorizedException.class, () -> shop.deleteUser(0));
        shop.getUserManager().login("Mattia", "123");
        int id = shop.getUserManager().createUser("JohnB", "123321", "Cashier");
        assertTrue(shop.deleteUser(id));
    }
    
    @Test
    public void testGetAllUsers() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
        assertThrows(UnauthorizedException.class, shop::getAllUsers);
        shop.getUserManager().login("Hossein", "123");
        assertThrows(UnauthorizedException.class, shop::getAllUsers);
        shop.getUserManager().login("Mattia", "123");
        assertEquals(2, (int) shop.getAllUsers().get(1).getId());
    }
    
    @Test
    public void testGetUser() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
        assertThrows(UnauthorizedException.class, () -> shop.getUser(0));
        shop.getUserManager().login("Hossein", "123");
        assertThrows(UnauthorizedException.class, () -> shop.deleteUser(0));
        shop.getUserManager().login("Mattia", "123");
        int id = shop.getUserManager().createUser("JohnB", "123321", "Cashier");
        assertEquals("JohnB", shop.getUser(id).getUsername());
        assertNull(shop.getUser(100));
    }
    
    @Test
    public void testUpdateUserRights() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
        assertThrows(UnauthorizedException.class, () -> shop.updateUserRights(0, "Cashier"));
        shop.getUserManager().login("Hossein", "123");
        assertThrows(UnauthorizedException.class, () -> shop.deleteUser(0));
        shop.getUserManager().login("Mattia", "123");
        int id = shop.getUserManager().createUser("JohnB", "123321", "Cashier");
        assertTrue(shop.updateUserRights(id, "ShopManager"));
        assertFalse(shop.updateUserRights(10, "ShopManager"));
    }
    
    @Test
    public void testLogin() throws InvalidPasswordException, InvalidUsernameException {
        assertNull(shop.login("Mattia", "123456"));
        User u = shop.login("Mattia", "123");
        assertEquals(shop.getUserManager().getUserLogged().getUsername(), u.getUsername());
    }
    
    @Test
    public void testGetProductOrderManager() {
        assertNotNull(shop.getProductOrderManager());
    }
    
    @Test
    public void testLogout() throws InvalidPasswordException, InvalidUsernameException {
        assertFalse(shop.logout());
        shop.getUserManager().login("Mattia", "123");
        assertTrue(shop.logout());
    }
    
    @Test
    public void testCreateProductType() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, () -> shop.createProductType("test", "123456789012", 25.0, "note"));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.createProductType("test", "123456789012", 25.0, "note"));
        shop.getUserManager().login("Hossain", "123");
        assertNotEquals(-1, (int) shop.createProductType("test", "123456789012", 25.0, "note"));
        shop.getUserManager().login("Mattia", "123");
        assertNotEquals(-1, (int) shop.createProductType("test1", "1234567890128", 22.0, "note"));
    }
    
    @Test
    public void testUpdateProduct() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductIdException {
        Integer id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.updateProduct(id, "test1", "123456789012", 22.0, "nota"));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.updateProduct(id, "test1", "123456789012", 22.0, "nota"));
        shop.getUserManager().login("Hossain", "123");
        assertTrue(shop.updateProduct(id, "test1", "123456789012", 22.0, "nota"));
        shop.getUserManager().login("Mattia", "123");
        assertTrue(shop.updateProduct(id, "test1", "123456789012", 24.0, "nota"));
    }
    
    @Test
    public void testDeleteProductType() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        Integer id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        Integer finalId = id;
        assertThrows(UnauthorizedException.class, () -> shop.updateProduct(finalId, "test1", "123456789012", 22.0, "nota"));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.updateProduct(finalId, "test1", "123456789012", 22.0, "nota"));
        shop.getUserManager().login("Hossain", "123");
        assertTrue(shop.deleteProductType(id));
        id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getUserManager().login("Mattia", "123");
        assertTrue(shop.deleteProductType(id));
    }
    
    @Test
    public void testGetAllProductTypes() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, shop::getAllProductTypes);
        shop.getUserManager().login("Stefano", "123");
        assertFalse(shop.getAllProductTypes().isEmpty());
        shop.getUserManager().login("Hossain", "123");
        assertFalse(shop.getAllProductTypes().isEmpty());
        shop.getUserManager().login("Mattia", "123");
        assertFalse(shop.getAllProductTypes().isEmpty());
    }
    
    @Test
    public void testGetProductTypeByBarCode() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypeByBarCode("123456789012"));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypeByBarCode("123456789012"));
        shop.getUserManager().login("Hossain", "123");
        assertNotNull(shop.getProductTypeByBarCode("123456789012"));
        shop.getUserManager().login("Mattia", "123");
        assertNotNull(shop.getProductTypeByBarCode("123456789012"));
    }
    
    @Test
    public void testGetProductTypesByDescription() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypesByDescription("test"));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.getProductTypesByDescription("test"));
        shop.getUserManager().login("Hossain", "123");
        assertFalse(shop.getProductTypesByDescription("test").isEmpty());
        shop.getUserManager().login("Mattia", "123");
        assertFalse(shop.getProductTypesByDescription("test").isEmpty());
    }
    
    @Test
    public void testUpdateQuantity() throws InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
        Integer id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getProductOrderManager().updatePosition(id, "10-A-10");
        assertThrows(UnauthorizedException.class, () -> shop.updateQuantity(id, 10));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.updateQuantity(id, 10));
        shop.getUserManager().login("Hossain", "123");
        assertTrue(shop.updateQuantity(id, 10));
        shop.getUserManager().login("Mattia", "123");
        assertTrue(shop.updateQuantity(id, 10));
    }
    
    @Test
    public void testUpdatePosition() throws InvalidPasswordException, InvalidUsernameException, InvalidLocationException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        Integer id = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.updatePosition(id, "10-A-10"));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.updatePosition(id, "10-A-10"));
        shop.getUserManager().login("Hossain", "123");
        assertTrue(shop.updatePosition(id, "10-A-10"));
        shop.getUserManager().login("Mattia", "123");
        assertTrue(shop.updatePosition(id, "10-A-10"));
    }
    
    @Test
    public void testIssueOrder() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        assertThrows(UnauthorizedException.class, () -> shop.issueOrder("123456789012", 20, 25.0));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.issueOrder("123456789012", 20, 25.0));
        shop.getUserManager().login("Hossain", "123");
        assertNotEquals(-1, (int) shop.issueOrder("123456789012", 20, 25.0));
        shop.getUserManager().login("Mattia", "123");
        assertNotEquals(-1, (int) shop.issueOrder("123456789012", 20, 25.0));
    }
    
    @Test
    public void testPayOrderForInvalid() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        assertThrows(UnauthorizedException.class, () -> shop.payOrderFor("123456789012", 10, 10));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.payOrderFor("123456789012", 10, 10));
    }
    
    @Test
    public void testPayOrderForAdmin() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getUserManager().login("Mattia", "123");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        assertNotEquals(-1, (int) shop.payOrderFor("123456789012", 10, 10));
    }
    
    @Test
    public void testPayOrderForShopManager() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getUserManager().login("Hossain", "123");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        assertNotEquals(-1, (int) shop.payOrderFor("123456789012", 10, 10));
    }
    
    @Test
    public void testPayOrderInvalid() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        Integer id = shop.getProductOrderManager().issueOrder("123456789012", 20, 25.0);
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        assertThrows(UnauthorizedException.class, () -> shop.payOrder(id));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.payOrder(id));
    }
    
    @Test
    public void testPayOrderAdmin() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidOrderIdException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        Integer id = shop.getProductOrderManager().issueOrder("123456789012", 20, 25.0);
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        shop.getUserManager().login("Mattia", "123");
        assertTrue(shop.payOrder(id));
    }
    
    @Test
    public void testPayOrderShopManager() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidOrderIdException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        Integer id = shop.getProductOrderManager().issueOrder("123456789012", 20, 25.0);
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        shop.getUserManager().login("Hossain", "123");
        assertTrue(shop.payOrder(id));
    }
    
    @Test
    public void testRecordOrderArrivalInvalid() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        final int id = shop.getProductOrderManager().payOrderFor("123456789012", 10, 10);
        assertNotEquals(-1, id);
        assertThrows(UnauthorizedException.class, () -> shop.recordOrderArrival(id));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.recordOrderArrival(id));
    }
    
    @Test
    public void testRecordOrderAdmin() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidLocationException, InvalidOrderIdException, InvalidProductIdException {
        Integer prodId = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getProductOrderManager().updatePosition(prodId, "10-A-10");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        final int id = shop.getProductOrderManager().payOrderFor("123456789012", 10, 10);
        assertNotEquals(-1, id);
        shop.getUserManager().login("Mattia", "123");
        assertTrue(shop.recordOrderArrival(id));
    }
    
    @Test
    public void testRecordOrderShopManager() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidLocationException, InvalidOrderIdException, InvalidProductIdException {
        Integer prodId = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getProductOrderManager().updatePosition(prodId, "10-A-10");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        final int id = shop.getProductOrderManager().payOrderFor("123456789012", 10, 10);
        assertNotEquals(-1, id);
        shop.getUserManager().login("Hossain", "123");
        assertTrue(shop.recordOrderArrival(id));
    }
    
    @Test
    public void testRecordOrderArrivalRFIDInvalid() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        final int id = shop.getProductOrderManager().payOrderFor("123456789012", 10, 10);
        assertNotEquals(-1, id);
        assertThrows(UnauthorizedException.class, () -> shop.recordOrderArrivalRFID(id, "000000000100"));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.recordOrderArrivalRFID(id, "000000000100"));
    }
    
    @Test
    public void testRecordOrderRFIDAdmin() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidLocationException, InvalidOrderIdException, InvalidProductIdException, InvalidRFIDException {
        Integer prodId = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getProductOrderManager().updatePosition(prodId, "10-A-10");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        final int id = shop.getProductOrderManager().payOrderFor("123456789012", 10, 10);
        assertNotEquals(-1, id);
        shop.getUserManager().login("Mattia", "123");
        assertTrue(shop.recordOrderArrivalRFID(id, "000000000100"));
    }
    
    @Test
    public void testRecordOrderRFIDShopManager() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidLocationException, InvalidOrderIdException, InvalidProductIdException, InvalidRFIDException {
        Integer prodId = shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getProductOrderManager().updatePosition(prodId, "10-A-10");
        shop.getTransactionManager().recordBalanceUpdate(Double.POSITIVE_INFINITY);
        final int id = shop.getProductOrderManager().payOrderFor("123456789012", 10, 10);
        assertNotEquals(-1, id);
        shop.getUserManager().login("Hossain", "123");
        assertTrue(shop.recordOrderArrivalRFID(id, "000000000100"));
    }
    
    @Test
    public void testGetAllOrdersInvalid() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidPasswordException, InvalidUsernameException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getProductOrderManager().issueOrder("123456789012", 20, 25.0);
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, shop::getAllOrders);
        
    }
    
    @Test
    public void testGetAllOrdersOk() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        shop.getProductOrderManager().createProductType("test", "123456789012", 25.0, "note");
        shop.getProductOrderManager().issueOrder("123456789012", 20, 25.0);
        shop.getUserManager().login("Hossain", "123");
        assertNotNull(shop.getAllOrders());
        shop.getUserManager().login("Mattia", "123");
        assertNotNull(shop.getAllOrders());
    }
    
    @Test
    public void testDefineCustomer() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
        assertThrows(UnauthorizedException.class, () -> shop.defineCustomer("JohnB"));
        shop.getUserManager().login("Mattia", "123");
        Integer id = shop.getCustomerManager().defineCustomer("JohnB");
        assertEquals(id, shop.getCustomer(id).getId());
    }
    
    @Test
    public void testModifyCustomer() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        assertThrows(UnauthorizedException.class, () -> shop.modifyCustomer(0, "JonhC", "1000000002"));
        shop.getUserManager().login("Mattia", "123");
        Integer id = shop.getCustomerManager().defineCustomer("JohnB");
        String cardId = shop.getCustomerManager().createCard();
        shop.getCustomerManager().modifyCustomer(id, "JohnC", cardId);
        assertEquals("JohnC", shop.getCustomer(id).getCustomerName());
        Integer id1 = shop.getCustomerManager().defineCustomer("MikeC");
        assertFalse(shop.modifyCustomer(id1, "MikeC", cardId));
    }
    
    @Test
    public void testDeleteCustomer() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
        assertThrows(UnauthorizedException.class, () -> shop.deleteCustomer(1));
        shop.getUserManager().login("Mattia", "123");
        Integer id = shop.getCustomerManager().defineCustomer("JohnB");
        assertTrue(shop.deleteCustomer(id));
        assertFalse(shop.deleteCustomer(100));
    }
    
    @Test
    public void testGetCustomer() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
        assertThrows(UnauthorizedException.class, () -> shop.getCustomer(1));
        shop.getUserManager().login("Mattia", "123");
        Integer id = shop.getCustomerManager().defineCustomer("JohnB");
        assertEquals(id, shop.getCustomer(id).getId());
        assertNull(shop.getCustomer(100));
    }
    
    @Test
    public void testGetAllCustomers() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException {
        assertThrows(UnauthorizedException.class, shop::getAllCustomers);
        shop.getUserManager().login("Mattia", "123");
        Integer id = shop.getCustomerManager().defineCustomer("JohnB");
        assertEquals(id, shop.getAllCustomers().get(0).getId());
    }
    
    @Test
    public void testCreateCard() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        assertThrows(UnauthorizedException.class, shop::createCard);
        shop.getUserManager().login("Mattia", "123");
        String cardID = shop.getCustomerManager().createCard();
        assertNotEquals(cardID, "");
    }
    
    @Test
    public void testAttachCardToCustomer() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerIdException, InvalidCustomerCardException {
        assertThrows(UnauthorizedException.class, () -> shop.attachCardToCustomer("1000000003", 1));
        shop.getUserManager().login("Mattia", "123");
        String cardID = shop.getCustomerManager().createCard();
        String cardID1 = shop.getCustomerManager().createCard();
        Integer id = shop.getCustomerManager().defineCustomer("JohnB");
        Integer id1 = shop.getCustomerManager().defineCustomer("Mike");
        assertTrue(shop.attachCardToCustomer(cardID, id));
        assertFalse(shop.attachCardToCustomer(cardID, id1));
        assertFalse(shop.attachCardToCustomer(cardID1, 100));
    }
    
    @Test
    public void testModifyPointsOnCard() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerIdException, InvalidCustomerCardException {
        assertThrows(UnauthorizedException.class, () -> shop.modifyPointsOnCard("1000000003", 1));
        shop.getUserManager().login("Mattia", "123");
        String cardID = shop.getCustomerManager().createCard();
        Integer id = shop.getCustomerManager().defineCustomer("JohnB");
        shop.getCustomerManager().attachCardToCustomer(cardID, id);
        assertTrue(shop.modifyPointsOnCard(cardID, 10));
        assertFalse(shop.modifyPointsOnCard(cardID, -40));
        assertFalse(shop.modifyPointsOnCard("1000000005", 10));
    }
    
    @Test
    public void testStartSaleTransaction() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, () -> shop.startSaleTransaction());
        shop.getUserManager().login("Mattia", "123");
        assertTrue(shop.startSaleTransaction() >= 0);
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.startSaleTransaction() >= 0);
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.startSaleTransaction() >= 0);
        
    }
    
    @Test
    public void testAddProductToSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException {
        assertThrows(UnauthorizedException.class, () -> shop.addProductToSale(1, "123456789012", 1));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 2);
        shop.addProductToSale(saleId, "123456789012", 1);
        assertTrue(shop.addProductToSale(saleId, "123456789012", 1));
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.addProductToSale(saleId, "123456789012", 1));
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.addProductToSale(saleId, "123456789012", 1));
    }
    
    @Test
    public void testDeleteProductFromSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidTransactionIdException {
        assertThrows(UnauthorizedException.class, () -> shop.deleteProductFromSale(1, "123456789012", 1));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 2);
        shop.addProductToSale(saleId, "123456789012", 1);
        assertTrue(shop.deleteProductFromSale(saleId, "123456789012", 1));
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.deleteProductFromSale(saleId, "123456789012", 1));
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.deleteProductFromSale(saleId, "123456789012", 1));
    }
    
    @Test
    public void testApplyDiscountRateToProduct() throws InvalidTransactionIdException, UnauthorizedException, InvalidDiscountRateException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException, InvalidProductDescriptionException, InvalidPricePerUnitException {
        assertThrows(UnauthorizedException.class, () -> shop.applyDiscountRateToProduct(1, "123456789012", 0.5));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 2);
        shop.addProductToSale(saleId, "123456789012", 1);
        
        assertTrue(shop.applyDiscountRateToProduct(saleId, "123456789012", 0.5));
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.applyDiscountRateToProduct(saleId, "123456789012", 0.5));
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.applyDiscountRateToProduct(saleId, "123456789012", 0.5));
    }
    
    @Test
    public void testApplyDiscountRateToSale() throws InvalidTransactionIdException, UnauthorizedException, InvalidDiscountRateException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidLocationException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, () -> shop.applyDiscountRateToSale(1, 0.5));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 2);
        shop.addProductToSale(saleId, "123456789012", 1);
        
        assertTrue(shop.applyDiscountRateToSale(saleId, 0.5));
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.applyDiscountRateToSale(saleId, 0.5));
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.applyDiscountRateToSale(saleId, 0.5));
    }
    
    @Test
    public void testComputePointsForSale() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException {
        assertThrows(UnauthorizedException.class, () -> shop.computePointsForSale(1));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 2);
        shop.addProductToSale(saleId, "123456789012", 1);
        assertTrue(shop.computePointsForSale(saleId) > 0);
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.computePointsForSale(saleId) > 0);
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.computePointsForSale(saleId) > 0);
    }
    
    @Test
    public void testEndSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException, InvalidProductDescriptionException, InvalidPricePerUnitException {
        assertThrows(UnauthorizedException.class, () -> shop.endSaleTransaction(1));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 2);
        shop.addProductToSale(saleId, "123456789012", 1);
        assertTrue(shop.endSaleTransaction(saleId));
        shop.getUserManager().login("Hossein", "123");
        saleId = shop.startSaleTransaction();
        assertTrue(shop.endSaleTransaction(saleId));
        shop.getUserManager().login("Stefano", "123");
        saleId = shop.startSaleTransaction();
        assertTrue(shop.endSaleTransaction(saleId));
    }
    
    @Test
    public void testDeleteSaleTransaction() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidTransactionIdException {
        assertThrows(UnauthorizedException.class, () -> shop.deleteSaleTransaction(1));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 1);
        shop.addProductToSale(saleId, "123456789012", 1);
        shop.endSaleTransaction(saleId);
        int retCode = shop.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        shop.returnProduct(retCode, pBarCode, 1);
        assertTrue(shop.deleteSaleTransaction(saleId));
        shop.getUserManager().login("Hossein", "123");
        saleId = shop.startSaleTransaction();
        assertTrue(shop.deleteSaleTransaction(saleId));
        shop.getUserManager().login("Stefano", "123");
        saleId = shop.startSaleTransaction();
        assertTrue(shop.deleteSaleTransaction(saleId));
    }
    
    @Test
    public void testGetSaleTransaction() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidTransactionIdException {
        assertThrows(UnauthorizedException.class, () -> shop.getSaleTransaction(1));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        shop.endSaleTransaction(saleId);
        assertNotNull(shop.getSaleTransaction(saleId));
        shop.getUserManager().login("Hossein", "123");
        assertNotNull(shop.getSaleTransaction(saleId));
        shop.getUserManager().login("Stefano", "123");
        assertNotNull(shop.getSaleTransaction(saleId));
    }
    
    @Test
    public void testStartReturnTransaction() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidTransactionIdException {
        assertThrows(UnauthorizedException.class, () -> shop.startReturnTransaction(1));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        shop.endSaleTransaction(saleId);
        assertTrue(shop.startReturnTransaction(saleId) >= 0);
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.startReturnTransaction(saleId) >= 0);
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.startReturnTransaction(saleId) >= 0);
    }
    
    @Test
    public void testReturnProduct() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException {
        assertThrows(UnauthorizedException.class, () -> shop.returnProduct(1, "", 1));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 1);
        shop.addProductToSale(saleId, "123456789012", 1);
        shop.endSaleTransaction(saleId);
        int retCode = shop.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        String fakeCode = "1234567890128";
        poManager.createProductType("test2", "1234567890128", 5.0, "note");
        assertTrue(shop.returnProduct(retCode, pBarCode, 1));
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.returnProduct(retCode, pBarCode, 1));
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.returnProduct(retCode, pBarCode, 1));
    }
    
    @Test
    public void testEndReturnTransaction() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidTransactionIdException, InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, () -> shop.endReturnTransaction(1, true));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 1);
        shop.addProductToSale(saleId, "123456789012", 1);
        shop.endSaleTransaction(saleId);
        int retCode = shop.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        shop.returnProduct(retCode, pBarCode, 1);
        assertTrue(shop.endReturnTransaction(retCode, true));
        shop.getUserManager().login("Hossein", "123");
        retCode = shop.startReturnTransaction(saleId);
        pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        shop.returnProduct(retCode, pBarCode, 1);
        assertTrue(shop.endReturnTransaction(retCode, true));
        retCode = shop.startReturnTransaction(saleId);
        pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        shop.returnProduct(retCode, pBarCode, 1);
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.endReturnTransaction(retCode, true));
    }
    
    @Test
    public void testDeleteReturnTransaction() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException {
        assertThrows(UnauthorizedException.class, () -> shop.deleteReturnTransaction(1));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 1);
        shop.addProductToSale(saleId, "123456789012", 1);
        shop.endSaleTransaction(saleId);
        int retCode = shop.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        shop.returnProduct(retCode, pBarCode, 1);
        assertFalse(shop.deleteReturnTransaction(retCode + 1));
        shop.getUserManager().login("Hossein", "123");
        assertFalse(shop.deleteReturnTransaction(retCode + 1));
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.deleteReturnTransaction(retCode));
    }
    
    @Test
    public void testReceiveCashPayment() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException {
        assertThrows(UnauthorizedException.class, () -> shop.receiveCashPayment(1, 1));
        shop.getUserManager().login("Mattia", "123");
        shop.getUserManager().login("Stefano", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "123456789012", 1);
        assertTrue(shop.receiveCashPayment(saleId, 10) > 0);
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.receiveCashPayment(saleId, 10) > 0);
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.receiveCashPayment(saleId, 10) > 0);
        
    }
    
    @Test
    public void testReceiveCreditCardPayment() throws InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidLocationException, InvalidProductIdException, InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException {
        assertThrows(UnauthorizedException.class, () -> shop.receiveCreditCardPayment(1, ""));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "123456789012", 1);
        String ccNumber = "79927398713";
        assertTrue(shop.receiveCreditCardPayment(saleId, ccNumber));//correct
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.receiveCreditCardPayment(saleId, ccNumber));//correct
        shop.getUserManager().login("Stefano", "123");
        String ccNumber2 = "1010101010101010101";
        assertTrue(shop.receiveCreditCardPayment(saleId, ccNumber2));//correct
    }
    
    @Test
    public void testReturnCashPayment() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException {
        assertThrows(UnauthorizedException.class, () -> shop.returnCashPayment(1));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 2);
        shop.addProductToSale(saleId, "123456789012", 1);
        shop.endSaleTransaction(saleId);
        int retCode = shop.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        shop.returnProduct(retCode, pBarCode, 1);
        String ccNumber = "1010101010101010101";
        shop.receiveCreditCardPayment(saleId, ccNumber);
        shop.endReturnTransaction(retCode, true);
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.returnCashPayment(retCode) > 0);
    }
    
    @Test
    public void testReturnCreditCardPayment() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException {
        assertThrows(UnauthorizedException.class, () -> shop.returnCreditCardPayment(1, ""));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 2);
        shop.addProductToSale(saleId, "123456789012", 1);
        shop.endSaleTransaction(saleId);
        
        int retCode = shop.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        shop.returnProduct(retCode, pBarCode, 1);
        String ccNumber = "79927398713";
        shop.receiveCreditCardPayment(saleId, ccNumber);
        shop.endReturnTransaction(retCode, true);
        assertTrue(shop.returnCreditCardPayment(retCode, ccNumber) > 0);
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.returnCreditCardPayment(retCode, ccNumber) > 0);
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.returnCreditCardPayment(retCode, ccNumber) > 0);
    }
    
    @Test
    public void testRecordBalanceUpdate() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        assertThrows(UnauthorizedException.class, () -> shop.recordBalanceUpdate(5.0));
        shop.getUserManager().login("Stefano", "123");
        assertThrows(UnauthorizedException.class, () -> shop.recordBalanceUpdate(5.0));
        shop.getUserManager().login("Mattia", "123");
        assertTrue(shop.recordBalanceUpdate(5.0));
        shop.getUserManager().login("Hossein", "123");
        assertTrue(shop.recordBalanceUpdate(5.0));
        
    }
    
    @Test
    public void testGetCreditsAndDebits() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException {
        assertThrows(UnauthorizedException.class, () -> shop.getCreditsAndDebits(LocalDate.now().minusDays(1), LocalDate.now().minusDays(-1)));
        shop.getUserManager().login("Mattia", "123");
        int saleId = shop.startSaleTransaction();
        ProductOrderManager poManager = shop.getProductOrderManager();
        poManager.createProductType("test", "123456789012", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("123456789012").getId(), "11-A-11");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("123456789012").getId(), 100);
        poManager.createProductType("test2", "12345678901286", 5.0, "note");
        poManager.updatePosition(poManager.getProductTypeByBarCode("12345678901286").getId(), "12-A-12");
        poManager.updateQuantity(poManager.getProductTypeByBarCode("12345678901286").getId(), 100);
        shop.addProductToSale(saleId, "12345678901286", 2);
        shop.addProductToSale(saleId, "123456789012", 1);
        shop.endSaleTransaction(saleId);
        
        int retCode = shop.startReturnTransaction(saleId);
        String pBarCode = poManager.getProductTypesByDescription("test").get(0).getBarCode();
        shop.returnProduct(retCode, pBarCode, 1);
        String ccNumber = "79927398713";
        shop.receiveCreditCardPayment(saleId, ccNumber);
        shop.endReturnTransaction(retCode, true);
        shop.returnCreditCardPayment(retCode, ccNumber);
        assertFalse(shop.getCreditsAndDebits(LocalDate.now().minusDays(1), LocalDate.now().minusDays(-1)).isEmpty()); //list exists
        
        
    }
    
    @Test
    public void testComputeBalance() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, () -> shop.computeBalance());
        shop.getUserManager().login("Stefano", "123");
        assertTrue(shop.computeBalance() >= 0);
    }
    
    @Test
    public void testAddOrder() {
        OrderObj order = new OrderObj(1,
                new ProductTypeObj(0, 1, "hello test", "123456789012", "note", 22.0, 0, new Position()),
                25, 2);
        assertTrue(shop.addOrder(order));
    }
    
    @After()
    public void clear() {
        shop.reset();
    }
}