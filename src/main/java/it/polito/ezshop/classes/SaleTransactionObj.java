package it.polito.ezshop.classes;

import java.time.LocalDate;
import java.util.*;

import java.time.LocalDate;
import java.util.List;

import it.polito.ezshop.data.*;

public class SaleTransactionObj implements it.polito.ezshop.data.SaleTransaction, BalanceOperation {
    private List<TicketEntry> entries = new ArrayList<TicketEntry>();
    private double price;
    
    public boolean updateEntry (TicketEntry entry){
        //this method updates a single entry in the entries list
        //returns true if successfull, false otherwise
        for(TicketEntry oldEntry : entries){
            if(oldEntry.getBarCode() == entry.getBarCode()){
                entries.remove(entries.indexOf(oldEntry));
                entries.add(entry);
                return true;
            }
        }
        return false;
    }
    public void updatePrice(double amount){
        this.price+=amount;
    }
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
