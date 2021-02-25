package persistence;

import model.Collectible;

/*
 * Code is taken from JsonSerializationDemo
 */

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkInventory(String name, int attackDamage, Collectible collectible) {
        assertEquals(name, collectible.getName());
    }
}
