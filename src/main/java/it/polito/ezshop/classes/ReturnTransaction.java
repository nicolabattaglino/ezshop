package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReturnTransaction extends Credit {

    private int transactionID;
    private Integer ticketNumber;
    private List<TicketEntry> entries = new ArrayList<TicketEntry>();
    private double price;
    private ReturnStatus status;
    
    public ReturnTransaction(int id, LocalDate date, double money, String type, int returning) {
        super(id, date, type);

        this.transactionID = returning;
        status = ReturnStatus.NEW;
    }
    
    public ReturnTransaction(ReturnTransaction r) {
        super(r.getBalanceId(), r.getDate(), r.getType());
        this.transactionID = r.transactionID;
        this.ticketNumber = r.ticketNumber;
        for (TicketEntry t : r.getEntries()) {
            this.entries.add(t);
        }
        
        this.price = r.price;
        this.status = r.status;
    }
    
    
    public ReturnStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReturnStatus status) {
        this.status = status;
    }
    
    public Integer getTicketNumber() {
        return ticketNumber;
    }
    
    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
    
    public List<TicketEntry> getEntries() {
        List<TicketEntry> output = new ArrayList<TicketEntry>();
        for (TicketEntry t : entries) {
            output.add(t);
        }
        return output;
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
    

    
}
