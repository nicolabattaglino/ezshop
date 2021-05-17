package it.polito.ezshop.classes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;

public class TransactionManager {
    public static final String ORDER_PATH = "data/orders.json";
    public static final String SALE_PATH = "data/sales.json";
    public static final String RETURN_PATH = "data/returns.json";
    public static final String CREDITCARD_PATH = "data/creditCards.json";
    private Double balance;
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private Map<Integer, OrderObj> orders;
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private Map<Integer, ReturnTransaction> returnTransactions; // list of all return transactions (they are also included in balanceOperation)
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private Map<Integer, SaleTransactionObj> saleTransactions; // list of all sale transactions (they are also included in balanceOperation)
    private EZShop shop;
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private Map<String, CreditCard> cards = new HashMap<String, CreditCard>();
    
    public TransactionManager(EZShop shop) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<Integer, OrderObj>> typeRef = new TypeReference<HashMap<Integer, OrderObj>>() {
        };
        File ordini = new File(ORDER_PATH);
        try {
            ordini.createNewFile();
            orders = mapper.readValue(ordini, typeRef);
        } catch (IOException e) {
            ordini.delete();
            try {
                ordini.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                orders = new HashMap<>();
            }
        }
        File sales = new File(SALE_PATH);
        TypeReference<HashMap<Integer, SaleTransactionObj>> typeRef1 = new TypeReference<HashMap<Integer, SaleTransactionObj>>() {
        };
        try {
            sales.createNewFile();
            saleTransactions = mapper.readValue(sales, typeRef1);
        } catch (IOException e) {
            e.printStackTrace();
            sales.delete();
            try {
                sales.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                saleTransactions = new HashMap<>();
            }
        }
    
        File returns = new File(RETURN_PATH);
        TypeReference<HashMap<Integer, ReturnTransaction>> typeRef2 = new TypeReference<HashMap<Integer, ReturnTransaction>>() {
        };
        try {
            returns.createNewFile();
            returnTransactions = mapper.readValue(returns, typeRef2);
        } catch (IOException e) {
            e.printStackTrace();
            returns.delete();
            try {
                returns.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                returnTransactions = new HashMap<>();
            }
        }
    
        File creditCards = new File(CREDITCARD_PATH);
        TypeReference<HashMap<String, CreditCard>> typeRef3 = new TypeReference<HashMap<String, CreditCard>>() {
        };
        try {
            creditCards.createNewFile();
            cards = mapper.readValue(creditCards, typeRef3);
        } catch (IOException e) {
            e.printStackTrace();
            creditCards.delete();
            try {
                creditCards.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                cards = new HashMap<>();
            }
        }
    
