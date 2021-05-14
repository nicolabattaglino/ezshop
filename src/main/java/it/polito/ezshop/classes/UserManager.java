package it.polito.ezshop.classes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;


import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class UserManager {

    public static final String USERS_PATH = "data/users.json";

    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private static LinkedList<User> userList;
    private Integer userIdGen = 0;
    private User loggedUser;
    
    public UserManager() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<LinkedList<User>> typeRef = new TypeReference<LinkedList<User>>() {
        };
        File users = new File(USERS_PATH);
        try {
            users.createNewFile();
            userList = mapper.readValue(users, typeRef);
            //System.out.println(userList.get(0).getUsername());
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
    }
    

    
    public User getUserLogged() {
        return loggedUser;
    }
    
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        UserRole r = null;
        if (username == null || username.equals(""))
            throw new InvalidUsernameException();

        if (password == null || password.equals(""))
            throw new InvalidPasswordException();

        if (role == null || role.equals("") ||
                (!role.equalsIgnoreCase("ADMINISTRATOR") &&
                        !role.equalsIgnoreCase("CASHIER") &&
                        !role.equalsIgnoreCase("SHOPMANAGER")))  // manage role enum
             throw new InvalidRoleException();

        for (User user : userList) {
            if (user.getUsername().equals(username))
                return -1;
        }

        System.out.println(role);
        if (userList.size() == 0) {
            userIdGen = 0;
        } else {
            userIdGen = userList.getLast().getId() + 1;
        }
        UserObj u = new UserObj(userIdGen, username, password, UserRole.valueOf(role.toUpperCase()));
        //System.out.println(u.getRole());

        if(!userList.add(u))
                return -1;
        try {
            persistUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userIdGen;

    }
    
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        int i = 0;
        if (id == null || id < 0)  // TODO if id == null
            throw new InvalidUserIdException();

        for (i = 0; i < userList.size(); i++) {
            User u = userList.get(i);
            if (u.getId().equals(id)) {
                if(u != userList.remove(i)){
                    return false;
                } else {
                    try {
                        persistUsers();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
        if (id < 0)
            throw new InvalidUserIdException();

        for (i = 0; i < userList.size(); i++) {
            User u = userList.get(i);
            if (u.getId().equals(id)) {
                return u;
            } else u = null;
        }

        return null;
    }
    
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        int i = 0;

        if (id == null || id < 0)
            throw new InvalidUserIdException();
        // todo è giusto cosi?
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
        int i = 0;
        if (username == null || username.equals(""))
            throw new InvalidUsernameException();
        if (password == null || password.equals(""))
            throw new InvalidUsernameException();

        for (i = 0; i < userList.size(); i++) {
            User u = userList.get(i);
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


    private void persistUsers() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(USERS_PATH), userList);
    }
/*
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
    */
}
