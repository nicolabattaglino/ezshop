package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class UserManagerTest {


    @Test
    public void testGetUserLogged() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
        UserManager um = new UserManager(null);
        um.createUser("SimAdmin","12345","Administrator");
        User userLogged = um.login("SimAdmin", "12345");
        assertEquals(um.getUserLogged().getUsername(),userLogged.getUsername());
    }

    @Test
    public void testCreateUser() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
        UserManager um = new UserManager(null);
        um.clear();
        um.createUser("SimAdmin","12345","Administrator");

        assertThrows(InvalidUsernameException.class, () -> um.createUser("","1234","Cashier"));
        assertThrows(InvalidUsernameException.class, () -> um.createUser(null,"1234","Cashier"));
        assertThrows(InvalidPasswordException.class, () -> um.createUser("John","","Cashier"));
        assertThrows(InvalidPasswordException.class, () -> um.createUser("John",null,"Cashier"));
        assertThrows(InvalidRoleException.class, () -> um.createUser("John","1234",""));
        assertThrows(InvalidRoleException.class, () -> um.createUser("John","1234",null));
        assertThrows(InvalidRoleException.class, () -> um.createUser("John","1234","employee"));
        assertEquals(-1, (int) um.createUser("SimAdmin", "12345", "Administrator"));
        assertEquals(um.getAllUsers().size(), (int) um.createUser("John", "54321", "Cashier"));
    }

    @Test
    public void testDeleteUser() throws InvalidUserIdException, UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        UserManager um = new UserManager(null);
        um.createUser("SimAdmin","12345","Administrator");
        um.createUser("John", "54321", "Cashier");
        assertThrows(InvalidUserIdException.class, () -> um.deleteUser(-2));
        assertThrows(InvalidUserIdException.class, () -> um.deleteUser(null));
        assertTrue(um.deleteUser(1));
    }

    @Test
    public void testGetAllUsers() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
        UserManager um = new UserManager(null);
        um.clear();
        um.createUser("SimAdmin","12345","Administrator");
        um.createUser("John", "54321", "Cashier");
        LinkedList <User> userList;
        userList = (LinkedList<User>) um.getAllUsers();
        assertEquals(2, userList.size());
    }

    @Test
    public void testGetUser() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        UserManager um = new UserManager(null);
        um.clear();
        um.createUser("SimAdmin","12345","Administrator");
        int userId = um.createUser("John", "54321", "Cashier");
        assertThrows(InvalidUserIdException.class, () -> um.getUser(-1));
        assertEquals("John", um.getUser(userId).getUsername());
        assertNull(um.getUser(10));
    }

    @Test
    public void testUpdateUserRights() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        UserManager um = new UserManager(null);
        um.clear();
        um.createUser("SimAdmin","12345","Administrator");
        int userId = um.createUser("John", "54321", "Cashier");
        assertThrows(InvalidUserIdException.class, () -> um.updateUserRights(-1, "Administrator"));
        assertThrows(InvalidUserIdException.class, () -> um.updateUserRights(null,"Administrator"));
        assertThrows(InvalidRoleException.class, () -> um.updateUserRights(0,""));
        assertThrows(InvalidRoleException.class, () -> um.updateUserRights(0,null));
        assertThrows(InvalidRoleException.class, () -> um.updateUserRights(0,null));
        assertThrows(InvalidRoleException.class, () -> um.updateUserRights(0,"t"));
        assertTrue(um.updateUserRights(userId,"ShopManager"));
    }

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
    public void clear() {

    }
}