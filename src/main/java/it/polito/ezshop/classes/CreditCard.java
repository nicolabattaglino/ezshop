package it.polito.ezshop.classes;

public class CreditCard {
    String Number;
    double balance;
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public String getNumber() {
        return Number;
    }
    
    public void setNumber(String creditCardNumber) {
        this.Number = creditCardNumber;
    }
}
