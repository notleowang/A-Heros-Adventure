package persistence;

import model.Environment;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Environment e = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyEnvironment() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyEnvironment.json");
        try {
            Environment e = reader.read();
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
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralPlayer() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralEnvironment.json");
        try {
            Environment e = reader.read();
            assertEquals("Joe", e.getPlayer().getName());
            assertEquals(75, e.getPlayer().getHealth());
            assertEquals(0, e.getPlayer().getArmour());
            assertEquals(5, e.getPlayer().getAttackDamage()); // Player atk = 5, Sword atk = 100
            assertFalse(e.getDarkSpirit().getIsAlive());
            assertTrue(e.getPlayer().getIsAlive());
            assertFalse(e.getZombie().getIsAlive());
            assertFalse(e.getGolem().getIsAlive());
            assertTrue(e.getDragon().getIsAlive());
            assertTrue(e.isGotDagger());
            assertTrue(e.isGotSword());
            assertTrue(e.isGotSword2());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
