package model;

import org.json.JSONObject;
import persistence.Writable;

/**
 * Represents an "environment".
 * - an environment contains all the elements in the game.
 * - the use of this class is solely for saving and loading.
 * - if an additional item wants to be "saved" or "loaded", it must be added to the environment as a field in order
 * to properly save/load it.
 */

public class Environment implements Writable {
    private Player player;
    private Enemy darkSpirit;
    private Enemy zombie;
    private Enemy dragon;
    private Enemy golem;
    private Sword sword;
    private Sword sword2;
    private Dagger dagger;
    private Armour armour;
    private boolean gotDagger;
    private boolean gotSword;
    private boolean gotSword2;

    // Environment constructor
    public Environment(Player player,
                       Enemy darkSpirit,
                       Enemy zombie,
                       Enemy golem,
                       Enemy dragon,
                       Sword sword,
                       Sword sword2,
                       Dagger dagger,
                       Armour armour,
                       Boolean gotSword,
                       Boolean gotDagger,
                       Boolean gotSword2) {
        this.player = player;
        this.darkSpirit = darkSpirit;
        this.zombie = zombie;
        this.golem = golem;
        this.dragon = dragon;
        this.sword = sword;
        this.sword2 = sword2;
        this.dagger = dagger;
        this.armour = armour;
        this.gotSword = gotSword;
        this.gotDagger = gotDagger;
        this.gotSword2 = gotSword2;
    }

    // GETTERS:

    public Player getPlayer() {
        return player;
    }

    public Enemy getDarkSpirit() {
        return darkSpirit;
    }

    public Enemy getZombie() {
        return zombie;
    }

    public Enemy getDragon() {
        return dragon;
    }

    public Enemy getGolem() {
        return golem;
    }

    public Sword getSword() {
        return sword;
    }

    public Sword getSword2() {
        return sword2;
    }

    public Dagger getDagger() {
        return dagger;
    }

    public Armour getArmour() {
        return armour;
    }

    public boolean isGotDagger() {
        return gotDagger;
    }

    public boolean isGotSword() {
        return gotSword;
    }

    public boolean isGotSword2() {
        return gotSword2;
    }

    // SETTERS:

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setDarkSpirit(Enemy darkSpirit) {
        this.darkSpirit = darkSpirit;
    }

    public void setZombie(Enemy zombie) {
        this.zombie = zombie;
    }

    public void setDragon(Enemy dragon) {
        this.dragon = dragon;
    }

    public void setGolem(Enemy golem) {
        this.golem = golem;
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }

    public void setSword2(Sword sword2) {
        this.sword2 = sword2;
    }

    public void setDagger(Dagger dagger) {
        this.dagger = dagger;
    }

    public void setArmour(Armour armour) {
        this.armour = armour;
    }

    public void setGotDagger(boolean gotDagger) {
        this.gotDagger = gotDagger;
    }

    public void setGotSword(boolean gotSword) {
        this.gotSword = gotSword;
    }

    public void setGotSword2(boolean gotSword2) {
        this.gotSword2 = gotSword2;
    }

    // EFFECTS: converts Environment to a jsonObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("player", player.toJson());
        json.put("darkSpirit", darkSpirit.toJson());
        json.put("zombie", zombie.toJson());
        json.put("dragon", dragon.toJson());
        json.put("golem", golem.toJson());
        json.put("sword", sword.toJson());
        json.put("sword2", sword2.toJson());
        json.put("dagger", dagger.toJson());
        json.put("armour", armour.toJson());
        json.put("gotDagger", gotDagger);
        json.put("gotSword", gotSword);
        json.put("gotSword2", gotSword2);
        return json;
    }
}
