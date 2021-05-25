package it.polito.ezshop.classes;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoyaltyCardObjTest {
    
    
    @Test
    public void testSetIsAttached() {
        LoyaltyCardObj l1 = new LoyaltyCardObj("1000000002");
        l1.setIsAttached(true);
        assertTrue(l1.getIsAttached());
        l1.setIsAttached(false);
        assertFalse(l1.getIsAttached());
    }
    
    @Test
    public void testSetCardCode() {
        LoyaltyCardObj l1 = new LoyaltyCardObj();
        l1.setCardCode("1000000003");
        assertEquals("1000000003", l1.getCardCode());
    }
    
    @Test
    public void testSetPoints() {
        LoyaltyCardObj l1 = new LoyaltyCardObj("1000000002");
        l1.setPoints(20);
        assertEquals(20, (int) l1.getPoints());
    }
    
    
}
