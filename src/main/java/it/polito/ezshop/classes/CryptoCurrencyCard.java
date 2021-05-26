package it.polito.ezshop.classes;

public class CryptoCurrencyCard {
    private Float number;
    private double balance;

    public CryptoCurrencyCard(Float float1, double balance){
        this.number = float1;
        this.balance= balance;
    }
    
    public double getBalanceCC() {
        return balance;
    }
    
    public void setBalanceCC(double balance) {
        this.balance = balance;
    }
    
    public Float getNumberCC() {
        return number;
    }
    
    public void setNumberCC(Float creditCardNumber) {
        this.number = creditCardNumber;
    }
}
