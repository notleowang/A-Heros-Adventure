package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectibleTest {

    Weapon testSword;
    Weapon testDagger;
    Armour testWoodenArmour;
    Player testPlayer;

    @BeforeEach
    void setup() {
        testSword = new Sword("a", 10, 0, 0);
        testDagger = new Dagger("b", 5, 0, 0);
        testPlayer = new Player("Bob", 100, 10);
        testWoodenArmour = new WoodenArmour("c", 5, 0, 0);
    }

    @Test
    void testGetName() {
        assertEquals(testSword.getName(), "a");
    }

    // Check for:
    //      - Player attackDamage increase
    //      - Inventory contains the weapon
    //      - Inventory size increased
    @Test
    void testAddSword() {
        testSword.addCollectible(testPlayer);
        assertEquals(10, testSword.getWeaponDamage());
        assertEquals(testPlayer.getAttackDamage(), 20);
        assertTrue(testPlayer.getInventory().contains(testSword));
        assertEquals(testPlayer.getInventory().size(), 1);
    }

    @Test
    void testAddDagger() {
        testDagger.addCollectible(testPlayer);
        assertEquals(testPlayer.getAttackDamage(), 15);
        assertTrue(testPlayer.getInventory().contains(testDagger));
        assertEquals(testPlayer.getInventory().size(), 1);
    }

    @Test
    void testAddTwoWeapons() {
        testSword.addCollectible(testPlayer);
        testDagger.addCollectible(testPlayer);
        assertEquals(testPlayer.getAttackDamage(), 25);
        assertTrue(testPlayer.getInventory().contains(testSword));
        assertTrue(testPlayer.getInventory().contains(testDagger));
        assertEquals(testPlayer.getInventory().size(), 2);
    }

    @Test
    void testAddWoodenArmour() {
        testWoodenArmour.addCollectible(testPlayer);
        assertEquals(testPlayer.getArmour(), 5);
        assertTrue(testPlayer.getInventory().contains(testWoodenArmour));
        assertEquals(testPlayer.getInventory().size(), 1);
    }

    @Test
    void testRemoveSword() {
        testSword.addCollectible(testPlayer);
        assertEquals(testPlayer.getAttackDamage(), 20);
        assertTrue(testPlayer.getInventory().contains(testSword));
        assertEquals(testPlayer.getInventory().size(), 1);

        testSword.removeCollectible(testPlayer);
        assertEquals(testPlayer.getAttackDamage(), 10);
        assertTrue(testPlayer.getInventory().isEmpty());
    }

    @Test
    void testRemoveArmour() {
        testWoodenArmour.addCollectible(testPlayer);
        assertEquals(testPlayer.getArmour(), 5);
        assertTrue(testPlayer.getInventory().contains(testWoodenArmour));
        assertEquals(testPlayer.getInventory().size(), 1);

        testWoodenArmour.removeCollectible(testPlayer);
        assertEquals(testPlayer.getArmour(), 0);
        assertTrue(testPlayer.getInventory().isEmpty());
    }
}
