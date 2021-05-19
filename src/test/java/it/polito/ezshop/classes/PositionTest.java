package it.polito.ezshop.classes;

import it.polito.ezshop.exceptions.InvalidLocationException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {
	
	
	@Test
	public void testConstructors() {
		Position p = new Position();
		Integer i = -1;
		assertEquals(i, p.aisleID);
		assertEquals(i, p.levelId);
		assertEquals(i, p.rackID);
		assertThrows(InvalidLocationException.class, () -> new Position("a-b-c"));
		assertThrows(InvalidLocationException.class, () -> new Position("0-01-03"));
		try {
			p = new Position("10-11-18");
		} catch (InvalidLocationException e) {
			e.printStackTrace();
			fail("Invalid position");
		}
		assertEquals((Integer) 10, p.aisleID);
		assertEquals((Integer) 11, p.rackID);
		assertEquals((Integer) 18, p.levelId);
	}

}
