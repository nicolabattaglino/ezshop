package it.polito.ezshop.classes;

import it.polito.ezshop.exceptions.InvalidLocationException;

import java.util.Arrays;
import java.util.Objects;

public class Position {
    public final Integer aisleID;
    public final Integer rackID;
    public final Integer levelId;
    private final boolean empty;
    
    public Position(String pos) throws InvalidLocationException {
        if (!pos.matches("[1-9][0-9]*-[1-9][0-9]*-[1-9][0-9]*"))
            throw new InvalidLocationException();
        Integer[] tokens = Arrays.stream(pos.split("-"))
                .map(Integer::parseInt)
                .filter(integer -> integer > 0)
                .toArray(Integer[]::new);
        if (tokens.length != 3) throw new InvalidLocationException();
        this.aisleID = tokens[0];
        this.rackID = tokens[1];
        this.levelId = tokens[2];
        this.empty = false;
    }
    
    public Position() {
        this.aisleID = -1;
        this.rackID = -1;
        this.levelId = -1;
        this.empty = true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return Objects.equals(aisleID, position.aisleID) && Objects.equals(rackID, position.rackID) && Objects.equals(levelId, position.levelId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(aisleID, rackID, levelId);
    }
    
    @Override
    public String toString() {
        if (empty) return "";
        return "" + aisleID + "-" + rackID + "-" + levelId;
    }
    
}
