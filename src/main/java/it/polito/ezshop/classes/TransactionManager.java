package it.polito.ezshop.classes;
import java.lang.Double;
import java.rmi.server.RemoteRef;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.data.*;
import java.util.stream.Collectors.*;
import java.math.*;

import javax.swing.TransferHandler.TransferSupport;
import javax.swing.plaf.metal.MetalBorders.ToolBarBorder;

import java.time.LocalDate;
import java.util.*;

public class TransactionManager {
    private Double balance =  Double.valueOf("0");
    private List<Order> orders = new LinkedList<Order>();
    private List<BalanceOperation> balanceOperations= new LinkedList<BalanceOperation>(); //list of all balance operations
    private List<ReturnTransaction> returnTransactions= new LinkedList<ReturnTransaction>(); // list of all return transactions (they are also included in balanceOperation)
    private List<SaleTransactionObj> saleTransactions= new LinkedList<SaleTransactionObj>(); // list of all sale transactions (they are also included in balanceOperation)
    private EZShop shop;
    private List<CreditCard> cards = new LinkedList<CreditCard>();


    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException{
        SaleTransactionObj output;
        for(SaleTransactionObj obj : saleTransactions){
            if(obj.getBalanceId() == transactionId) return obj;
        }
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
        // in the current design the return transaction's informations are created during the return product function, the end return method only closes the return
        ReturnTransaction target = null;
        for(ReturnTransaction returning : returnTransactions){
            if(returning.getBalanceId() == returnId) target = returning;
        }
        if(target == null)return false;
        if(target.getStatus()!= "New")return false;
        if(!commit){
            returnTransactions.remove(returnTransactions.indexOf(target));
        }
        target.setStatus("Closed");
        return true;

    }
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
         // exceptions are checked in shop
         ReturnTransaction target = null;
         for(ReturnTransaction returning : returnTransactions){
             if(returning.getBalanceId() == returnId) target = returning;
         }
         if(target == null)return false;
         if(target.getStatus()!= "Closed")return false;
         else{
             int amount=0;
             SaleTransactionObj sale = this.getSaleTransactionObj(target.getTransactionID());
             if(sale == null) return false;
             int priceReduction=0;
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
                         priceReduction += amount*saleEntry.getPricePerUnit();
                         try {
                            shop.updateQuantity(shop.getProductTypeByBarCode(saleEntry.getBarCode()).getId(), targetEntry.getAmount());
                        } catch (InvalidProductIdException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (InvalidProductCodeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                         //this line updates the quantity by the amount stored in the return transaction.
                         // it might need to connect to the productOrderManager directly to avoid user problems!
                     }
                 }
         }
         // update the old sale
         for(TicketEntry toAdd : toBeUpdated){
            sale.updateEntry(toAdd);
         }
         sale.updatePrice(priceReduction);
         
     }
        
        return true;
 
     }


    private SaleTransactionObj getSaleTransactionObj(int transactionID) {
        SaleTransactionObj output;
        for(SaleTransactionObj obj : saleTransactions){
            if(obj.getBalanceId() == transactionID) return obj;
        }
        return null;
    }

    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        SaleTransactionObj transaction = this.getSaleTransactionObj(ticketNumber);
        if(transaction==null) return -1;
        if(transaction.getPrice() > cash) return -1;
        return cash - transaction.getPrice();
    }

    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        CreditCard carta= null;
        for(CreditCard card : cards){
            if(card.getNumber() == creditCard) carta = card;
        }
        if (carta== null) return false;
        if(!this.luhn(carta.getNumber())) return false;
        SaleTransaction transaction = null;
        for(SaleTransactionObj sale : saleTransactions){
            if(sale.getBalanceId() == (int) ticketNumber) transaction= sale;
        }
        if(transaction == null) return false;
        if (carta.getBalance()< transaction.getPrice()) return false;
        carta.setBalance(carta.getBalance()- transaction.getPrice());
        this.recordBalanceUpdate(transaction.getPrice());
        return true;
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

    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        List<BalanceOperation> output= new LinkedList<BalanceOperation>();
        output.add((BalanceOperation) saleTransactions);
        output.add((BalanceOperation) returnTransactions);
        return output;
    }

    public double computeBalance() throws UnauthorizedException {
        return balance;
    }
    public void clear() {

    }
    private boolean luhn (String creditCard){
        int number = Integer.parseInt(creditCard);
        //step 1
        int step1N=0;
        int tmp;
        for(int i =100; i<Math.pow(10,17);i*=100 ){
            tmp = number %i;
            tmp= tmp*10/i;
            tmp*=2;
            if(tmp>=10) tmp = (tmp%10)+ (tmp/10);
            //step 2
            step1N +=tmp;
        }
        //step 3
        int step3N=0;
        for( int i =10; i<Math.pow(10,16); i*=100){
            tmp = number%i;
            tmp = tmp *10/i;
            step3N += tmp;
        }
        //step 4
        if(((step1N+step3N)%10)==0) return true;
        else return false;
    }
     
    public Order addCompletedOrder(Integer orderId){
        return null;
        

    }
    boolean addOrder(Order order ){
        return true;

    }
}

