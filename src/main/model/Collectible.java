package model;

import persistence.Writable;

/**
 * A collectable is something that the Player can pick up and put in their inventory
 * - Weapons
 * - Armour
 */

public abstract class Collectible extends Item implements Writable {

    protected String nm;

    // EFFECTS: Constructs a collectible with a name at the given X and Y position
    public Collectible(String nm, int posX, int posY) {
        super(nm, posX, posY);
        this.nm = nm;
    }

    // GETTERS:
    public String getName() {
        return nm;
    }

    public abstract void addCollectible(Player player);

    public abstract void removeCollectible(Player player);

}
