package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {

    Player player;
    Enemy darkSpirit;
    Enemy zombie;
    Enemy golem;
    Enemy dragon;
    Sword sword;
    Sword sword2;
    Dagger dagger;
    Armour armour;
    Boolean gotDagger;
    Boolean gotSword;
    Boolean gotSword2;

    @BeforeEach
    void setup() {
        player = new Player("Player", 100, 5);
        darkSpirit = new Enemy("DarkSpirit", 1, 0, 5, 0, 0, true);
        zombie = new Enemy("Zombie", 10, 0, 10, 0, 0, true);
        golem = new Enemy("Golem", 20, 0, 20, 0, 0, true);
        dragon = new Enemy("Dragon", 100, 0, 25, 0, 0, true);
        sword = new Sword("Sword", 100, 0, 0);
        sword2 = new Sword("Sword2", 20, 0, 0);
        dagger = new Dagger("Dagger", 5, 0, 0);
        armour = new Armour("Armour", 5, 0, 0);
        gotDagger = false;
        gotSword = false;
        gotSword2 = false;
    }

    @Test
    void testWriterInvalidFile() {
        try {
            Environment e = new Environment(player,
                    darkSpirit,
                    zombie,
                    golem,
                    dragon,
                    sword,
                    sword2,
                    dagger,
                    armour,
                    gotSword,
                    gotDagger,
                    gotSword2);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyEnvironment() {
        try {
            Environment e = new Environment(player,
                    darkSpirit,
                    zombie,
                    golem,
                    dragon,
                    sword,
                    sword2,
                    dagger,
                    armour,
                    gotSword,
                    gotDagger,
                    gotSword2);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyEnvironment.json");
            writer.open();
            writer.write(e);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyEnvironment.json");
            e = reader.read();
            assertEquals("Player", e.getPlayer().getName());
            assertEquals(100, e.getPlayer().getHealth());
            assertEquals(0, e.getPlayer().getArmour());
            assertEquals(5, e.getPlayer().getAttackDamage());
            assertTrue(e.getDarkSpirit().getIsAlive());
            assertTrue(e.getZombie().getIsAlive());
            assertTrue(e.getGolem().getIsAlive());
            assertTrue(e.getDragon().getIsAlive());
            assertFalse(e.isGotDagger());
            assertFalse(e.isGotSword());
            assertFalse(e.isGotSword2());
            assertEquals(0, e.getPlayer().getInventory().size());
            assertTrue(e.getPlayer().getInventory().isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralEnvironment() {
        try {
            Environment e = new Environment(player,
                    darkSpirit,
                    zombie,
                    golem,
                    dragon,
                    sword,
                    sword2,
                    dagger,
                    armour,
                    gotSword,
                    gotDagger,
                    gotSword2);
            e.getPlayer().attack(darkSpirit);
            e.getPlayer().takeDamage(25);
            e.getPlayer().addCollectible(e.getSword());
            e.getPlayer().addCollectible(e.getDagger());
            // e.getSword().putWeaponInInventory(e.getPlayer());
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralEnvironment.json");
            writer.open();
            writer.write(e);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralEnvironment.json");
            e = reader.read();
            assertEquals("Player", e.getPlayer().getName());
            assertEquals(75, e.getPlayer().getHealth());
            assertEquals(0, e.getPlayer().getArmour());
            assertEquals(5, e.getPlayer().getAttackDamage()); // Player atk = 5, Sword atk = 100
            assertFalse(e.getDarkSpirit().getIsAlive());
            assertTrue(e.getPlayer().getIsAlive());
            assertTrue(e.getZombie().getIsAlive());
            assertTrue(e.getGolem().getIsAlive());
            assertTrue(e.getDragon().getIsAlive());
            assertFalse(e.isGotDagger());
            assertFalse(e.isGotSword());
            assertFalse(e.isGotSword2());
            List<Collectible> inventory = e.getPlayer().getInventory();
            assertEquals(2, e.getPlayer().getInventory().size());
            checkInventory("Sword", 100, inventory.get(0));
            checkInventory("Dagger", 5, inventory.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
