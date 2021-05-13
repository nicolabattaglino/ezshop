package it.polito.ezshop.classes;

import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;

public class OrderObj implements Order {
    
    private static int OrderIdGen; //TODO aggiungilo al design
    
    private Integer orderId;
    private ProductType product;
    private double pricePerUnit;
    private int quantity;
    private String supplier; //TODO vedi che farci
    private OrderStatus status;
    private BalanceOperationObj balanceOp;
    

    public BalanceOperationObj getBalanceOperation (){
        return (BalanceOperationObj)balanceOp;
    }

    public OrderObj() {}

    public OrderObj(ProductType product, double pricePerUnit, int quantity) {
        this.orderId = ++OrderIdGen;
        this.product = new ProductTypeObj(product);
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        status = OrderStatus.ISSUED;
    }
    
    public String getProductCode() {
        return product.getBarCode();
    }
    
    public void setProductCode(String productCode) {
        this.product.setBarCode(productCode);
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
        return status.name();
    }
    
    public void setStatus(String status) {
        this.status = OrderStatus.valueOf(status.toUpperCase());
    }
    
    public Integer getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    
    @Override
    public Integer getBalanceId() {
        return balanceOp.getBalanceId();
    }
    
    public void setBalanceId(Integer balanceId) {
        balanceOp.setBalanceId(balanceId);
    }
    
    
}
