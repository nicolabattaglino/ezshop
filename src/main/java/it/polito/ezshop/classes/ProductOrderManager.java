package it.polito.ezshop.classes;

import it.polito.ezshop.EZShop;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ProductOrderManager {
    
    private final EZShop shop;
    private final HashMap<String, ProductType> productMap = new HashMap<>();
    private int prouctIdGen; // TODO mettilo di default a 1
    
    public ProductOrderManager(EZShop shop) {
        this.shop = shop;
        //TODO load map from persistance
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
        
        ProductType candidate = null;
        
        for (ProductType productType : productMap.values()) {
            if (productType.getId().equals(id)) candidate = productType;
        }
        if (candidate == null) return false;
        
        //TODO vedi se la remove rimuove anche la chiave
        productMap.remove(candidate.getBarCode());
        candidate.setProductDescription(newDescription);
        candidate.setBarCode(newCode);
        candidate.setPricePerUnit(newPrice);
        candidate.setNote(newNote == null ? "" : newNote);
        productMap.put(newCode, candidate);
        return true;
    }
    
    public boolean deleteProductType(Integer id) throws InvalidProductIdException {
        if (id == null || id <= 0) throw new InvalidProductIdException();
        ProductType candidate = null;
        
        for (ProductType productType : productMap.values()) {
            if (productType.getId().equals(id)) candidate = productType;
        }
        if (candidate == null) return false;
        
        //TODO vedi se la remove rimuove anche la chiave
        productMap.remove(candidate.getBarCode());
        return true;
    }
    
    public List<ProductType> getAllProductTypes() {
        //TODO chiedi per quanto riguarda l'aliasing
        return new ArrayList<>(productMap.values());
    }
    
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException {
        if (!checkBarcode(barCode)) throw new InvalidProductCodeException();
        return productMap.get(barCode);
    }
    
    public List<ProductType> getProductTypesByDescription(String description) {
        final String desc = description == null ? "" : description;
        return productMap.values().stream().filter(productType -> productType.getProductDescription().equals(desc)).collect(Collectors.toList());
    }
    
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException {
        if (productId == null || productId <= 0) throw new InvalidProductIdException();
        ProductType target;
        for (ProductType p : productMap.values()) {
            if (p.getId().equals(productId) && p.getLocation() != null) {
                int quantity = p.getQuantity() + toBeAdded;
                if (quantity >= 0) {
                    p.setQuantity(quantity);
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
        for (ProductType p : productMap.values()) {
            if (p.getLocation().equals(newPos) && !newPos.equals("")) return false;
            if (p.getId().equals(productId))
                target = p;
        }
        if (target == null) return false;
        target.setLocation(newPos);
        return true;
    }
    
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException {
        if (quantity <= 0) throw new InvalidQuantityException();
        if (pricePerUnit <= 0) throw new InvalidPricePerUnitException();
        ProductType p = getProductTypeByBarCode(productCode);
        if (p == null) return -1;
        Order order = new OrderObj(p, pricePerUnit, quantity);
        //todo aggiungilo da qualche parte
        return order.getOrderId();
    }
    
}
