package it.polito.ezshop.classes;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.data.*;

import java.time.LocalDate;
import java.util.*;
import it.polito.ezshop.data.*;

public class OrderObj extends BalanceOperationObj implements Order {
    private Integer balanceId;
    private String productCode;
    private double pricePerUnit;
    private int quantity;
    private String status;
    private Integer orderId;
    
     public String getProductCode(){
        return productCode;
    }

    public void setProductCode(String productCode){
        this.productCode=productCode;
        return;
    }

    public double getPricePerUnit(){
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit){
        this.pricePerUnit=pricePerUnit;
        return;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity=quantity;
        return;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
        return;
    }

    public Integer getOrderId(){
        return orderId;
    }

    public void setOrderId(Integer orderId){
        this.orderId=orderId;
        return;
    }
    @Override
    public Integer getBalanceId(){
        return balanceId;
    }

    public void setBalanceId(Integer balanceId){
        this.balanceId=balanceId;
        return;
    }
}
