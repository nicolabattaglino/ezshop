package it.polito.ezshop.classes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionManager {
    
    public static final String ORDER_PATH = "data/orders.json";
    public static final String SALE_PATH = "data/sales.json";
    public static final String RETURN_PATH = "data/returns.json";
    public static final String CREDITCARD_PATH = "data/creditCards.json";
    //TODO ADD TO DESIGN
    public static final String CRYPTOCURRENCY_PATH = "data/cryptoCurrency.json";
    public static final String BALANCEOPERATION_PATH = "data/balanceOperations.json";
    public static final String GENERATOR_PATH = "data/transactionManagerGenerators.json";
    
    private final EZShop shop;
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
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private Map<String, CreditCard> cards = new HashMap<String, CreditCard>();
    // TODO: ADD TO DESIGN 
    private Map<Float, CryptoCurrencyCard> ccc = new HashMap<Float, CryptoCurrencyCard>();

    private int saleGen;
    private int returnGen;
    private int balanceOperationGen;
    
    // todo remove it HOSSAIN PART
    
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
            } catch (IOException ex) {
                ex.printStackTrace();
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
            returns.delete();
            try {
                returns.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                returnTransactions = new HashMap<>();
            }
        }
        
        // File cryptoCurreCards = new File(CRYPTOCURRENCY_PATH);
        // TypeReference<HashMap<Float, CryptoCurrencyCard>> typeRef6 = new TypeReference<HashMap<Float, CryptoCurrencyCard>>() {
        // };
        // try {
        //     cryptoCurreCards.createNewFile();
        //     ccc = mapper.readValue(cryptoCurreCards, typeRef6);
        // } catch (IOException e) {
        //     cryptoCurreCards.delete();
        //     try {
        //         cryptoCurreCards.createNewFile();
        //     } catch (IOException ioException) {
        //         ioException.printStackTrace();
        //     } finally {
        //         cards = new HashMap<>();
        //     }
        // }
        
        File creditCards = new File(CREDITCARD_PATH);
        TypeReference<HashMap<String, CreditCard>> typeRef3 = new TypeReference<HashMap<String, CreditCard>>() {
        };
        try {
            creditCards.createNewFile();
            cards = mapper.readValue(creditCards, typeRef3);
        } catch (IOException e) {
            creditCards.delete();
            try {
                creditCards.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
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
            balanceOperation.delete();
            try {
                balanceOperation.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                balanceOperations = new HashMap<>();
            }
        }
        
        this.shop = shop;
        try {
            File myObj = new File(GENERATOR_PATH);
            Scanner myReader = new Scanner(myObj);
            if (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                saleGen = (int) Integer.parseInt(data);
                saleGen++;
            } else saleGen = 1;
            if (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                returnGen = (int) Integer.parseInt(data);
                returnGen++;
            } else returnGen = 1;
            if (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                balanceOperationGen = (int) Integer.parseInt(data);
                balanceOperationGen++;
            } else balanceOperationGen = 1;
            myReader.close();
        } catch (FileNotFoundException e) {
            //System.err.println("An error occurred.");
            saleGen = 1;
            returnGen = 1;
            balanceOperationGen = 1;
            //e.printStackTrace();
        }
        try {
            this.persistGenerators();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    
    public Integer startSaleTransaction() {
        SaleTransactionObj sale = new SaleTransactionObj(saleGen++, LocalDate.now(), 0.0, "Sale");
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
        if (productCode == null || Long.parseLong(productCode) <= 0) throw new InvalidTransactionIdException();
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) {
            return false;
        }
        ProductType prodotto = shop.getProductOrderManager().getProductTypeByBarCode(productCode);
        if (prodotto == null) throw new InvalidProductCodeException();
        if (amount < 0) throw new InvalidQuantityException();
        if (!sale.getStatus().equals(SaleStatus.STARTED)) return false;
        
        try {
            if (!shop.getProductOrderManager().updateQuantity(prodotto.getId(), -1 * amount)) return false;
        } catch (InvalidProductIdException e) {
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
        if (!sale.getStatus().equals(SaleStatus.STARTED)) return false;
        if (productCode == null) throw new InvalidProductCodeException();
        ProductType prodotto = shop.getProductOrderManager().getProductTypeByBarCode(productCode);
        if (prodotto == null) return false;
        try {
            if (!shop.getProductOrderManager().updateQuantity(prodotto.getId(), amount)) return false;
        } catch (InvalidProductIdException e) {
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
        if (!sale.getStatus().equals(SaleStatus.STARTED)) return false;
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
        if (transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        if (discountRate < 0.0 || discountRate >= 1.00) throw new InvalidDiscountRateException();
        if (discountRate > 1 || discountRate < 0) return false;
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        if (sale.getStatus() == SaleStatus.PAYED) return false;
        double oldD = sale.getDiscountRate();
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
        if (!sale.getStatus().equals(SaleStatus.STARTED)) return false; // the transaction wasn't opern
        sale.setStatus(SaleStatus.CLOSED);
        try {
            this.persistSales();
        } catch (IOException e) {
            sale.setStatus(SaleStatus.STARTED);
            return false;
        }
        return true;
    }
    
    public boolean deleteSaleTransaction(Integer transactionId) throws InvalidTransactionIdException {
        if (transactionId == null || transactionId <= 0) throw new InvalidTransactionIdException();
        SaleTransactionObj sale = saleTransactions.get(transactionId);
        if (sale == null) return false;
        SaleTransactionObj oldS = new SaleTransactionObj(sale);
        if (sale.getStatus() == SaleStatus.PAYED) return false; // the transaction wasn't open
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
    
    public List<Order> getAllOrders() {
        return orders.values()
                .stream()
                .map(OrderObj::new)
                .collect(Collectors.toList());
    }
    
    // todo remove it STEFANO PART  
    
    public Integer startReturnTransaction(Integer saleNumber) throws InvalidTransactionIdException {
        if (saleNumber == null || saleNumber <= 0) throw new InvalidTransactionIdException();
        double money = 0;
        SaleTransaction toBeReturned = this.getSaleTransaction(saleNumber);
        if (toBeReturned == null) return -1;
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
    
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
        if (returnId == null || returnId <= 0) throw new InvalidTransactionIdException();
        if (productCode == null || productCode.equals("") || !this.shop.getProductOrderManager().checkBarcode(productCode))
            throw new InvalidProductCodeException();
        if (amount <= 0) throw new InvalidQuantityException();
        ReturnTransaction target = getReturnTransaction(returnId);
        if (target == null) {
            return false;
        }
        ReturnTransaction oldR = new ReturnTransaction(target);
        if (shop.getProductOrderManager().getProductTypeByBarCode(productCode) == null) return false;
        int oldID = target.getTransactionID();
        SaleTransaction oldSale = this.getSaleTransaction(oldID);
        List<TicketEntry> products = oldSale.getEntries();
        TicketEntry prodotto = null;
        for (TicketEntry product : products) {
            if (product.getBarCode().equals(productCode)) {
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
    
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException {
        if (returnId == null || returnId <= 0) throw new InvalidTransactionIdException();
        // in the current design the return transaction's informations are created during the return product function, the end return method only closes the return
        ReturnTransaction target = returnTransactions.get(returnId);
        if (target == null) return false;
        if (target.getStatus() != ReturnStatus.NEW) return false;
        if (!commit) {
            returnTransactions.remove(target.getBalanceId());
        } else {
            target.setStatus(ReturnStatus.ENDED);
        }
        try {
            this.persistReturns();
        } catch (IOException e) {
            if (commit) target.setStatus(ReturnStatus.NEW);
            else returnTransactions.put(target.getBalanceId(), target);
        }
        return true;
        
    }
    
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        if (returnId == null || returnId <= 0) throw new InvalidTransactionIdException();
        ReturnTransaction target = returnTransactions.get(returnId);
        if (target == null) return false;
        if (target.getStatus() == ReturnStatus.ENDED) return false;
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
                    if (saleEntry.getBarCode().equals(targetEntry.getBarCode())) {
                        amount = saleEntry.getAmount() - targetEntry.getAmount();
                        // calculate the difference between the sold amount and the amount to be returned
                        saleEntry.setAmount(amount);
                        toBeUpdated.add(saleEntry);
                        priceReduction += amount * saleEntry.getPricePerUnit();
                        try {
                            shop.getProductOrderManager().updateQuantity(shop.getProductOrderManager().getProductTypeByBarCode(saleEntry.getBarCode()).getId(), targetEntry.getAmount());
                        } catch (InvalidProductIdException e) {
                            e.printStackTrace();
                        } catch (InvalidProductCodeException e) {
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
                e.printStackTrace();
            }
        }
        
        return true;
        
    }
    
    private SaleTransactionObj getSaleTransactionObj(int transactionID) {
        
        return saleTransactions.get(transactionID);
    }
    
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException {
        if (ticketNumber == null || ticketNumber <= 0) throw new InvalidTransactionIdException();
        if (cash <= 0) throw new InvalidParameterException();
        
        SaleTransactionObj transaction = this.getSaleTransactionObj(ticketNumber);
        if (transaction == null) return -1;
        if (transaction.getPrice() > cash) return -1;
        return cash - transaction.getPrice();
    }
    
    // public boolean receiveCryptoCurrencyCardPayment(Integer ticketNumber, String cryptoCurrencyCard) throws InvalidTransactionIdException, InvalidCryptoCurrencyCardException {
    //     if (ticketNumber == null || ticketNumber <= 0) throw new InvalidTransactionIdException();
    //     if (cryptoCurrencyCard == null || cryptoCurrencyCard.equals("") ||  !this.luhn(cryptoCurrencyCard)) throw new InvalidCryptoCurrencyCardException();
        
    //     CryptoCurrencyCard carta = cards.get(cryptoCurrencyCard);
    //     if (carta == null) return false;
    //     SaleTransaction transaction = saleTransactions.get(ticketNumber);
    //     if (transaction == null) return false;
    //     if (carta.getBalance() < transaction.getPrice()) return false;
    //     carta.setBalance(carta.getBalance() - transaction.getPrice());
    //     this.recordBalanceUpdate(transaction.getPrice());
    //     return true;
    // }

    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException {
        if (ticketNumber == null || ticketNumber <= 0) throw new InvalidTransactionIdException();
        if (creditCard == null || creditCard.equals("") || !this.luhn(creditCard))
            throw new InvalidCreditCardException();
        CreditCard carta = cards.get(creditCard);
        if (carta == null) return false;
        SaleTransaction transaction = saleTransactions.get(ticketNumber);
        if (transaction == null) return false;
        if (carta.getBalance() < transaction.getPrice()) return false;
        carta.setBalance(carta.getBalance() - transaction.getPrice());
        this.recordBalanceUpdate(transaction.getPrice());
        return true;
    }
    
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException {
        if (returnId == null || returnId <= 0) throw new InvalidTransactionIdException();
        
        ReturnTransaction rTransaction = returnTransactions.get(returnId);
        if (rTransaction == null) return -1;
        if (rTransaction.getStatus() != ReturnStatus.ENDED) return -1;
        double price = returnTransactions.get(rTransaction.getTransactionID()).getPrice(); //this price is the amount of money the customer will receive
        if (!recordBalanceUpdate(-1 * price)) return -1;
        return price;
    }

    // public double returnCryptoCurrencyCardPayment(Integer returnId, String cryptoCurrencyCard) throws InvalidTransactionIdException, InvalidCryptoCurrencyCardException {
    //     if (returnId == null || returnId <= 0) throw new InvalidTransactionIdException();
    //     if (cryptoCurrencyCard == null || cryptoCurrencyCard.equals("")|| !this.luhn(cryptoCurrencyCard)) throw new InvalidCryptoCurrencyCardException();
    //     CryptoCurrencyCard carta = ccc.get(cryptoCurrencyCard);
    //     if (carta == null) return -1;
    //     ReturnTransaction rTransaction = returnTransactions.get(returnId);
    //     if (rTransaction == null) return -1;
    //     if (rTransaction.getStatus() != ReturnStatus.ENDED) return -1;
    //     double output = rTransaction.getPrice();
    //     if (!recordBalanceUpdate(output)) return -1;
    //     return output;
    // }

    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException {
        if (returnId == null || returnId <= 0) throw new InvalidTransactionIdException();
        if (creditCard == null || creditCard.equals("") || !this.luhn(creditCard))
            throw new InvalidCreditCardException();
        CreditCard carta = cards.get(creditCard);
        if (carta == null) return -1;
        ReturnTransaction rTransaction = returnTransactions.get(returnId);
        if (rTransaction == null) return -1;
        if (rTransaction.getStatus() != ReturnStatus.ENDED) return -1;
        double output = rTransaction.getPrice();
        if (!recordBalanceUpdate(output)) return -1;
        return output;
    }
    
    // public boolean recordBalanceUpdateCCC(double toBeAdded) {
    //     if (toBeAdded >= 0) {
    //         CreditCC transaction = new CreditCC(balanceOperationGen++, LocalDate.now(), "CreditCC");
    //         transaction.setMoney(toBeAdded);
    //         balanceOperations.put(transaction.getBalanceId(), transaction);
    //     }
    //     try {
    //         this.persistBalanceOperations();
    //     } catch (IOException e) {
    //         balanceOperations.remove(balanceOperationGen - 1);
    //         return false;
    //     }
    //     try {
    //         persistGenerators();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return true;
    // }
    
    public boolean recordBalanceUpdate(double toBeAdded) {
        if (toBeAdded >= 0) {
            Credit transaction = new Credit(balanceOperationGen++, LocalDate.now(), "Credit");
            transaction.setMoney(toBeAdded);
            balanceOperations.put(transaction.getBalanceId(), transaction);
            
        } else {
            Debit transaction = new Debit(balanceOperationGen++, LocalDate.now(), "Debit");
            transaction.setMoney(toBeAdded);
            balanceOperations.put(transaction.getBalanceId(), transaction);
            if (computeBalance() < 0) {
                balanceOperations.remove(transaction.getBalanceId());
                return false;
            }
        }
        try {
            this.persistBalanceOperations();
        } catch (IOException e) {
            balanceOperations.remove(balanceOperationGen - 1);
            return false;
        }
        try {
            persistGenerators();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) {
        List<BalanceOperation> output = new LinkedList<BalanceOperation>();
        for (SaleTransactionObj sale : saleTransactions.values()) {
            if ((from == null && to == null) ||
                    (from == null && sale.getDate().isBefore(to)) ||
                    (from != null && sale.getDate().isAfter(from) && to == null) ||
                    ((from != null && to != null) && sale.getDate().isAfter(from) && sale.getDate().isBefore(to))) {
                output.add((BalanceOperation) sale);
            }
        }
        for (ReturnTransaction rTransaction : returnTransactions.values()) {
            if ((from == null && to == null) ||
                    (from == null && rTransaction.getDate().isBefore(to)) ||
                    (from != null && rTransaction.getDate().isAfter(from) && to == null) ||
                    ((from != null && to != null) && rTransaction.getDate().isAfter(from) && rTransaction.getDate().isBefore(to))) {
                output.add((BalanceOperation) rTransaction);
            }
        }
        for (OrderObj order : orders.values()) {
            if (order.getBalanceOperation() != null &&
                    ((from == null && to == null) ||
                            (from == null && order.getBalanceOperation().getDate().isBefore(to)) ||
                            (from != null && order.getBalanceOperation().getDate().isAfter(from) && to == null) ||
                            ((from != null && to != null) && order.getBalanceOperation().getDate().isAfter(from) && order.getBalanceOperation().getDate().isBefore(to))))
                output.add(order.getBalanceOperation());
            
        }
        return output;
    }
    
    public double computeBalance() {
        double balance;
        balance = 0;
        for (BalanceOperationObj operation : balanceOperations.values()) {
            balance += operation.getMoney();
        }
        return balance;
    }
    
    public void clear() {
        //maybe this needs to clear orders too?
        //yes and also has to clear the files
        saleTransactions.clear();
        returnTransactions.clear();
        //cards.clear(); TODO MAYBE IT HAS TO BE REMOVED
        orders.clear();
        balanceOperations.clear();
        File fold = new File(GENERATOR_PATH);
        fold.delete();
        File fold1 = new File(RETURN_PATH);
        fold1.delete();
        File fold2 = new File(SALE_PATH);
        fold2.delete();
        File fold3 = new File(BALANCEOPERATION_PATH);
        fold3.delete();
        File fold4 = new File(ORDER_PATH);
        fold4.delete();
        
    }
    
    public boolean luhn(String ccNumber) {
        if (ccNumber == null) return false;
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = 0;
            try {
                n = Integer.parseInt(ccNumber.substring(i, i + 1));
            } catch (NumberFormatException e) {
                return false;
            }
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        if (sum == 0) return false;
        return (sum % 10 == 0);
    }
    
    public OrderObj addCompletedOrder(Integer orderId) {
        OrderObj target = orders.get(orderId);
        if (target == null) return null;
        switch (OrderStatus.valueOf(target.getStatus())) {
            case PAYED:
                target.setStatus(OrderStatus.COMPLETED.name());
                try {
                    persistOrders();
                } catch (IOException e) {
                    target.setStatus(OrderStatus.PAYED.name());
                    return null;
                }
                return new OrderObj(target);
            case COMPLETED:
                return new OrderObj(target);
            default:
                return null;
        }
    }
    
    public boolean addOrder(OrderObj order) {
        // update the balance if the order status is payed check also if the field balanceOperation of the order
        // is not null and in that case add it to the balanceOperations map.
        // if there is not enough balance return false
        // add persistence
        double tot = order.getPricePerUnit() * order.getQuantity();
        if (order.getStatus().equals(OrderStatus.ISSUED.name())) {
            BalanceOperationObj operation = new Debit(balanceOperationGen++, LocalDate.now(), "Debit");
            order.setBalanceOperation(operation);
            order.setBalanceId(operation.getBalanceId());
            order.getBalanceOperation().setMoney(-tot);
            orders.put(order.getOrderId(), new OrderObj(order));
        }
        if (order.getStatus().equals(OrderStatus.PAYED.name())) {
            if (!orders.containsKey(order.getOrderId())) {
                order = new OrderObj(order);
                orders.put(order.getOrderId(), order);
                order.setBalanceOperation(new Debit(balanceOperationGen++, LocalDate.now(), "Debit"));
            }
            order = orders.get(order.getOrderId());
            order.setStatus("PAYED");
            final BalanceOperation orderOperation = order.getBalanceOperation();
            orderOperation.setDate(LocalDate.now());
            balanceOperations.put(orderOperation.getBalanceId(), (BalanceOperationObj) orderOperation);
            if (computeBalance() < 0) {
                balanceOperations.remove(orderOperation.getBalanceId());
                return false;
            }
        }
        try {
            this.persistBalanceOperations();
        } catch (IOException e) {
            balanceOperations.remove(balanceOperationGen - 1);
            return false;
        }
        try {
            this.persistOrders();
        } catch (IOException e) {
            e.printStackTrace();
            balanceOperations.remove(balanceOperationGen - 1);
            return false;
        }
        try {
            persistGenerators();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
        
    }
    
    private void persistCards() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(CREDITCARD_PATH), cards);
    }
    
    // private void persistCCCards() throws IOException {
    //     ObjectMapper mapper = new ObjectMapper();
    //     mapper.writerWithDefaultPrettyPrinter()
    //             .writeValue(new File(CRYPTOCURRENCY_PATH), cards);
    // }

    private void persistOrders() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(ORDER_PATH), orders);
    }
    
    private void persistSales() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(SALE_PATH), saleTransactions);
    }
    
    private void persistReturns() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(RETURN_PATH), returnTransactions);
    }
    
    private void persistBalanceOperations() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(BALANCEOPERATION_PATH), balanceOperations);
    }
    
    private void persistGenerators() throws IOException {
        try {
            File fold = new File(GENERATOR_PATH);
            fold.delete();
            FileWriter myWriter = new FileWriter(GENERATOR_PATH);
            myWriter.write(saleGen + "\n");
            myWriter.write(returnGen + "\n");
            myWriter.write(balanceOperationGen + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
// public void defineCryptoCurrencyCards(){
    //     CryptoCurrencyCard cc  = new CryptoCurrencyCard((float) 7.9927398713, 25.3);
    //     CryptoCurrencyCard cc2  = new CryptoCurrencyCard((float)1.010101010101010101, 12.3);
    //     ccc.put(cc.getNumberCC(), cc);
    //     ccc.put(cc2.getNumberCC(), cc2);
    //     try {
    //         this.persistCCCards();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    //TODO IMPROVE IT
public void defineCreditCards() {
        CreditCard cc = new CreditCard("79927398713", 25.3);
        CreditCard cc2 = new CreditCard("1010101010101010101", 12.3);
        cards.put(cc.getNumber(), cc);
        cards.put(cc2.getNumber(), cc2);
        try {
            this.persistCards();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

