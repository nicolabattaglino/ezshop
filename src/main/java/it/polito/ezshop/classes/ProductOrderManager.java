package it.polito.ezshop.classes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ProductOrderManager {
    
    public static final String PRODUCT_TYPES_PATH = "data/product_types.json";
    public static final String PRODUCTS_PATH = "data/products.json";
    public static final String PRODUCT_GEN_PATH = "data/product_gen.json";
    public static final String ORDER_GEN_PATH = "data/order_gen.json";
    private final EZShop shop;
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private Map<String, ProductTypeObj> productTypesMap;
    @JsonDeserialize
    private Map<String, Product> RFIDMap;
    
    private int productIdGen;
    private int orderIdGen;
    
    public ProductOrderManager(EZShop shop) {
        this.shop = shop;
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, ProductTypeObj>> typeRef = new TypeReference<HashMap<String, ProductTypeObj>>() {
        };
        File producTtypes = new File(PRODUCT_TYPES_PATH);
        try {
            producTtypes.createNewFile();
            productTypesMap = mapper.readValue(producTtypes, typeRef);
        } catch (IOException e) {
            producTtypes.delete();
            try {
                producTtypes.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                productTypesMap = new HashMap<>();
            }
        }
        TypeReference<HashMap<String, Product>> typeRef2 = new TypeReference<HashMap<String, Product>>() {
        };
        File products = new File(PRODUCTS_PATH);
        try {
            products.createNewFile();
            RFIDMap = mapper.readValue(products, typeRef2);
        } catch (IOException e) {
            products.delete();
            try {
                products.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                RFIDMap = new HashMap<>();
            }
        }
    
        RFIDMap.values().forEach(p -> {
            p.setProductType(productTypesMap.get(p.getBarcode()));
        });
    
        File productGen = new File(PRODUCT_GEN_PATH);
        try {
            productGen.createNewFile();
            productIdGen = mapper.readValue(productGen, Integer.class);
        } catch (IOException e) {
            productGen.delete();
            try {
                productGen.createNewFile();
                mapper.writerWithDefaultPrettyPrinter().writeValue(productGen, 1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                productIdGen = 1;
            }
        }
        File orderGen = new File(ORDER_GEN_PATH);
        try {
            orderGen.createNewFile();
            orderIdGen = mapper.readValue(productGen, Integer.class);
        } catch (IOException e) {
            orderGen.delete();
            try {
                orderGen.createNewFile();
                mapper.writerWithDefaultPrettyPrinter().writeValue(orderGen, 1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                orderIdGen = 1;
            }
        }
    }
    
    public boolean checkBarcode(String barCode) {
        if (barCode == null) return false;
        if (!barCode.matches("[0-9]{12,14}")) return false;
        final int length = barCode.length();
        int checkValue = barCode.charAt(length - 1) - '0';
        int res = 0;
        for (int i = 0; i < length - 1; i++) {
            int factor = 1;
            if (i % 2 == 0) factor = 3;
            res += factor * (barCode.charAt(length - i - 2) - '0');
        }
        
        res = 10 - (res % 10);
        
        return res == checkValue;
    }
    
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException {
        if (!checkBarcode(productCode)) {
            throw new InvalidProductCodeException();
        }
        if (description == null || description.trim().length() == 0)
            throw new InvalidProductDescriptionException();
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();
        if (productTypesMap.containsKey(productCode))
            return -1;
        productIdGen++;
        productTypesMap.put(productCode, new ProductTypeObj(0, productIdGen, description, productCode, (note == null) ? "" : note, pricePerUnit, 0));
        try {
            persistProductTypes();
            persistGen();
        } catch (IOException e) {
            productTypesMap.remove(productCode);
            productIdGen--;
            return -1;
        }
        return productIdGen;
    }
    
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
        if (id == null || id <= 0) throw new InvalidProductIdException();
        if (newPrice <= 0) {
            throw new InvalidPricePerUnitException();
        }
        if (newDescription == null || newDescription.trim().length() == 0)
            throw new InvalidProductDescriptionException();
        if (!checkBarcode(newCode))
            throw new InvalidProductCodeException();
        ProductTypeObj productTypeObj = productTypesMap.get(newCode);
        if (productTypeObj != null && !productTypeObj.getId().equals(id))
            return false;
        
        ProductTypeObj candidate = null;
        
        for (ProductTypeObj productType : productTypesMap.values()) {
            if (productType.getId().equals(id)) candidate = productType;
        }
        if (candidate == null) return false;
        ProductTypeObj old = new ProductTypeObj(candidate);
        productTypesMap.remove(candidate.getBarCode());
        candidate.setProductDescription(newDescription);
        candidate.setBarCode(newCode);
        candidate.setPricePerUnit(newPrice);
        candidate.setNote(newNote == null ? "" : newNote);
        productTypesMap.put(newCode, candidate);
        try {
            persistProductTypes();
        } catch (IOException e) {
            productTypesMap.remove(candidate.getBarCode());
            productTypesMap.put(old.getBarCode(), old);
            return false;
        }
        
        return true;
    }
    
    public boolean deleteProductType(Integer id) throws InvalidProductIdException {
        if (id == null || id <= 0) throw new InvalidProductIdException();
        ProductTypeObj candidate = null;
        
        for (ProductTypeObj productType : productTypesMap.values()) {
            if (productType.getId().equals(id)) candidate = productType;
        }
        if (candidate == null) return false;
        productTypesMap.remove(candidate.getBarCode());
        try {
            persistProductTypes();
        } catch (IOException e) {
            productTypesMap.put(candidate.getBarCode(), candidate);
            return false;
        }
        return true;
    }
    
    public List<ProductType> getAllProductTypes() {
        ArrayList<ProductType> ret = new ArrayList<>(productTypesMap.values().size());
        for (ProductTypeObj value : productTypesMap.values()) {
            ret.add(new ProductTypeObj(value));
        }
        return ret;
    }
    
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException {
        if (!checkBarcode(barCode)) throw new InvalidProductCodeException();
        ProductTypeObj product = productTypesMap.get(barCode);
        return product == null ? null : new ProductTypeObj(product);
    }
    
    public List<ProductType> getProductTypesByDescription(String description) {
        final String finalDescription = description == null ? "" : description;
        return productTypesMap.values().stream()
                .filter(productType -> productType.getProductDescription().matches(".*" + finalDescription + ".*"))
                .map(ProductTypeObj::new)
                .collect(Collectors.toList());
    }
    
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException {
        if (productId == null || productId <= 0) throw new InvalidProductIdException();
        ProductType target = null;
        for (ProductType p : productTypesMap.values()) {
            if (p.getId().equals(productId) && !p.getLocation().equals("")) {
                final int oldQuantity = p.getQuantity();
                int quantity = oldQuantity + toBeAdded;
                if (quantity >= 0) {
                    p.setQuantity(quantity);
                    try {
                        persistProductTypes();
                    } catch (IOException e) {
                        p.setQuantity(oldQuantity);
                        return false;
                    }
                    return true;
                } else return false;
            }
        }
        return false;
    }
    
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException {
        if (productId == null || productId <= 0) throw new InvalidProductIdException();
        newPos = (newPos == null) ? "" : newPos.toUpperCase();
        if (!"".equals(newPos) && !newPos.matches("[0-9]+-[a-zA-Z]+-[0-9]+"))
            throw new InvalidLocationException();
        ProductTypeObj target = null;
        String oldLoc = null;
        for (ProductTypeObj p : productTypesMap.values()) {
            oldLoc = p.getLocation();
            if (oldLoc.equals(newPos) && !newPos.equals("") && !p.getId().equals(productId)) return false;
            if (p.getId().equals(productId))
                target = p;
        }
        if (target == null) return false;
        target.setPosition(newPos.length() == 0 ? new Position() : new Position(newPos));
        try {
            persistProductTypes();
        } catch (IOException e) {
            target.setLocation(oldLoc);
            return false;
        }
        return true;
    }
    
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException {
        if (quantity <= 0) throw new InvalidQuantityException();
        if (pricePerUnit <= 0) throw new InvalidPricePerUnitException();
        ProductType p = getProductTypeByBarCode(productCode);
        if (p == null) return -1;
        OrderObj order = new OrderObj(orderIdGen++, p, pricePerUnit, quantity);
        try {
            persistGen();
        } catch (IOException e) {
            orderIdGen--;
            return -1;
        }
        shop.addOrder(order);
        return order.getOrderId();
    }
    
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
            throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException {
        if (!checkBarcode(productCode)) throw new InvalidProductCodeException();
        if (quantity <= 0) throw new InvalidQuantityException();
        if (pricePerUnit <= 0) throw new InvalidPricePerUnitException();
        ProductType target = getProductTypeByBarCode(productCode);
        if (target == null) return -1;
        OrderObj o = new OrderObj(orderIdGen++, target, pricePerUnit, quantity);
        try {
            persistGen();
        } catch (IOException e) {
            orderIdGen--;
            return -1;
        }
        o.setStatus("payed");
        if (!shop.addOrder(o)) return -1;
        return o.getOrderId();
    }
    
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if (orderId == null || orderId <= 0) throw new InvalidOrderIdException();
        Optional<Order> target = shop.getTransactionManager().getAllOrders().stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst();
        if (!target.isPresent()) return false;
        final Order order = target.get();
        if (OrderStatus.valueOf(order.getStatus()) == OrderStatus.ISSUED) {
            order.setStatus("PAYED");
            return shop.addOrder((OrderObj) order);
        }
        return false;
    }
    
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, InvalidLocationException {
        if (orderId == null || orderId <= 0) throw new InvalidOrderIdException();
        OrderObj order = shop.getTransactionManager().addCompletedOrder(orderId);
        if (order == null) return false;
        String oldStatus = order.getStatus();
        ProductTypeObj target = productTypesMap.get(order.getProductCode());
        int quantity = target.getQuantity();
        try {
            if (!updateQuantity(target.getId(), order.getQuantity())) throw new InvalidLocationException();
        } catch (InvalidProductIdException e) {
            e.printStackTrace();
        }
        try {
            persistProductTypes();
        } catch (IOException e) {
            order.setStatus(oldStatus);
            target.setQuantity(quantity);
            shop.addOrder(order);
        }
        return true;
    }
    
    //TODO COMPLETA
    public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException, InvalidRFIDException {
        if (orderId == null || orderId <= 0) throw new InvalidOrderIdException();
        if (RFIDfrom == null || !RFIDfrom.matches("[0-9]{10}")) throw new InvalidOrderIdException();
        OrderObj order = shop.getTransactionManager().addCompletedOrder(orderId);
        if (order == null) return false;
        String oldStatus = order.getStatus();
        ProductTypeObj target = productTypesMap.get(order.getProductCode());
        int quantity = target.getQuantity();
        try {
            if (!updateQuantity(target.getId(), order.getQuantity())) throw new InvalidLocationException();
        } catch (InvalidProductIdException e) {
            e.printStackTrace();
        }
        long rfidStart = Integer.parseInt(RFIDfrom);
        final long startingPoint = rfidStart;
        HashSet<String> RFIDs = new HashSet<>();
        
        for (int i = 0; i < order.getQuantity(); ) {
            final String newRfid = String.format("%010d", rfidStart);
            if (RFIDMap.containsKey(newRfid)) {
                rfidStart = (rfidStart + 1) % 10000000000L;
                if (rfidStart == startingPoint)
                    throw new InvalidRFIDException();
            } else {
                RFIDs.add(newRfid);
                rfidStart = (rfidStart + 1) % 10000000000L;
                i++;
            }
        }
        
        RFIDs.forEach(rfid -> {
            Product product = new Product(rfid, order.getProductCode());
            product.setProductType(target);
            RFIDMap.put(rfid, product);
        });
        
        try {
            persistProductTypes();
            persistProducts();
        } catch (IOException e) {
            order.setStatus(oldStatus);
            target.setQuantity(quantity);
            for (String rfid : RFIDs) {
                RFIDMap.remove(rfid);
            }
            shop.addOrder(order);
        }
        return true;
    }
    
    private void persistProductTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(PRODUCT_TYPES_PATH), productTypesMap);
    }
    
    private void persistProducts() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(PRODUCTS_PATH), RFIDMap);
    }
    
    private void persistGen() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(PRODUCT_GEN_PATH), productIdGen);
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(ORDER_GEN_PATH), orderIdGen);
    }
    
    public void clear() {
        productTypesMap.clear();
        (new File(PRODUCT_TYPES_PATH)).delete();
        (new File(PRODUCTS_PATH)).delete();
        (new File(PRODUCT_GEN_PATH)).delete();
        (new File(ORDER_GEN_PATH)).delete();
    }
    
    
    /**
     * This method adds a Product to the RFIDMap
     *
     * @param product the product that has to be added
     * @return true if the operation is successful
     * false   if the ProductType referred to the product does not exist,
     * @throws InvalidRFIDException if the RFID code is already present into the RFIDMap
     */
    public boolean putProduct(Product product) throws InvalidRFIDException {
        final ProductTypeObj productType = productTypesMap.get(product.getBarcode());
        if (productType == null) return false;
        product.setProductType(productType);
        final Product product1 = RFIDMap.put(product.getRFID(), product);
        if (product1 != null) {
            RFIDMap.put(product.getRFID(), product);
            throw new InvalidRFIDException();
        }
        try {
            persistProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    /**
     * This method get a Product given its own RFID from the RFIDMap
     *
     * @param RFID the RFID of the Product that has to be gotten
     * @return the Product mapped by its own RFID, null otherwise
     * @throws InvalidRFIDException if the RFID code is empty, null or invalid
     */
    public Product getProduct(String RFID) throws InvalidRFIDException {
        if (RFID == null || !RFID.matches("[0-9]{10}")) throw new InvalidRFIDException();
        final int key = Integer.parseInt(RFID);
        return RFIDMap.get(key);
    }
    
    /**
     * This method remove a Product given its own RFID from the RFIDMap
     *
     * @param RFID the RFID of the Product that has to be gotten
     * @return the Product mapped by its own RFID, null otherwise
     * @throws InvalidRFIDException if the RFID code is empty, null or invalid
     */
    public Product removeProduct(String RFID) throws InvalidRFIDException {
        if (RFID == null || !RFID.matches("[0-9]{10}")) throw new InvalidRFIDException();
        final int key = Integer.parseInt(RFID);
        final Product remove = RFIDMap.remove(key);
        if (remove != null) {
            try {
                persistProducts();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return remove;
    }
    
    
}
