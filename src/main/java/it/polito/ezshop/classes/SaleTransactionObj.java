package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleTransactionObj extends BalanceOperationObj implements it.polito.ezshop.data.SaleTransaction {
    private List<TicketEntry> entries = new ArrayList<TicketEntry>();
    private double price;
    private double discount = 0;
    private int balanceId;
    private LocalDate date;
    private double money;
    private String type;
    private Integer ticketNumber;
    private List<TicketEntry> tickets = new ArrayList<TicketEntry>();
    private String status;
    
    
    public SaleTransactionObj(LocalDate date, double money, String type) {
        super(date, type);
        this.money = money;
        this.price = this.money;
        this.status = "new"; //equal to started, other states are closed and payed
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
        tickets.remove(entry);
        this.updatePrice();
    }
    
    public void addEntry(TicketEntry entry) {
        tickets.add(entry);
        this.updatePrice();
    }
    
    public boolean updateEntry(TicketEntry entry) {
        //this method updates a single entry in the entries list
        //returns true if successfull, false otherwise
        for (TicketEntry oldEntry : entries) {
            if (oldEntry.getBarCode() == entry.getBarCode()) {
                entries.remove(entries.indexOf(oldEntry));
                entries.add(entry);
                return true;
            }
        }
        return false;
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
        return discount;
    }
    
    public void setDiscountRate(double discountRate) {
        this.discount = discountRate;
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
        return entries;
    }
    
    @Override
    public void setEntries(List<TicketEntry> entries) {
        this.entries = entries;
        this.updatePrice();
        
    }
    
}
