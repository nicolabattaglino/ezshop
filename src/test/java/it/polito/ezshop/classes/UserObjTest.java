package it.polito.ezshop.classes;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserObjTest {

    @Test
    public void testSetId(){
        UserObj u1 = new UserObj(0,"MikeBB","12345",UserRole.ADMINISTRATOR);
        u1.setId(2);
        assertTrue(2 == u1.getId());
    }
    @Test
    public void testSetUsername(){
        UserObj u1 = new UserObj();
        u1.setUsername("Tim");
        assertTrue(u1.getUsername().equals("Tim"));
    }
    @Test
    public void testSetPassword(){
        UserObj u1 = new UserObj();
        u1.setPassword("123123");
        assertTrue(u1.getPassword().equals("123123"));
    }


    @Test
    public void testSetRoleShopManager(){
        UserObj u1 = new UserObj();
        u1.setRole("shopmanager");
        assertTrue(u1.getRole().equals(UserRole.SHOPMANAGER.toString()));
    }

    @Test
    public void testSetRoleAdministrator(){
        UserObj u1 = new UserObj();
        u1.setRole("administrator");
        assertTrue(u1.getRole().equals(UserRole.ADMINISTRATOR.toString()));
    }
    @Test
    public void testSetRoleCashier(){
        UserObj u1 = new UserObj();
        u1.setRole("cashier");
        assertTrue(u1.getRole().equals(UserRole.CASHIER.toString()));
    }
    @Test
    public void testSetRoleInvalidString(){
        UserObj u1 = new UserObj();
        u1.setRole("t");
        assertFalse(u1.getRole().equals("t"));
    }
    @Test
    public void testSetRoleNull(){
        UserObj u1 = new UserObj();
        u1.setRole(null);
        assertFalse(u1.getRole() == null);
    }

}