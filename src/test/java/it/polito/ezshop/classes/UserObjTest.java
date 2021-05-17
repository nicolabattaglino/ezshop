package it.polito.ezshop.classes;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserObjTest {

    @Test
    public void testSettersAndGetters(){
        UserObj u = new UserObj(0,"John","123456",UserRole.ADMINISTRATOR);
        assertNotNull(u);
        UserObj u1 = new UserObj();
        assertNotNull(u1);
        u1.setId(2);
        assertTrue(2 == u1.getId());
        u1.setUsername("Tim");
        assertTrue(u1.getUsername().equals("Tim"));
        u1.setPassword("123123");
        assertTrue(u1.getPassword().equals("123123"));
        u1.setRole("shopmanager");
        assertTrue(u1.getRole().equals(UserRole.SHOPMANAGER.toString()));
        u1.setRole("administrator");
        assertTrue(u1.getRole().equals(UserRole.ADMINISTRATOR.toString()));
        u1.setRole("cashier");
        assertTrue(u1.getRole().equals(UserRole.CASHIER.toString()));
        u1.setRole("t");
    }

}