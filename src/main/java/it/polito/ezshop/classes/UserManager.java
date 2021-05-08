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
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private Integer userIdGen = 0;
    private UserObj loggedUser;
    private static ArrayList<UserObj> userList = new ArrayList<>();

    public UserManager() {
        readFromFile();
    }


    public UserObj getUserLogged(){
        return loggedUser;
    }

    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        UserRole r = null;
            if (role.equals("Administrator")){
                r = UserRole.ADMINISTRATOR;
            } else if (role.equals("Cashier")) {
                r = UserRole.CASHIER;
            } else if (role.equals("ShopManager")){
                r = UserRole.SHOP_MANAGER;
            } else r = null;

            UserObj u = new UserObj(userList.size(), username, password, r);
            userIdGen = userList.size();
            userList.add(u);
            writeToFile();
            return userIdGen;

    }

    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        int i = 0;
            for (i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId() == id) {
                    userList.remove(i);
                    return true;
                }
            }
        return false;
    }

    public List<UserObj> getAllUsers() throws UnauthorizedException {
        return userList;
    }

    public UserObj getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        int i = 0;

            for (i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId() == id) {
                    return userList.get(i);
                }
            }

        return null;
    }

    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        int i = 0;
        UserRole r = null;


            if (role.equals("Administrator")) { // add to lower
                r = UserRole.ADMINISTRATOR;
            } else if (role.equals("Cashier")) {
                r = UserRole.CASHIER;
            } else if (role.equals("ShopManager")) {
                r = UserRole.SHOP_MANAGER;
            } else r = null;

            for (i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId() == id) {
                    UserObj u = userList.get(i);
                    u.setRole(r);
                    return true;
                }
            }

        return false;
    }

    public UserObj login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
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

    private void writeToFile(){
        int i = 0;
        JSONArray userListJSON = new JSONArray();
        for (i = 0; i < userList.size(); i++) {
            JSONObject userDetails = new JSONObject();
            userDetails.put("id", userList.get(i).getId());
            userDetails.put("username", userList.get(i).getUsername());
            userDetails.put("password", userList.get(i).getPassword());
            userDetails.put("role", userList.get(i).getRole().toString());
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

    private void readFromFile(){
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("users.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray employeeList = (JSONArray) obj;
            System.out.println(employeeList);

            //Iterate over employee array
            employeeList.forEach( usr -> parseEmployeeObject( (JSONObject) usr ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private static void parseEmployeeObject(JSONObject user)
    {
        UserRole r;

        String password = (String) user.get("password");
        System.out.println(password);

        String role = (String) user.get("role");
        System.out.println(role);
        Integer id = Integer.parseInt((String) user.get("id"));
        System.out.println(id);

        String username = (String) user.get("username");
        System.out.println(username);
        switch (role) {
            case "Administrator":
                r = UserRole.ADMINISTRATOR;
                break;
            case "Cashier":
                r = UserRole.CASHIER;
                break;
            case "ShopManager":
                r = UserRole.SHOP_MANAGER;
                break;
            default:
                r = null;
                break;
        }
        UserObj u = new UserObj(id, username, password, r);
        userList.add(u);

    }

}
