# CPSC 210 Personal Project

This is the first personal project I made, using Java and JavaSwing.

## A 2D Platformer Game by Leo Wang

### What will the application do?  

This application is a **text-based adventure game**. The game consists 
of a character where the player is able interact with options that are given on the screen.
In the game, the player will have an *inventory* to keep track of specific **items** 
that can be picked up and equipped. If the item is a weapon, the player can expect to recieve
an **attack boost**. The player will also be able to keep track of their health and
damage during battle. The player will have the freedom to fight any of the *enemies* in their way.
The end of the game can be achieved if the player is able to defeat all of the enemies. If the player
dies during a fight, the application with end and the game is over. The player will have the ability
to **save** and **load** their progress.

### Who is the intended audience?

The game is targeted towards a **younger** audience, however the game is **not** restricted
to any specific age group and can be explored by anyone of any age if they desire. The
application is a game, so those who use it are expected to be looking for a source of 
entertainment and short-term amusement.

### Why did I choose to do this project?

This project is of interest to me because I grew up playing multiple games, like Mario, Sonic the Hedgehog, etc.
Although I would like to learn how to make three-dimensional games, I believe that starting out with a 
something as fundamental as a text-based game will help me learn how to approach a three-dimensional game in the future.

## User Stories
- As a user, I want to be able to obtain items and put it into my inventory.   
- As a user, I want to be able to know what my current health is during fight.
- As a user, I want to be able to know how much damage I can do during a fight.
- As a user, I want to be able to have a weapon and do more damage.
- As a user, I want to be able to attack an enemy.
- As a user, I want to be able to explore areas and achieve loot.
- As a user, I want to be able to converse with other charcters.
- As a user, I want to be able to save my Inventory to file.
- As a user, I want to be able to be able to load my Inventory from file.
- As a user, I want to be able to save my stats to file.
- As a user, I want to be able to load my stats to file. 
- As a user, I want to be able to save my progress of defeating enemies to file.
- As a user, I want to be able to load my progress of defeating enemies to file. 

## Phase 4: Task 2

Included a type hierarchy.  

### Classes
- Collectible
- Weapon (Overrided addCollectible and removeCollectible)
- Armour (Overrided addCollectible and removeCollectible)

## Phase 4: Task3

#### Reflection:
Looking at my UML diagram, I notice that I have a lot of classes that extend other classes.
However, I know that some of these abstract classes are redundant. For example,
when I look at my Entity class, it has no specific methods or any type of functionality.
So, if I had more time, I would have definitely made it so that there were less
redundant classes like my Entity class, since I feel like I just made it more complicated
by adding these classes.  
  
If I had more time, I think I would've organized my UI classes better. When looking at my
UI folder, everything related to the GUI is cramped into one class (Game class), up
to the point that there are extensive lines of code in that class, making it extremely
hard to read and navigate. If I had more time,
I definitely would've made more classes to reduce the amount of coupling and avoid
the difficulties of finding methods within the Game class.


