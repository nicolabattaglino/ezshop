package it.polito.ezshop.classes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreditCardTest {
    
    @Test
    public void testSetBalance() {
        CreditCard c = new CreditCard("1234567890", 25.3);
        c.setBalance(25.3);
        assertEquals(25.3, c.getBalance(), 0);
    }
    
    @Test
    public void testSetNumber() {
        CreditCard c = new CreditCard("1234567890", 25.3);
        c.setNumber("1234567890");
        assertEquals("1234567890", c.getNumber());
    }
}