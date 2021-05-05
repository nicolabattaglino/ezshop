package it.polito.ezshop.classes;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.data.*;

import java.time.LocalDate;
import java.util.*;

public class ReturnTransaction implements BalanceOperation,it.polito.ezshop.data.SaleTransaction {
    private int balanceId;
    private LocalDate date;
    private double money;
    private String type;
    private int returningID;
    private Integer ticketNumber;
    private double discountRate;
    private  List<TicketEntry> entries = new ArrayList<TicketEntry>();
    private double price;
    private boolean status=false;
    
    
    public Integer getTicketNumber(){
        return ticketNumber;
    }

    public void setTicketNumber(Integer ticketNumber){
        this.ticketNumber = ticketNumber;
    }

    public List<TicketEntry> getEntries(){
        return entries;
    }

    public void setEntries(List<TicketEntry> entries){
        this.entries= entries;
    }
    public void addEntry(TicketEntry entry){
        entries.add(entry);
    }
    public double getDiscountRate(){
        return discountRate;
    }

    public void setDiscountRate(double discountRate){
        this.discountRate = discountRate;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public ReturnTransaction(int balanceId, LocalDate date, double money, String type, int returning){
        this.balanceId=balanceId;
        this. date= date;
        this. money = money;
        this.type= type;
        this.returningID = returning;
    }
    public int getTransactionID(){
        //this method returns the ID of the sale transaction the return is linked to
        return returningID;
    }

    public int getBalanceId(){
        return balanceId;
    }

    public void setBalanceId(int balanceId){
        this.balanceId = balanceId;
        return;
    }

    public LocalDate getDate(){
    return date;
    }

    public void setDate(LocalDate date){
        this.date=date;
        return;
    }

    public double getMoney(){
        return money;
    }

    public void setMoney(double money){
        this.money=money;
        return;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type=type;
        return;
    }
    
}
