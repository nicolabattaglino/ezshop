package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;

public class OrderObj implements Order {
    
    private static int OrderIdGen; //TODO aggiungilo al design
    
    private Integer orderId;
    private ProductTypeObj product;
    private double pricePerUnit;
    private int quantity;
    private String supplier; //TODO vedi che farci
    private OrderStatus status;
    private BalanceOperationObj balanceOp;
    
    @JsonCreator
    public OrderObj(@JsonProperty("orderId") Integer orderId,
                    @JsonProperty("product") ProductTypeObj product,
                    @JsonProperty("pricePerUnit") double pricePerUnit,
                    @JsonProperty("quantity") int quantity,
                    @JsonProperty("supplier") String supplier,
                    @JsonProperty("status") OrderStatus status,
                    @JsonProperty("balanceOp") BalanceOperationObj balanceOp) {
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
    }
    
    @JsonProperty("balanceOp")
    public BalanceOperationObj getBalanceOperation() {
        return (BalanceOperationObj) balanceOp;
    }
    
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
    public String toString() {
        return "OrderObj{" +
                "orderId=" + orderId +
                ", product=" + product +
                ", pricePerUnit=" + pricePerUnit +
                ", quantity=" + quantity +
                ", supplier='" + supplier + '\'' +
                ", status=" + status +
                ", balanceOp=" + balanceOp +
                '}';
    }
}
