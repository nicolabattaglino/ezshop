package it.polito.ezshop.classes;

import static org.junit.Assert.*;

import org.junit.Test;

public class LoyaltyCardObjTest {


	@Test
	public void testSetIsAttached(){
		LoyaltyCardObj l1 = new LoyaltyCardObj("1000000002");
		l1.setIsAttached(true);
		assertTrue(l1.getIsAttached());
		l1.setIsAttached(false);
		assertFalse(l1.getIsAttached());
	}
	@Test
	public void testSetCardCode(){
		LoyaltyCardObj l1 = new LoyaltyCardObj();
		l1.setCardCode("1000000003");
		assertTrue(l1.getCardCode().equals("1000000003"));
	}
	@Test
	public void testSetPoints(){
		LoyaltyCardObj l1 = new LoyaltyCardObj("1000000002");
		l1.setPoints(20);
		assertTrue(l1.getPoints() == 20);
	}


}
