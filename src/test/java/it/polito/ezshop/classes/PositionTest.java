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
        assertThrows(InvalidLocationException.class, () -> new Position("0-1-3"));
        assertThrows(InvalidLocationException.class, () -> new Position("1-0-3"));
        assertThrows(InvalidLocationException.class, () -> new Position("1-1-0"));
        assertThrows(InvalidLocationException.class, () -> new Position("1-00-01"));
        assertThrows(InvalidLocationException.class, () -> new Position("--"));
        try {
            p = new Position("10-11-18");
            assertEquals((Integer) 10, p.aisleID);
            assertEquals((Integer) 11, p.rackID);
            assertEquals((Integer) 18, p.levelId);
        
            p = new Position("010-011-018");
            assertEquals((Integer) 10, p.aisleID);
            assertEquals((Integer) 11, p.rackID);
            assertEquals((Integer) 18, p.levelId);
        
        } catch (InvalidLocationException e) {
            fail("Invalid position" + e);
        }
        assertEquals((Integer) 10, p.aisleID);
        assertEquals((Integer) 11, p.rackID);
        assertEquals((Integer) 18, p.levelId);
    }
    
}
