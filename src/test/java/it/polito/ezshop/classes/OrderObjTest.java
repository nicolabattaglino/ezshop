package it.polito.ezshop.classes;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class OrderObjTest {


    @Test
    public void testSetBalanceOperation() {
        ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
        OrderObj o = new OrderObj(25, p, 28.5, 27);
        final Debit debit = new Debit(21, LocalDate.now(), "debit");
        o.setBalanceOperation(debit);
        assertEquals(debit, o.getBalanceOperation());

    }

    @Test
    public void testSetProductCode() {
        ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
        OrderObj o = new OrderObj(25, p, 28.5, 27);
        final String productCode = "1234558783";
        o.setProductCode(productCode);
        assertEquals(productCode, o.getProductCode());
    }

    @Test
    public void testSetPricePerUnit() {
        ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
        OrderObj o = new OrderObj(25, p, 28.5, 27);
        o.setPricePerUnit(352.2);
        assertEquals(352.2, o.getPricePerUnit(), 0.0);
    }

    @Test
    public void testSetQuantity() {
        ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
        OrderObj o = new OrderObj(25, p, 28.5, 27);
        o.setQuantity(28);
        assertEquals(28, o.getQuantity());
    }

    @Test
    public void testSetStatus() {
        ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
        OrderObj o = new OrderObj(25, p, 28.5, 27);
        o.setStatus("ISSUED");
        assertEquals("ISSUED", o.getStatus());
        o.setStatus("PAYED");
        assertEquals("PAYED", o.getStatus());
        o.setStatus("COMPLETED");
        assertEquals("COMPLETED", o.getStatus());
        assertThrows(RuntimeException.class, () -> o.setStatus("abc"));
    }

    @Test
    public void testSetOrderId() {
        ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
        OrderObj o = new OrderObj(25, p, 28.5, 27);
        o.setOrderId(98);
        assertEquals((Integer) 98, o.getOrderId());
    }

    @Test
    public void testSetBalanceId() {
        ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
        OrderObj o = new OrderObj(25, p, 28.5, 27);
        final Debit debit = new Debit(21, LocalDate.now(), "debit");
        o.setBalanceOperation(debit);
        o.setBalanceId(98);
        assertEquals((Integer) 98, o.getBalanceId());
    }

    @Test
    public void testSetProduct() {
        ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
        OrderObj o = new OrderObj(25, p, 28.5, 27);
        final ProductTypeObj product = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
        o.setProduct(product);
        assertEquals(product, o.getProduct());
    }

    @Test
    public void testSetSupplier() {
        ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
        OrderObj o = new OrderObj(25, p, 28.5, 27);
        o.setSupplier("Ferrero");
        assertEquals("Ferrero", o.getSupplier());
    }

    @Test
    public void testConstructors() {
        ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
        final Debit debit = new Debit(21, LocalDate.now(), "debit");
        OrderObj o = new
                OrderObj(1,
                p,
                0.5,
                50,
                "supplier",
                OrderStatus.ISSUED,
                debit);
		OrderObj o2 = new OrderObj(o);
		assertEquals(o.getBalanceId(), o2.getBalanceId());
    }
}
