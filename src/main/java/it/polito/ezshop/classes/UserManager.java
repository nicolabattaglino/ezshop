package it.polito.ezshop.classes;

import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private Integer userIdGen = 0;
    private UserObj loggedUser;
    private static ArrayList<UserObj> userList = new ArrayList<UserObj>();

    public UserManager() {
    }

    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

        UserObj u = new UserObj(userIdGen, username, password, role);
        userIdGen = userList.size() + 1;
        userList.add(u);
        return userIdGen-1;
    }

    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return false;
    }

    public List<User> getAllUsers() throws UnauthorizedException {
        return null;
    }

    public UserObj getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return userList.get(id);
    }

    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        return false;
    }

    public UserObj login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        int i = 0;
        for (i=0; i < userList.size(); i++) {
            if (userList.get(i).getUsername() == username && userList.get(i).getPassword() == password) {
                loggedUser = userList.get(i);
                return loggedUser;
            } else return null;
        }
        return null;
    }
    public boolean logout() {
        loggedUser = null;
        return true;
    }

}
