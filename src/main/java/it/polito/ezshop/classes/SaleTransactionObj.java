package it.polito.ezshop.classes;

import java.time.LocalDate;
import java.util.*;

import java.time.LocalDate;
import java.util.List;

import it.polito.ezshop.data.*;

public class SaleTransactionObj implements it.polito.ezshop.data.SaleTransaction, BalanceOperation {
    public int getBalanceId(){
        return 0;
    }

    public void setBalanceId(int balanceId){
        return;
    }

    public LocalDate getDate(){
        return null;
    }

    public void setDate(LocalDate date){
        return;
    }

    public double getMoney(){
        return 0;
    }

    public void setMoney(double money){
        return;
    }

    public String getType(){
        return null;
    }

    public void setType(String type){
        return;
    }
    
    public Integer getTicketNumber(){
        return 0;
    }

    public void setTicketNumber(Integer ticketNumber){
        return;
    }

    

    public double getDiscountRate(){
        return 0;
    }

    public void setDiscountRate(double discountRate){
        return;
    }

    public double getPrice(){
        return 0;
    }

    public void setPrice(double price){
        return;
    }

   

    @Override
    public List<TicketEntry> getEntries() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
        // TODO Auto-generated method stub
        
    }
    
}