        this.shop = shop;
        balance = 0.0;
    
    }


    public Integer startSaleTransaction()  {
        SaleTransactionObj sale = new SaleTransactionObj(LocalDate.now(), 0.0, "Sale");
        saleTransactions.put((Integer) sale.getBalanceId(), sale);
        try {
            this.persistSales();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        return sale.getBalanceId();
    }
    
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) {
            throw new InvalidTransactionIdException();
        }
        ProductType prodotto = shop.getProductOrderManager().getProductTypeByBarCode(productCode);
        if (prodotto == null) throw new InvalidProductCodeException();
        if (amount < 0) throw new InvalidQuantityException();
        if (!sale.getStatus().equals("new")) return false;
    
        try {
            if (!shop.getProductOrderManager().updateQuantity(prodotto.getId(), -1 * amount)) return false;
        } catch (InvalidProductIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TicketEntryObj ticket = new TicketEntryObj(amount, productCode, prodotto.getProductDescription(), prodotto.getPricePerUnit());
        sale.addEntry(ticket);
        try {
            this.persistSales();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return true;
    }
    
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
        if (transactionId <= 0 || transactionId == null) throw new InvalidTransactionIdException();
        if (amount < 0) throw new InvalidQuantityException();
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (!sale.getStatus().equals("new")) return false;
        if (productCode == null) throw new InvalidProductCodeException();
        ProductType prodotto = shop.getProductOrderManager().getProductTypeByBarCode(productCode);
        if (prodotto == null) return false;
        try {
            if (!shop.getProductOrderManager().updateQuantity(prodotto.getId(), amount)) return false;
        } catch (InvalidProductIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TicketEntryObj ticket = new TicketEntryObj(amount, productCode, prodotto.getProductDescription(), prodotto.getPricePerUnit());
        sale.deleteEntry(ticket);
        try {
            this.persistSales();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return true;
    }
    
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException {
        if (transactionId <= 0 || transactionId == null) throw new InvalidTransactionIdException();
        if (productCode == null) throw new InvalidProductCodeException();
        if (discountRate < 0.0 || discountRate >= 1.00) throw new InvalidDiscountRateException();
        if (discountRate > 1 || discountRate < 0) return false;
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (!sale.getStatus().equals("new")) return false;
        List<TicketEntry> tickets = sale.getEntries();
        int i;
        for (i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getBarCode() == productCode) {
                tickets.get(i).setDiscountRate(discountRate);
            }
        }
        sale.setEntries(tickets);
        try {
            this.persistSales();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException {
        if (transactionId <= 0 || transactionId == null) throw new InvalidTransactionIdException();
        if (discountRate < 0.0 || discountRate >= 1.00) throw new InvalidDiscountRateException();
        if (discountRate > 1 || discountRate < 0) return false;
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (sale.getStatus() == "payted") return false;
        sale.setDiscountRate(discountRate);
        try {
            this.persistSales();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException {
        if (transactionId <= 0 || transactionId == null) throw new InvalidTransactionIdException();
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return -1;
    
        return (int) (sale.getPrice() / 10);
    }
    
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException {
        if (transactionId <= 0 || transactionId == null) throw new InvalidTransactionIdException();
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (!sale.getStatus().equals("new")) return false; // the transaction wasn't opern
        sale.setStatus("closed");
        try {
            this.persistSales();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public boolean deleteSaleTransaction(Integer transactionId) throws InvalidTransactionIdException {
        if (transactionId <= 0 || transactionId == null) throw new InvalidTransactionIdException();
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (sale.getStatus() == "payed") return false; // the transaction wasn't opern
        saleTransactions.remove(transactionId);
        try {
            this.persistSales();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException {
        if (transactionId <= 0 || transactionId == null) throw new InvalidTransactionIdException();
        return saleTransactions.get(transactionId);
    }
    
    public List<Order> getAllOrders()  {
        List<Order> output = new ArrayList<Order>(orders.values());
        return output;
    }
    
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException {
        if (saleNumber <= 0 || saleNumber == null) throw new InvalidTransactionIdException();
        double money = 0;
        SaleTransaction toBeReturned = this.getSaleTransaction(saleNumber);
        List<TicketEntry> tickets = toBeReturned.getEntries();
        for (TicketEntry ticket : tickets) {
            money += (ticket.getAmount() * ticket.getPricePerUnit() * ticket.getDiscountRate());
        }
    
        ReturnTransaction returning = new ReturnTransaction(LocalDate.now(), money, "Return", (int) saleNumber);
        returnTransactions.put(returning.getBalanceId(), returning);
        Integer output = returning.getBalanceId();
        try {
            this.persistReturns();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
    
    private ReturnTransaction getReturnTransaction(Integer returnId) {
        return returnTransactions.get(returnId);
    }
    
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (returnId <= 0 || returnId == null) throw new InvalidTransactionIdException();
        if (productCode == null) throw new InvalidProductCodeException();
        if (amount < 0) throw new InvalidQuantityException();
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
        try {
            this.persistReturns();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, InvalidProductIdException, InvalidProductCodeException {
        if (returnId <= 0 || returnId == null) throw new InvalidTransactionIdException();
        // in the current design the return transaction's informations are created during the return product function, the end return method only closes the return
        ReturnTransaction target = returnTransactions.get(returnId);
        if (target == null) return false;
        if (target.getStatus() != "New") return false;
        if (!commit) {
            returnTransactions.remove(target.getBalanceId());
        }
        target.setStatus("Closed");
        try {
            this.persistReturns();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
        
    }
    
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException,UnauthorizedException  {
        if (returnId <= 0 || returnId == null) throw new InvalidTransactionIdException();
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
                    } else toBeUpdated.add(saleEntry);
                }
            }
            // update the old sale
            sale.setEntries(toBeUpdated);
            target.setStatus("Ended");
            
        }
        try {
            this.persistReturns();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return true;
        
    }
    
    
    private SaleTransactionObj getSaleTransactionObj(int transactionID) {
        
        return saleTransactions.get(transactionID);
    }
    
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException  {
        if (ticketNumber <= 0 || ticketNumber == null) throw new InvalidTransactionIdException();
        if (cash <= 0) throw new InvalidParameterException();
    
        SaleTransactionObj transaction = this.getSaleTransactionObj(ticketNumber);
        if (transaction == null) return -1;
        if (transaction.getPrice() > cash) return -1;
        return cash - transaction.getPrice();
    }
    
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException {
        if (ticketNumber <= 0 || ticketNumber == null) throw new InvalidTransactionIdException();
        if (creditCard == "" || creditCard == null) throw new InvalidCreditCardException();
    
        CreditCard carta = cards.get(creditCard);
        if (carta == null) return false;
        if (!this.luhn(carta.getNumber())) throw new InvalidCreditCardException();
        SaleTransaction transaction = saleTransactions.get(ticketNumber);
        if (transaction == null) return false;
        if (carta.getBalance() < transaction.getPrice()) return false;
        carta.setBalance(carta.getBalance() - transaction.getPrice());
        this.recordBalanceUpdate(transaction.getPrice());
        return true;
    }
    
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException  {
        if (returnId <= 0 || returnId == null) throw new InvalidTransactionIdException();
    
        ReturnTransaction rTransaction = returnTransactions.get(returnId);
        if (rTransaction == null) return -1;
        if (rTransaction.getStatus() != "Ended") return -1;
        double price = returnTransactions.get(rTransaction.getTransactionID()).getPrice(); //this price is the amount of money the customer will riceive
        if (!recordBalanceUpdate(-1 * price)) return -1;
        return price;
    }
    
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException {
        if (returnId <= 0 || returnId == null) throw new InvalidTransactionIdException();
        if (creditCard.equals("") || creditCard == null) throw new InvalidCreditCardException();
        CreditCard carta = cards.get(creditCard);
        if (carta == null) return -1;
        if (!this.luhn(carta.getNumber())) throw new InvalidCreditCardException();
        ReturnTransaction rTransaction = returnTransactions.get(returnId);
        if (rTransaction == null) return -1;
        double output = rTransaction.getPrice();
        if (!recordBalanceUpdate(output)) return -1;
        return output;
    }
    
    public boolean recordBalanceUpdate(double toBeAdded) {
        this.balance += toBeAdded;
        if (computeBalance() < 0) {
            this.balance -= toBeAdded;
            return false;
        } else return true;
    }
    
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to)  {
        List<BalanceOperation> output = new LinkedList<BalanceOperation>();
        for (SaleTransactionObj sale : saleTransactions.values()) {
            if ((from == null && to == null) ||
                    (sale.getDate().isAfter(from) && to == null) ||
                    (from == null && sale.getDate().isBefore(to)) ||
                    (sale.getDate().isAfter(from) && sale.getDate().isBefore(to))) {
                output.add((BalanceOperation) sale);
            }
        }
        for (ReturnTransaction rTransaciton : returnTransactions.values()) {
            if ((from == null && to == null) ||
                    (rTransaciton.getDate().isAfter(from) && to == null) ||
                    (from == null && rTransaciton.getDate().isBefore(to)) ||
                    (rTransaciton.getDate().isAfter(from) && rTransaciton.getDate().isBefore(to))) {
                output.add((BalanceOperation) rTransaciton);
            }
        }
        for (OrderObj order : orders.values()) {
            if (order.getBalanceOperation() != null &&
                    ((from == null && to == null) ||
                            (order.getBalanceOperation().getDate().isAfter(from) && to == null) ||
                            (from == null && order.getBalanceOperation().getDate().isBefore(to)) ||
                            (order.getBalanceOperation().getDate().isAfter(from) && order.getBalanceOperation().getDate().isBefore(to))))
                output.add(order.getBalanceOperation());
        
        }
        return output;
    }
    
    public double computeBalance()  {
        return balance;
    }
    
    public void clear() {
        //maybe this needs to clear orders too?
        //yes and also has to clear the files
        saleTransactions.clear();
        returnTransactions.clear();
        cards.clear();
        orders.clear();
        balance = 0.0;
    }
    
    public boolean luhn(String creditCard) {
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
    
    public OrderObj addCompletedOrder(Integer orderId) {
        OrderObj target = orders.get(orderId);
        if (target == null) return null;
        target = new OrderObj(target);
        switch (OrderStatus.valueOf(target.getStatus())) {
            case PAYED:
                target.setStatus(OrderStatus.COMPLETED.name());
                try {
                    persistReturns();
                } catch (IOException e) {
                    target.setStatus(OrderStatus.PAYED.name());
                    return null;
                }
                return target;
            case COMPLETED:
                return target;
            default:
                return null;
        }
    }
    
    public boolean addOrder(OrderObj order) {
        //update the balance if the order status is payed check also if the field balanceOperation of the order
        // is not null and in that case add it to the balanceOperations map.
        // if there is not enough balance return false
        // add persistance
        orders.put(order.getOrderId(), order);
        return true;
        
    }
    
    private void persistCards() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(CREDITCARD_PATH), cards);
        
    }
    
    private void persistOrders() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(ORDER_PATH), orders);
    }
    
    private void persistSales() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(SALE_PATH), saleTransactions);
    }
    
    private void persistReturns() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(RETURN_PATH), returnTransactions);
    }
}

