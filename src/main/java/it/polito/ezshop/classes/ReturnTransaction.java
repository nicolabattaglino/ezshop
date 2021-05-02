package it.polito.ezshop.classes;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.data.*;

import java.time.LocalDate;
import java.util.List;

public class ReturnTransaction implements BalanceOperation {
    public int getBalanceId(){
        return 0;
    }

    public void setBalanceId(int balanceId){
        return;
    }

    public LocalDate getDate(){
    return null;
    }

    public void setDate(LocalDate date){
        return;
    }

    public double getMoney(){
        return 0;
    }

    public void setMoney(double money){
        return;
    }

    public String getType(){
        return"";
    }

    public void setType(String type){
        return;
    }
    
}
