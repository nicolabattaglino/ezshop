package it.polito.ezshop.classes;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class OrderObjTest {
	
	
	@Test
	public void testSetBalanceOperation() {
		OrderObj o = new OrderObj(25, null, 28.5, 27);
		final Debit debit = new Debit(21, LocalDate.now(), "debit");
		o.setBalanceOperation(debit);
		assertEquals(debit, o.getBalanceOperation());
		
	}
	
	@Test
	public void testSetProductCode() {
		OrderObj o = new OrderObj(25, null, 28.5, 27);
		final String productCode = "1234558783";
		o.setProductCode(productCode);
		assertEquals(productCode, o.getProductCode());
	}
	
	@Test
	public void testSetPricePerUnit() {
		OrderObj o = new OrderObj(25, null, 28.5, 27);
		o.setPricePerUnit(352.2);
		assertEquals(352.2, o.getPricePerUnit(), 0.0);
	}
	
	@Test
	public void testSetQuantity() {
		OrderObj o = new OrderObj(25, null, 28.5, 27);
		o.setQuantity(28);
		assertEquals(28, o.getQuantity());
	}
	
	@Test
	public void testSetStatus() {
		OrderObj o = new OrderObj(25, null, 28.5, 27);
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
		OrderObj o = new OrderObj(25, null, 28.5, 27);
		o.setOrderId(98);
		assertEquals((Integer) 98, o.getOrderId());
	}
	
	@Test
	public void testSetBalanceId() {
		OrderObj o = new OrderObj(25, null, 28.5, 27);
		o.setBalanceId(98);
		assertEquals((Integer) 98, o.getBalanceId());
	}
	
	@Test
	public void testSetProduct() {
		OrderObj o = new OrderObj(25, null, 28.5, 27);
		final ProductTypeObj product = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
		o.setProduct(product);
		assertEquals(product, o.getProduct());
	}
	
	@Test
	public void testSetSupplier() {
		OrderObj o = new OrderObj(25, null, 28.5, 27);
		o.setSupplier("Ferrero");
		assertEquals("Ferrero", o.getSupplier());
	}
}
