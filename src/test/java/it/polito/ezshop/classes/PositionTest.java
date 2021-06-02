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
        assertEquals("", p.rackID);
        assertEquals(i, p.levelId);
        assertThrows(InvalidLocationException.class, () -> new Position("A-BB-C"));
        assertThrows(InvalidLocationException.class, () -> new Position("0-1-3"));
        assertThrows(InvalidLocationException.class, () -> new Position("A-1-3"));
        assertThrows(InvalidLocationException.class, () -> new Position("3-1-B"));
        assertThrows(InvalidLocationException.class, () -> new Position("0-1-a"));
        assertThrows(InvalidLocationException.class, () -> new Position("1-0-3"));
        assertThrows(InvalidLocationException.class, () -> new Position("1-2-0"));
        assertThrows(InvalidLocationException.class, () -> new Position("0a-aa-01"));
        assertThrows(InvalidLocationException.class, () -> new Position("--"));
        try {
            p = new Position("10-CC-18");
            assertEquals((Integer) 10, p.aisleID);
            assertEquals("CC", p.rackID);
            assertEquals((Integer) 18, p.levelId);
            
            p = new Position("10-aa-18");
            assertEquals((Integer) 10, p.aisleID);
            assertEquals("AA", p.rackID);
            assertEquals((Integer) 18, p.levelId);
            
            p = new Position("014-Bb-019");
            assertEquals((Integer) 14, p.aisleID);
            assertEquals("BB", p.rackID);
            assertEquals((Integer) 19, p.levelId);
            
        } catch (InvalidLocationException e) {
            fail("Invalid position" + e);
        }
    }
    
}
