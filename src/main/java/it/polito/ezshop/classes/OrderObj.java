package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;

import java.util.Objects;

public class OrderObj implements Order {
    
    
    private Integer orderId;
    private ProductTypeObj product;
    private double pricePerUnit;
    private int quantity;
    private String supplier = "";
    private OrderStatus status;
    private Debit balanceOp;
    
    
    @JsonCreator
    public OrderObj(@JsonProperty("orderId") Integer orderId,
                    @JsonProperty("product") ProductTypeObj product,
                    @JsonProperty("pricePerUnit") double pricePerUnit,
                    @JsonProperty("quantity") int quantity,
                    @JsonProperty("supplier") String supplier,
                    @JsonProperty("status") OrderStatus status,
                    @JsonProperty("balanceOp") Debit balanceOp) {
        this.orderId = orderId;
        this.product = product;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.supplier = supplier;
        this.status = status;
        this.balanceOp = balanceOp;
        
    }
    
    public OrderObj(OrderObj orderObj) {
        this.orderId = orderObj.orderId;
        this.product = orderObj.product;
        this.pricePerUnit = orderObj.pricePerUnit;
        this.quantity = orderObj.quantity;
        this.supplier = orderObj.supplier;
        this.status = orderObj.status;
        this.balanceOp = orderObj.balanceOp;
        this.product = new ProductTypeObj(product);
    }
    
    public OrderObj(Integer orderId, ProductType product, double pricePerUnit, int quantity) {
        this.orderId = orderId;
        this.product = new ProductTypeObj(product);
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        status = OrderStatus.ISSUED;
    }
    
    @JsonProperty("balanceOp")
    public BalanceOperation getBalanceOperation() {
        return (BalanceOperation) balanceOp;
    }
    
    @JsonProperty("balanceOp")
    public void setBalanceOperation(BalanceOperationObj operation) {
        this.balanceOp = (Debit) operation;
        
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
    @JsonIgnore
    public Integer getBalanceId() {
        return balanceOp.getBalanceId();
    }
    
    @JsonIgnore
    public void setBalanceId(Integer balanceId) {
        balanceOp.setBalanceId(balanceId);
    }
    
    public ProductTypeObj getProduct() {
        return product;
    }
    
    public void setProduct(ProductTypeObj product) {
        this.product = product;
    }
    
    public String getSupplier() {
        return supplier;
    }
    
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderObj)) return false;
        OrderObj orderObj = (OrderObj) o;
        return Double.compare(orderObj.pricePerUnit, pricePerUnit) == 0 && quantity == orderObj.quantity && Objects.equals(orderId, orderObj.orderId) && Objects.equals(product, orderObj.product) && Objects.equals(supplier, orderObj.supplier) && status == orderObj.status && Objects.equals(balanceOp, orderObj.balanceOp);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(orderId, product, pricePerUnit, quantity, supplier, status, balanceOp);
    }
}
