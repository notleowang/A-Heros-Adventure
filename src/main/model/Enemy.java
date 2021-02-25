package model;

import org.json.JSONObject;
import persistence.Writable;

/**
 * An enemy is an Entity that can attack the Player.
 * All enemies have just attack, therefore no need for abstract class (yet).
 */

public class Enemy extends GameCharacter implements Writable {

    // Construct an enemy with a name, health, armour, atckdamage, position, and whether it's alive
    public Enemy(String name, int health, int armour, int attackDamage, int posX, int posY, Boolean isAlive) {
        super(name, health, armour, attackDamage, posX, posY);
        this.isAlive = isAlive;
    }

    // EFFECTS: converts enemy to a jsonObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("health", health);
        json.put("attackDamage", attackDamage);
        json.put("armour", armour);
        json.put("isAlive", isAlive);
        return json;
    }
}
