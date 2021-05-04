package it.polito.ezshop.classes;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.data.*;

import java.time.LocalDate;
import java.util.*;
import it.polito.ezshop.data.*;

public abstract class BalanceOperationObj implements BalanceOperation {
    private int balanceId;
    private LocalDate date;
    private double money;
    private String type;

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
