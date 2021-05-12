package it.polito.ezshop.classes;

import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class UserManager {
    
    private static LinkedList<User> userList = new LinkedList<>();
    private Integer userIdGen = 0;
    private User loggedUser;
    
    public UserManager() {
        readFromFile();
    }
    

    
    public User getUserLogged() {
        return loggedUser;
    }
    
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        UserRole r = null;
        role = role.toUpperCase();
        if (username == null || username.equals("")) {
            throw new InvalidUsernameException();
        } else if (password == null || password.equals("")) {
            throw new InvalidPasswordException();
        } else if (role == null || role.equals("") ||
                (!role.equals("ADMINISTRATOR") &&
                        !role.equals("CASHIER") &&
                        !role.equals("SHOPMANAGER"))) { // manage role enum
            throw new InvalidRoleException();
        } else {
            System.out.println(role);
            userIdGen = userList.getLast().getId() + 1;
            UserObj u = new UserObj(userIdGen, username, password, UserRole.valueOf(role));
            System.out.println(u.getRole());
            userList.add(u);
            writeToFile();
            return userIdGen;
        }
    }
    
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        int i = 0;
        if (id < 0 || id == null) { // TODO if id == null
            throw new InvalidUserIdException();
        } else {
            for (i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId() == id) {
                    userList.remove(i);
                    writeToFile();
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<User> getAllUsers() throws UnauthorizedException {
        return userList;
    }
    
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        int i = 0;
        if (id < 0) {
            throw new InvalidUserIdException();
        } else {
            for (i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId() == id) {
                    return userList.get(i);
                }
            }
        }
        
        return null;
    }
    
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        int i = 0;
        role = role.toUpperCase();
        if (id < 0) {
            throw new InvalidUserIdException();
        } else if (role == null || role.equals("") ||
                (!role.toUpperCase().equals(UserRole.ADMINISTRATOR.toString()) &&
                        !role.toUpperCase().equals(UserRole.CASHIER.toString()) &&
                        !role.toUpperCase(Locale.ROOT).equals(UserRole.SHOPMANAGER.toString()))) {
            throw new InvalidRoleException();
        } else {
            for (i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId() == id) {
                    User u = userList.get(i);
                    u.setRole(role);
                    writeToFile();
                    return true;
                }
            }
        }
        return false;
    }
    
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        int i = 0;
        for (i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(username) && userList.get(i).getPassword().equals(password)) {
                loggedUser = userList.get(i);
                return loggedUser;
            }
        }
        return null;
    }
    
    public boolean logout() {
        loggedUser = null;
        return true;
    }

    private static void parseUserObject(JSONObject user) {
        UserRole r;

        String password = (String) user.get("password");
        //System.out.println(password);

        String role = (String) user.get("role");
        //System.out.println(role);
        Integer id = Integer.valueOf(user.get("id").toString());
        //System.out.println(id);

        String username = (String) user.get("username");
        //System.out.println(username);
        switch (role) {
            case "ADMINISTRATOR":
                r = UserRole.ADMINISTRATOR;
                break;
            case "CASHIER":
                r = UserRole.CASHIER;
                break;
            case "SHOPMANAGER":
                r = UserRole.SHOPMANAGER;
                break;
            default:
                r = null;
                break;
        }
        User u = new UserObj(id, username, password, r);
        userList.add(u);

    }

    private void writeToFile() {
        int i = 0;
        JSONArray userListJSON = new JSONArray();
        for (i = 0; i < userList.size(); i++) {
            JSONObject userDetails = new JSONObject();
            userDetails.put("id", userList.get(i).getId());
            userDetails.put("username", userList.get(i).getUsername());
            userDetails.put("password", userList.get(i).getPassword());
            userDetails.put("role", userList.get(i).getRole());
            userListJSON.add(userDetails);
        }
        //Write JSON file
        try (FileWriter file = new FileWriter("users.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(userListJSON.toJSONString());
            file.flush();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void readFromFile() {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("users.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            
            JSONArray employeeList = (JSONArray) obj;
            //System.out.println(employeeList);
            
            //Iterate over employee array
            employeeList.forEach(usr -> parseUserObject((JSONObject) usr));
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
}
