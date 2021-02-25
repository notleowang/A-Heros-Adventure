package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameCharacterTest {
    GameCharacter testGameCharacter;

    @BeforeEach
    void setup() {
        testGameCharacter = new Enemy("Ghost", 100, 0, 10, 0, 0,
                true);
    }

    @Test
    void testConstructor() {
        assertEquals(testGameCharacter.getName(), "Ghost");
        assertEquals(testGameCharacter.getHealth(), 100);
        assertEquals(testGameCharacter.getArmour(), 0);
        assertEquals(testGameCharacter.getAttackDamage(), 10);
        assertTrue(testGameCharacter.getIsAlive());
    }

    @Test
    void testTakeDamage() {
        testGameCharacter.takeDamage(20);
        assertEquals(testGameCharacter.getHealth(), 80);
        assertTrue(testGameCharacter.getIsAlive());

        // Additional test for taking more than one hit
        testGameCharacter.takeDamage(40);
        assertEquals(testGameCharacter.getHealth(), 40);
        assertTrue(testGameCharacter.getIsAlive());
    }

    @Test
    void testTakeDamageAndDie() {
        testGameCharacter.takeDamage(100);
        assertEquals(testGameCharacter.getHealth(), 0);
        assertFalse(testGameCharacter.getIsAlive());

    }

    // Testing for negative ints
    @Test
    void testTakeAlotOfDamage() {
        testGameCharacter.takeDamage(400);
        assertEquals(testGameCharacter.getHealth(), -300);
        assertFalse(testGameCharacter.getIsAlive());
    }

    @Test
    void testAttackOther() {
        GameCharacter otherGameCharacter = new Player("c", 100, 0);
        testGameCharacter.attack(otherGameCharacter);
        assertEquals(otherGameCharacter.getHealth(), 90);
    }

}
