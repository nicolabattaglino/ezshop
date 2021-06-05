package it.polito.ezshop.classes;

import it.polito.ezshop.exceptions.InvalidLocationException;

import java.util.Objects;

public class Position {
    public final Integer aisleID;
    public final String rackID;
    public final Integer levelId;
    private final boolean empty;
    
    public Position(String pos) throws InvalidLocationException {
        if (!pos.matches("[0-9]+-[a-zA-Z]+-[0-9]+"))
            throw new InvalidLocationException();
        String[] tokens = pos.split("-");
        if (tokens.length != 3) throw new InvalidLocationException();
        this.aisleID = Integer.parseInt(tokens[0]);
        this.rackID = tokens[1].toUpperCase();
        this.levelId = Integer.parseInt(tokens[2]);
        this.empty = false;
    }
    
    public Position() {
        this.aisleID = -1;
        this.rackID = "";
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
