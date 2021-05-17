package it.polito.ezshop.classes;

import static org.junit.Assert.*;

import org.junit.Test;

public class LoyaltyCardObjTest {

	@Test
	public void testGettersAndSetters() {

		LoyaltyCardObj l = new LoyaltyCardObj();
		assertNotNull(l);
		LoyaltyCardObj l1 = new LoyaltyCardObj("1000000002");
		assertNotNull(l1);
		l1.setIsAttached(true);
		assertTrue(l1.getIsAttached());
		l1.setPoints(20);
		assertTrue(l1.getPoints() == 20);


	}

}
