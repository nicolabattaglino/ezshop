package it.polito.ezshop.classes;

import static org.junit.Assert.*;

import org.junit.Test;

public class CustomerObjTest {


	@Test
	public void testSetLoyaltyCard(){
		CustomerObj c = new CustomerObj();
		LoyaltyCardObj card = new LoyaltyCardObj();
		c.setLoyaltyCard(card);
		assertSame(card, c.getLoyaltyCard());
	}

	@Test
	public void testSetCustomerName(){
		CustomerObj c = new CustomerObj();
		c.setCustomerName("Antonio");
		assertEquals("Antonio", c.getCustomerName());
	}

	@Test
	public void testSetCustomerCard(){
		CustomerObj cu = new CustomerObj(2, "John");
		LoyaltyCardObj card1 = new LoyaltyCardObj("1000000001");
		cu.setCustomerCard("1000000001");
		cu.setPoints(10);
		assertTrue(cu.getPoints() == 10);

	}

	@Test
	public void testSetId(){
		CustomerObj c = new CustomerObj();
		c.setId(10);
		assertTrue(10 == c.getId());
	}
	@Test
	public void testSetPoints(){
		CustomerManager cm = new CustomerManager(null);
		cm.createCard();
		CustomerObj cu = new CustomerObj(2, "John");
		cu.setCustomerCard("1000000000");
		cu.setPoints(10);
		assertTrue(cu.getPoints() == 10);
	}



	@Test
	public void testGetCardFromNewCustomer(){
		CustomerObj cu = new CustomerObj(2, "John");
		assertNull(cu.getCustomerCard());
		assertNull(cu.getLoyaltyCard());
		LoyaltyCardObj card = new LoyaltyCardObj("1000000001");
		cu.setCustomerCard("1000000001");
		assertEquals(card.getCardCode(), cu.getCustomerCard());


	}



}
