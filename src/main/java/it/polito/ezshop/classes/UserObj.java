package it.polito.ezshop.classes;

import it.polito.ezshop.data.User;

public class UserObj implements User {
    
    private Integer id;
    private String username;
    private String password;
    private UserRole role;
    
    public UserObj(Integer id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public UserObj() {
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return String.valueOf(role);
    }
    
    public void setRole(String role) {
        
        if (role == null) return;
        
        if (role.equalsIgnoreCase("ADMINISTRATOR") ||
                role.equalsIgnoreCase("CASHIER") ||
                role.equalsIgnoreCase("SHOPMANAGER")) {
            switch (UserRole.valueOf(role)) {
                case Administrator:
                    this.role = UserRole.Administrator;
                    break;
                case Cashier:
                    this.role = UserRole.Cashier;
                    break;
                case ShopManager:
                    this.role = UserRole.ShopManager;
                    break;
            }
        }
        
    }
}
