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
        UserManager um = new UserManager(null);
        um.createUser("SimAdmin","12345","Administrator");
        User userLogged = um.login("SimAdmin", "12345");
        assertEquals(userLogged.getUsername(),um.getUserLogged().getUsername());
        um.logout();
        userLogged = um.login("ZZZ", "0000");
        assertNull(userLogged);

    }

    @Test
    public void testLogout() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
        UserManager um = new UserManager(null);
        assertFalse(um.logout());
        um.createUser("SimAdmin","12345","Administrator");
        User userLogged = um.login("SimAdmin", "12345");
        assertTrue(um.logout());
    }

    @Test
    public void getUserLogged() {
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
    public void logout() {
    }

    @Test
    public void clear() {
    }
}