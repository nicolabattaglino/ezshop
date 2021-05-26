package it.polito.ezshop.classes;

import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class UserManagerTest {
    
    UserManager um;
    
    @Before
    public void initManager() {
        um = new UserManager(null);
    }
    
    @Test
    public void testGetUserLogged() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
        um.createUser("SimAdmin", "12345", "Administrator");
        User userLogged = um.login("SimAdmin", "12345");
        assertEquals(um.getUserLogged().getUsername(), userLogged.getUsername());
    }
    
    @Test
    public void testCreateUser() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
        um.createUser("SimAdmin", "12345", "Administrator");
        
        assertThrows(InvalidUsernameException.class, () -> um.createUser("", "1234", "Cashier"));
        assertThrows(InvalidUsernameException.class, () -> um.createUser(null, "1234", "Cashier"));
        assertThrows(InvalidPasswordException.class, () -> um.createUser("John", "", "Cashier"));
        assertThrows(InvalidPasswordException.class, () -> um.createUser("John", null, "Cashier"));
        assertThrows(InvalidRoleException.class, () -> um.createUser("John", "1234", ""));
        assertThrows(InvalidRoleException.class, () -> um.createUser("John", "1234", null));
        assertThrows(InvalidRoleException.class, () -> um.createUser("John", "1234", "employee"));
        assertEquals(-1, (int) um.createUser("SimAdmin", "12345", "Administrator"));
        assertEquals(um.getAllUsers().size(), (int) um.createUser("John", "54321", "Cashier"));
    }
    
    @Test
    public void testDeleteUser() throws InvalidUserIdException, UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        um.createUser("SimAdmin", "12345", "Administrator");
        um.createUser("John", "54321", "Cashier");
        assertThrows(InvalidUserIdException.class, () -> um.deleteUser(-2));
        assertThrows(InvalidUserIdException.class, () -> um.deleteUser(null));
        assertTrue(um.deleteUser(1));
    }
    
    @Test
    public void testGetAllUsers() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
        assertTrue(um.getAllUsers().isEmpty());
        um.createUser("SimAdmin", "12345", "Administrator");
        um.createUser("John", "54321", "Cashier");
        LinkedList<User> userList;
        userList = (LinkedList<User>) um.getAllUsers();
        assertEquals(2, userList.size());
    }
    
    @Test
    public void testGetUser() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {

        um.createUser("SimAdmin", "12345", "Administrator");
        int userId = um.createUser("John", "54321", "Cashier");
        assertThrows(InvalidUserIdException.class, () -> um.getUser(-1));
        assertEquals("John", um.getUser(userId).getUsername());
        assertNull(um.getUser(10));
    }
    
    @Test
    public void testUpdateUserRights() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {

        um.createUser("SimAdmin", "12345", "Administrator");
        int userId = um.createUser("John", "54321", "Cashier");
        assertThrows(InvalidUserIdException.class, () -> um.updateUserRights(-1, "Administrator"));
        assertThrows(InvalidUserIdException.class, () -> um.updateUserRights(null, "Administrator"));
        assertThrows(InvalidRoleException.class, () -> um.updateUserRights(0, ""));
        assertThrows(InvalidRoleException.class, () -> um.updateUserRights(0, null));
        assertThrows(InvalidRoleException.class, () -> um.updateUserRights(0, null));
        assertThrows(InvalidRoleException.class, () -> um.updateUserRights(0, "t"));
        assertTrue(um.updateUserRights(userId, "ShopManager"));
        assertFalse(um.updateUserRights(100,"Cashier"));
    }
    
    @Test
    public void testLogin() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {

        assertThrows(InvalidUsernameException.class, () -> um.login(null,"12345"));
        assertThrows(InvalidUsernameException.class, () -> um.login("","12345"));
        assertThrows(InvalidPasswordException.class, () -> um.login("JohnB",null));
        assertThrows(InvalidPasswordException.class, () -> um.login("JohnB",""));

        um.createUser("SimAdmin", "12345", "Administrator");
        User userLogged = um.login("SimAdmin", "12345");
        assertEquals(userLogged.getUsername(), um.getUserLogged().getUsername());
        um.logout();
        userLogged = um.login("ZZZ", "0000");
        assertNull(userLogged);
        
    }
    
    @Test
    public void testLogout() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {

        assertFalse(um.logout());
        um.createUser("SimAdmin", "12345", "Administrator");
        um.login("SimAdmin", "12345");
        assertTrue(um.logout());
    }
    
    
    @After
    public void clear() {
        um.clear();
    }
}