package de.uulm.team020.server.game.phases.main;

import java.util.HashSet;
import java.util.Objects;

import de.uulm.team020.datatypes.Character;

/**
 * Controls a faction hold by a Player
 * 
 * @author Florian Sihler
 * @version 1.0, 04/19/2020
 */
public class Faction extends HashSet<Character> {

    private static final long serialVersionUID = -1713310616366536690L;

    private String name;

    /**
     * Construct a new Faction, hold by a Player
     * 
     * @param name Name of the faction, is used for debugging reasons
     */
    public Faction(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Faction [name=").append(name).append(", members=").append(super.toString()).append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(name);
        return result;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Faction)) {
            return false;
        }
        Faction other = (Faction) obj;
        if (!Objects.equals(name, other.name))
            return false;
        return super.equals(obj);
    }

}