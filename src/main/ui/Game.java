package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * Game is the entire GUI interface.
 * - Contains all of JavaSwing objects.
 */

public class Game {

    private Music mu;

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
    private boolean gotSword;
    private boolean gotDagger;
    private boolean gotSword2;

    private final JFrame frame;
    private final Container con;

    private JPanel titlePanel;
    private JPanel buttonsPanel;
    private JPanel gameTextPanel;
    private JPanel backButtonPanel;
    private JPanel backToMainMenuButtonPanel;
    private JPanel instructionsPanel;
    private JPanel instructionsTitlePanel;
    private JPanel choicePanel;
    private JPanel playerStatusPanel;
    private JPanel enemyStatusPanel;
    private JPanel inventoryTitlePanel;
    private JPanel inventoryItemsPanel;
    private JPanel backToChoicesFromInventory;
    private JPanel backToChoicesFromFightScreen;
    private JPanel backToChoicesFromExploreScreen;
    private JPanel backToPathsFromBlackSmithScreen;
    private JPanel backToPathsFromHowlingAbyssScreen;
    private JPanel enemyButtonsPanel;
    private JPanel enemyAvatarPanel;
    private JPanel playerAvatarPanel;
    private JPanel battleActionsPanel;
    private JPanel blackSmithPanel;
    private JPanel howlingAbyssPanel;
    private JPanel exploreTitlePanel;
    private JPanel blackSmithButtonPanel;
    private JPanel howlingAbyssButtonPanel;
    private JPanel blacksmithAvatarPanel;
    private JPanel blacksmithTextPanel;
    private JPanel blacksmithQuestionsPanel;
    private JPanel howlingAbyssPicturePanel;
    private JPanel howlingAbyssContinueButtonPanel;

    private JLabel enemyHealthValue;
    private JLabel playerHealthValue;
    private JLabel playerDamageValue;

