package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player testPlayer;
    GameCharacter enemy;
    Collectible c1;
    Collectible c2;
    Collectible c3;
    Collectible c4;
    Sword s1;
    Dagger d1;
    WoodenArmour a1;


    @BeforeEach
    void setup() {
        enemy = new Enemy("e1", 20, 0, 5, 0, 0, true);
        testPlayer = new Player("Leo", 100, 1);
        s1 = new Sword("a", 10, 0, 0);
        d1 = new Dagger("a", 5, 0, 0);
        a1 = new WoodenArmour("a", 5, 0, 0);
    }

    @Test
    void testPlayerConstructor() {
        Player p = new Player("Bob", 100, 1);
        assertEquals(p.getName(), "Bob");
        assertEquals(p.getHealth(), 100);
        assertEquals(p.getArmour(), 0);
        assertEquals(p.getAttackDamage(), 1);
        assertTrue(testPlayer.getInventory().isEmpty());
        assertTrue(testPlayer.getIsAlive());
    }

    @Test
    void testAddOneCollectible() {
        testPlayer.addCollectible(c1);
        assertEquals(testPlayer.getInventory().size(), 1);
        assertTrue(testPlayer.getInventory().contains(c1));
    }

    @Test
    void testAddTwoCollectible() {
        testPlayer.addCollectible(c1);
        testPlayer.addCollectible(c2);
        assertEquals(testPlayer.getInventory().size(), 2);
        assertTrue(testPlayer.getInventory().contains(c1));
        assertTrue(testPlayer.getInventory().contains(c2));
        assertFalse(testPlayer.isInventoryFull());
    }

    @Test
    void testRemoveCollectible() {
        testPlayer.addCollectible(c1);
        assertEquals(testPlayer.getInventory().size(), 1);
        assertTrue(testPlayer.getInventory().contains(c1));

        testPlayer.removeCollectible(c1);
        assertTrue(testPlayer.getInventory().isEmpty());
    }

    @Test
    void testAddMoreThanMaxCollectibles() {
        testPlayer.addCollectible(c1);
        testPlayer.addCollectible(c2);
        testPlayer.addCollectible(c3);
        assertTrue(testPlayer.isInventoryFull());
        testPlayer.addCollectible(c4);
        assertEquals(testPlayer.getInventory().size(), 3);
        assertTrue(testPlayer.getInventory().contains(c1));
        assertTrue(testPlayer.getInventory().contains(c2));
        assertTrue(testPlayer.getInventory().contains(c3));
        assertTrue(testPlayer.isInventoryFull());
    }

    @Test
    void testIsInventoryFull() {
        testPlayer.addCollectible(c1);
        assertFalse(testPlayer.isInventoryFull());
        testPlayer.addCollectible(c2);
        assertFalse(testPlayer.isInventoryFull());
        testPlayer.addCollectible(c3);
        assertTrue(testPlayer.isInventoryFull());
    }

    @Test
    void testAttackWithNoWeapon() {
        assertTrue(testPlayer.getInventory().isEmpty());
        testPlayer.attack(enemy);
        // Player attackDamage is 1
        assertEquals(enemy.getHealth(), 19);
        assertTrue(enemy.getIsAlive());
    }

    @Test
    void testAttackWithSword() {
        s1.addCollectible(testPlayer);
        assertTrue(testPlayer.getInventory().contains(s1));
        testPlayer.attack(enemy);
        // Play attackDamage is now 10 + 1 = 11
        assertEquals(enemy.getHealth(), 9);
        assertTrue(enemy.getIsAlive());
    }

    @Test
    void testKillEnemy() {
        s1.addCollectible(testPlayer);
        testPlayer.attack(enemy);
        testPlayer.attack(enemy);
        assertEquals(enemy.getHealth(), -2);
        assertFalse(enemy.getIsAlive());
    }

    @Test
    void testAttackAlreadyDeadEnemy() {
        s1.addCollectible(testPlayer);
        testPlayer.attack(enemy);
        testPlayer.attack(enemy);
        testPlayer.attack(enemy);
        assertFalse(enemy.getIsAlive());
        // Should still produce -2 since isAlive is no longer true
        assertEquals(enemy.getHealth(), -2);
        assertFalse(enemy.getIsAlive());
    }

    @Test
    void testEnemyAttackPlayer() {
        GameCharacter e2 = new Enemy("strong zombie", 100, 0, 50, 0, 0,
                true);
        e2.attack(testPlayer);
        assertEquals(testPlayer.getHealth(), 50);
        assertTrue(testPlayer.getIsAlive());
        e2.attack(testPlayer);
        assertEquals(testPlayer.getHealth(), 0);
        assertFalse(testPlayer.getIsAlive());
    }

    @Test
    void testHeal() {
        testPlayer.takeDamage(10);
        assertEquals(testPlayer.getHealth(), 90);
        testPlayer.heal();
        assertEquals(testPlayer.getHealth(), Player.MAX_HEALTH); //100
    }
}