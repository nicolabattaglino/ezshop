package it.polito.ezshop.classes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;

public class TransactionManager {
    public static final String ORDER_PATH = "data/orders.json";
    public static final String SALE_PATH = "data/sales.json";
    public static final String RETURN_PATH = "data/returns.json";
    public static final String CREDITCARD_PATH = "data/creditCards.json";
    public static final String BALANCEOPERATION_PATH = "data/balanceOperations.json";
    public static final String GENERATOR_PATH = "data/transactionManagerGenerators.json";
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private Map<Integer, BalanceOperationObj> balanceOperations;
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private Map<Integer, OrderObj> orders;
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private Map<Integer, ReturnTransaction> returnTransactions; // list of all return transactions (they are also included in balanceOperation)
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private Map<Integer, SaleTransactionObj> saleTransactions; // list of all sale transactions (they are also included in balanceOperation)
    private final EZShop shop;
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private Map<String, CreditCard> cards = new HashMap<String, CreditCard>();
    private int saleGen;
    private int returnGen;
    private int balanceOperationGen;




    
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
            returns.delete();
            try {
                returns.createNewFile();
            } catch (IOException ioException) {
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
            } finally {
                cards = new HashMap<>();
            }
        }

        File balanceOperation = new File(BALANCEOPERATION_PATH);
        TypeReference<HashMap<Integer, BalanceOperationObj>> typeRef4 = new TypeReference<HashMap<Integer, BalanceOperationObj>>() {
        };
        try {
            balanceOperation.createNewFile();
            balanceOperations = mapper.readValue(balanceOperation, typeRef4);
        } catch (IOException e) {
            e.printStackTrace();
            balanceOperation.delete();
            try {
                balanceOperation.createNewFile();
            } catch (IOException ioException) {
            } finally {
                balanceOperations = new HashMap<>();
            }
        }
    
        this.shop = shop;
        try {
            File myObj = new File(GENERATOR_PATH);
            Scanner myReader = new Scanner(myObj);
            if(myReader.hasNextLine()) {
                String data = myReader.nextLine();
                saleGen = (int) Integer.parseInt(data);
            }
            else saleGen=0;
            if(myReader.hasNextLine()) {
                String data = myReader.nextLine();
                returnGen = (int) Integer.parseInt(data);
            }
            else returnGen=0;
            if(myReader.hasNextLine()) {
                String data = myReader.nextLine();
                balanceOperationGen = (int) Integer.parseInt(data);
            }
            else balanceOperationGen=0;
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            this.persistGenerators();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }


    public Integer startSaleTransaction()  {
        SaleTransactionObj sale = new SaleTransactionObj(saleGen++,LocalDate.now(), 0.0, "Sale");
        saleTransactions.put((Integer) sale.getBalanceId(), sale);
        try {
            this.persistSales();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            persistGenerators();
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
        if (transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
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
        if (transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        if (productCode == null) throw new InvalidProductCodeException();
        if (discountRate < 0.0 || discountRate >= 1.00) throw new InvalidDiscountRateException();
        if (discountRate > 1 || discountRate < 0) return false;
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (!sale.getStatus().equals("new")) return false;
        List<TicketEntry> tickets = sale.getEntries();
        int i;
        for (i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getBarCode().equals(productCode)) {
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
        if (transactionId ==null || transactionId <= 0) throw new InvalidTransactionIdException();
        if (discountRate < 0.0 || discountRate >= 1.00) throw new InvalidDiscountRateException();
        if (discountRate > 1 || discountRate < 0) return false;
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (sale.getStatus() == SaleStatus.PAYED) return false;
        double oldD= sale.getDiscountRate();
        sale.setDiscountRate(discountRate);
        try {
            this.persistSales();
        } catch (IOException e) {
            sale.setDiscountRate(oldD);
            return false;
        }
        return true;
    }
    
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException {
        if (transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return -1;
    
        return (int) (sale.getPrice() / 10);
    }
    
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException {
        if (transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (!sale.getStatus().equals("new")) return false; // the transaction wasn't opern
        sale.setStatus(SaleStatus.CLOSED);
        try {
            this.persistSales();
        } catch (IOException e) {
            sale.setStatus(SaleStatus.STARTED);
            return  false;
        }
        return true;
    }
    
    public boolean deleteSaleTransaction(Integer transactionId) throws InvalidTransactionIdException {
        if (transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        SaleTransactionObj oldS = new SaleTransactionObj(sale);
        if (sale == null) return false;
        if (sale.getStatus() == SaleStatus.PAYED) return false; // the transaction wasn't opern
        saleTransactions.remove(transactionId);
        try {
            this.persistSales();
        } catch (IOException e) {
            saleTransactions.put(oldS.getBalanceId(), oldS);
        }
        return true;
    }
    
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException {
        if (transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        return saleTransactions.get(transactionId);
    }
    
    public List<Order> getAllOrders()  {
        List<Order> output = new ArrayList<Order>(orders.values());
        return output;
    }
    
    public Integer startReturnTransaction(Integer saleNumber) throws InvalidTransactionIdException {
        if (saleNumber == null || saleNumber <= 0) throw new InvalidTransactionIdException();
        double money = 0;
        SaleTransaction toBeReturned = this.getSaleTransaction(saleNumber);
        List<TicketEntry> tickets = toBeReturned.getEntries();
        for (TicketEntry ticket : tickets) {
            money += (ticket.getAmount() * ticket.getPricePerUnit() * ticket.getDiscountRate());
        }
    
        ReturnTransaction returning = new ReturnTransaction(returnGen++, LocalDate.now(), money, "Return", (int) saleNumber);
        returnTransactions.put(returning.getBalanceId(), returning);
        Integer output = returning.getBalanceId();
        try {
            this.persistReturns();
        } catch (IOException e) {
            returnTransactions.remove(output);
            return null;
        }
        return output;
    }
    
    private ReturnTransaction getReturnTransaction(Integer returnId) {
        return returnTransactions.get(returnId);
    }
    
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (returnId == null || returnId <= 0 ) throw new InvalidTransactionIdException();
        if (productCode == null) throw new InvalidProductCodeException();
        if (amount < 0) throw new InvalidQuantityException();
        ReturnTransaction target = getReturnTransaction(returnId);
        ReturnTransaction oldR = new ReturnTransaction(target);
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
        //the previous line updates the price in the return transaction. The price in the return transaction is the amount of money that will be returned to the customer
        try {
            this.persistReturns();
        } catch (IOException e) {
            returnTransactions.remove(target.getBalanceId());
            returnTransactions.put(oldR.getBalanceId(), oldR);
        }
        return true;
    }
    
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, InvalidProductIdException, InvalidProductCodeException {
        if (returnId == null || returnId <= 0 ) throw new InvalidTransactionIdException();
        // in the current design the return transaction's informations are created during the return product function, the end return method only closes the return
        ReturnTransaction target = returnTransactions.get(returnId);
        if (target == null) return false;
        if (target.getStatus() != ReturnStatus.NEW) return false;
        if (!commit) {
            returnTransactions.remove(target.getBalanceId());
        }
        else {
            target.setStatus(ReturnStatus.ENDED);
        }
        try {
            this.persistReturns();
        } catch (IOException e) {
            if(commit) target.setStatus(ReturnStatus.NEW);
            else returnTransactions.put(target.getBalanceId(), target);
        }
        return true;
        
    }
    
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException,UnauthorizedException  {
        if (returnId == null || returnId <= 0 ) throw new InvalidTransactionIdException();
        ReturnTransaction target = returnTransactions.get(returnId);
        if (target == null) return false;
        if (target.getStatus() != ReturnStatus.CLOSED) return false;
        else {
            ReturnTransaction oldR = new ReturnTransaction(target);

            int amount = 0;
            SaleTransactionObj sale = this.getSaleTransactionObj(target.getTransactionID());
            SaleTransactionObj oldS = new SaleTransactionObj((sale));
            if (sale == null) return false;
            int priceReduction = 0;
            List<TicketEntry> targetEntries = target.getEntries();
            List<TicketEntry> saleEntries = sale.getEntries();
            List<TicketEntry> toBeUpdated = new ArrayList<TicketEntry>();
            for (TicketEntry saleEntry : saleEntries) {
                for (TicketEntry targetEntry : targetEntries) {
                    if (saleEntry.getBarCode().equals(targetEntry.getBarCode())) {
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
                            return false;
                        }

                        //this line updates the quantity by the amount stored in the return transaction.
                        // it might need to connect to the productOrderManager directly to avoid user problems!
                    } else toBeUpdated.add(saleEntry);
                }
            }
            // update the old sale
            sale.setEntries(toBeUpdated);
            target.setStatus(ReturnStatus.ENDED);


            try {
                this.persistReturns();
            } catch (IOException e) {
                for (TicketEntry saleEntry : saleEntries) {
                    for (TicketEntry targetEntry : targetEntries) {
                        if (saleEntry.getBarCode().equals(targetEntry.getBarCode())) {
                            amount = saleEntry.getAmount() - targetEntry.getAmount();
                            // calculate the difference between the sold amount and the amount to be returned
                            saleEntry.setAmount(amount);
                            toBeUpdated.add(saleEntry);
                            priceReduction += amount * saleEntry.getPricePerUnit();
                            try {
                                shop.getProductOrderManager().updateQuantity(shop.getProductTypeByBarCode(saleEntry.getBarCode()).getId(), -1*targetEntry.getAmount());
                            } catch (InvalidProductIdException e2) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (InvalidProductCodeException e2) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                return false;
                            }

                            //this line updates the quantity by the amount stored in the return transaction.
                            // it might need to connect to the productOrderManager directly to avoid user problems!
                        } else toBeUpdated.add(saleEntry);
                    }
                }
                saleTransactions.remove(sale.getBalanceId());
                returnTransactions.remove(target.getBalanceId());
                saleTransactions.put(oldS.getBalanceId(), oldS);
                returnTransactions.put(oldR.getBalanceId(), oldR);
            }
        }
        
        return true;
        
    }
    
    
    private SaleTransactionObj getSaleTransactionObj(int transactionID) {
        
        return saleTransactions.get(transactionID);
    }
    
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException  {
        if (ticketNumber == null || ticketNumber <= 0 ) throw new InvalidTransactionIdException();
        if (cash <= 0) throw new InvalidParameterException();
    
        SaleTransactionObj transaction = this.getSaleTransactionObj(ticketNumber);
        if (transaction == null) return -1;
        if (transaction.getPrice() > cash) return -1;
        return cash - transaction.getPrice();
    }
    
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException {
        if (ticketNumber == null || ticketNumber <= 0 ) throw new InvalidTransactionIdException();
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
        if (returnId == null || returnId<= 0 ) throw new InvalidTransactionIdException();
    
        ReturnTransaction rTransaction = returnTransactions.get(returnId);
        if (rTransaction == null) return -1;
        if (rTransaction.getStatus() != ReturnStatus.ENDED) return -1;
        double price = returnTransactions.get(rTransaction.getTransactionID()).getPrice(); //this price is the amount of money the customer will riceive
        if (!recordBalanceUpdate(-1 * price)) return -1;
        return price;
    }
    
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException {
        if (returnId  == null || returnId <= 0 ) throw new InvalidTransactionIdException();
        if (creditCard == null || creditCard.equals("")) throw new InvalidCreditCardException();
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
        if(toBeAdded>=0){
            Credit transaction= new Credit(balanceOperationGen++,LocalDate.now(), "Credit");
            transaction.setMoney(toBeAdded);
            balanceOperations.put(transaction.getBalanceId(),transaction);

        }
        else {
            Debit transaction = new Debit(balanceOperationGen++,LocalDate.now(), "Debit");
            transaction.setMoney(toBeAdded);
            balanceOperations.put(transaction.getBalanceId(),transaction);
            if(computeBalance()<0){
                balanceOperations.remove(transaction.getBalanceId());
                return false;
            }
        }
        try {
            this.persistBalanceOperations();
        } catch (IOException e) {
            balanceOperations.remove(balanceOperationGen-1);
            return false;
        }
        try {
            persistGenerators();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to)  {
        List<BalanceOperation> output = new LinkedList<BalanceOperation>();
        for (SaleTransactionObj sale : saleTransactions.values()) {
            if ((from == null && to == null) ||
                    (from == null  &&sale.getDate().isBefore(to)) ||
                    (from!=null &&sale.getDate().isAfter(from) && to == null) ||
                    ((from != null && to != null)&&sale.getDate().isAfter(from) &&  sale.getDate().isBefore(to))) {
                output.add((BalanceOperation) sale);
            }
        }
        for (ReturnTransaction rTransaciton : returnTransactions.values()) {
            if ((from == null && to == null) ||
                    (from == null  &&rTransaciton.getDate().isBefore(to)) ||
                    (from!=null &&rTransaciton.getDate().isAfter(from) && to == null) ||
                    ((from != null && to != null)&&rTransaciton.getDate().isAfter(from) &&  rTransaciton.getDate().isBefore(to))) {
                output.add((BalanceOperation) rTransaciton);
            }
        }
        for (OrderObj order : orders.values()) {
            if (order.getBalanceOperation() != null &&
                    ((from == null && to == null) ||
                            (from == null  &&order.getBalanceOperation().getDate().isBefore(to)) ||
                            (from!=null &&order.getBalanceOperation().getDate().isAfter(from) && to == null) ||
                            ((from != null && to != null)&&order.getBalanceOperation().getDate().isAfter(from) &&  order.getBalanceOperation().getDate().isBefore(to))))
                output.add(order.getBalanceOperation());
        
        }
        return output;
    }
    
    public double computeBalance()  {
        double balance;
        balance = 0;
        for(BalanceOperationObj operation : balanceOperations.values()){
            balance += operation.getMoney();
        }
        return balance;
    }
    
    public void clear() {
        //maybe this needs to clear orders too?
        //yes and also has to clear the files
        saleTransactions.clear();
        returnTransactions.clear();
        cards.clear();
        orders.clear();
        File fold = new File(GENERATOR_PATH);
        fold.delete();
        File fold1 = new File(RETURN_PATH);
        fold1.delete();
        File fold2 = new File(SALE_PATH);
        fold2.delete();
        File fold3 = new File(CREDITCARD_PATH);
        fold3.delete();
        File fold4 = new File(ORDER_PATH);
        fold4.delete();

    }
    
    public boolean luhn(String ccNumber)
    {
        if (ccNumber==null) return false;
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--)
        {
            try {
                int i1 = Integer.parseInt(ccNumber.substring(i, i + 1));
            } catch(NumberFormatException e){
                return false;
        }
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));

            if(n<0)return  false;
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        if(sum==0) return  false;
        return (sum % 10 == 0);
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
        double tot = order.getPricePerUnit() * order.getQuantity();
        if(order.getStatus().equals(OrderStatus.ISSUED.name())) {
            BalanceOperationObj operation = new Debit(balanceOperationGen++,LocalDate.now(), "Debit");
            order.setBalanceOperation((BalanceOperationObj) operation);
            order.setBalanceId(operation.getBalanceId());
            orders.put(order.getOrderId(), order);
        }
        if(order.getStatus().equals(OrderStatus.PAYED.name())){
            if(!this.recordBalanceUpdate(-1*tot)) return false;

        }
        try {
            this.persistOrders();
        } catch (IOException e) {
            balanceOperations.remove(balanceOperationGen-1);
            return false;
        }
        try {
            persistGenerators();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void persistBalanceOperations() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(BALANCEOPERATION_PATH), balanceOperations);
    }

    private void persistGenerators() throws IOException {
        try {
            File fold = new File(GENERATOR_PATH);
            fold.delete();
            FileWriter myWriter = new FileWriter(GENERATOR_PATH);
            myWriter.write(saleGen+ "\n");
            myWriter.write(returnGen+ "\n");
            myWriter.write(balanceOperationGen+ "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

