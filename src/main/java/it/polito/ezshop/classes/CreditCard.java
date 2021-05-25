package it.polito.ezshop.classes;

public class CreditCard {
    private String number;
    private double balance;

    public CreditCard(String number, double balance){
        this.number = number;
        this.balance= balance;
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
