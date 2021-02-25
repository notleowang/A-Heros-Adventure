package model;

import org.json.JSONObject;
import persistence.Writable;

/**
 * A Weapon is a Collectible that if in inventory, provides additional attack.
 */

public class Weapon extends Collectible implements Writable {

    protected int weaponDamage;

    // Constructs a Weapon with a base attack damage, name, and position
    public Weapon(String nm, int weaponDamage, int posX, int posY) {
        super(nm, posX, posY);
        this.weaponDamage = weaponDamage;
    }

    // GETTERS:
    public int getWeaponDamage() {
        return weaponDamage;
    }

    @Override
    // MODIFIES: player
    // EFFECTS: adds collectible to player's inventory and increases player's attackDamage
    //            - adds the additional attack damage from the weapon to the player's attackDamage
    public void addCollectible(Player player) {
        player.addCollectible(this);
        player.attackDamage += weaponDamage;
    }

    @Override
    // MODIFIES: player
    // EFFECTS: removes collectible to player's inventory and decreases player's attackDamage
    //            - subtracts the additional attack damage from the weapon from the player's attackDamage
    public void removeCollectible(Player player) {
        player.getInventory().remove(this);
        player.attackDamage -= weaponDamage;
    }

    // EFFECTS: converts Weapons to a jsonObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("nm", nm);
        json.put("weaponDamage", weaponDamage);
        return json;
    }

}
