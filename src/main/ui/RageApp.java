package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/*
 * RageApp is the previous console-ui class.
 */

// RageApp Game
public class RageApp {
    private Environment environment;
    private static final String JSON_GAME = "./data/player.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Player player;
    private Enemy darkSpirit;
    private Enemy zombie;
    private Enemy dragon;
    private Enemy golem;
    private Sword sword;
    private Sword sword2;
    private Dagger dagger;
    private Armour armour;
    private Scanner input;
    private boolean keepGoing = true;
    private boolean gotSword;
    private boolean gotDagger;
    private boolean gotSword2;

    // EFFECTS: runs the game
    public RageApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_GAME);
        jsonReader = new JsonReader(JSON_GAME);
        runRageApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runRageApp() {
        String command = null;
        input = new Scanner(System.in);
        setUpEnvironment();

        init();

        while (keepGoing) {

            if (allEnemiesSlain()) {
                endGameEnemiesAllDead();
                break;
            }
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommandOne(command);
            }
        }

        if (!player.getIsAlive()) {
            System.out.println("\nA bittersweet ending...");
        } else {
            System.out.println("\nT'was a fun adventure " + player.getName() + "!");
        }
    }

    // MODIFIES: this
    // EFFECTS: sets up environment
    private void setUpEnvironment() {
        environment = new Environment(player,
                darkSpirit,
                zombie,
                dragon,
                golem,
                sword,
                sword2,
                dagger,
                armour,
                gotSword,
                gotDagger,
                gotSword2);
    }

    // EFFECTS: ends the game because all enemies dead
    private void endGameEnemiesAllDead() {
        keepGoing = false;
        System.out.println("Thank you " + player.getName() + ", you defeated all the enemies!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private String makeName() {
        Scanner userInput = new Scanner(System.in);
        String playerName;
        System.out.print("Hero, what is your name?: ");
        playerName = userInput.next();
        System.out.println("Welcome to Rage, " + playerName + "!");
        return playerName;
    }

    // EFFECTS: checks if all enemies have died, if so, change allEnemiesAlive to false
    private boolean allEnemiesSlain() {
        return !(darkSpirit.getIsAlive()) && !(zombie.getIsAlive()) && !(dragon.getIsAlive()) && !(golem.getIsAlive());
    }

    // MODIFIES: e, p
    // EFFECTS: Checks if enemy is still alive,
    //          - If alive, then attack, otherwise no battle initiated
    private void battle(Enemy e, Player p) {
        if (!e.getIsAlive()) {
            System.out.println("You've already defeated the " + e.getName() + "!");
        } else {
            doAttack(e, p);
        }
    }

    // EFFECTS: typing an option starts a battle
    private void processCommandOne(String command) {
        switch (command) {
            case "i":
                showInventory();
                break;
            case "1":
                battle(darkSpirit, player);
                break;
            case "2":
                battle(zombie, player);
                break;
            case "3":
                battle(dragon, player);
                break;
            case "4":
                doJumpOffCliff();
                break;
            default:
                processCommandTwo(command);
                break;
        }
    }

    // EFFECTS: typing an option starts an action
    private void processCommandTwo(String command) {
        switch (command) {
            case "5":
                talkToDealer();
                break;
            case "6":
                exploreHowlingAbyss();
                break;
            case "r":
                doHeal();
                break;
            case "h":
                howToPlay();
                break;
            case "q":
                break;
//            case "x":
//                addWeapon();
            default:
                processSaveAndLoad(command);
        }
    }

    // EFFECTS: typing an option starts an action
    private void processSaveAndLoad(String command) {
        switch (command) {
            case "s":
                saveEnvironment();
                break;
            case "l":
                loadEnvironment();
                break;
            default:
                System.out.println("Choose an action Hero!");
        }
    }

    // EFFECTS: saves the environment to file
    private void saveEnvironment() {
        try {
            setEnvironment();
            jsonWriter.open();
            jsonWriter.write(environment);
            jsonWriter.close();
            System.out.println("Saved " + player.getName() + " to " + JSON_GAME);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_GAME);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadEnvironment() {
        try {
            environment = jsonReader.read();
            loadEnvironmentObjects();
            System.out.println("Loaded " + player.getName() + " from " + JSON_GAME);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_GAME);
        }
    }

    // MODIFIES: this
    // EFFECTS: updates all the Environment objects
    private void loadEnvironmentObjects() {
        player = environment.getPlayer();
        darkSpirit = environment.getDarkSpirit();
        zombie = environment.getZombie();
        dragon = environment.getDragon();
        golem = environment.getGolem();
        dagger = environment.getDagger();
        sword = environment.getSword();
        sword2 = environment.getSword2();
        armour = environment.getArmour();
        gotDagger = environment.isGotDagger();
        gotSword = environment.isGotSword();
        gotSword2 = environment.isGotSword2();
    }

    // MODIFIES: this
    // EFFECTS: initializes Entities and the Player
    private void init() {
        player = new Player(makeName(), 100, 5);
        darkSpirit = new Enemy("Dark Spirit", 5, 0, 5, 0, 0, true);
        zombie = new Enemy("Zombie", 20, 0, 10, 0, 0, true);
        dragon = new Enemy("Dragon", 400, 0, 90, 0, 0, true);
        golem = new Enemy("Golem", 200, 0, 45, 0, 0, true);
        dagger = new Dagger("Stone Dagger", 15, 0, 0);
        sword = new Sword("Iron Sword", 45, 0, 0);
        sword2 = new Sword("Excalibur", 400, 0, 0);
        armour = new Armour("Wooden Armour", 0, 0, 0);
        // Make the other enemies and swords and stuff
        input = new Scanner(System.in);
        setEnvironment();
    }

    // MODIFIES: this
    // EFFECTS: initializes environment
    private void setEnvironment() {
        environment.setPlayer(player);
        environment.setDarkSpirit(darkSpirit);
        environment.setZombie(zombie);
        environment.setDragon(dragon);
        environment.setGolem(golem);
        environment.setDagger(dagger);
        environment.setSword(sword);
        environment.setSword2(sword2);
        environment.setArmour(armour);
        environment.setGotDagger(gotDagger);
        environment.setGotSword(gotSword);
        environment.setGotSword2(gotSword2);
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("\ti >> Inventory");                           //check inventory
        System.out.println("\t1 >> Fight the Dark Spirit!");              //die if health <= 5
        System.out.println("\t2 >> Wrestle a Zombie!");                   //die if health <= 50
        System.out.println("\t3 >> Slay the Dragon!");                    //die if health <= 90
        System.out.println("\t4 >> What's that shiny thing over there?"); //die lol
        System.out.println("\t5 >> Talk to the Blacksmith");              //get dagger OR/AND sword
        System.out.println("\t6 >> Explore in the Howling Abyss");        //get sword2
        System.out.println("\tr >> Go to rest");                          //health back to 100
        System.out.println("\ts >> Save Game");                           //save game
        System.out.println("\tl >> Load Game");                           //load game
        System.out.println("\th >> How To Play");                         //pulls up help screen
        System.out.println("\tq >> Retire");                              //end game
    }

    // EFFECTS: prints the attack damage and armour of player
    private void showStats() {
        System.out.println(player.getName() + "'s Stats:");
        System.out.println("Attack Damage: " + player.getAttackDamage());
        System.out.println("Armour: " + player.getArmour());
    }

    // EFFECTS: prints out what is currently in Inventory
    private void showInventory() {
        String selection = "";          // force entry into loop

        while (!(selection.equals("1"))) {
            System.out.println("You look through your inventory");
            inventoryMenu(); // displays current inventory
            selection = input.next();
            selection = selection.toLowerCase();
        }

    }

    // EFFECTS: prints out inventory menu
    private void inventoryMenu() {
        printInventoryItems();
        System.out.println("1 >> Return to Camp");
    }

    // EFFECTS: prints out all the items in player's inventory using for loop
    private void printInventoryItems() {
        // int max = Player.MAX_INVENTORY_SIZE;
        inventoryDivider();
        showStats();
        if (player.getInventory().size() == 0) {
            System.out.println("You don't have anything in your inventory!");
        } else {
            // System.out.println("You have used " + player.getInventory().size() + "/" + max + " inventory slots");
            System.out.println("Currently, you have " + player.getInventory().size() + " items in your Inventory.");
            for (int i = 0; i <= player.getInventory().size() - 1; i++) {
                System.out.println((i + 1) + ") " + player.getInventory().get(i).getName());
            }
        }
    }

    // EFFECTS: prints out Inventory Divider
    private void inventoryDivider() {
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("                                    INVENTORY");
        System.out.println("-----------------------------------------------------------------------------------------");
    }

//    // EFFECTS: add weapon to player inventory
//    private void addWeapon() {
//        Weapon x = new Weapon("poop", 10, 0, 0);
//        Weapon y = new Weapon("pee", 10, 0, 0);
//        Weapon z = new Weapon("fart", 10, 0, 0);
//        x.addCollectible(player);
//        y.addCollectible(player);
//        z.addCollectible(player);
//        System.out.println("weapons succesfully put into inventory");
//    }

    // EFFECTS: prints out status' of Player and Enemy
    private void printStatus(Enemy e, Player p) {
        System.out.println("Health: " + p.getHealth() + ".");
        System.out.println(e.getName() + " Health: " + e.getHealth() + ".");
        System.out.println("Attack Damage: " + p.getAttackDamage() + ".");
        System.out.println(e.getName() + "'s Attack Damage: " + e.getAttackDamage() + ".");
        System.out.println("a >> Attack");
        System.out.println("f >> Retreat for now");
    }

    // MODIFIES: this
    // EFFECTS: brings player health to 0
    private void doJumpOffCliff() {
        player.takeDamage(player.getHealth());
        System.out.println("You lost your footing and fell off a steep cliff...");
        keepGoing = false;
    }

    // EFFECTS: displays the instructions on how to play the game
    private void howToPlay() {
        System.out.println("                                    HOW TO PLAY");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Type a number to perform an action.");
        System.out.println("In a fight, you always attack first, then the opponent.");
        System.out.println("In a fight, both opponents take damage until one is defeated.");
        System.out.println("You can replenish your health by resting");
        System.out.println("Talk to the BlackSmith, maybe he'll give you something to help on your adventure.");
        System.out.println("HINT: Need help fighting the dragon? Try exploring the Howling Abyss!");
    }

    // MODIFIES: this
    // EFFECTS: bring player health back to MAX_HEALTH
    private void doHeal() {
        System.out.println("A new day arises! (You replenished your Health)");
        player.heal();
    }

    // MODIFIES: e
    // EFFECTS: starts fight and prints fight event status
    private void hitEnemy(Enemy e, Player p) {
        p.attack(e);
        System.out.println("You hit the " + e.getName() + " for " + p.getAttackDamage() + " damage!");
    }

    // MODIFIES: p
    // EFFECTS: starts fight and prints fight event status
    private void hitPlayer(Enemy e, Player p) {
        p.takeDamage(e.getAttackDamage());
        System.out.println("The " + e.getName() + " hit you for " + e.getAttackDamage() + " damage!");
    }

    // EFFECTS: begins a fighting loop until one GameCharacter is defeated.
    // - if the Player dies, game over.
    // - if the Enemy dies, decrease health accordingly and move on.
    private void doAttack(Enemy e, Player p) {
        String selection = "";          // force entry into loop

        while (!(selection.equals("a") || selection.equals("f"))) {
            if (p.getHealth() <= 0) {
                System.out.println("You Died!");
                keepGoing = false;
                break;
            }
            printStatus(e, p);
            selection = input.next();
            selection = selection.toLowerCase();
        }

        if (selection.equals("a")) {
            checkGolem(e, p);
        }

        if (selection.equals("f")) {
            System.out.println("You run back to camp");
        }
    }

    // MODIFIES: e, p
    // EFFECTS: Checks if the enemy is a golem
    //          - If enemy is golem, attack golem method
    //          - If enemy is !golem, attack other enemy
    private void checkGolem(Enemy e, Player p) {
        hitEnemy(e, p);
        if (e.getName().equals("Golem")) {
            doAttackGolem(e, p);
        } else {
            if (e.getHealth() <= 0) {
                System.out.println("You have slain the " + e.getName() + "!");
            } else {
                hitPlayer(e, p);
                doAttack(e, p);
            }
        }
    }

    // MODIFIES: e, p
    // EFFECTS: Attacks golem and checking if golem is slain,
    //          - if slain, add Excalibur to player inventory
    //          - otherwise, attackMenu again
    private void doAttackGolem(Enemy e, Player p) {
        if (e.getHealth() <= 0) {
            System.out.println("You have slain the " + golem.getName() + "!");
            System.out.println("The Golem was guarding the Excalibur, a Legendary Sword!");
            p.addCollectible(sword2);
            sword2.addCollectible(p);
            System.out.println("Attack Damage increased to " + p.getAttackDamage() + "!");
        } else {
            hitPlayer(e, p);
            doAttack(e, p);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds dagger to inventory and increases players' attack accordingly
    private void getDagger() {
        if (gotDagger) {
            System.out.println("Hey, " + player.getName() + "! I can't find another weapon for you at the moment.");
        } else {
            System.out.println("Hey, " + player.getName() + "! Yeah here, take this " + dagger.getName());
            dagger.addCollectible(player);
            gotDagger = true;
            System.out.println("Attack Damage increased to " + player.getAttackDamage() + "!");
        }
    }

    // EFFECTS: just prints the sword name (gotta pass checkstyle)
    private String snm() {
        return sword.getName();
    }

    // MODIFIES: this
    // EFFECTS: adds sword to inventory and increases players' attack accordingly
    private void getSword() {
        if (gotSword) {
            System.out.println("Hey, " + player.getName() + "! How's the sword? I can't give another one.");
        } else {
            System.out.println("Dragons you say?! Oh man, those creatures can only be slain by refined swords!");
            System.out.println("Here, allow me to give you this " + snm() + ". You never know when you'll need it.");
            sword.addCollectible(player);
            gotSword = true;
            System.out.println("Attack Damage increased to " + player.getAttackDamage() + "!");
        }
    }

    // EFFECTS: begins a chat loop with Weapons Dealer
    // - If "1" is selected, get dagger
    // - If "2" is selected, get sword
    // - If "3" is selected, leave blacksmith
    private void talkToDealer() {
        String selection = "";          // force entry into loop
        System.out.println("Hey how can I help you?");

        while (!(selection.equals("1") || selection.equals("2") || selection.equals("3"))) {
            weaponsDealerMenu();
            selection = input.next();
            selection = selection.toLowerCase();
        }

        if (selection.equals("1")) {
            getDagger();
        }

        if (selection.equals("2")) {
            getSword();
        }

        if (selection.equals("3")) {
            System.out.println("You left the BlackSmith");
        }
    }

    // EFFECTS: prints out options the player can choose when talking to Blacksmith
    private void weaponsDealerMenu() {
        System.out.println("1 >> Ask if there is a Weapon you can use");
        System.out.println("2 >> Ask him what he thinks about Dragons");
        System.out.println("3 >> Leave the BlackSmith");
    }

    // EFFECTS: prints out options the player can choose when exploring the Howling Abyss
    private void howlingAbyssMenu() {
        System.out.println("1 >> Walk closer to the large boulder");
        System.out.println("2 >> Leave Howling Abyss");
    }

    // EFFECTS: begins a chat loop with Weapons Dealer
    // - If "1" is selected, fight the golem!!!!
    // - If "2" is selected, leave Howling Abyss
    private void exploreHowlingAbyss() {
        String selection = "";          // force entry into loop

        while (!(selection.equals("1") || selection.equals("2"))) {
            System.out.println("You explore the vast Howling Abyss. You see a strange and large boulder.");
            howlingAbyssMenu();
            selection = input.next();
            selection = selection.toLowerCase();
        }

        if (selection.equals("1")) {
            System.out.println("IT'S A GOLEM!");
            battle(golem, player);
        }

        if (selection.equals("2")) {
            System.out.println("You left the Howling Abyss");
        }
    }
}


