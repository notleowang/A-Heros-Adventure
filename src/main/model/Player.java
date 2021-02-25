package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player that interacts with everything else.
 * The player has health, an inventory, items.
 */

public class Player extends GameCharacter implements Writable {

    private List<Collectible> inventory;
    public static final int MAX_INVENTORY_SIZE = 3;
    public static final int MAX_HEALTH = 100;

    // Create a new player with name, health, and attackdamage
    //      Constructed with:
    //      - 0 armour
    //      - empty inventory
    //      - empty list of buffs
    public Player(String name, int health, int attackDamage) {
        super(name, health, 0, attackDamage, 0, 0);
        this.armour = 0;                        //give player 0 base armour
        this.inventory = new ArrayList<>();     //give player empty inventory
    }

    // GETTERS:
    public List<Collectible> getInventory() {
        return inventory;
    }

    // EFFECTS: produce true if inventory is full, otherwise false
    public boolean isInventoryFull() {
        return inventory.size() == MAX_INVENTORY_SIZE;
    }

    // MODIFIES: this
    // EFFECTS: adds an item that was picked up to the inventory
    public void addCollectible(Collectible collectible) {
        if (inventory.size() < MAX_INVENTORY_SIZE) {
            inventory.add(collectible);
        }
    }

    // MODIFIES: this
    // EFFECTS: remove an item from inventory
    public void removeCollectible(Collectible collectible) {
        inventory.remove(collectible);
    }

    // MODIFIES: this
    // EFFECTS: brings player health back to MAX_HEALTH (default 100)
    public void heal() {
        health = MAX_HEALTH;
    }

    // EFFECTS: converts Player to a jsonObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("health", health);
        json.put("attackDamage", attackDamage);
        json.put("armour", armour);
        json.put("collectibles", collectiblesToJson());
        return json;
    }

    // EFFECTS: converts player's collectibles into jsonObjects
    private JSONArray collectiblesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Collectible c : inventory) {
            jsonArray.put(c.toJson());
        }

        return jsonArray;
    }

}

