package it.polito.ezshop.classes;

import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerManagerTest {
    
    @Test
    public void defineCustomer() throws InvalidCustomerNameException, InvalidCustomerIdException, UnauthorizedException {
        CustomerManager cm = new CustomerManager(null);
        cm.clear();
        assertThrows(InvalidCustomerNameException.class, () -> cm.defineCustomer(""));
        assertThrows(InvalidCustomerNameException.class, () -> cm.defineCustomer(null));
        int customerId = cm.defineCustomer("PaulJ");
        assertEquals(0, (int) cm.getCustomer(customerId).getId());
        assertEquals(-1, (int) cm.defineCustomer("PaulJ"));
        assertEquals(1, (int) cm.defineCustomer("MikeP"));
        
    }
    
    @Test
    public void modifyCustomer() throws InvalidCustomerIdException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException {
        CustomerManager cm = new CustomerManager(null);
        cm.clear();
        cm.createCard();
        cm.createCard();
        cm.createCard();
        int customerId = cm.defineCustomer("MIkeR");
        ;
        assertThrows(InvalidCustomerIdException.class, () -> cm.modifyCustomer(-1, "MikeR", "1000000009"));
        assertThrows(InvalidCustomerNameException.class, () -> cm.modifyCustomer(1, "", "1000000009"));
        assertThrows(InvalidCustomerNameException.class, () -> cm.modifyCustomer(1, null, "1000000009"));
        assertThrows(InvalidCustomerCardException.class, () -> cm.modifyCustomer(1, "MikeR", "111"));
        assertFalse(cm.modifyCustomer(customerId, "MikeR", "1000000009"));
        int customerId1 = cm.defineCustomer("DanyT");
        ;
        cm.attachCardToCustomer("1000000001", customerId1);
        assertFalse(cm.modifyCustomer(customerId, "MikeR", "1000000001"));
        assertTrue(cm.modifyCustomer(customerId, "BobN", null));
        assertFalse(cm.attachCardToCustomer("1000000003", 0));
        cm.attachCardToCustomer("1000000002", 0);
        assertTrue(cm.modifyCustomer(customerId, "BobN", ""));
        assertTrue(cm.modifyCustomer(customerId, "BobN", "1000000000"));
    }
    
    @Test
    public void deleteCustomer() throws InvalidCustomerNameException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerCardException {
        CustomerManager cm = new CustomerManager(null);
        cm.clear();
        assertFalse(cm.deleteCustomer(0));
        int id = cm.defineCustomer("MIkeR");
        createCard();
        cm.attachCardToCustomer("1000000000", id);
        int id1 = cm.defineCustomer("Paul");
        assertThrows(InvalidCustomerIdException.class, () -> cm.deleteCustomer(-1));
        assertTrue(cm.deleteCustomer(id));
        assertTrue(cm.deleteCustomer(id1));
    }
    
    @Test
    public void getCustomer() throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException {
        CustomerManager cm = new CustomerManager(null);
        cm.clear();
        assertThrows(InvalidCustomerIdException.class, () -> cm.getCustomer(-1));
        assertNull(cm.getCustomer(0));
        int id = cm.defineCustomer("MIkeR");
        assertEquals(id, (int) cm.getCustomer(id).getId());
        
    }
    
    @Test
    public void getAllCustomers() throws InvalidCustomerNameException, UnauthorizedException {
        CustomerManager cm = new CustomerManager(null);
        cm.clear();
        int id = cm.defineCustomer("MIkeR");
        assertEquals(id, (int) cm.getAllCustomers().get(id).getId());
    }
    
    @Test
    public void createCard() {
        CustomerManager cm = new CustomerManager(null);
        cm.clear();
        assertEquals("1000000000", cm.createCard());
        assertEquals("1000000001", cm.createCard());
        
    }
    
    @Test
    public void attachCardToCustomer() throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerNameException {
        CustomerManager cm = new CustomerManager(null);
        cm.clear();
        
        assertThrows(InvalidCustomerIdException.class, () -> cm.attachCardToCustomer("1000000000", -1));
        assertThrows(InvalidCustomerCardException.class, () -> cm.attachCardToCustomer("", 0));
        assertThrows(InvalidCustomerCardException.class, () -> cm.attachCardToCustomer("t", 0));
        assertThrows(InvalidCustomerCardException.class, () -> cm.attachCardToCustomer(null, 0));
        assertFalse(cm.attachCardToCustomer("1000000000", 1));
        
        // test is attached
        cm.createCard();
        cm.createCard();
        cm.createCard();
        int customerId = cm.defineCustomer("MIkeR");
        int customerId1 = cm.defineCustomer("DanyT");
        assertTrue(cm.attachCardToCustomer("1000000000", customerId));
        assertFalse(cm.attachCardToCustomer("1000000000", customerId1));
        
    }
    
    @Test
    public void modifyPointsOnCard() throws UnauthorizedException, InvalidCustomerCardException, InvalidCustomerNameException, InvalidCustomerIdException {
        CustomerManager cm = new CustomerManager(null);
        cm.clear();
        assertThrows(InvalidCustomerCardException.class, () -> cm.modifyPointsOnCard("", 10));
        assertThrows(InvalidCustomerCardException.class, () -> cm.modifyPointsOnCard(null, 10));
        assertThrows(InvalidCustomerCardException.class, () -> cm.modifyPointsOnCard("t", 10));
        assertFalse(cm.modifyPointsOnCard("1000000000", 10));
        int customerId = cm.defineCustomer("MIkeR");
        cm.createCard();
        cm.attachCardToCustomer("1000000000", customerId);
        assertTrue(cm.modifyPointsOnCard("1000000000", 10));
        
    }
    
    @Test
    public void clear() {
    }
}