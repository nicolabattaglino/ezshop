package it.polito.ezshop.classes;

import it.polito.ezshop.exceptions.InvalidLocationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ProductTypeObjTest {
	
	@Test
	public void testSetPosition() {
		ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
		p.setPosition(new Position());
		assertEquals(new Position(), p.getPosition());
		try {
			final Position position = new Position("10-10-10");
			p.setPosition(position);
			assertEquals(position, p.getPosition());
		} catch (InvalidLocationException e) {
			fail("Invalid position");
		}
	}
	
	@Test
	public void testSetDiscountRate() {
		ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
		p.setDiscountRate(0.5);
		assertEquals(0.5, p.getDiscountRate(), 0.0);
	}
	
	@Test
	public void testSetQuantity() {
		ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
		p.setQuantity(25);
		assertEquals((Integer) 25, p.getQuantity());
	}
	
	@Test
	public void testSetLocation() {
		ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
		p.setLocation("");
		assertEquals("", p.getLocation());
		p.setLocation(null);
		assertEquals("", p.getLocation());
		p.setLocation("12-13-14");
		assertEquals("12-13-14", p.getLocation());
	}
	
	@Test
	public void testSetNote() {
		ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
		p.setNote("prova");
		assertEquals("prova", p.getNote());
	}
	
	@Test
	public void testSetProductDescription() {
		ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
		p.setProductDescription("prova");
		assertEquals("prova", p.getProductDescription());
	}
	
	@Test
	public void testSetBarCode() {
		ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
		p.setBarCode("2152155151");
		assertEquals("2152155151", p.getBarCode());
	}
	
	@Test
	public void testSetPricePerUnit() {
		ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
		p.setPricePerUnit(28.7);
		assertEquals(28.7, p.getPricePerUnit(), 0.0);
	}
	
	@Test
	public void testSetId() {
		ProductTypeObj p = new ProductTypeObj(20, 0, "descr", "11111111111111", "notes", 10.0, 0.25, new Position());
		p.setId(28);
		assertEquals((Integer) 28, p.getId());
	}
}
