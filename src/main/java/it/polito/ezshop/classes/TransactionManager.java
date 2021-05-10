package it.polito.ezshop.classes;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

import java.time.LocalDate;
import java.util.*;

public class TransactionManager {
    private Double balance = Double.valueOf("0");
    //non so se orders mi serve
    private List<Order> orders = new LinkedList<Order>();
    private List<BalanceOperation> balanceOperations = new LinkedList<BalanceOperation>(); //list of all balance operations
    private Map<Integer, ReturnTransaction> returnTransactions = new HashMap<Integer, ReturnTransaction>(); // list of all return transactions (they are also included in balanceOperation)
    private Map<Integer, SaleTransactionObj> saleTransactions = new HashMap<Integer, SaleTransactionObj>(); // list of all sale transactions (they are also included in balanceOperation)
    private EZShop shop;
    private Map<String, CreditCard> cards = new HashMap<String, CreditCard>();
    private ProductOrderManager POManager;
    
    public TransactionManager(EZShop shop, ProductOrderManager POManager) {
        this.shop = shop;
        this.POManager = POManager;
    }
    
    public Integer startSaleTransaction() throws UnauthorizedException {
        SaleTransactionObj sale = new SaleTransactionObj(LocalDate.now(), 0.0, "Sale");
        return sale.getBalanceId();
    }
    
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (sale.getStatus() != "new") return false;
        ProductType prodotto = POManager.getProductTypeByBarCode(productCode);
        if (prodotto == null) return false;
        try {
            if (!POManager.updateQuantity(prodotto.getId(), -1 * amount)) return false;
        } catch (InvalidProductIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TickerEntryObj ticket = new TickerEntryObj(amount, productCode, prodotto.getProductDescription(), prodotto.getPricePerUnit());
        sale.addEntry(ticket);
        return true;
    }
    
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (sale.getStatus() != "new") return false;
        ProductType prodotto = POManager.getProductTypeByBarCode(productCode);
        if (prodotto == null) return false;
        try {
            if (!POManager.updateQuantity(prodotto.getId(), amount)) return false;
        } catch (InvalidProductIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TickerEntryObj ticket = new TickerEntryObj(amount, productCode, prodotto.getProductDescription(), prodotto.getPricePerUnit());
        sale.deleteEntry(ticket);
        return true;
    }
    
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        if (discountRate > 1 || discountRate < 0) return false;
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (sale.getStatus() != "new") return false;
        List<TicketEntry> tickets = sale.getEntries();
        int i;
        for (i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getBarCode() == productCode) {
                tickets.get(i).setDiscountRate(discountRate);
            }
        }
        sale.setEntries(tickets);
        return true;
    }
    
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        if (discountRate > 1 || discountRate < 0) return false;
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (sale.getStatus() == "payted") return false;
        sale.setDiscountRate(discountRate);
        return true;
    }
    
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        //TODO implement
        return 0;
    }
    
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        //TODO implement
        return false;
    }
    
    public boolean deleteSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        //TODO implement
        return false;
    }
    
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return saleTransactions.get(transactionId);
    }
    
    public List<Order> getAllOrders() throws UnauthorizedException {
        return orders;
    }
    
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        int money = 0;
        SaleTransaction toBeReturned = this.getSaleTransaction(saleNumber);
        List<TicketEntry> tickets = toBeReturned.getEntries();
        for (TicketEntry ticket : tickets) {
            money += (ticket.getAmount() * ticket.getPricePerUnit() * ticket.getDiscountRate());
        }
        
        ReturnTransaction returning = new ReturnTransaction(Collections.max(balanceOperations.stream().map(s -> s.getBalanceId()).collect(java.util.stream.Collectors.toList())) + 1, LocalDate.now(), money, "Return", saleNumber);
        balanceOperations.add(returning);
        returnTransactions.put(returning.getBalanceId(), returning);
        Integer output = returning.getBalanceId();
        return output;
    }
    
    private ReturnTransaction getReturnTransaction(Integer returnId) {
        return returnTransactions.get(returnId);
    }
    
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        //Exceptions are checked in the shop
        ReturnTransaction target = getReturnTransaction(returnId);
        if (target == null) {
            return false;
        }
        if (shop.getProductTypeByBarCode(productCode) == null) return false;
        int oldID = target.getTransactionID();
        SaleTransaction oldSale = this.getSaleTransaction(oldID);
        List<TicketEntry> products = oldSale.getEntries();
        TicketEntry prodotto = null;
        for (TicketEntry product : products) {
            if (product.getBarCode() == productCode) {
                prodotto = product;
                break;
            }
        }
        if (prodotto == null) return false;
        if (prodotto.getAmount() < amount) return false;
        prodotto.setAmount(amount);
        target.addEntry(prodotto);
        target.setPrice(target.getPrice() + prodotto.getPricePerUnit() * amount);
        //the previous line updates the price in the return transactio. The price in the return transaction is the amount of money that will be returned to the customer
        return true;
    }
    
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException, InvalidProductIdException, InvalidProductCodeException {
        // exceptions are checked in shop
        // in the current design the return transaction's informations are created during the return product function, the end return method only closes the return
        ReturnTransaction target = returnTransactions.get(returnId);
        if (target == null) return false;
        if (target.getStatus() != "New") return false;
        if (!commit) {
            returnTransactions.remove(target.getBalanceId());
        }
        target.setStatus("Closed");
        return true;
        
    }
    
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        // exceptions are checked in shop
        ReturnTransaction target = returnTransactions.get(returnId);
        if (target == null) return false;
        if (target.getStatus() != "Closed") return false;
        else {
            int amount = 0;
            SaleTransactionObj sale = this.getSaleTransactionObj(target.getTransactionID());
            if (sale == null) return false;
            int priceReduction = 0;
            List<TicketEntry> targetEntries = target.getEntries();
            List<TicketEntry> saleEntries = sale.getEntries();
            List<TicketEntry> toBeUpdated = new ArrayList<TicketEntry>();
            for (TicketEntry saleEntry : saleEntries) {
                for (TicketEntry targetEntry : targetEntries) {
                    if (saleEntry.getBarCode() == targetEntry.getBarCode()) {
                        amount = saleEntry.getAmount() - targetEntry.getAmount();
                        // calculate the difference between the sold amount and the amount to be returned
                        saleEntry.setAmount(amount);
                        toBeUpdated.add(saleEntry);
                        priceReduction += amount * saleEntry.getPricePerUnit();
                        try {
                            shop.getProductOrderManager().updateQuantity(shop.getProductTypeByBarCode(saleEntry.getBarCode()).getId(), targetEntry.getAmount());
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
            for (TicketEntry toAdd : toBeUpdated) {
                sale.updateEntry(toAdd);
            }
            target.setStatus("Ended");
            
        }
        
        return true;
        
    }
    
    
    private SaleTransactionObj getSaleTransactionObj(int transactionID) {
        
        return saleTransactions.get(transactionID);
    }
    
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        SaleTransactionObj transaction = this.getSaleTransactionObj(ticketNumber);
        if (transaction == null) return -1;
        if (transaction.getPrice() > cash) return -1;
        return cash - transaction.getPrice();
    }
    
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        CreditCard carta = cards.get(creditCard);
        if (carta == null) return false;
        if (!this.luhn(carta.getNumber())) return false;
        SaleTransaction transaction = saleTransactions.get(ticketNumber);
        if (transaction == null) return false;
        if (carta.getBalance() < transaction.getPrice()) return false;
        carta.setBalance(carta.getBalance() - transaction.getPrice());
        this.recordBalanceUpdate(transaction.getPrice());
        return true;
    }
    
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        ReturnTransaction rTransaction = returnTransactions.get(returnId);
        if (rTransaction == null) return -1;
        if (rTransaction.getStatus() != "Ended") return -1;
        double price = returnTransactions.get(rTransaction.getTransactionID()).getPrice(); //this price is the amount of money the customer will riceive
        if (!recordBalanceUpdate(-1 * price)) return -1;
        return price;
    }
    
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        CreditCard carta = cards.get(creditCard);
        if (carta == null) return -1;
        if (!this.luhn(carta.getNumber())) return -1;
        ReturnTransaction rTransaction = returnTransactions.get(returnId);
        if (rTransaction == null) return -1;
        double output = rTransaction.getPrice();
        if (!recordBalanceUpdate(output)) return -1;
        return output;
    }
    
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        this.balance += toBeAdded;
        if (computeBalance() < 0) {
            this.balance -= toBeAdded;
            return false;
        } else return true;
    }
    
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        List<BalanceOperation> output = new LinkedList<BalanceOperation>();
        output.add((BalanceOperation) saleTransactions);
        output.add((BalanceOperation) returnTransactions);
        return output;
    }
    
    public double computeBalance() throws UnauthorizedException {
        return balance;
    }
    
    public void clear() {
        //maybe this needs to clear orders too?
        saleTransactions.clear();
        returnTransactions.clear();
        cards.clear();
        balance = 0.0;
    }
    
    private boolean luhn(String creditCard) {
        int number = Integer.parseInt(creditCard);
        //step 1
        int step1N = 0;
        int tmp;
        for (int i = 100; i < Math.pow(10, 17); i *= 100) {
            tmp = number % i;
            tmp = tmp * 10 / i;
            tmp *= 2;
            if (tmp >= 10) tmp = (tmp % 10) + (tmp / 10);
            //step 2
            step1N += tmp;
        }
        //step 3
        int step3N = 0;
        for (int i = 10; i < Math.pow(10, 16); i *= 100) {
            tmp = number % i;
            tmp = tmp * 10 / i;
            step3N += tmp;
        }
        //step 4
        if (((step1N + step3N) % 10) == 0) return true;
        else return false;
    }
    
    public Order addCompletedOrder(Integer orderId) {
        return null;
        
        
    }
    
    boolean addOrder(Order order) {
        return true;
        
    }
}

