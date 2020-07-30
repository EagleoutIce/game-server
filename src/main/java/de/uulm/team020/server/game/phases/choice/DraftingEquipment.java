package de.uulm.team020.server.game.phases.choice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;

/**
 * Just a simple typedef to allow easier use in code
 * 
 * @author Florian Sihler
 * @version 1.0, 04/19/2020
 */
public class DraftingEquipment extends HashMap<UUID, List<GadgetEnum>> {

    private static final long serialVersionUID = -4049909625551986058L;

    /**
     * Just construct a new map for emplacing data
     */
    public DraftingEquipment() {
        super();
    }

    public DraftingEquipment(Map<UUID, List<GadgetEnum>> data) {
        super(data);
    }

}