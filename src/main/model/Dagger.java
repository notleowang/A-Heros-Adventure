package model;

/**
 * A dagger is a weapon that provides the player with more attackDamage if picked up.
 */

public class Dagger extends Weapon {

    // Constructs a dagger collectible with a base attack damage, name, and position
    public Dagger(String nm, int weaponDamage, int posX, int posY) {
        super(nm, weaponDamage, posX, posY);
    }

}
