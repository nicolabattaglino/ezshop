package it.polito.ezshop.classes;

import static org.junit.Assert.*;

import org.junit.Test;

public class CustomerObjTest {


	@Test
	public void testSettersAndGetters(){
		CustomerObj c = new CustomerObj();
		c.setCustomerName("Antonio");
		assertEquals("Antonio", c.getCustomerName());
		c.setId(10);
		assertTrue(10 == c.getId());
		LoyaltyCardObj card = new LoyaltyCardObj();
		c.setLoyaltyCard(card);
		assertTrue(card == c.getLoyaltyCard());
		card = c.getLoyaltyCard();
		c.setPoints(5);
		assertTrue(5 == card.getPoints());
		c.setCustomerName(null);
		assertEquals(null, c.getCustomerName());
		CustomerObj cu = new CustomerObj(2, "John");
		assertNotNull(cu);
	}

	@Test
	public void testGetCardFromNewCustomer(){
		CustomerObj cu = new CustomerObj(2, "John");
		assertEquals(cu.getCustomerCard(), "");

	}



}
