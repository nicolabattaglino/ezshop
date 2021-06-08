package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polito.ezshop.data.TicketEntry;

import javax.sound.sampled.Port;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

public class SaleTransactionObj extends Credit implements it.polito.ezshop.data.SaleTransaction {
    private List< TicketEntry> entries = new ArrayList<>();
    private double price;
    private double discountRate = 0;
    private boolean priceSet = false;
    private Integer ticketNumber;
    private SaleStatus status;
    private Map<String, Product> products = new HashMap<>();

    @JsonCreator
    public SaleTransactionObj(@JsonProperty("id") int id, @JsonProperty("date") LocalDate date, @JsonProperty("money") double money, @JsonProperty("type") String type) {
        super(id, date, type);
        this.price = this.getMoney();
        this.status = SaleStatus.STARTED; //equal to started, other states are closed and payed
        this.ticketNumber = id;
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
    public void setProduct(Product product){
        this.products.put(product.getRFID(), product);
    }

    public Product getProduct(String rfid){
        return products.get(rfid);
    }
    public void deleteProduct (String rfid){
        products.remove(rfid);
    }
    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;

    }


    private void updatePrice() {
        double prezzo = 0;
        for (TicketEntry entry : entries) {
            prezzo += (entry.getAmount() * entry.getPricePerUnit()) * (1 - entry.getDiscountRate());
        }
        price = prezzo * (1 - discountRate);
        this.setMoney(prezzo);
    }

    public void deleteEntry(TicketEntry entry) {
        entries.remove(entry);
        priceSet = false;
        this.updatePrice();
    }

    public void addEntry(TicketEntry entry) {
        entries.add(entry);
        priceSet = false;
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
        priceSet = false;
        this.updatePrice();
        return;
    }

    public double getPrice() {
        if (!priceSet)
            this.updatePrice();
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        priceSet = true;
        return;
    }


    public TicketEntry getEntry(String barCode){
        for (TicketEntry t : entries) {
            if(t.getBarCode()==barCode){
                entries.remove(t);
                return t;
            }
        }
        return  null;
    }

    @Override
    public List<TicketEntry> getEntries() {
        List<TicketEntry> output = new ArrayList<TicketEntry>();
        for (TicketEntry t : entries) {
            output.add(t);
        }
        return output;
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
        this.entries = entries;
        priceSet = false;
        this.updatePrice();

    }

}
