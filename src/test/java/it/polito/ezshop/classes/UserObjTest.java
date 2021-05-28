package it.polito.ezshop.classes;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserObjTest {
    
    @Test
    public void testSetId() {
        UserObj u1 = new UserObj(0, "MikeBB", "12345", UserRole.Administrator);
        u1.setId(2);
        assertEquals(2, (int) u1.getId());
    }
    
    @Test
    public void testSetUsername() {
        UserObj u1 = new UserObj();
        u1.setUsername("Tim");
        assertEquals("Tim", u1.getUsername());
    }
    
    @Test
    public void testSetPassword() {
        UserObj u1 = new UserObj();
        u1.setPassword("123123");
        assertEquals("123123", u1.getPassword());
    }
    
    
    @Test
    public void testSetRoleShopManager() {
        UserObj u1 = new UserObj();
        u1.setRole("shopmanager");
        assertEquals(u1.getRole(), UserRole.ShopManager.toString());
    }
    
    @Test
    public void testSetRoleAdministrator() {
        UserObj u1 = new UserObj();
        u1.setRole("administrator");
        assertEquals(u1.getRole(), UserRole.Administrator.toString());
    }
    
    @Test
    public void testSetRoleCashier() {
        UserObj u1 = new UserObj();
        u1.setRole("cashier");
        assertEquals(u1.getRole(), UserRole.Cashier.toString());
    }
    
    @Test
    public void testSetRoleInvalidString() {
        UserObj u1 = new UserObj();
        u1.setRole("t");
        assertNotEquals("t", u1.getRole());
    }
    
    @Test
    public void testSetRoleNull() {
        UserObj u1 = new UserObj(0, "MikeBB", "12345", UserRole.Administrator);
        u1.setRole(null);
        assertNotNull(u1.getRole());
    }
    
}