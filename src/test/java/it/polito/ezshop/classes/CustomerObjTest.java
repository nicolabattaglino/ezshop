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
		CustomerManager cm = new CustomerManager(null);
		cm.createCard();
		CustomerObj cu = new CustomerObj(1, "John");
		assertNull(cu.getCustomerCard());
		cu.setCustomerCard("1000000000");
		assertEquals("1000000000", cu.getCustomerCard());
	}

	@Test
	public void testSetId(){
		CustomerObj c = new CustomerObj();
		c.setId(10);
		assertEquals(10, (int) c.getId());
	}
	@Test
	public void testSetPoints(){
		CustomerObj cu = new CustomerObj(2, "John");
		LoyaltyCardObj l = new LoyaltyCardObj();
		cu.setLoyaltyCard(l);
		cu.setPoints(10);
		assertEquals(10, (int) cu.getPoints());
	}

	@Test
	public void testConstructor(){
		CustomerObj cu = new CustomerObj(2, "John");
		LoyaltyCardObj l = new LoyaltyCardObj();
		cu.setLoyaltyCard(l);
		CustomerObj cu1 = new CustomerObj(cu);
		assertEquals(cu1.getId(), cu.getId());
	}

}
