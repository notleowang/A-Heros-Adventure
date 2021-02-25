package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnvironmentTest {
    Environment testEnvironment;

    Player p = new Player("Bob", 100, 5);
    Enemy e1 = new Enemy("e1", 1, 1, 1, 0, 0, true);
    Enemy e2 = new Enemy("e2", 2, 2, 2, 0, 0, true);
    Enemy e3 = new Enemy("e3", 3, 3, 3, 0, 0, true);
    Enemy e4 = new Enemy("e4", 4, 4, 4, 0, 0, true);
    Sword s1 = new Sword("s1", 100, 0, 0);
    Sword s2 = new Sword("s2", 200, 0, 0);
    Dagger d1 = new Dagger("d1", 5, 0, 0);
    Armour a1 = new Armour("a1", 5, 0, 0);

    @BeforeEach
    void setup() {
        testEnvironment = new Environment(p, e1, e2, e3, e4, s1, s2, d1, a1, false, false,
                false);
    }

    @Test
    void testSetters() {
        testEnvironment.setPlayer(p);
        testEnvironment.setDarkSpirit(e1);
        testEnvironment.setZombie(e2);
        testEnvironment.setGolem(e3);
        testEnvironment.setDragon(e4);
        testEnvironment.setSword(s1);
        testEnvironment.setSword2(s2);
        testEnvironment.setDagger(d1);
        testEnvironment.setArmour(a1);
        testEnvironment.setGotSword(true);
        testEnvironment.setGotDagger(true);
        testEnvironment.setGotSword2(true);
    }

    @Test
    void testAllGetters() {
        // Name Getters
        assertEquals("Bob", testEnvironment.getPlayer().getName());
        assertEquals("e1", testEnvironment.getDarkSpirit().getName());
        assertEquals("e2", testEnvironment.getZombie().getName());
        assertEquals("e3", testEnvironment.getGolem().getName());
        assertEquals("e4", testEnvironment.getDragon().getName());
        assertEquals("s1", testEnvironment.getSword().getName());
        assertEquals("s2", testEnvironment.getSword2().getName());
        assertEquals("d1", testEnvironment.getDagger().getName());
        assertEquals("a1", testEnvironment.getArmour().getName());

        // Health Getters
        assertEquals(100, testEnvironment.getPlayer().getHealth());
        assertEquals(1, testEnvironment.getDarkSpirit().getHealth());
        assertEquals(2, testEnvironment.getZombie().getHealth());
        assertEquals(3, testEnvironment.getGolem().getHealth());
        assertEquals(4, testEnvironment.getDragon().getHealth());

        // AttackDamage Getters
        assertEquals(5, testEnvironment.getPlayer().getAttackDamage());
        assertEquals(1, testEnvironment.getDarkSpirit().getAttackDamage());
        assertEquals(2, testEnvironment.getZombie().getAttackDamage());
        assertEquals(3, testEnvironment.getGolem().getAttackDamage());
        assertEquals(4, testEnvironment.getDragon().getAttackDamage());

        // Armour Getters
        assertEquals(1, testEnvironment.getDarkSpirit().getArmour());
        assertEquals(2, testEnvironment.getZombie().getArmour());
        assertEquals(3, testEnvironment.getGolem().getArmour());
        assertEquals(4, testEnvironment.getDragon().getArmour());

        // isAlive?
        assertTrue(testEnvironment.getDarkSpirit().getIsAlive());
        assertTrue(testEnvironment.getZombie().getIsAlive());
        assertTrue(testEnvironment.getGolem().getIsAlive());
        assertTrue(testEnvironment.getDragon().getIsAlive());
    }

}
