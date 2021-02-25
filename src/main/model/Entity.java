package model;

/**
 * An Entity is anything in the game that has a position (according to an article?)
 * - the player
 * - items
 * - enemies
 * - obstacles
 */

public abstract class Entity {
    protected int posX;
    protected int posY;

    // Constructs an entity with a position
    public Entity(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    // Getters (not sure if I need to test position right now?)

/*
    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }
*/

}
