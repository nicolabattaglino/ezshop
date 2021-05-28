package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditCard {
    private String number;
    private double balance;
    
    @JsonCreator
    public CreditCard(@JsonProperty("number") String number, @JsonProperty("balance") double balance) {
        this.number = number;
        this.balance = balance;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public String getNumber() {
        return number;
    }
    
    public void setNumber(String creditCardNumber) {
        this.number = creditCardNumber;
    }
}
