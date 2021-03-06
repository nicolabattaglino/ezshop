package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polito.ezshop.data.BalanceOperation;

import java.time.LocalDate;

public abstract class BalanceOperationObj implements BalanceOperation {
    private int balanceId;
    private LocalDate date;
    private double money;
    private String type; //type e' equivalente a description?
    
    @JsonCreator
    public BalanceOperationObj(@JsonProperty("id") int id, @JsonProperty("date") LocalDate date, @JsonProperty("type") String type) {
        super();
        this.date = date;
        this.type = type;
        this.balanceId = id;
    }
    
    public int getBalanceId() {
        return balanceId;
    }
    
    public void setBalanceId(int balanceId) {
        this.balanceId = balanceId;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public double getMoney() {
        return money;
    }
    
    public void setMoney(double money) {
        this.money = money;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    
}
