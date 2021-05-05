package it.polito.ezshop.classes;
import java.lang.Double;
import java.rmi.server.RemoteRef;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.data.*;
import java.util.stream.Collectors.*;

import java.time.LocalDate;
import java.util.*;

public class TransactionManager {
    private Double balance =  Double.valueOf("0");
    private List<Order> orders = new LinkedList<Order>();
    private List<BalanceOperation> balanceOperations= new LinkedList<BalanceOperation>(); //list of all balance operations
    private List<ReturnTransaction> returnTransactions= new LinkedList<ReturnTransaction>(); // list of all return transactions (they are also included in balanceOperation)
    private List<SaleTransactionObj> saleTransactions= new LinkedList<SaleTransactionObj>(); // list of all sale transactions (they are also included in balanceOperation)
    private EZShop shop;


    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException{
        return null;
    }

    public List<Order> getAllOrders() throws UnauthorizedException {
        return orders;
    }

    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        int money =0;
        SaleTransaction toBeReturned = this.getSaleTransaction(saleNumber);
        List<TicketEntry> tickets = toBeReturned.getEntries();
        for (TicketEntry ticket : tickets){
            money+= (ticket.getAmount() * ticket.getPricePerUnit() * ticket.getDiscountRate());
        }
        
        ReturnTransaction returning = new ReturnTransaction(Collections.max(balanceOperations.stream().map(s-> s.getBalanceId()).collect(java.util.stream.Collectors.toList())), LocalDate.now(), money, "Return", saleNumber);
        balanceOperations.add(returning);
        returnTransactions.add(returning);
        Integer output = returning.getBalanceId();
        return output;
    }
    private ReturnTransaction getReturnTransaction (Integer returnId){
        for (ReturnTransaction output : returnTransactions ){
                if(output.getBalanceId() == (int)returnId) return output;
        }
        return null;
    }

    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        //Exceptions are checked in the shop
        ReturnTransaction target = getReturnTransaction(returnId);
        if(target==null){ 
            return false;
        }
        if(shop.getProductTypeByBarCode(productCode)== null) return false;
        int oldID = target.getTransactionID();
        SaleTransaction oldSale = this.getSaleTransaction(oldID);
        List<TicketEntry> products =  oldSale.getEntries();
        TicketEntry prodotto= null;
        for (TicketEntry product : products){
            if(product.getBarCode() == productCode){
                prodotto = product;
                break;
            }
        }
        if(prodotto == null) return false;
        if(prodotto.getAmount()< amount) return false;
        prodotto.setAmount(amount);
        target.addEntry(prodotto);
        return true;
    }
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException, InvalidProductIdException, InvalidProductCodeException {
        // exceptions are checked in shop
        ReturnTransaction target = null;
        for(ReturnTransaction returning : returnTransactions){
            if(returning.getBalanceId() == returnId) target = returning;
        }
        if(target == null)return false;
        if(!commit){
            returnTransactions.remove(returnTransactions.indexOf(target));
        }
        else{
            int amount=0;
            SaleTransaction sale = this.getSaleTransaction(target.getTransactionID());
            List<TicketEntry> targetEntries= target.getEntries();
            List<TicketEntry> saleEntries= sale.getEntries(); 
            List<TicketEntry> toBeUpdated= new ArrayList <TicketEntry>(); 
            for (TicketEntry saleEntry : saleEntries ){
                for(TicketEntry targetEntry : targetEntries ){
                    if(saleEntry.getBarCode() == targetEntry.getBarCode()){
                        amount = saleEntry.getAmount() - targetEntry.getAmount();
                        // calculate the difference between the sold amount and the amount to be returned
                        saleEntry.setAmount(amount);
                        toBeUpdated.add(saleEntry);
                        shop.updateQuantity(shop.getProductTypeByBarCode(saleEntry.getBarCode()).getId(), targetEntry.getAmount());
                        //this line updates the quantity by the amount stored in the return transaction.
                        // it might need to connect to the productOrderManager directly to avoid user problems!
                    }
                }
        }
    }
        return true;

    }
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }


    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        this.balance += toBeAdded;
        if(computeBalance()<0){
            this.balance -= toBeAdded;
            return false;
        }
        else return true;
    }

    public List<BalanceOperationObj> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        return null;
    }

    public double computeBalance() throws UnauthorizedException {
        return balance;
    }
    public void clear() {

    }
     
    public Order addCompletedOrder(Integer orderId){
        return null;
        

    }
    boolean addOrder(Order order ){
        return true;

    }
}

