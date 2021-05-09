package it.polito.ezshop.classes;

public class CreditCard {
    String creditCardNumber;
    double balance;

    public double getBalance (){
        return balance;
    }
     public void setBalance (double balance){
        this.balance = balance;
     }    
      public String getNumber (){
          return creditCardNumber;
      }
      public void setNumber(String creditCardNumber){
          this.creditCardNumber = creditCardNumber;
      }
}
