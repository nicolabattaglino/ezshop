package it.polito.ezshop.classes;
import java.util.*;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.data.*;

public class ProductOrderManager {
    private List<OrderObj> orders = new LinkedList<OrderObj>();

    public List<Order> getAllOrders(){
        return orders;
    }



}