    private final Font titleFont = new Font("Times New Roman", Font.PLAIN, 60);
    private final Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);
    private final Font defaultFont = new Font("Times New Roman", Font.PLAIN, 20);
    private final Font smallButtonFont = new Font("Times New Roman", Font.PLAIN, 10);
    private final Font playerStatusFont = new Font("Times New Roman", Font.PLAIN, 40);
    private final Font enemyStatusFont = new Font("Times New Roman", Font.PLAIN, 20);
    private final Font midButtonFont = new Font("Times New Roman", Font.PLAIN, 15);

    private JButton playButton;

    private final Color buttonColor = (Color.BLACK);
    private final Color buttonFontColor = (Color.ORANGE);

    private JTextArea htpTextArea;
    private JTextArea blacksmithTextArea;

    private final TitleScreenHandler tsHandler = new TitleScreenHandler();
    private final HtpHandler htpHandler = new HtpHandler();
    private final ChoiceHandler choiceHandler = new ChoiceHandler();
    private final InventoryHandler inventoryHandler = new InventoryHandler();
    private final ChoosePathHandler choosePathHandler = new ChoosePathHandler();
    private final ChooseEnemyHandler chooseEnemyHandler = new ChooseEnemyHandler();
    private final MainMenuHandler mainMenuHandler = new MainMenuHandler();
    private final MusicHandler musicHandler = new MusicHandler();
    private final BattleHandler battleHandler = new BattleHandler();
    private final BlackSmithHandler blackSmithHandler = new BlackSmithHandler();
    private final HowlingAbyssHandler howlingAbyssHandler = new HowlingAbyssHandler();

    private Boolean isPlaying = false;
    private Boolean isMusicPlaying = false;
    private Boolean keepGoing = true;

    // CONSTRUCTOR
    public Game() {
        frame = new JFrame();
        frame.setTitle("Hero");                                 // Set Title
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Allows the ability to exit application
        frame.setResizable(false);                              // Prevent frame from being resized
        frame.setSize(800, 600);                  // Set dimensions of window
        frame.getContentPane().setBackground(Color.BLACK);      // Set background color
        frame.setLayout(null);                                  // Makes our Layout the way we want to customize it
        frame.setLocationRelativeTo(null);                      // Centers Window
        frame.setVisible(true);                                 // Makes frame visible

        con = frame.getContentPane();

        makeTitlePanel();
        makeButtonPanel();

        renderAllButtons();

        ImageIcon image = new ImageIcon(".//images//icon.png");
        frame.setIconImage(image.getImage());                               // creates cool image icon
        startMusic();

        jsonWriter = new JsonWriter(JSON_GAME);
        jsonReader = new JsonReader(JSON_GAME);
    }

    // EFFECTS: Infinite Loop checking if all enemies have been defeated
    // Use of Thread - so that it runs in the background
    Thread allEnemiesDead = new Thread(() -> {
        while (keepGoing) {
            if (allEnemiesSlain()) {
                endGameEnemiesAllDead();
            } else {
                // System.out.println("yo this is working");
            }
        }
    });

    // MODIFIES: this
    // EFFECT: print out final message and exit game
    private void endGameEnemiesAllDead() {
        keepGoing = false;
        JOptionPane.showMessageDialog(null, "YOU SAVED THE VILLAGE!\n"
                + "Thanks for playing " + player.getName());
        frame.dispose();
    }

    // MODIFIES: mu
    // EFFECTS: starts the music
    private void startMusic() {
        mu = new Music();
        String themeMusic = ".//music//music.wav";
        mu.setFile(themeMusic);
        mu.loop();
        isMusicPlaying = true;
    }

    // MODIFIES: mu
    // EFFECTS: stops the music
    private void stopMusic() {
        mu.stop();
        isMusicPlaying = false;
    }

    // EFFECTS: makes the title panel
    private void makeTitlePanel() {
        titlePanel = new JPanel();
        titlePanel.setBounds(100, 60, 600, 100);
        titlePanel.setBackground(Color.BLACK);
        con.add(titlePanel);

        JLabel titleLabel = new JLabel("HERO");
        titleLabel.setForeground(Color.ORANGE);
        titleLabel.setFont(titleFont);
        titlePanel.add(titleLabel);

        initializePanels();
    }

    // EFFECTS: initializes Panels and hides them
    private void initializePanels() {
        renderBackToMainMenuButton();
        hideMainMenuButtonPanel();
    }

    // EFFECTS: makes the button panel
    private void makeButtonPanel() {
        buttonsPanel = new JPanel(new GridLayout(5, 0));
        buttonsPanel.setBounds(300, 180, 200, 300);
        buttonsPanel.setBackground(Color.BLACK);
        con.add(buttonsPanel);
    }

    // EFFECTS: renders all the buttons onto frame
    private void renderAllButtons() {
        renderPlayButton();
        renderHtpButton();
        renderSaveButton();
        renderLoadButton();
        renderQuitButton();
    }

    // EFFECTS: creates the music button
    private void renderMusicButton() {
        JButton musicButton = new JButton("Music");
        musicButton.setBorder(new LineBorder(Color.ORANGE));
        musicButton.setPreferredSize(new Dimension(80, 25));
        musicButton.setBackground(buttonColor);
        musicButton.setForeground(buttonFontColor);
        musicButton.setFont(smallButtonFont);
        musicButton.setFocusPainted(false);
        backToMainMenuButtonPanel.add(musicButton);
        musicButton.addActionListener(musicHandler);
    }

    // EFFECTS: creates the play button
    private void renderPlayButton() {
        playButton = new JButton("PLAY");
        playButton.setBorder(new LineBorder(Color.ORANGE));
        playButton.setBackground(buttonColor);
        playButton.setForeground(buttonFontColor);
        playButton.setFont(buttonFont);
        playButton.setFocusPainted(false);
        buttonsPanel.add(playButton);
        playButton.addActionListener(tsHandler);
        playButton.setActionCommand("p");
    }

    // EFFECTS: creates the how to play button
    private void renderHtpButton() {
        JButton htpButton = new JButton("INSTRUCTIONS");
        htpButton.setBorder(new LineBorder(Color.ORANGE));
        htpButton.setBackground(buttonColor);
        htpButton.setForeground(buttonFontColor);
        htpButton.setFocusPainted(false);
        htpButton.setFont(buttonFont);
        buttonsPanel.add(htpButton);
        htpButton.addActionListener(tsHandler);
        htpButton.setActionCommand("h");
    }

    // EFFECTS: creates the save button
    private void renderSaveButton() {
        JButton saveButton = new JButton("SAVE");
        saveButton.setBorder(new LineBorder(Color.ORANGE));
        saveButton.setBackground(buttonColor);
        saveButton.setForeground(buttonFontColor);
        saveButton.setFont(buttonFont);
        saveButton.setFocusPainted(false);
        buttonsPanel.add(saveButton);
        saveButton.addActionListener(tsHandler);
        saveButton.setActionCommand("s");
    }

    // EFFECTS: creates the load button
    private void renderLoadButton() {
        JButton loadButton = new JButton("LOAD");
        loadButton.setBorder(new LineBorder(Color.ORANGE));
        loadButton.setBackground(buttonColor);
        loadButton.setForeground(buttonFontColor);
        loadButton.setFont(buttonFont);
        loadButton.setFocusPainted(false);
        buttonsPanel.add(loadButton);
        loadButton.addActionListener(tsHandler);
        loadButton.setActionCommand("l");
    }

    // EFFECTS: creates the quit button
    private void renderQuitButton() {
        JButton quitButton = new JButton("QUIT");
        quitButton.setBorder(new LineBorder(Color.ORANGE));
        quitButton.setBackground(buttonColor);
        quitButton.setForeground(buttonFontColor);
        quitButton.setFont(buttonFont);
        quitButton.setFocusPainted(false);
        buttonsPanel.add(quitButton);
        quitButton.addActionListener(tsHandler);
        quitButton.setActionCommand("q");
    }

    // EFFECTS: creates the backToTitle button
    private void renderBackToTitleButton() {
        JButton backToTitleButton = new JButton("BACK");
        backToTitleButton.setPreferredSize(new Dimension(100, 50));
        backToTitleButton.setBorder(new LineBorder(Color.ORANGE));
        backToTitleButton.setBackground(buttonColor);
        backToTitleButton.setForeground(buttonFontColor);
        backToTitleButton.setFont(buttonFont);
        backToTitleButton.setFocusPainted(false);
        backButtonPanel.add(backToTitleButton);
        backToTitleButton.addActionListener(htpHandler);
    }

    // EFFECTS: creates the Inventory button
    private void renderInventoryButton() {
        JButton inventoryButton = new JButton("INVENTORY");
        inventoryButton.setBorder(new LineBorder(Color.ORANGE));
        inventoryButton.setBackground(buttonColor);
        inventoryButton.setForeground(buttonFontColor);
        inventoryButton.setFont(buttonFont);
        inventoryButton.setFocusPainted(false);
        choicePanel.add(inventoryButton);
        inventoryButton.addActionListener(choiceHandler);
        inventoryButton.setActionCommand("i");
    }

    // EFFECTS: creates the fight button
    private void renderFightButton() {
        JButton fightButton = new JButton("FIGHT");
        fightButton.setBorder(new LineBorder(Color.ORANGE));
        fightButton.setBackground(buttonColor);
        fightButton.setForeground(buttonFontColor);
        fightButton.setFont(buttonFont);
        fightButton.setFocusPainted(false);
        choicePanel.add(fightButton);
        fightButton.addActionListener(choiceHandler);
        fightButton.setActionCommand("f");
    }

    // EFFECTS: creates the explore button
    private void renderExploreButton() {
        JButton exploreButton = new JButton("EXPLORE");
        exploreButton.setBorder(new LineBorder(Color.ORANGE));
        exploreButton.setBackground(buttonColor);
        exploreButton.setForeground(buttonFontColor);
        exploreButton.setFont(buttonFont);
        exploreButton.setFocusPainted(false);
        choicePanel.add(exploreButton);
        exploreButton.addActionListener(choiceHandler);
        exploreButton.setActionCommand("e");
    }

    // EFFECTS: creates the rest button
    private void renderRestButton() {
        JButton restButton = new JButton("REST");
        restButton.setBorder(new LineBorder(Color.ORANGE));
        restButton.setBackground(buttonColor);
        restButton.setForeground(buttonFontColor);
        restButton.setFont(buttonFont);
        restButton.setFocusPainted(false);
        choicePanel.add(restButton);
        restButton.addActionListener(choiceHandler);
        restButton.setActionCommand("r");
    }

    // EFFECTS: render backtoMainMenu button
    private void renderBackToMainMenuButton() {
        backToMainMenuButtonPanel = new JPanel(new GridLayout(2, 1));
        backToMainMenuButtonPanel.setBounds(650, 500, 100, 50);
        backToMainMenuButtonPanel.setBackground(Color.BLACK);
        con.add(backToMainMenuButtonPanel);

        JButton backToMainMenuButton = new JButton("Main Menu");
        backToMainMenuButton.setBorder(new LineBorder(Color.ORANGE));
        backToMainMenuButton.setPreferredSize(new Dimension(80, 25));
        backToMainMenuButton.setBackground(buttonColor);
        backToMainMenuButton.setForeground(buttonFontColor);
        backToMainMenuButton.setFont(smallButtonFont);
        backToMainMenuButton.setFocusPainted(false);
        backToMainMenuButtonPanel.add(backToMainMenuButton);
        backToMainMenuButton.addActionListener(mainMenuHandler);
        backToMainMenuButton.setActionCommand("b");
        renderMusicButton();
    }

    // EFFECTS: render the back to game button in inventory screen
    private void renderBackToGameFromInventoryButton() {
        backToChoicesFromInventory = new JPanel();
        backToChoicesFromInventory.setBounds(60, 500, 100, 50);
        backToChoicesFromInventory.setBackground(Color.BLACK);
        con.add(backToChoicesFromInventory);

        JButton backToChoicesFromInventoryButton = new JButton("Back");
        backToChoicesFromInventoryButton.setBorder(new LineBorder(Color.ORANGE));
        backToChoicesFromInventoryButton.setPreferredSize(new Dimension(80, 25));
        backToChoicesFromInventoryButton.setBackground(buttonColor);
        backToChoicesFromInventoryButton.setForeground(buttonFontColor);
        backToChoicesFromInventoryButton.setFont(smallButtonFont);
        backToChoicesFromInventoryButton.setFocusPainted(false);
        backToChoicesFromInventory.add(backToChoicesFromInventoryButton);
        backToChoicesFromInventoryButton.addActionListener(inventoryHandler);
        backToChoicesFromInventoryButton.setActionCommand("b");
    }

    // EFFECTS: render the back to game button in fight screen
    private void renderBackToGameFromEnemyScreenButton() {
        backToChoicesFromFightScreen = new JPanel();
        backToChoicesFromFightScreen.setBounds(60, 500, 100, 50);
        backToChoicesFromFightScreen.setBackground(Color.BLACK);
        con.add(backToChoicesFromFightScreen);

        JButton backToChoicesFromFightScreenButton = new JButton("Back");
        backToChoicesFromFightScreenButton.setBorder(new LineBorder(Color.ORANGE));
        backToChoicesFromFightScreenButton.setPreferredSize(new Dimension(80, 25));
        backToChoicesFromFightScreenButton.setBackground(buttonColor);
        backToChoicesFromFightScreenButton.setForeground(buttonFontColor);
        backToChoicesFromFightScreenButton.setFont(smallButtonFont);
        backToChoicesFromFightScreenButton.setFocusPainted(false);
        backToChoicesFromFightScreen.add(backToChoicesFromFightScreenButton);
        backToChoicesFromFightScreenButton.addActionListener(chooseEnemyHandler);
        backToChoicesFromFightScreenButton.setActionCommand("b");
    }

    // EFFECTS: render the back to game button in fight screen
    private void renderBackToGameFromExploreScreenButton() {
        backToChoicesFromExploreScreen = new JPanel();
        backToChoicesFromExploreScreen.setBounds(60, 500, 100, 50);
        backToChoicesFromExploreScreen.setBackground(Color.BLACK);
        con.add(backToChoicesFromExploreScreen);

        JButton backToChoicesFromExploreScreenButton = new JButton("Back");
        backToChoicesFromExploreScreenButton.setBorder(new LineBorder(Color.ORANGE));
        backToChoicesFromExploreScreenButton.setPreferredSize(new Dimension(80, 25));
        backToChoicesFromExploreScreenButton.setBackground(buttonColor);
        backToChoicesFromExploreScreenButton.setForeground(buttonFontColor);
        backToChoicesFromExploreScreenButton.setFont(smallButtonFont);
        backToChoicesFromExploreScreenButton.setFocusPainted(false);
        backToChoicesFromExploreScreen.add(backToChoicesFromExploreScreenButton);
        backToChoicesFromExploreScreenButton.addActionListener(choosePathHandler);
        backToChoicesFromExploreScreenButton.setActionCommand("b");
    }

    // EFFECTS: render the back to game button in blacksmith screen
    private void renderBackToGameFromBlackSmithScreenButton() {
        backToPathsFromBlackSmithScreen = new JPanel();
        backToPathsFromBlackSmithScreen.setBounds(60, 500, 100, 50);
        backToPathsFromBlackSmithScreen.setBackground(Color.black);
        con.add(backToPathsFromBlackSmithScreen);

        JButton backToPathsFromBlackSmithScreenButton = new JButton("Back");
        backToPathsFromBlackSmithScreenButton.setBorder(new LineBorder(Color.ORANGE));
        backToPathsFromBlackSmithScreenButton.setPreferredSize(new Dimension(80, 25));
        backToPathsFromBlackSmithScreenButton.setBackground(buttonColor);
        backToPathsFromBlackSmithScreenButton.setForeground(buttonFontColor);
        backToPathsFromBlackSmithScreenButton.setFont(smallButtonFont);
        backToPathsFromBlackSmithScreenButton.setFocusPainted(false);
        backToPathsFromBlackSmithScreen.add(backToPathsFromBlackSmithScreenButton);
        backToPathsFromBlackSmithScreenButton.addActionListener(blackSmithHandler);
        backToPathsFromBlackSmithScreenButton.setActionCommand("b");
    }

    // EFFECTS: creates the darkSpirit button
    private void renderDarkSpiritButton() {
        JButton darkSpiritButton = new JButton("DARK SPIRIT");
        darkSpiritButton.setBorder(new LineBorder(Color.ORANGE));
        darkSpiritButton.setBackground(buttonColor);
        darkSpiritButton.setForeground(buttonFontColor);
        darkSpiritButton.setFont(buttonFont);
        darkSpiritButton.setFocusPainted(false);
        enemyButtonsPanel.add(darkSpiritButton);
        darkSpiritButton.addActionListener(chooseEnemyHandler);
        darkSpiritButton.setActionCommand("1");
    }

    // EFFECTS: creates the zombie button
    private void renderZombieButton() {
        JButton zombieButton = new JButton("ZOMBIE");
        zombieButton.setBorder(new LineBorder(Color.ORANGE));
        zombieButton.setBackground(buttonColor);
        zombieButton.setForeground(buttonFontColor);
        zombieButton.setFont(buttonFont);
        zombieButton.setFocusPainted(false);
        enemyButtonsPanel.add(zombieButton);
        zombieButton.addActionListener(chooseEnemyHandler);
        zombieButton.setActionCommand("2");
    }

    // EFFECTS: creates the dragon button
    private void renderDragonButton() {
        JButton dragonButton = new JButton("DRAGON");
        dragonButton.setBorder(new LineBorder(Color.ORANGE));
        dragonButton.setBackground(buttonColor);
        dragonButton.setForeground(buttonFontColor);
        dragonButton.setFont(buttonFont);
        dragonButton.setFocusPainted(false);
        enemyButtonsPanel.add(dragonButton);
        dragonButton.addActionListener(chooseEnemyHandler);
        dragonButton.setActionCommand("3");
    }

    // EFFECTS: renders the blackSmith button
    private void renderBlackSmithButton() {
        JButton blackSmithButton = new JButton("BLACKSMITH");
        blackSmithButton.setBorder(new LineBorder(Color.ORANGE));
        blackSmithButton.setPreferredSize(new Dimension(150, 40));
        blackSmithButton.setBackground(buttonColor);
        blackSmithButton.setForeground(buttonFontColor);
        blackSmithButton.setFont(buttonFont);
        blackSmithButton.setFocusPainted(false);
        blackSmithButtonPanel.add(blackSmithButton);
        blackSmithButton.addActionListener(choosePathHandler);
        blackSmithButton.setActionCommand("1");
    }

    // EFFECTS: renders the howlingAbyss button
    private void renderHowlingAbyssButton() {
        JButton howlingAbyssButton = new JButton("HOWLING ABYSS");
        howlingAbyssButton.setBorder(new LineBorder(Color.ORANGE));
        howlingAbyssButton.setPreferredSize(new Dimension(180, 40));
        howlingAbyssButton.setBackground(buttonColor);
        howlingAbyssButton.setForeground(buttonFontColor);
        howlingAbyssButton.setFont(buttonFont);
        howlingAbyssButton.setFocusPainted(false);
        howlingAbyssButtonPanel.add(howlingAbyssButton);
        howlingAbyssButton.addActionListener(choosePathHandler);
        howlingAbyssButton.setActionCommand("2");
    }

    // EFFECTS: creates the attack button
    private void renderAttackButton(Enemy e) {
        JButton attackButton = new JButton("ATTACK");
        attackButton.setBorder(new LineBorder(Color.ORANGE));
        attackButton.setBackground(buttonColor);
        attackButton.setForeground(buttonFontColor);
        attackButton.setFont(buttonFont);
        attackButton.setFocusPainted(false);
        battleActionsPanel.add(attackButton);
        attackButton.addActionListener(battleHandler);
        if (e == darkSpirit) {
            attackButton.setActionCommand("1");
        } else if (e == zombie) {
            attackButton.setActionCommand("2");
        } else if (e == golem) {
            attackButton.setActionCommand("3");
        } else if (e == dragon) {
            attackButton.setActionCommand("4");
        }
    }

    // EFFECTS: creates the runAway button
    private void renderRunAwayButton(Enemy e) {
        JButton runAwayButton = new JButton("LEAVE");
        runAwayButton.setBorder(new LineBorder(Color.ORANGE));
        runAwayButton.setBackground(buttonColor);
        runAwayButton.setForeground(buttonFontColor);
        runAwayButton.setFont(buttonFont);
        runAwayButton.setFocusPainted(false);
        battleActionsPanel.add(runAwayButton);
        runAwayButton.addActionListener(battleHandler);
        if (e != golem) {
            runAwayButton.setActionCommand("r");
        } else {
            runAwayButton.setActionCommand("r2");
        }
    }

    // EFFECTS: renders the continue button
    private void renderContinueButton() {
        howlingAbyssContinueButtonPanel = new JPanel();
        howlingAbyssContinueButtonPanel.setBounds(280, 430, 250, 80);
        howlingAbyssContinueButtonPanel.setBackground(Color.black);
        con.add(howlingAbyssContinueButtonPanel);

        JButton howlingAbyssContinueButton = new JButton("CONTINUE WALKING");
        howlingAbyssContinueButton.setBorder(new LineBorder(Color.ORANGE));
        howlingAbyssContinueButton.setPreferredSize(new Dimension(230, 50));
        howlingAbyssContinueButton.setBackground(buttonColor);
        howlingAbyssContinueButton.setForeground(buttonFontColor);
        howlingAbyssContinueButton.setFont(buttonFont);
        howlingAbyssContinueButton.setFocusPainted(false);
        howlingAbyssContinueButtonPanel.add(howlingAbyssContinueButton);
        howlingAbyssContinueButton.addActionListener(howlingAbyssHandler);
        howlingAbyssContinueButton.setActionCommand("g");
    }

    // EFFECTS: hides all the title screen panels
    private void hideTitleScreenPanels() {
        buttonsPanel.setVisible(false);
        titlePanel.setVisible(false);
    }

    // EFFECTS: shows all the title screen panels
    private void showTitleScreenPanels() {
        hideMainMenuButtonPanel();
        buttonsPanel.setVisible(true);
        titlePanel.setVisible(true);
    }

    // EFFECTS: hides all the htp screen panels
    private void hideHtpScreenPanels() {
        backButtonPanel.setVisible(false);
        instructionsPanel.setVisible(false);
        instructionsTitlePanel.setVisible(false);
    }

    // EFFECTS: hides all the game screen panels
    private void hideGameScreenPanels() {
        choicePanel.setVisible(false);
        gameTextPanel.setVisible(false);
    }

    // EFFECTS: hides all the game screen panels
    private void showGameScreenPanels() {
        showMainMenuButtonPanel();
        choicePanel.setVisible(true);
        gameTextPanel.setVisible(true);
    }

    // EFFECTS: hides all the inventory panels
    private void hideInventoryScreenPanels() {
        inventoryItemsPanel.setVisible(false);
        inventoryTitlePanel.setVisible(false);
        backToChoicesFromInventory.setVisible(false);
    }

    // EFFECTS: hides all the enemybuttons on the panel
    private void hideEnemyScreenPanels() {
        enemyButtonsPanel.setVisible(false);
        backToChoicesFromFightScreen.setVisible(false);
    }

    // EFFECTS: hides the player status panel
    private void hidePlayerStatusPanels() {
        playerStatusPanel.setVisible(false);
    }

    // EFFECTS: shows the player status panel
    private void showPlayerStatusPanels() {
        playerStatusPanel.setVisible(true);
    }

    // EFFECTS: shows the choose enemies screen
    private void showChooseEnemiesScreen() {
        backToChoicesFromFightScreen.setVisible(true);
        enemyButtonsPanel.setVisible(true);
    }

    // EFFECTS: hides the main menu panel
    private void hideMainMenuButtonPanel() {
        backToMainMenuButtonPanel.setVisible(false);
    }

    // EFFECTS: hides the explore panel
    private void hideChoosePathScreenPanels() {
        backToChoicesFromExploreScreen.setVisible(false);
        howlingAbyssButtonPanel.setVisible(false);
        blackSmithButtonPanel.setVisible(false);
        exploreTitlePanel.setVisible(false);
        blackSmithPanel.setVisible(false);
        howlingAbyssPanel.setVisible(false);
    }

    // EFFECTS: shows the explore panel
    private void showChoosePathScreenPanels() {
        backToChoicesFromExploreScreen.setVisible(true);
        howlingAbyssButtonPanel.setVisible(true);
        blackSmithButtonPanel.setVisible(true);
        exploreTitlePanel.setVisible(true);
        blackSmithPanel.setVisible(true);
        howlingAbyssPanel.setVisible(true);
    }

    // EFFECTS: hide battle avatars
    private void hideBattleAvatars() {
        playerAvatarPanel.setVisible(false);
        enemyAvatarPanel.setVisible(false);
    }

    // EFFECTS: shows the main menu panel
    private void showMainMenuButtonPanel() {
        backToMainMenuButtonPanel.setVisible(true);
    }

    // EFFECTS: hides the battle screen panel
    private void hideBattleScreenPanels() {
        battleActionsPanel.setVisible(false);
        enemyStatusPanel.setVisible(false);
    }

    // EFFECTS: hides the blacksmith screen panels
    private void hideBlackSmithScreenPanels() {
        blacksmithQuestionsPanel.setVisible(false);
        blacksmithAvatarPanel.setVisible(false);
        backToPathsFromBlackSmithScreen.setVisible(false);
        blacksmithTextPanel.setVisible(false);
    }

    // EFFECTS: hides the howlingabyss screen panels
    private void hideHowlingAbyssScreenPanels() {
        backToPathsFromHowlingAbyssScreen.setVisible(false);
        howlingAbyssContinueButtonPanel.setVisible(false);
        howlingAbyssPicturePanel.setVisible(false);
    }

    // EFFECTS: shows the howlingabyss screen panels
    private void showHowlingAbyssScreen() {
        hidePlayerStatusPanels();
        backToPathsFromHowlingAbyssScreen.setVisible(true);
        howlingAbyssContinueButtonPanel.setVisible(true);
        howlingAbyssPicturePanel.setVisible(true);
    }

    // EFFECTS: renders the instructions
    private void renderInstructions() {
        instructionsPanel = new JPanel();
        instructionsPanel.setBounds(120, 180, 600, 400);
        instructionsPanel.setBackground(Color.BLACK);
        con.add(instructionsPanel);

        instructionsTitlePanel = new JPanel();
        instructionsTitlePanel.setBounds(100, 60, 600, 100);
        instructionsTitlePanel.setBackground(Color.BLACK);
        con.add(instructionsTitlePanel);

        htpTextArea = new JTextArea();
        htpTextAreaText();
        htpTextArea.setFont(defaultFont);
        htpTextArea.setEditable(false);
        htpTextArea.setBounds(120, 120, 600, 400);
        htpTextArea.setBackground(Color.BLACK);
        htpTextArea.setForeground(Color.ORANGE);
        htpTextArea.setFont(defaultFont);
        htpTextArea.setLineWrap(true);
        instructionsPanel.add(htpTextArea);

        JLabel htpLabel = new JLabel("HOW TO PLAY");
        htpLabel.setForeground(Color.ORANGE);
        htpLabel.setFont(titleFont);
        instructionsTitlePanel.add(htpLabel);
    }

    // EFFECTS: renders the text for htp
    private void htpTextAreaText() {
        htpTextArea.setText("In a fight, you always attack first, then the opponent.\n\n"
                + "In a fight, both opponents take damage until one is defeated. \n\n"
                + "You can replenish your health by resting.\n\n"
                + "Talk to the BlackSmith, maybe he'll give you something to help on your\nadventure.");
    }

    // MODIFIES: this, player
    // EFFECTS: starts loading the game and taking user input
    private void startGame() {
        ImageIcon icon = new ImageIcon(".//images//happyface.png");
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon newicon = new ImageIcon(newimg);
        if (!isPlaying) {
            String name = JOptionPane.showInputDialog(null,
                    "What shall we call you hero?", "Creating Player", JOptionPane.INFORMATION_MESSAGE);
            if (name != null) {
                JOptionPane.showMessageDialog(null, "Welcome " + name + "!",
                        "Player Creation Success!", JOptionPane.INFORMATION_MESSAGE, newicon);
            } else {
                JOptionPane.showMessageDialog(null, "Doesn't look like you entered a name.\n"
                                + "Please enter a valid name.",
                        "Player Creation Error", JOptionPane.ERROR_MESSAGE);
                startGame(); // loops itself until a player name is given xd
            }
            setUpEnvironment();
            createEnvironment(name);
            isPlaying = true;
            allEnemiesDead.start();
            playButton.setText("CONTINUE");
        }
        hideTitleScreenPanels();
    }

    // EFFECTS: renders the player status at the top of the window
    private void renderPlayerStatus() {
        playerStatusPanel = new JPanel();
        playerStatusPanel.setBounds(160, 20, 600, 50);
        playerStatusPanel.setBackground(Color.BLACK);
        playerStatusPanel.setLayout(new GridLayout(1, 4));
        con.add(playerStatusPanel);

        JLabel playerHealthLabel = new JLabel("HP:");
        playerHealthLabel.setFont(playerStatusFont);
        playerHealthLabel.setForeground(Color.WHITE);
        playerStatusPanel.add(playerHealthLabel);

        playerHealthValue = new JLabel(Integer.toString(player.getHealth()));
        playerHealthValue.setFont(playerStatusFont);
        playerHealthValue.setForeground(Color.GREEN);
        playerStatusPanel.add(playerHealthValue);

        JLabel playerDamageLabel = new JLabel("ATK:");
        playerDamageLabel.setFont(playerStatusFont);
        playerDamageLabel.setForeground(Color.WHITE);
        playerStatusPanel.add(playerDamageLabel);

        playerDamageValue = new JLabel(Integer.toString(player.getAttackDamage()));
        playerDamageValue.setFont(playerStatusFont);
        playerDamageValue.setForeground(Color.RED);
        playerStatusPanel.add(playerDamageValue);
    }

    // EFFECTS: renders the enemy's status
    private void renderEnemyStatus(Enemy e) {
        enemyStatusPanel = new JPanel();
        enemyStatusPanel.setBounds(450, 100, 200, 100);
        enemyStatusPanel.setBackground(Color.black);
        enemyStatusPanel.setLayout(new GridLayout(1, 4));
        con.add(enemyStatusPanel);

        JLabel enemyHealthLabel = new JLabel("HP:");
        enemyHealthLabel.setFont(enemyStatusFont);
        enemyHealthLabel.setForeground(Color.WHITE);
        enemyStatusPanel.add(enemyHealthLabel);

        enemyHealthValue = new JLabel(Integer.toString(e.getHealth()));
        enemyHealthValue.setFont(enemyStatusFont);
        enemyHealthValue.setForeground(Color.GREEN);
        enemyStatusPanel.add(enemyHealthValue);

        JLabel enemyDamageLabel = new JLabel("ATK:");
        enemyDamageLabel.setFont(enemyStatusFont);
        enemyDamageLabel.setForeground(Color.WHITE);
        enemyStatusPanel.add(enemyDamageLabel);

        JLabel enemyDamageValue = new JLabel(Integer.toString(e.getAttackDamage()));
        enemyDamageValue.setFont(enemyStatusFont);
        enemyDamageValue.setForeground(Color.RED);
        enemyStatusPanel.add(enemyDamageValue);
    }

    // EFFECTS: renders the game screen
    private void renderGameScreen() {
        startGame();
        renderPlayerStatus();

        gameTextPanel = new JPanel();
        gameTextPanel.setBounds(100, 100, 600, 100);
        gameTextPanel.setBackground(Color.BLACK);
        con.add(gameTextPanel);

        JTextArea gameTextArea = new JTextArea("You enter a village as a new recruit. The village has recently had "
                + "some\ndisturbances around the area. What do you do?");
        gameTextArea.setEditable(false);
        gameTextArea.setBounds(100, 100, 600, 200);
        gameTextArea.setBackground(Color.BLACK);
        gameTextArea.setForeground(Color.ORANGE);
        gameTextArea.setFont(defaultFont);
        gameTextArea.setLineWrap(true);
        gameTextPanel.add(gameTextArea);

        renderBackToMainMenuButton();
        renderGameButtons();
    }

    // EFFECTS: render all game buttons
    private void renderGameButtons() {
        choicePanel = new JPanel();
        choicePanel.setBounds(250, 200, 300, 300);
        choicePanel.setBackground(Color.RED);
        choicePanel.setLayout(new GridLayout(4, 1));
        con.add(choicePanel);

        renderInventoryButton();
        renderFightButton();
        renderExploreButton();
        renderRestButton();
    }

    // EFFECTS: renders the How to Play screen
    private void renderHowToPlayScreen() {
        hideTitleScreenPanels();

        backButtonPanel = new JPanel();
        backButtonPanel.setBounds(15, 15, 100, 60);
        backButtonPanel.setBackground(Color.black);
        con.add(backButtonPanel);

        renderInstructions();
        renderBackToTitleButton();
    }

    // REQUIRES: a file exists
    //           - that is, a game must have been created
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

    // EFFECTS: render the load screen
    private void renderLoadScreen() {
        loadEnvironment();
        JOptionPane.showMessageDialog(null, "" + player.getName()
                + "'s file Loaded Succesfully!");
    }

    // EFFECTS: render the save screen
    private void renderSaveScreen() {
        saveEnvironment();
        JOptionPane.showMessageDialog(null, "" + player.getName()
                + "'s file Saved Succesfully!");
    }

    // EFFECTS: render the Inventory screen
    private void renderInventoryScreen() {
        hideGameScreenPanels();

        inventoryTitlePanel = new JPanel();
        inventoryTitlePanel.setBounds(100, 100, 600, 100);
        inventoryTitlePanel.setBackground(Color.BLACK);
        con.add(inventoryTitlePanel);

        JLabel ivLabel = new JLabel("INVENTORY");
        ivLabel.setForeground(Color.ORANGE);
        ivLabel.setFont(titleFont);
        inventoryTitlePanel.add(ivLabel);

        renderInventoryItems();
        renderBackToGameFromInventoryButton();
    }

    // MODIFIES: this
    // EFFECTS: render the items in inventory
    private void renderInventoryItems() {
        inventoryItemsPanel = new JPanel();
        inventoryItemsPanel.setBackground(Color.black);
        inventoryItemsPanel.setLayout(new GridLayout(1, 3));
        inventoryItemsPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        inventoryItemsPanel.setBounds(250, 200, 300, 100);
        con.add(inventoryItemsPanel);
        if (gotDagger) {
            addToInventoryItemsPanel(renderDagger());
        }
        if (gotSword) {
            addToInventoryItemsPanel(renderSword());
        }
        if (gotSword2) {
            addToInventoryItemsPanel(renderExcalibur());
        }
    }

    // EFFECTS: scales image given a width, height, and file name
    private ImageIcon scaleImage(int w, int h, String file) {
        ImageIcon icon = new ImageIcon(file);
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }

    // EFFECTS: render dagger image
    private JLabel renderDagger() {
        JLabel daggerImg = new JLabel();
        daggerImg.setIcon(scaleImage(100, 100, ".//images//dagger.png"));
        return daggerImg;
    }

    // EFFECTS: render sword image
    private JLabel renderSword() {
        JLabel swordImg = new JLabel();
        swordImg.setIcon(scaleImage(100, 100, ".//images//sword.png"));
        return swordImg;
    }

    // EFFECTS: render excalibur image
    private JLabel renderExcalibur() {
        JLabel excaliburImg = new JLabel();
        excaliburImg.setIcon(scaleImage(100, 100, ".//images//excalibur.png"));
        return excaliburImg;
    }

    // EFFECTS: add the weapon image to the panel
    private void addToInventoryItemsPanel(JLabel img) {
        inventoryItemsPanel.add(img);
    }

    // EFFECTS: render the ChooseEnemy screen
    private void renderChooseEnemyScreen() {
        hideGameScreenPanels();

        enemyButtonsPanel = new JPanel();
        enemyButtonsPanel.setBounds(300, 180, 200, 200);
        enemyButtonsPanel.setBackground(Color.RED);
        enemyButtonsPanel.setLayout(new GridLayout(3, 1));
        con.add(enemyButtonsPanel);

        renderDarkSpiritButton();
        renderZombieButton();
        renderDragonButton();

        renderBackToGameFromEnemyScreenButton();
    }

    // EFFECTS: render the Battle screen with given enemy
    private void renderBattleScreen(Enemy e) {
        battleActionsPanel = new JPanel(new GridLayout(2, 1));
        battleActionsPanel.setBounds(305, 420, 200, 80);
        battleActionsPanel.setBackground(Color.blue);
        con.add(battleActionsPanel);
        renderEnemyStatus(e);
        renderAttackButton(e);
        renderRunAwayButton(e);
        renderEnemyBattleAvatar(e);
        renderPlayerBattleAvatar();
    }

    // EFFECTS: renders the player avatar during battle
    private void renderEnemyBattleAvatar(Enemy e) {
        JLabel avatarImg = new JLabel();
        if (e == darkSpirit) {
            avatarImg.setIcon(scaleImage(170, 200, ".//images//darkSpirit.png"));
        } else if (e == zombie) {
            avatarImg.setIcon(scaleImage(170, 240, ".//images//zombie.png"));
        } else if (e == golem) {
            avatarImg.setIcon(scaleImage(170, 240, ".//images//golem.png"));
        } else if (e == dragon) {
            avatarImg.setIcon(scaleImage(170, 240, ".//images//dragon.png"));
        }
        enemyAvatarPanel = new JPanel();
        enemyAvatarPanel.setBounds(480, 170, 100, 200);
        enemyAvatarPanel.setBackground(Color.black);
        con.add(enemyAvatarPanel);
        enemyAvatarPanel.add(avatarImg);
    }

    // EFFECTS: renders the player avatar during battle
    private void renderPlayerBattleAvatar() {
        JLabel avatarImg = new JLabel();
        avatarImg.setIcon(scaleImage(170, 250, ".//images//player.png"));

        playerAvatarPanel = new JPanel();
        playerAvatarPanel.setBounds(200, 150, 120, 250);
        playerAvatarPanel.setBackground(Color.black);
        con.add(playerAvatarPanel);
        playerAvatarPanel.add(avatarImg);
    }

    // EFFECTS: attaches blackSmith img to jpanel
    private void renderBlackSmithImg() {
        JLabel blackSmithImg = new JLabel();
        blackSmithImg.setIcon(scaleImage(200, 350, ".//images//blacksmith.jpg"));
        blackSmithImg.setBorder(new LineBorder(Color.ORANGE, 5));
        blackSmithPanel.add(blackSmithImg);
    }

    // EFFECTS: attaches howlingAbyss img to jpanel
    private void renderHowlingAbyssImg() {
        JLabel howlingAbyssImg = new JLabel();
        howlingAbyssImg.setIcon(scaleImage(200, 350, ".//images//howlingabyss.png"));
        howlingAbyssImg.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5));
        howlingAbyssPanel.add(howlingAbyssImg);
    }

    // EFFECTS: makes and renders title panel and label
    private void makeExploreTitlePanelAndLabel() {
        exploreTitlePanel = new JPanel();
        exploreTitlePanel.setBounds(100, 60, 600, 100);
        exploreTitlePanel.setBackground(Color.BLACK);
        con.add(exploreTitlePanel);

        JLabel exploreLabel = new JLabel("EXPLORE");
        exploreLabel.setForeground(Color.ORANGE);
        exploreLabel.setFont(titleFont);
        exploreTitlePanel.add(exploreLabel);
    }

    // EFFECTS: render the ChoosePath screen
    private void renderChoosePathScreen() {
        hideGameScreenPanels();
        hidePlayerStatusPanels();
        renderBackToGameFromExploreScreenButton();
        makeExploreTitlePanelAndLabel();

        blackSmithPanel = new JPanel();
        blackSmithPanel.setBackground(Color.blue);
        blackSmithPanel.setBounds(150, 120, 200, 300);
        renderBlackSmithImg();

        howlingAbyssPanel = new JPanel();
        howlingAbyssPanel.setBackground(Color.red);
        howlingAbyssPanel.setBounds(450, 120, 200, 300);
        renderHowlingAbyssImg();

        renderBlackSmithButtonPanel();
        renderHowlingAbyssButtonPanel();
        renderBlackSmithButton();
        renderHowlingAbyssButton();

        con.add(blackSmithPanel);
        con.add(howlingAbyssPanel);
    }

    // EFFECTS: renders the BlackSmithButton panel
    private void renderBlackSmithButtonPanel() {
        blackSmithButtonPanel = new JPanel();
        blackSmithButtonPanel.setBounds(150, 430, 200, 50);
        blackSmithButtonPanel.setBackground(Color.black);
        con.add(blackSmithButtonPanel);
    }

    // EFFECTS: renders the HowlingAbyssButton panel
    private void renderHowlingAbyssButtonPanel() {
        howlingAbyssButtonPanel = new JPanel();
        howlingAbyssButtonPanel.setBounds(450, 430, 200, 50);
        howlingAbyssButtonPanel.setBackground(Color.black);
        con.add(howlingAbyssButtonPanel);
    }

    // EFFECTS: renders the blacksmith screen buttons
    private void renderBlackSmithQuestionButtons() {
        JButton getDaggerButton = new JButton();
        getDaggerButton.setText("Ask if there's anything you can use");
        getDaggerButton.setPreferredSize(new Dimension(100, 10));
        getDaggerButton.setBorder(new LineBorder(Color.ORANGE));
        getDaggerButton.setBackground(buttonColor);
        getDaggerButton.setForeground(buttonFontColor);
        getDaggerButton.setFont(midButtonFont);
        getDaggerButton.setFocusPainted(false);
        getDaggerButton.addActionListener(blackSmithHandler);
        getDaggerButton.setActionCommand("1");

        JButton getSwordButton = new JButton();
        getSwordButton.setText("Ask about Dragons");
        getSwordButton.setPreferredSize(new Dimension(100, 10));
        getSwordButton.setBorder(new LineBorder(Color.ORANGE));
        getSwordButton.setBackground(buttonColor);
        getSwordButton.setForeground(buttonFontColor);
        getSwordButton.setFont(midButtonFont);
        getSwordButton.setFocusPainted(false);
        getSwordButton.addActionListener(blackSmithHandler);
        getSwordButton.setActionCommand("2");

        blacksmithQuestionsPanel.add(getDaggerButton);
        blacksmithQuestionsPanel.add(getSwordButton);
    }

    // EFFECTS: creates the blacksmith screen buttons panel
    private void renderBlackSmithQuestionButtonsPanel() {
        blacksmithQuestionsPanel = new JPanel(new GridLayout(2, 1));
        blacksmithQuestionsPanel.setBounds(175, 300, 250, 100);
        blacksmithQuestionsPanel.setBackground(Color.black);
        con.add(blacksmithQuestionsPanel);
    }

    // EFFECTS: renders the entire blacksmith avatar from panel and label
    private void renderBlackSmithAvatar() {
        JLabel avatarImg = new JLabel();
        avatarImg.setIcon(scaleImage(200, 380, ".//images//blacksmith.png"));

        blacksmithAvatarPanel = new JPanel();
        blacksmithAvatarPanel.setBounds(520, 150, 200, 400);
        blacksmithAvatarPanel.setBackground(Color.black);
        con.add(blacksmithAvatarPanel);
        blacksmithAvatarPanel.add(avatarImg);
    }

    // EFFECTS: renders the blacksmith text panel
    private void renderBlackSmithTextArea() {
        blacksmithTextPanel = new JPanel();
        blacksmithTextPanel.setBounds(100, 200, 400, 200);
        blacksmithTextPanel.setBackground(Color.black);
        con.add(blacksmithTextPanel);

        blacksmithTextArea = new JTextArea("Hey " + player.getName() + "! How can I help you?");
        blacksmithTextArea.setEditable(false);
        blacksmithTextArea.setBounds(100, 100, 400, 200);
        blacksmithTextArea.setBackground(Color.BLACK);
        blacksmithTextArea.setForeground(Color.ORANGE);
        blacksmithTextArea.setFont(defaultFont);
        blacksmithTextArea.setLineWrap(true);
        blacksmithTextPanel.add(blacksmithTextArea);
    }

    // EFFECTS: renders the blacksmith screen
    private void renderBlackSmithScreen() {
        renderBackToGameFromBlackSmithScreenButton();
        renderBlackSmithQuestionButtonsPanel();
        renderBlackSmithQuestionButtons();
        renderBlackSmithAvatar();
        renderBlackSmithTextArea();
    }

    // EFFECTS: render the back to game button in blacksmith screen
    private void renderBackToGameFromHowlingAbyssScreenButton() {
        backToPathsFromHowlingAbyssScreen = new JPanel();
        backToPathsFromHowlingAbyssScreen.setBounds(60, 500, 100, 50);
        backToPathsFromHowlingAbyssScreen.setBackground(Color.black);
        con.add(backToPathsFromHowlingAbyssScreen);

        JButton backToPathsFromHowlingAbyssScreenButton = new JButton("Back");
        backToPathsFromHowlingAbyssScreenButton.setBorder(new LineBorder(Color.ORANGE));
        backToPathsFromHowlingAbyssScreenButton.setPreferredSize(new Dimension(80, 25));
        backToPathsFromHowlingAbyssScreenButton.setBackground(buttonColor);
        backToPathsFromHowlingAbyssScreenButton.setForeground(buttonFontColor);
        backToPathsFromHowlingAbyssScreenButton.setFont(smallButtonFont);
        backToPathsFromHowlingAbyssScreenButton.setFocusPainted(false);
        backToPathsFromHowlingAbyssScreen.add(backToPathsFromHowlingAbyssScreenButton);
        backToPathsFromHowlingAbyssScreenButton.addActionListener(howlingAbyssHandler);
        backToPathsFromHowlingAbyssScreenButton.setActionCommand("b");
    }

    // EFFECTS: render the back to game button in blacksmith screen
    private void renderHowlingAbyssPicturePanel() {
        JLabel pic = new JLabel();
        pic.setIcon(scaleImage(600, 300, ".//images//howlingabysspic.jpg"));

        howlingAbyssPicturePanel = new JPanel();
        howlingAbyssPicturePanel.setBounds(100, 100, 600, 300);
        howlingAbyssPicturePanel.setBackground(Color.black);
        con.add(howlingAbyssPicturePanel);
        howlingAbyssPicturePanel.add(pic);
    }

    // EFFECTS: renders the howlingAbyss screen
    private void renderHowlingAbyssScreen() {
        renderBackToGameFromHowlingAbyssScreenButton();
        renderContinueButton();
        renderHowlingAbyssPicturePanel();
    }

    // MODIFIES: player
    // EFFECTS: render the Rest screen
    private void renderRestScreen() {
        JOptionPane.showMessageDialog(null, "You had a good nights rest!\n"
                + "Health regenerated!");
        doHeal();
    }

    // MODIFIES: this
    // EFFECTS: do an action based off button clicked in title screen
    public class TitleScreenHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String choice = e.getActionCommand();
            switch (choice) {
                case "p":
                    renderGameScreen();
                    break;
                case "h":
                    renderHowToPlayScreen();
                    break;
                case "s":
                    renderSaveScreen();
                    break;
                case "l":
                    isPlaying = true;
                    playButton.setText("CONTINUE");
                    renderLoadScreen();
                    break;
                case "q":
                    renderQuitScreen();
                    break;
            }
        }
    }

    // EFFECTS: do an action based off button clicked in game screen
    public class ChoiceHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String choice = e.getActionCommand();
            switch (choice) {
                case "i":
                    hideMainMenuButtonPanel();
                    renderInventoryScreen();
                    break;
                case "f":
                    hideMainMenuButtonPanel();
                    renderChooseEnemyScreen();
                    break;
                case "e":
                    hideMainMenuButtonPanel();
                    renderChoosePathScreen();
                    break;
                case "r":
                    renderRestScreen();
                    break;
            }
        }
    }

    // EFFECTS: do an action based off button clicked in game screen
    public class InventoryHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String choice = e.getActionCommand();
            if ("b".equals(choice)) {
                showGameScreenPanels();
                hideInventoryScreenPanels();
            }
        }
    }

    // EFFECTS: do an action based off button clicked in game screen
    public class ChooseEnemyHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String choice = e.getActionCommand();
            switch (choice) {
                case "1":
                    hideEnemyScreenPanels();
                    ifNotAliveDoNotOpen(darkSpirit);
                    break;
                case "2":
                    hideEnemyScreenPanels();
                    ifNotAliveDoNotOpen(zombie);
                    break;
                case "3":
                    hideEnemyScreenPanels();
                    ifNotAliveDoNotOpen(dragon);
                    break;
                case "b":
                    showGameScreenPanels();
                    hideEnemyScreenPanels();
            }
        }
    }

    // EFFECTS: do an action based off button clicked in game screen
    public class MainMenuHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            hidePlayerStatusPanels();
            hideGameScreenPanels();
            showTitleScreenPanels();
        }
    }

    // EFFECTS: do an action based off button clicked in game screen
    public class ChoosePathHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String choice = e.getActionCommand();
            switch (choice) {
                case "1":
                    hideChoosePathScreenPanels();
                    renderBlackSmithScreen();
                    break;
                case "2":
                    hideChoosePathScreenPanels();
                    renderHowlingAbyssScreen();
                    break;
                case "b":
                    showGameScreenPanels();
                    showPlayerStatusPanels();
                    hideChoosePathScreenPanels();
            }
        }
    }

    // EFFECTS: do an action based off button clicked in battle screen
    public class BattleHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String choice = e.getActionCommand();
            switch (choice) {
                case "1":
                    doAttack(darkSpirit, player);
                    break;
                case "2":
                    doAttack(zombie, player);
                    break;
                case "3":
                    doAttack(golem, player);
                    break;
                case "4":
                    doAttack(dragon, player);
                    break;
                case "r":
                    hideBattleScreen();
                    showChooseEnemiesScreen();
                    break;
                case "r2":
                    hideBattleScreen();
                    showHowlingAbyssScreen();
            }
        }
    }

    // EFFECTS: made to pass checkstyle test
    private void hideBattleScreen() {
        hideBattleScreenPanels();
        hideBattleAvatars();
    }

    // EFFECTS: do an action based off button clicked in game screen
    public class BlackSmithHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String choice = e.getActionCommand();
            switch (choice) {
                case "1":
                    hasDagger();
                    break;
                case "2":
                    hasSword();
                    break;
                case "b":
                    hideBlackSmithScreenPanels();
                    showChoosePathScreenPanels();
                    break;
            }
        }
    }

    // EFFECTS: do an action based off button clicked in game screen
    public class HowlingAbyssHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String choice = e.getActionCommand();
            switch (choice) {
                case "b":
                    hideHowlingAbyssScreenPanels();
                    showChoosePathScreenPanels();
                    break;
                case "g":
                    hideHowlingAbyssScreenPanels();
                    showPlayerStatusPanels();
                    ifNotAliveDoNotOpen(golem);
            }
        }
    }

    // EFFECTS: spits out a window saying "ITS A GOLEM!!!"
    private void printItsAGolem() {
        JOptionPane.showMessageDialog(null, "IT'S A GOLEM!!!");
    }

    // MODIFIES: player
    // EFFECTS: checks if player already has weapon
    //          - if false, give player dagger and set gotDagger = true
    //          - otherwise tell player no more dagger
    private void hasDagger() {
        if (!gotDagger) {
            dagger.addCollectible(player);
            blacksmithTextArea.setText("Of course, " + player.getName() + "! Here, take this "
                    + dagger.getName() + "\n\nAttack Damage Increased!");
            gotDagger = true;
            playerDamageValue.setText(Integer.toString(player.getAttackDamage()));
        } else {
            alreadyGotWeapon(dagger);
        }
    }

    // MODIFIES: player
    // EFFECTS: checks if player already has sword
    //          - if false, give player sword and set gotSword = true
    //          - otherwise tell player no more sword
    private void hasSword() {
        if (!gotSword) {
            sword.addCollectible(player);
            blacksmithTextArea.setText("Dragons? They're very powerful creatures. \n"
                    + "Here, " + player.getName() + "! Take this "
                    + sword.getName() + "\n\n Attack Damage Increased!");
            gotSword = true;
            playerDamageValue.setText(Integer.toString(player.getAttackDamage()));
        } else {
            alreadyGotWeapon(sword);
        }
    }

    // EFFECTS: changes textArea for blacksmith
    private void alreadyGotWeapon(Collectible c) {
        blacksmithTextArea.setText("Sorry " + player.getName() + "! \n"
                + "I can't find another " + c.getName() + "\nfor you at the moment.");
    }

    // MODIFIES: e, golem
    // EFFECTS: checks if enemy is already dead
    //          - if already dead, then don't change screen
    private void ifNotAliveDoNotOpen(Enemy e) {
        if (e != golem) {
            if (!(e.getIsAlive())) {
                printAlreadyDefeatedEnemy(e);
                showChooseEnemiesScreen();
            } else {
                renderBattleScreen(e);
            }
        } else if (!(golem.getIsAlive())) {
            printAlreadyDefeatedEnemy(golem);
            showHowlingAbyssScreen();
        } else {
            renderBattleScreen(golem);
            printItsAGolem();
        }
    }

    // MODIFIES: mu
    // EFFECTS: mute/unmute button action
    public class MusicHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isMusicPlaying) {
                stopMusic();
            } else {
                startMusic();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: opens window asking player for confirmation of quitting application
    private void renderQuitScreen() {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to end your adventure?",
                "Exit Application?",
                JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION) {
            keepGoing = false;
            frame.dispose();
        }
    }

    // EFFECTS: do an action based off button clicked for htp screen
    public class HtpHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            hideHtpScreenPanels();
            showTitleScreenPanels();
        }
    }

    // MODIFIES: this
    // EFFECTS: starts loading in the game
    private void createEnvironment(String name) {
        player = new Player(name, 100, 5);
        darkSpirit = new Enemy("Dark Spirit", 5, 0, 5, 0, 0, true);
        zombie = new Enemy("Zombie", 20, 0, 10, 0, 0, true);
        dragon = new Enemy("Dragon", 400, 0, 90, 0, 0, true);
        golem = new Enemy("Golem", 200, 0, 45, 0, 0, true);
        dagger = new Dagger("Stone Dagger", 15, 0, 0);
        sword = new Sword("Iron Sword", 45, 0, 0);
        sword2 = new Sword("Excalibur", 400, 0, 0);
        armour = new WoodenArmour("Wooden Armour", 0, 0, 0);
        // Make the other enemies and swords and stuff
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

    // EFFECTS: checks if all enemies have died, if so, change allEnemiesAlive to false
    private boolean allEnemiesSlain() {
        return !(darkSpirit.getIsAlive()) && !(zombie.getIsAlive()) && !(dragon.getIsAlive()) && !(golem.getIsAlive());
    }

    // Music Class for playing music
    public static class Music {

        Clip clip;

        public void setFile(String soundFileName) {

            try {
                File file = new File(soundFileName);
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);
            } catch (Exception e) {
                System.out.println("No Music File Found");
            }
        }

        // MODIFIES: mu
        // EFFECTS: play the music
        public void play() {
            clip.setFramePosition(0);
            clip.start();
        }

        // MODIFIES: mu
        // EFFECTS: loop the music
        public void loop() {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

        // MODIFIES: mu
        // EFFECTS: stop the music
        public void stop() {
            clip.stop();
            clip.close();
        }
    }

    // EFFECTS: spits out window telling player enemy already got defeated
    private void printAlreadyDefeatedEnemy(Enemy e) {
        JOptionPane.showMessageDialog(null, "You already defeated the "
                + e.getName() + "!");
    }

    // MODIFIES: this
    // EFFECTS: spits out a window telling player they died :(
    private void printYouDiedWindow() {
        JOptionPane.showMessageDialog(null, "You died...");
        frame.dispose();
    }

    // EFFECTS: spits out a window telling you defeated the enemy :)
    //          also goes back to chooseEnemies Screen
    private void printYouDefeatedEnemy(Enemy e) {
        JOptionPane.showMessageDialog(null, "You defeated the "
                + e.getName() + "!");
        hideBattleScreenPanels();
        hideBattleAvatars();
        showChooseEnemiesScreen();
    }

    // MODIFIES: this
    // EFFECTS: bring player health back to MAX_HEALTH
    private void doHeal() {
        player.heal();
        playerHealthValue.setText(Integer.toString(player.getHealth()));
    }

    // MODIFIES: e, p, this
    // EFFECTS: performs damage
    // - if the Player dies, game over.
    // - if the Enemy dies, decrease health accordingly and move on.
    private void doAttack(Enemy e, Player p) {
        hitEnemy(e, p);
        if (p.getHealth() <= 0) {
            printYouDiedWindow();
        } else if (e.getHealth() <= 0) {
            enemyHealthValue.setText("0");
            if (e == golem) {
                printDroppedExcalibur();
            } else {
                printYouDefeatedEnemy(e);
            }
        } else {
            hitPlayer(e, p);
        }
    }

    // MODIFIES: this
    // EFFECTS: spits out a window telling player they obtained the excalibur
    //          also gives player excalibur
    //          condition - golem is killed
    public void printDroppedExcalibur() {
        sword2.addCollectible(player);
        playerDamageValue.setText(Integer.toString(player.getAttackDamage()));
        gotSword2 = true;
        JOptionPane.showMessageDialog(null, "The Golem was guarding the Excalibur!\n"
                + "A legendary weapon! Attack Damage Increased SIGNIFICANTLY.");
        hideBattleScreenPanels();
        hideBattleAvatars();
        showHowlingAbyssScreen();
    }

    // MODIFIES: e, this
    // EFFECTS: starts fight and prints fight event status
    private void hitEnemy(Enemy e, Player p) {
        p.attack(e);
        enemyHealthValue.setText(Integer.toString(e.getHealth()));
    }

    // MODIFIES: p, this
    // EFFECTS: starts fight and prints fight event status
    private void hitPlayer(Enemy e, Player p) {
        p.takeDamage(e.getAttackDamage());
        if (p.getHealth() <= 0) {
            playerHealthValue.setText("0");
            printYouDiedWindow();
        } else {
            playerHealthValue.setText(Integer.toString(p.getHealth()));
        }
    }
}
