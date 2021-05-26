package it.polito.ezshop.classes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class UserManager {
    
    public static final String USERS_PATH = "data/users.json";
    public static final String USERS_ID_PATH = "data/userIdGen.json";
    
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private LinkedList<UserObj> userList;
    private Integer userIdGen = 0;
    private User loggedUser;

    
    public UserManager() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<LinkedList<UserObj>> typeRef = new TypeReference<LinkedList<UserObj>>() {
        };
        File users = new File(USERS_PATH);
        try {
            users.createNewFile();
            userList = mapper.readValue(users, typeRef);
        } catch (IOException e) {
            users.delete();
            try {
                users.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                userList = new LinkedList<>();
            }
        }
        TypeReference<Integer> typeRef2 = new TypeReference<Integer>() {
        };
        File usersId = new File(USERS_ID_PATH);
        try {
            usersId.createNewFile();
            userIdGen = mapper.readValue(usersId, typeRef2);
        } catch (IOException e) {
            usersId.delete();
            try {
                usersId.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                userIdGen = 0;
            }
        }
        
    }
    
    
    public User getUserLogged() {
        if (loggedUser == null) return null;
        return new UserObj(loggedUser.getId(), loggedUser.getUsername(), loggedUser.getPassword(), UserRole.valueOf(loggedUser.getRole()));
    }
    
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        if (username == null || username.equals(""))
            throw new InvalidUsernameException();
        
        if (password == null || password.equals(""))
            throw new InvalidPasswordException();
        
        if (role == null || role.equals("") ||
                (!role.equalsIgnoreCase("ADMINISTRATOR") &&
                        !role.equalsIgnoreCase("CASHIER") &&
                        !role.equalsIgnoreCase("SHOPMANAGER")))
            throw new InvalidRoleException();
    
        for (User user : userList) {
            if (user.getUsername().equals(username))
                return -1;
        }
        if (userList.size() == 0) {
            userIdGen = 0;
        } else {
            userIdGen = userIdGen + 1;
        }
    
    
        UserObj u = new UserObj(userIdGen, username, password, UserRole.valueOf(role.toUpperCase()));
    
        if (!userList.add(u))
            return -1;
        try {
            persistUsers();
            persistUsersId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userIdGen;
        
    }
    
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        int i = 0;
        if (id == null || id < 0)
            throw new InvalidUserIdException();
        UserObj u;
        for (i = 0; i < userList.size(); i++) {
            u = userList.get(i);
            if (u.getId().equals(id)) {
                if (u != userList.remove(i)) {
                    return false;
                } else {
                    try {
                        persistUsers();
                    } catch (IOException e) {
                       // userList.add(u);
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<User> getAllUsers() throws UnauthorizedException {
        return new LinkedList<User>(userList);
    }
    
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        int i = 0;
        if (id < 0)
            throw new InvalidUserIdException();
        
        for (User u : userList) {
            if (u.getId().equals(id)) {
                return new UserObj(u.getId(), u.getUsername(), u.getPassword(), UserRole.valueOf(u.getRole()));
            } else u = null;
        }
        return null;
    }
    
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        int i = 0;
        String ur = null;
        if (id == null || id < 0)
            throw new InvalidUserIdException();
        if (role == null || role.equals("") ||
                (!role.toUpperCase().equals(UserRole.ADMINISTRATOR.toString()) &&
                        !role.toUpperCase().equals(UserRole.CASHIER.toString()) &&
                        !role.toUpperCase().equals(UserRole.SHOPMANAGER.toString())))
            throw new InvalidRoleException();

        for (i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(id)) {
                User u = userList.get(i);
                u.setRole(role.toUpperCase());
                try {
                    persistUsers();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
    
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {

        if (username == null || username.equals("")) {
            throw new InvalidUsernameException();
        } else if (password == null || password.equals("")) {
            throw new InvalidPasswordException();
        }
        int i = 0;
        for (User u : userList) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                loggedUser = u;
                return loggedUser;
            }
        }
        return null;
    }
    
    public boolean logout() {
        if (loggedUser == null)
            return false;
        loggedUser = null;
        return true;
    }
    
    public void clear() {
        userList.clear();
        loggedUser = null;
        File users = new File(USERS_PATH);
        File usersId = new File(USERS_ID_PATH);
        users.delete();
        usersId.delete();
        
    }
    
    private void persistUsers() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(USERS_PATH), userList);
        
    }
    
    private void persistUsersId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(USERS_ID_PATH), userIdGen);
    }
    
}
