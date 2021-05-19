package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleTransactionObj extends Credit implements it.polito.ezshop.data.SaleTransaction {
    private List<TicketEntry> entries = new ArrayList<TicketEntry>();
    private double price;
    private double discountRate = 0;

    private Integer ticketNumber;
    private SaleStatus status;
    
    
    public SaleTransactionObj(int id, LocalDate date, double money, String type) {
        super(id, date, type);
        this.price = this.getMoney();
        this.status = SaleStatus.STARTED; //equal to started, other states are closed and payed
    }
    
    public SaleTransactionObj(SaleTransactionObj s) {
        super(s.getBalanceId(), s.getDate(), s.getType());
        this.ticketNumber = s.ticketNumber;
        for (TicketEntry t : s.getEntries()) {
            this.entries.add(t);
        }
        
        this.price = s.price;
        this.status = s.status;
        this.discountRate = s.discountRate;
    }
    
    public SaleStatus getStatus() {
        return status;
    }

    
    private void updatePrice() {
        int prezzo = 0;
        for (TicketEntry entry : entries) {
            prezzo += entry.getAmount() * entry.getPricePerUnit() * entry.getDiscountRate();
        }
        price = prezzo;
        this.setMoney(prezzo) ;
    }
    
    public void deleteEntry(TicketEntry entry) {
        entries.remove(entry);
        this.updatePrice();
    }
    
    public void addEntry(TicketEntry entry) {
        entries.add(entry);
        this.updatePrice();
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
        for (TicketEntry t : entries) {
            output.add(t);
        }
        return output;
    }
    
    @Override
    public void setEntries(List<TicketEntry> entries) {
        this.entries = entries;
        this.updatePrice();
        
    }
    
}
