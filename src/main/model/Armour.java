package model;

import org.json.JSONObject;
import persistence.Writable;

/**
 * Armour is a Collectible that if in inventory, provides additional armour
 */

public class Armour extends Collectible implements Writable {

    protected int armourArmour;

    // Constructs a new armour with a given position
    public Armour(String nm, int armourArmour, int posX, int posY) {
        super(nm, posX, posY);
        this.armourArmour = armourArmour;
    }

    @Override
    // MODIFIES: player
    // EFFECTS: puts armour into player inventory and increases armour
    public void addCollectible(Player player) {
        player.addCollectible(this);
        player.armour += armourArmour;
    }

    @Override
    // MODIFIES: player
    // EFFECTS: removes armour from player inventory and decreases armour
    public void removeCollectible(Player player) {
        player.getInventory().remove(this);
        player.armour -= armourArmour;
    }

    // EFFECTS: converts armour to a jsonObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("nm", nm);
        json.put("armourArmour", armourArmour);
        return json;
    }

}
