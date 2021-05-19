package it.polito.ezshop.classes;

import it.polito.ezshop.exceptions.InvalidLocationException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {
	
	@Test
	public void testConstructors() {
		Position p = new Position();
		assertEquals("", p.toString());
		assertThrows(InvalidLocationException.class, () -> new Position("a-b-c"));
		assertThrows(InvalidLocationException.class, () -> new Position("0-0-0"));
		try {
			new Position("10-11-15");
		} catch (InvalidLocationException e) {
			e.printStackTrace();
			fail("Invalid position");
		}
	}

}
