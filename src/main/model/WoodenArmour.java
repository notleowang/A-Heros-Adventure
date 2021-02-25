package model;

/**
 * Wooden Armour is a Collectible that if in inventory, provides additional armour.
 */

public class WoodenArmour extends Armour {

    // Constructs a wooden armour collectible with a base armourArmour
    public WoodenArmour(String nm, int armourArmour, int posX, int posY) {
        super(nm, armourArmour, posX, posY);
    }

}
