package it.polito.ezshop.classes;

import it.polito.ezshop.data.Order;

public class OrderObj implements Order {
    private Integer balanceId;
    private String productCode;
    private double pricePerUnit;
    private int quantity;
    private String status;
    private Integer orderId;
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public double getPricePerUnit() {
        return pricePerUnit;
    }
    
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    
    @Override
    public Integer getBalanceId() {
        return balanceId;
    }
    
    public void setBalanceId(Integer balanceId) {
        this.balanceId = balanceId;
    }
}
