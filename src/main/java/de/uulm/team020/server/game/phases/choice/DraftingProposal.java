package de.uulm.team020.server.game.phases.choice;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.util.AbstractPair;

/**
 * Contains a generated Proposal to be send with the an Item Choice Request
 * 
 * @author Florian Sihler
 * @version 1.0, 04/08/2020
 */
public class DraftingProposal extends AbstractPair<List<GadgetEnum>, List<UUID>> {

    /**
     * Initialize a new Proposal, will Gadgets Key and Characters both to an empty
     * List.
     */
    public DraftingProposal() {
        super(new LinkedList<>(), new LinkedList<>());
    }

    /**
     * Initialize a new Proposal
     * 
     * @param gadgets    the gadgets to choose
     * @param characters the characters to choose
     */
    public DraftingProposal(List<GadgetEnum> gadgets, List<UUID> characters) {
        super(gadgets, characters);
    }

    public List<GadgetEnum> getGadgets() {
        return this.getLeft();
    }

    public void setGadgets(List<GadgetEnum> gadgets) {
        this.setLeft(gadgets);
    }

    public void addGadget(GadgetEnum gadget) {
        this.getLeft().add(gadget);
    }

    public List<UUID> getCharacters() {
        return this.getRight();
    }

    public void setCharacters(List<UUID> characters) {
        this.setRight(characters);
    }

    public void addCharacter(UUID character) {
        this.getRight().add(character);
    }

    @Override
    public String toString() {
        return "DraftingProposal [gadgets=" + getLeft() + ", characters=" + getRight() + "]";
    }
}