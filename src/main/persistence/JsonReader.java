package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.*;
import org.json.*;

/*
 * Code is heavily inspired from JsonSerializationDemo
 * JsonReader is a class that reads a source (file).
 * - Specifically reads an environment and loads it in.
 */

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads player from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Environment read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseEnvironment(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses environment from JSON object and returns it
    private Environment parseEnvironment(JSONObject jsonObject) {
        Player player = parsePlayer(jsonObject.getJSONObject("player"));
        Enemy darkSpirit = parseEnemy(jsonObject.getJSONObject("darkSpirit"));
        Enemy zombie = parseEnemy(jsonObject.getJSONObject("zombie"));
        Enemy golem = parseEnemy(jsonObject.getJSONObject("golem"));
        Enemy dragon = parseEnemy(jsonObject.getJSONObject("dragon"));
        Sword sword = parseSword(jsonObject.getJSONObject("sword"));
        Sword sword2 = parseSword(jsonObject.getJSONObject("sword2"));
        Dagger dagger = parseDagger(jsonObject.getJSONObject("dagger"));
        Armour armour = parseArmour(jsonObject.getJSONObject("armour"));
        Boolean gotSword = jsonObject.getBoolean("gotSword");
        Boolean gotDagger = jsonObject.getBoolean("gotDagger");
        Boolean gotSword2 = jsonObject.getBoolean("gotSword2");
        Environment e;
        e = new Environment(player, darkSpirit, zombie, golem, dragon, sword, sword2, dagger, armour, gotSword,
                gotDagger, gotSword2);
        return e;
    }

    // EFFECTS: parses enemy from JSON object and returns it
    private Enemy parseEnemy(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int health = jsonObject.getInt("health");
        int attackDamage = jsonObject.getInt("attackDamage");
        int armour = jsonObject.getInt("armour");
        Boolean isAlive = jsonObject.getBoolean("isAlive");
        return new Enemy(name, health, armour, attackDamage, 0, 0, isAlive);
    }

    // EFFECTS: parses player from JSON object and returns it
    private Player parsePlayer(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int health = jsonObject.getInt("health");
        int attackDamage = jsonObject.getInt("attackDamage");
        Player p = new Player(name, health, attackDamage);
        addCollectibles(p, jsonObject);
        return p;
    }

    // EFFECTS: parses dagger from JSON object and returns it
    private Dagger parseDagger(JSONObject jsonObject) {
        String name = jsonObject.getString("nm");
        int weaponDamage = jsonObject.getInt("weaponDamage");
        return new Dagger(name, weaponDamage, 0, 0);
    }

    // EFFECTS: parses armour from JSON object and returns it
    private Armour parseArmour(JSONObject jsonObject) {
        String name = jsonObject.getString("nm");
        int armourArmour = jsonObject.getInt("armourArmour");
        return new Armour(name, armourArmour, 0, 0);
    }

    // EFFECTS: parses sword from JSON object and returns it
    private Sword parseSword(JSONObject jsonObject) {
        String name = jsonObject.getString("nm");
        int weaponDamage = jsonObject.getInt("weaponDamage");
        return new Sword(name, weaponDamage, 0, 0);
    }

    // MODIFIES: p
    // EFFECTS: parses Collectibles from JSON object and adds them to player
    private void addCollectibles(Player p, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("collectibles");
        for (Object json : jsonArray) {
            JSONObject nextCollectible = (JSONObject) json;
            addWeapon(p, nextCollectible);
        }
    }

    // MODIFIES: p
    // EFFECTS: parses weapon from JSON object and adds them to player
    private void addWeapon(Player p, JSONObject jsonObject) {
        String name = jsonObject.getString("nm");
        int weaponDamage = jsonObject.getInt("weaponDamage");
        Weapon w = new Weapon(name, weaponDamage, 0, 0);
        p.addCollectible(w);
    }
}
