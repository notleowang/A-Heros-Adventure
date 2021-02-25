package model;

/**
 * A sword is a weapon that provides the player with more attackDamage if picked up.
 */

public class Sword extends Weapon {

    // Constructs a sword collectible with a base attack damage, name, and position
    public Sword(String nm, int weaponDamage, int posX, int posY) {
        super(nm, weaponDamage, posX, posY);
    }



}