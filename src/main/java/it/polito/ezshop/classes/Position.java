package it.polito.ezshop.classes;

import java.util.Objects;

public class Position {
    public final Integer aisleID;
    public final Integer rackID;
    public final Integer levelId;
    private final boolean empty;
    
    public Position(Integer aisleID, Integer rackID, Integer levelId) {
        this.aisleID = aisleID;
        this.rackID = rackID;
        this.levelId = levelId;
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
        if (!empty) return "";
        return "" + aisleID + "-" + rackID + "-" + levelId;
    }
}
