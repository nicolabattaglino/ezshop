package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReturnTransaction extends BalanceOperationObj {
    private int balanceId;
    private LocalDate date;
    private double money;
    private String type;
    private int transactionID;
    private Integer ticketNumber;
    private List<TicketEntry> entries = new ArrayList<TicketEntry>();
    private double price;
    private String status ;
    
    public ReturnTransaction( LocalDate date, double money, String type, int returning) {
        super(date, type);
        this.money = money;
        this.transactionID = returning;
        status = "new";
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getTicketNumber() {
        return ticketNumber;
    }
    
    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
    
    public List<TicketEntry> getEntries() {
        return entries;
    }
    
    public void setEntries(List<TicketEntry> entries) {
        this.entries = entries;
    }
    
    public void addEntry(TicketEntry entry) {
        entries.add(entry);
    }
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getTransactionID() {
        //this method returns the ID of the sale transaction the return is linked to
        return transactionID;
    }
    
    public int getBalanceId() {
        return balanceId;
    }
    
    public void setBalanceId(int balanceId) {
        this.balanceId = balanceId;
        return;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
        return;
    }
    
    public double getMoney() {
        return money;
    }
    
    public void setMoney(double money) {
        this.money = money;
        return;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
        return;
    }
    
}
