package de.uulm.team020.server.game.phases.choice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;

/**
 * This datatype holds the drafting-item choices of a player. It will throw
 * exceptions if the choice is illegal for any reason.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/10/2020
 */
public class DraftingChoice {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Drafting");

    private static final int MAX_CHARACTERS = 4;
    private static final int MAX_GADGETS = 6;
    private static final int MAX_ITEMS = 8;

    private final String name;
    private List<UUID> characters;
    private List<GadgetEnum> gadgets;

    /**
     * Construct a new drafting-choice object
     * 
     * @param name The name for references in error-management
     */
    public DraftingChoice(String name) {
        this.characters = new ArrayList<>(MAX_CHARACTERS);
        this.gadgets = new ArrayList<>(MAX_GADGETS);
        this.name = name;
    }

    /**
     * Add a new character to the choices. This one will not forbid adding an
     * already existing character if duplicate characters are forbidden.
     * 
     * @param characterId The character to add
     * 
     * @throws DraftingChoiceException If it is not allowed to add further
     *                                 characters
     */
    public void addCharacter(UUID characterId) throws DraftingChoiceException {
        if (itemsLeft() == 0) {
            throw new DraftingChoiceException(name, "items", MAX_ITEMS);
        } else if (charactersLeft() == 0) {
            throw new DraftingChoiceException(name, "characters", MAX_CHARACTERS);
        }
        magpie.writeInfo("Player '" + name + "' chose Character: " + characterId, "Choice");
        characters.add(characterId);
    }

    /**
     * Add a new gadget to the choices. This one will not forbid adding an already
     * existing gadget.
     * 
     * @param gadget The gadget to add
     * 
     * @throws DraftingChoiceException If it is not allowed to add further gadgets
     */
    public void addGadget(GadgetEnum gadget) throws DraftingChoiceException {
        if (itemsLeft() == 0) {
            throw new DraftingChoiceException(name, "items", MAX_ITEMS);
        } else if (gadgetsLeft() == 0) {
            throw new DraftingChoiceException(name, "gadgets", MAX_GADGETS);
        }

        magpie.writeInfo("Player '" + name + "' chose Gadget: " + gadget, "Choice");
        gadgets.add(gadget);
    }

    public List<UUID> getCharacters() {
        return characters;
    }

    public List<GadgetEnum> getGadgets() {
        return gadgets;
    }

    /**
     * Returns the number of items the player has left to choose
     * 
     * @return Number in [0, {@value #MAX_ITEMS}] about the number of items the
     *         player has left to choose.
     * 
     * @see #itemsChosen()
     */
    public int itemsLeft() {
        return MAX_ITEMS - (characters.size() + gadgets.size());
    }

    /**
     * Returns the number of items the player has already chosen
     * 
     * @return Number in [0, {@value #MAX_ITEMS}] about the number of items the
     *         player has already chosen
     * 
     * @see #itemsLeft()
     */
    public int itemsChosen() {
        return characters.size() + gadgets.size();
    }

    /**
     * Number of characters the player is allowed to chose (based on the current
     * choice).
     * 
     * @return Number of characters the player has left.
     * 
     * @see #gadgetsLeft()
     */
    public int charactersLeft() {
        return Math.min(itemsLeft(), MAX_CHARACTERS - characters.size());
    }

    /**
     * Number of gadgets the player is allowed to chose (based on the current
     * choice).
     * 
     * @return Number of gadgets the player has left.
     * 
     * @see #charactersLeft()
     */
    public int gadgetsLeft() {
        return Math.min(itemsLeft(), MAX_GADGETS - gadgets.size());
    }

    @Override
    public String toString() {
        return "DraftingChoice [characters=" + characters + ", gadgets=" + gadgets + ", name=" + name + "]";
    }

}