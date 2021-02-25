package model;

/**
 * Represents anything that can be picked up by the player.
 *      - Weapons
 *      - Armour
 *      - Future Implementations:
 *              - Buffs? (Separate it into a different Abstract Class; Don't want buffs in inventory
 *              and Collectibles adds Items to Inventory)
 */

public abstract class Item extends Entity {

    protected String nm;

    // EFFECTS: creates an item at the given position
    public Item(String nm, int posX, int posY) {
        super(posX, posY);
        this.nm = nm;
    }

}
