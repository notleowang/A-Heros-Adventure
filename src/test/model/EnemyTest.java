package model;

import org.junit.jupiter.api.BeforeEach;

class EnemyTest {

    Enemy testEnemy;
    Player testPlayer;

    @BeforeEach
    void setup() {
        testEnemy = new Enemy("Dummy", 100, 0, 10, 0, 0, true);
        testPlayer = new Player("Leo", 100, 10);
    }

}
