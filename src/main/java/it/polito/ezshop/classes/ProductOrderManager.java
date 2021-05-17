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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductOrderManager {
    
    private static final String PRODUCTS_PATH = "data/products.json";
    private final EZShop shop;
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonDeserialize
    private HashMap<String, ProductTypeObj> productMap;
    private int prouctIdGen; // TODO mettilo di default a 1
    
    public ProductOrderManager(EZShop shop) {
        this.shop = shop;
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, ProductTypeObj>> typeRef = new TypeReference<HashMap<String, ProductTypeObj>>() {
        };
        File products = new File(PRODUCTS_PATH);
        try {
            products.createNewFile();
            productMap = mapper.readValue(products, typeRef);
        } catch (IOException e) {
            products.delete();
            try {
                products.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                productMap = new HashMap<>();
            }
        }
    }
    
    private boolean checkBarcode(String barCode) {
        if (barCode == null) return false;
        if (!barCode.matches("^([0-9]{8}|[0-9]{12,14}|[0-9]{17,18})$")) return false;
        final int length = barCode.length();
        int checkValue = barCode.charAt(length - 1);
        //TODO vedi se va bene
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
        if (productMap.containsKey(productCode))
            return -1;
        prouctIdGen++;
        productMap.put(productCode, new ProductTypeObj(0, prouctIdGen, description, productCode, (note == null) ? "" : note, pricePerUnit, 0));
        try {
            persistProducts();
        } catch (IOException e) {
            productMap.remove(productCode);
            return -1;
        }
        return prouctIdGen;
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
        if (productMap.containsKey(newCode)) return false;

        ProductTypeObj candidate = null;

        for (ProductTypeObj productType : productMap.values()) {
            if (productType.getId().equals(id)) candidate = productType;
        }
        if (candidate == null) return false;
        ProductTypeObj old = new ProductTypeObj(candidate);
        //TODO vedi se la remove rimuove anche la chiave
        productMap.remove(candidate.getBarCode());
        candidate.setProductDescription(newDescription);
        candidate.setBarCode(newCode);
        candidate.setPricePerUnit(newPrice);
        candidate.setNote(newNote == null ? "" : newNote);
        productMap.put(newCode, candidate);
        try {
            persistProducts();
        } catch (IOException e) {
            productMap.remove(candidate.getBarCode());
            productMap.put(old.getBarCode(), old);
            return false;
        }

        return true;
    }
    
    public boolean deleteProductType(Integer id) throws InvalidProductIdException {
        if (id == null || id <= 0) throw new InvalidProductIdException();
        ProductTypeObj candidate = null;

        for (ProductTypeObj productType : productMap.values()) {
            if (productType.getId().equals(id)) candidate = productType;
        }
        if (candidate == null) return false;
        productMap.remove(candidate.getBarCode());
        try {
            persistProducts();
        } catch (IOException e) {
            productMap.put(candidate.getBarCode(), candidate);
            return false;
        }
        return true;
    }
    
    public List<ProductType> getAllProductTypes() {
        ArrayList<ProductType> ret = new ArrayList<>(productMap.values().size());
        for (ProductTypeObj value : productMap.values()) {
            ret.add(new ProductTypeObj(value));
        }
        return ret;
    }
    
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException {
        if (!checkBarcode(barCode)) throw new InvalidProductCodeException();
        return new ProductTypeObj(productMap.get(barCode));
    }
    
    public List<ProductType> getProductTypesByDescription(String description) {
        final String desc = description == null ? "" : description;
        return productMap.values().stream()
                .filter(productType -> productType.getProductDescription().equals(desc))
                .map(ProductTypeObj::new)
                .collect(Collectors.toList());
    }
    
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException {
        if (productId == null || productId <= 0) throw new InvalidProductIdException();
        ProductType target;
        for (ProductType p : productMap.values()) {
            if (p.getId().equals(productId) && p.getLocation() != null) {
                final int oldQuantity = p.getQuantity();
                int quantity = oldQuantity + toBeAdded;
                if (quantity >= 0) {
                    p.setQuantity(quantity);
                    try {
                        persistProducts();
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
        newPos = (newPos == null) ? "" : newPos;
        ProductType target = null;
        String oldLoc = "";
        for (ProductType p : productMap.values()) {
            oldLoc = p.getLocation();
            if (oldLoc.equals(newPos) && !newPos.equals("")) return false;
            if (p.getId().equals(productId))
                target = p;
        }
        if (target == null) return false;
        target.setLocation(newPos);
        try {
            persistProducts();
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
        OrderObj order = new OrderObj(p, pricePerUnit, quantity);
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
        OrderObj o = new OrderObj(target, pricePerUnit, quantity);
        o.setStatus("payed");
        if (!shop.addOrder(o)) return -1;
        return o.getOrderId();
    }

    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if (orderId == null || orderId <= 0) throw new InvalidOrderIdException();
        Optional<Order> target = shop.getAllOrders().stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst();
        if (!target.isPresent()) return false;
        switch (OrderStatus.valueOf(target.get().getStatus())) {
            case ISSUED:
                return shop.addOrder((OrderObj) target.get());
            case PAYED:
                return true;
            default:
                return false;
        }
    }

    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, InvalidLocationException, UnauthorizedException {
        if (orderId == null || orderId <= 0) throw new InvalidOrderIdException();
        OrderObj order = shop.getTransactionManager().addCompletedOrder(orderId);
        if (order == null) return false;
        String oldStatus = order.getStatus();
        ProductTypeObj target = productMap.get(order.getProductCode());
        if (target.getLocation().length() == 0) throw new InvalidLocationException();
        int quantity = target.getQuantity();
        target.setQuantity(quantity + order.getQuantity());
        try {
            persistProducts();
        } catch (IOException e) {
            order.setStatus(oldStatus);
            target.setQuantity(quantity);
            shop.addOrder(order);
        }
        return true;
    }


    private void persistProducts() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(PRODUCTS_PATH), productMap);

    }

    public void clear() {
        productMap.clear();
        File products = new File(PRODUCTS_PATH);
        products.delete();
    }
}
