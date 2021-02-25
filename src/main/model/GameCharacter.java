package model;

/**
 * Represents anything that has health, armour, attack, and a name.
 */

public abstract class GameCharacter extends Entity {

    protected String name;
    protected int health;
    protected int armour;
    protected int attackDamage;
    protected boolean isAlive;

    // EFFECTS: Constructor for character
    //           - name, health, armour, attack damage, position
    public GameCharacter(String name, int health, int armour, int attackDamage, int posX, int posY) {
        super(posX, posY);
        this.name = name;
        this.health = health;
        this.armour = armour;
        this.attackDamage = attackDamage;
        this.isAlive = true;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getArmour() {
        return armour;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    // TODO: need to account damage with armour...
    // MODIFIES: this
    // EFFECTS: reduce the character's health by the damage taken
    //             - If health reaches below or equal to 0, set isAlive to false
    public void takeDamage(int dmg) {
        if (health > 0) {
            health -= dmg;
            if (health <= 0) {
                isAlive = false;
            }
        }
    }

    // MODIFIES: otherGameCharacter
    // EFFECTS: deals current attack damage to otherGameCharacter
    public void attack(GameCharacter otherGameCharacter) {
        otherGameCharacter.takeDamage(attackDamage);
    }
}
