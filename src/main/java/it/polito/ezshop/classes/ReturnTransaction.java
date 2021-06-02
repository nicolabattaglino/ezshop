package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReturnTransaction extends Credit {
    
    private final int transactionID;
    
    private List<TicketEntry> entries = new ArrayList<TicketEntry>();
    private double price;
    private ReturnStatus status;
    
    @JsonCreator
    public ReturnTransaction(@JsonProperty("id") int id, @JsonProperty("date") LocalDate date, @JsonProperty("money") double money, @JsonProperty("type") String type, @JsonProperty("returning") int returning) {
        super(id, date, type);
        
        this.transactionID = returning;
        status = ReturnStatus.NEW;
    }
    
    public ReturnTransaction(ReturnTransaction r) {
        super(r.getBalanceId(), r.getDate(), r.getType());
        this.transactionID = r.transactionID;
        this.entries.addAll(r.getEntries());
        
        this.price = r.price;
        this.status = r.status;
    }
    
    
    public ReturnStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReturnStatus status) {
        this.status = status;
    }
    
    public List<TicketEntry> getEntries() {
        List<TicketEntry> output = new ArrayList<TicketEntry>();
        for (TicketEntry t : entries) {
            TicketEntryObj t1 = new TicketEntryObj(t);
            output.add(t1);
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
