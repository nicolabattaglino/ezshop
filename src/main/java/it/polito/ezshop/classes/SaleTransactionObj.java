package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SaleTransactionObj extends Credit implements it.polito.ezshop.data.SaleTransaction {
    private List<TicketEntry> entries = new ArrayList<TicketEntry>();
    private double price;
    private double discountRate = 0;
    private int balanceId;
    private LocalDate date;
    private double money;
    private String type;
    private Integer ticketNumber;
    private String status;


    public SaleTransactionObj(int id,LocalDate date, double money, String type) {
        super(id, date, type);
        this.money = money;
        this.price = this.money;
        this.status = "new"; //equal to started, other states are closed and payed
    }

    public SaleTransactionObj(SaleTransactionObj s){
        super(s.balanceId, s.date, s.type);
        this.money=s.money;
        this.ticketNumber=s.ticketNumber;
        for( TicketEntry t: s.getEntries() ){
            this.entries.add(t);
        }

        this.price =s.price;
        this.status=s.status ;
        this.discountRate=s.discountRate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    private void updatePrice() {
        int prezzo = 0;
        for (TicketEntry entry : entries) {
            prezzo += entry.getAmount() * entry.getPricePerUnit() * entry.getDiscountRate();
        }
        price = prezzo;
        money = prezzo;
    }
    
    public void deleteEntry(TicketEntry entry) {
        entries.remove(entry);
        this.updatePrice();
    }
    
    public void addEntry(TicketEntry entry) {
        entries.add(entry);
        this.updatePrice();
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
    
    public Integer getTicketNumber() {
        return ticketNumber;
    }
    
    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
        return;
    }
    
    
    public double getDiscountRate() {
        return discountRate;
    }
    
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
        this.updatePrice();
        return;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
        return;
    }
    
    
    @Override
    public List<TicketEntry> getEntries() {
        // TODO Auto-generated method stub
        List<TicketEntry> output = new ArrayList<TicketEntry>();
        for(TicketEntry t : entries){
            output.add(t);
        }
        return  output;
    }
    
    @Override
    public void setEntries(List<TicketEntry> entries) {
        this.entries = entries;
        this.updatePrice();
        
    }
    
}
