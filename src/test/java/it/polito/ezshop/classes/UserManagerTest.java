package it.polito.ezshop.classes;

import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserManagerTest {

    @Test
    public void testLogin() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
        it.polito.ezshop.data.EZShop shop= new it.polito.ezshop.data.EZShop();
        UserManager um = new UserManager(shop);
        um.createUser("SimAdmin","12345","Administrator");
        User userLogged = um.login("SimAdmin", "12345");
        assertEquals(userLogged.getUsername(),um.getUserLogged().getUsername());
        um.logout();
        userLogged = um.login("ZZZ", "0000");
        assertNull(userLogged);

    }

    @Test
    public void testLogout() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
        it.polito.ezshop.data.EZShop shop= new it.polito.ezshop.data.EZShop();
        UserManager um = new UserManager(shop);
        assertFalse(um.logout());
        um.createUser("SimAdmin","12345","Administrator");
        User userLogged = um.login("SimAdmin", "12345");
        assertTrue(um.logout());
    }
}