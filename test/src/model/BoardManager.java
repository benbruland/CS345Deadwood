//Authors: Benjamin Bruland, Lukas McIntosh

package model;

import controller.Board_Controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import java.util.Random;

public final class BoardManager{

    /* Primitive Attributes */
    private int numPlayers;   
    private int dayOfGame = 1;    // Current day of game
    private int numberOfDays; // Total number of days to play
    private int numberOfScenesRemaining; //number of scenes remaining in a particular day
    
    //BoardManager is a singleton class. This is the instance var
    private static BoardManager instance;

    /* Non-Primitive Attributes */
    private Board gameBoard;
    private Player activePlayer;


    //TODO Implement Constructors
    private BoardManager() {
        this.numPlayers = 0;
        this.dayOfGame = 1;
        this.numberOfDays = 3;
    }

    private BoardManager(int numPlyrs, ArrayList<String> nameList) {
        assert numPlyrs >= 2 && numPlyrs <= 8 : "Invalid number of players: " + numPlyrs ;
        this.numPlayers = numPlyrs;
        this.numberOfDays = numPlyrs > 3 ? 4 : 3;
        this.dayOfGame = 1; 
        this.initBoard(numPlyrs, nameList);
        this.activePlayer = chooseFirstPlayer();
    }

    public static BoardManager getInstance(int numPlyrs, ArrayList<String> nameList) {
        
        if (BoardManager.instance == null) {
            BoardManager.instance = new BoardManager(numPlyrs, nameList);
        }

        return BoardManager.instance;
    }

    public static BoardManager getInstance() {
        if (BoardManager.instance == null) {
            BoardManager.instance = new BoardManager();
        } 
        return BoardManager.instance;
    }


    private Player chooseFirstPlayer() {
       
        assert this.gameBoard != null : "Gameboard not initialized before choosing first player";
        assert this.gameBoard.getPlayers().size() > 0 : "Gameboard players array not filled";
       
        int playerIndex = randInt(0, this.numPlayers - 1);
        return this.gameBoard.getPlayers().get(playerIndex); 
    }

    private Player makeInitialPlayer(int numPlayers, int playerID) {
        assert playerID >= 0 && playerID < numPlayers : "playerID must represent a valid index into players array.";
        Player newPlayer = new Player(playerID);

        switch(numPlayers) {
            case 5:
                newPlayer.setPlayerCredits(2);
            case 6:
                newPlayer.setPlayerCredits(4);
            case 7:
                newPlayer.setRank(2);
            case 8:
                newPlayer.setRank(2);
        }

        this.gameBoard.getTrailers().addPlayerToRoom(newPlayer);
        newPlayer.setBoardManager(this);
        return newPlayer;
    }

    public ArrayList<Card> getDeck() {
        return this.getBoard().getDeck();
    }

    // Sets up the board for the given number of players
    // Responsible for the creation of the gameBoard object
    private void initBoard(int numPlayers, ArrayList<String> nameList) {
        XMLParser parser = new XMLParser();
        this.gameBoard = parser.parseBoard();
        Board board = this.gameBoard;
        ArrayList<Player> players = new ArrayList<Player>();
        
        for (int i = 0; i < numPlayers; i++) {
            Player newPlayer = makeInitialPlayer(numPlayers, i);
            newPlayer.setName(nameList.get(i));
            System.out.println("Setting Player name to " + nameList.get(i));
            players.add(newPlayer);
        }
       
        board.setPlayers(players);
        this.numberOfScenesRemaining = 10;
        this.dealScenes(numberOfScenesRemaining);
    }

    public Player getActivePlayer() {
        return this.activePlayer;
    }

    public int getCurrentDay() {
        return this.dayOfGame;
    }

    private void movePlayersTo(ArrayList<Player> plys, Room plyRoom) {
        for (Player ply: plys) {
            ply.setPlayerRoom(plyRoom);
        }
    }

    private void resetOffCardRoles(Room plyRoom) {
        ArrayList<Role> offCardRoles = plyRoom.getOffCardRoles();
        for (Role plyRole: offCardRoles) {
            plyRole.setRoleAvailable(true);
        }
    }

    /* Increments the day counter, checks for end of game, and returns winning player if game over and null if not. */
    public boolean cycleGameDay() {
        boolean gameFinished = false;
        ArrayList<Room> rooms = this.getBoard().getBoardRooms();
        if (this.getCurrentDay() == numberOfDays) {
            gameFinished = true;
        } else {
            this.movePlayersTo(this.getBoard().getPlayers(),this.getBoard().getTrailers());
            this.dealScenes(10);
            this.dayOfGame++;
            for (Room plyRoom: rooms) {
                resetOffCardRoles(plyRoom);
            }
        }
        return gameFinished;
    }

    // Returns a random integer in range [min, max]
    private int randInt(int min, int max) {

        Random randGen = new Random();
        int randomNum = randGen.nextInt((max - min) + 1) + min;
    
        return randomNum;
    }

    public int calculatePlayerScore(Player ply) {
        return ply.getPlayerCredits() + ply.getPlayerDollars() + (ply.getPlayerRank() * 5);
    }
    //  Function returns an array list of winners. It returns an array list
    //  because it is possible to tie this game.
    public ArrayList<Player> scoreGame() {
        ArrayList<Player> plyrs = this.gameBoard.getPlayers();
        ArrayList<Player> winners = new ArrayList<Player>();
        int maxScore = -1;

        for(Player plyr : plyrs){
            int playerScore = calculatePlayerScore(plyr);
            if(playerScore > maxScore){
                maxScore = playerScore;
            }    
        }

        for (Player plyr: plyrs) {
            int playerScore = calculatePlayerScore(plyr);
            if (playerScore == maxScore) {
                winners.add(plyr);
            }
        }

        return winners;
    }

    private ArrayList<Integer> getBonusDice(int budget) {
        
        ArrayList<Integer> diceRolls = new ArrayList<Integer>();
        for (int i = 0; i < budget; i++) {
            diceRolls.add(randInt(1,6));
        }
        return diceRolls;
    }
    
    public void payBonuses(Room roomToPayout) {
        Card roomCard = roomToPayout.getRoomScene().getSceneCard();
        int budget = roomCard.getBudget();
        ArrayList<Player> offCardPlayers = roomToPayout.getPlayersOffCard();
        ArrayList<Player> onCardPlayers = roomToPayout.getPlayersOnCard();
        onCardPlayers.sort((Player a, Player b) -> compareRoles(a.getRole(), b.getRole()));
        
        if (onCardPlayers.size() != 0) {
            
            ArrayList<Integer> diceRolls = getBonusDice(budget);
            diceRolls.sort(Collections.reverseOrder());
            int diceCounter = 0;

            for (int i = 0; i < diceRolls.size(); i++) {
                Player onCardPlayer = onCardPlayers.get(diceCounter % onCardPlayers.size());
                onCardPlayer.setPlayerDollars(onCardPlayer.getPlayerDollars() + diceRolls.get(i));
            }

            for (int i = 0; i < offCardPlayers.size(); i++) {
                Player offCardPlayer = offCardPlayers.get(i);
                offCardPlayer.setPlayerDollars(offCardPlayer.getPlayerDollars() + offCardPlayer.getRole().getRoleLevel());
            }
        }
        
    }

    // This will be called when a player acts on a role regardless of the outcome
    // It is responsible for awarding the player credits / dollars based on the rules of the game.
    public void awardPlayer(boolean actSuccess, Player plyr) {
        if (actSuccess) {
            if (plyr.getRole().getIsOnCardRole()) {
                plyr.setPlayerCredits(plyr.getPlayerCredits() + 2);
            } else {
                plyr.setPlayerDollars(plyr.getPlayerDollars() + 1);
                plyr.setPlayerCredits(plyr.getPlayerCredits() + 1);
            }
            plyr.getPlayerRoom().getRoomScene().removeShotCounter();
        } else if (!actSuccess && !plyr.getRole().getIsOnCardRole()) {
            plyr.setPlayerDollars(plyr.getPlayerDollars() + 1);
        }
    }

    public void setActivePlayer(Player plyr) {
        this.activePlayer = plyr;
    }

    public Board getBoard() {
        return this.gameBoard;
    }

    //This function is only supposed to be called once per game.
    //It shuffles the deck of 40 cards into a random order
    public ArrayList<Card> shuffleDeck(ArrayList<Card> deck) {
        ArrayList<Card> shuffledDeck = new ArrayList<Card>();
        shuffledDeck.addAll(deck);

        int numCards = deck.size();
		
		for (int i = 0; i < numCards; i++) {
			int randItem = randInt(0, numCards-1);
			Card temp = shuffledDeck.get(i);	
			shuffledDeck.set(i, shuffledDeck.get(randItem)); 
			shuffledDeck.set(randItem, temp);
        }

        for (int i = 0; i < deck.size(); i++) {
            assert shuffledDeck.contains(deck.get(i)) : "Deck missing a card after shuffling";
            assert deck.contains(shuffledDeck.get(i)) : "Deck missing a card after shuffling";
        }

        assert shuffledDeck.size() == deck.size() : "Elements in deck have gone missing after shuffle";
        
        this.gameBoard.setDeck(shuffledDeck);
        
        return shuffledDeck;

    }

    /*
        Select 10 random cards, assign them to scene objects.
        Remove these cards from the pool deck
    */
    
    private void removeCardFromDeck(int cardIndex) {
        int originalDeckSize = this.getDeck().size();
        this.gameBoard.getDeck().remove(cardIndex);

        assert this.getDeck().size() == originalDeckSize - 1 : "Item not correctly removed from deck";

    }

    // This function creates randomized scene objects
    // It will remove the cards which were assigned to the random scene objects
    // to prevent re-use of cards.
    // it returns an array of scenes which were randomly assigned a card
    // from the board's deck ArrayList.

    public ArrayList<Scene> createRandomScenes(int numScenes) {
        this.shuffleDeck(this.getDeck());
        
        ArrayList<Scene> randomScenes = new ArrayList<Scene>();
        int originalDeckSize = this.getDeck().size();

        for (int i = 0; i < numScenes; i++) {
            Scene randScene = new Scene();
            randScene.setSceneCard(this.getDeck().get(i));
            randScene.setBoardManager(this);
            randomScenes.add(randScene);
            this.removeCardFromDeck(i);
        }

        assert randomScenes.size() == numScenes : 
                "Random scenes ArrayList not filled size: " 
                        + randomScenes.size() + " desired size: " + numScenes;

        assert this.getDeck().size() == originalDeckSize - numScenes : "Items not correctly removed from deck.";
        return randomScenes;
    }

    public void setPlayerRole(Player ply, Role role) {
        ply.setRole(role);
        role.setRoleAvailable(false);
    }


    // This function ensures that the scene's pointer to its room
    // and the room's pointer to its scene are respectively correct / synced.
    private void assignSceneToRoom(Scene scene, Room room) {
        scene.setSceneRoom(room);
        room.setRoomScene(scene);
        scene.setShotsRemaining(room.getNumTakes());
        room.printRoom();
    }

    
    private int compareRoles(Role a, Role b) {
        return  a.getRoleLevel() - b.getRoleLevel();
    }

    public ArrayList<Role> getAvailableRoles(Scene scene) {
        ArrayList<Role> availableRoles = new ArrayList<Role>();
        ArrayList<Role> onCardRoles = scene.getOnCardRoles();
        ArrayList<Role> offCardRoles = scene.getOffCardRoles();
        
        for (int i = 0; i < onCardRoles.size(); i++ ) {
            Role onCardRole = onCardRoles.get(i);
            if (onCardRole.getRoleAvailable()) {
                availableRoles.add(onCardRole);
            }
        }

        for (int i = 0; i < offCardRoles.size(); i++ ) {
            Role offCardRole = offCardRoles.get(i);
            if (offCardRole.getRoleAvailable()) {
                availableRoles.add(offCardRole);
            }
        }
        availableRoles.sort((Role a, Role b) -> compareRoles(a,b));
        return availableRoles;
    }

    public String getActionChoice(ArrayList<String> actions, Scanner choiceScanner) {
        String choice = "default";
        int actionIndex;
        boolean validChoice = false;
        boolean indexInRange = false;
        while (!validChoice) {
            choice = choiceScanner.next();
            actionIndex = stringIsNumeric(choice) ? Integer.parseInt(choice) : -1;
            indexInRange = actionIndex >= 1 && actionIndex <= actions.size();
            
            if (indexInRange) {
                choice = actions.get(actionIndex-1);
            }

            validChoice = indexInRange;
        }
        return choice;
    } 

    public void doPlayerTurn(Player ply) {
        
        boolean done = false;
        Scanner choiceScanner = new Scanner(System.in);
        String choice = "";
        ArrayList<String> validActions = getValidActions(ply);
        boolean endOfDay = this.numberOfScenesRemaining == 1;
        while (!done && !endOfDay) {
            ply.printPlayer();    
            printAvailableActions("",validActions);
            System.out.println("Enter a valid action as a number: ");

            choice = getActionChoice(validActions, choiceScanner);

            done = doAction(ply, choice, choiceScanner);
            validActions = registerAction(validActions, ply, choice);
        }
    }
    
    public boolean doAction(Player ply, String choice, Scanner choiceScanner) {
        boolean turnFinished = false;
        switch (choice) {
            case "move":
                doMoveIo(choiceScanner, ply);
                break;
            case "upgrade":
                doUpgradeIo(ply, choiceScanner);
                
                break;
            case "rehearse":
                doRehearseIo(ply);
                break;
            case "act":
                doActIo(ply);
                break;
            case "take role":
                doTakeRoleIo(ply, choiceScanner);
                break;
            case "end":
                turnFinished = true;
                break;
            default:
                System.out.println("doAction: Arrived at default case");
        }
        return turnFinished;
    }

    //This modifies the pool of valid actions that a player can perform. It maintains 2a list
    //of actions a player may perform based on their previous actions.
    public ArrayList<String> registerAction(ArrayList<String> actions, Player ply, String chosenAction) {
        Room playerRoom = ply.getPlayerRoom();
        Room castingOffice = this.getBoard().getCastingOffice();
        Room trailers = this.getBoard().getTrailers();

        if (actions.contains(chosenAction)) {
            actions.remove(chosenAction);
        }
        
        if (chosenAction.equals("move")) {
            if (playerRoom != castingOffice && playerRoom != trailers) {
                if (!actions.contains("take role") && getAvailableRoles(playerRoom.getRoomScene()).size() != 0) {
                    actions.add("take role");
                }
                actions.remove("upgrade");
            } else if (playerRoom == castingOffice || playerRoom == trailers) {
                actions.remove("take role");
            }
        } else if (chosenAction.equals("rehearse")) {
            actions.remove("act");
        } else if (chosenAction.equals("act")) {
            actions.remove("rehearse");
        } else if (chosenAction.equals("upgrade")) {
            actions.remove("move");
        } else if (chosenAction.equals("take role")) {
            actions.remove("move");
        }


        return actions;
    }
    
    private ArrayList<String> getAvailableRanks(int playerRank) {
        System.out.println("Available Ranks: ");
        int maxRank = 6;
        ArrayList<String> rankChoices = new ArrayList<String>();
        for (int i = playerRank+1; i <= maxRank; i++) {
            rankChoices.add(String.valueOf(i));
        }
        return rankChoices;
    }

    private void printAvailableRanks(int playerRank) {
        int maxRank = 6;

        for (int i = playerRank+1; i <= maxRank; i++) {
            int creditCost = this.getBoard().getCreditUpgradeCost(i);
            int dollarCost = this.getBoard().getDollarUpgradeCost(i);
            System.out.printf("Rank %d: \n\t* Credits: %d\n\t* Dollars: %d\n", i, creditCost, dollarCost);
        }
    }

    public void doUpgradeIo(Player ply, Scanner choiceScanner) {
        int maxRank = 6;
        int playerRank = ply.getPlayerRank();
        
        ArrayList<String> currencyChoices = new ArrayList<String>(Arrays.asList("credits", "dollars"));
        ArrayList<String> rankChoices = getAvailableRanks(ply.getPlayerRank());

        if (playerRank < maxRank) {
            
            System.out.println("Enter a choice of rank as a number.\n");
            printAvailableActions("Rank ", rankChoices);
            boolean chooseRank = true;
            while (chooseRank) {
                printAvailableRanks(ply.getPlayerRank());
                System.out.printf("Player Dollars: %d Player Credits: %d\n", ply.getPlayerDollars(), ply.getPlayerCredits());
                String rankChoice = getActionChoice(rankChoices, choiceScanner);
                int rank = Integer.parseInt(rankChoice);
                boolean canUpgrade = false;
                System.out.println("Select currency to use: ");
                printAvailableActions("Currency ", currencyChoices);
                
                String currencyChoice = getActionChoice(currencyChoices, choiceScanner);
                
                if (currencyChoice.equals("dollars")) {
                    canUpgrade = ply.upgrade(rank, ply.getPlayerDollars(), 0);
                } else {
                    canUpgrade = ply.upgrade(rank, 0, ply.getPlayerCredits());
                }

                if (!canUpgrade) {
                    System.out.println("Insufficient funds! Try again? Y/n?");
                    chooseRank = choiceScanner.next().toLowerCase().contains("y");
                } else {
                    chooseRank = false;
                }
            } // end rank choosing loop
        } // end max rank check
    } // End upgradeIo

    public void doMoveIo(Scanner choiceScanner, Player ply) {
        ArrayList<String> neighboringRooms = ply.getPlayerRoom().getNeighborNames();
        System.out.println("Enter desired room as a number:");
        printAvailableActions("",neighboringRooms);
        String roomChoice = getActionChoice(neighboringRooms, choiceScanner);
        ply.move(this.getBoard().getRoomByName(roomChoice));

        /* if scene card is face down */
        if (!ply.getPlayerRoom().getFlippedOver()){
            //bc.flipCardImage(ply.getPlayerRoom().getRoomScene().getImageTitle(), ply.getPlayerRoom().getRoomName());
            ply.getPlayerRoom().setFlippedOver(true);
        }

    }

    //Prints a list of role objects.
    public void printRoles(ArrayList<Role> roleList) {
        int roleNum = 1;
        for (Role r: roleList) {
            System.out.print(roleNum + ". ");
            r.printRole();
            roleNum++;
        }
    }

    private boolean stringIsNumeric(String input) {
        
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException exc) {
            return false;
        }

        return true;
    }

    public void doTakeRoleIo(Player ply, Scanner choiceScanner) {
        Room plyRoom = ply.getPlayerRoom();
        ArrayList<Role> roles = this.getAvailableRoles(plyRoom.getRoomScene());
        boolean choiceValid = false;
        int choice = -1;
        System.out.println("Enter role selection as a number\n\t* Enter any character other than a valid selection to escape role selection.");
        System.out.println("Available roles:");
        this.printRoles(roles);
        while (!choiceValid) {
            String number = choiceScanner.next();
            choice = stringIsNumeric(number) ? Integer.parseInt(number):-1;
            boolean choiceInValidRange = (choice >= 1) && (choice <= roles.size());
            boolean roleIsAvailable = choiceInValidRange ? roles.get(choice-1).getRoleAvailable() : false;

            if (choiceInValidRange && roleIsAvailable && ply.playerCanTakeRole(roles.get(choice-1))) {
                this.setPlayerRole(ply, roles.get(choice-1));
                ply.setRehearsalBonus(0);
                ply.getRole().printRole();
                choiceValid = true;   
            } else {
                System.out.println("Invalid choice");
                System.out.println("Choose a different role? y/n: ");
                choiceValid = !choiceScanner.next().toLowerCase().equals("y");
                if (!choiceValid) {
                    System.out.println("Enter choice as a number.");
                }
            }
        }
    }

    public boolean doActIo(Player ply) {
        boolean actSuccess = ply.act();
        Room room = ply.getPlayerRoom();
        System.out.println("Card: " + room.getRoomScene().getSceneCard().getName());
        if (actSuccess) {
            System.out.printf("%s acted successfully on role: %s\n", ply.getName(), ply.getRole().getRoleName());
        } else {
            System.out.println("Acting not successful.");
        }
        

        if (room.getRoomScene().getShotsRemaining() == 0) {
            System.out.println("Room's scene is finished! Awarding bonuses!\n");
            
            this.payBonuses(room);
            this.finishRoomScene(room);
            System.out.println("Scenes remaining: " + this.numberOfScenesRemaining);
            System.out.println("Game Day: " + this.dayOfGame);
        }

        return actSuccess;
    }

    private void finishRoomScene(Room room) {
        ArrayList<Role> offCards = room.getOffCardRoles();
        ArrayList<Role> onCards = room.getRoomScene().getOnCardRoles();

        ArrayList<Player> playersInRoom = room.getPlayersInRoom();
        for (Player p: playersInRoom) {
            p.setPlayerInRole(false);
        }   
        
        for (Role plyRole: onCards) {
            plyRole.setRoleAvailable(false);
        }

        for (Role plyRole: offCards) {
            plyRole.setRoleAvailable(false);
        }

        this.numberOfScenesRemaining--;
    }

    public boolean doRehearseIo(Player ply) {
        boolean rehearseSuccess = ply.rehearse();
        if (!rehearseSuccess) {
            System.out.println("Rehearsal is not permitted, the player has a 100% chance to act successfully.");
            System.out.println("Acting for " + ply.getName() + ".");
            doActIo(ply);
        } else {
            System.out.println("Player rehearsal bonus now: " + ply.getRehearsalBonus());
        }

        if (ply.getPlayerRoom().getRoomScene().getShotsRemaining() == 0) {
            System.out.println("Scene finished! Paying out bonuses.");
        }

        return rehearseSuccess;
    }

    public void printAvailableActions(String prefix, ArrayList<String> actions) {
        System.out.println("Valid Actions: ");
        int actionNum = 1;
        for (String action: actions) {
            System.out.printf("\t* %d. %s%s\n", actionNum,  prefix,  action);
            actionNum++;
        }
    }

    // Based on which room a player is in, there is a set of valid actions
    // a player may perform. This function returns those actions as an array list
    // of strings. This function is intended to be called before the player has performed
    // any actions once per doPlayerTurn(). 
    //registerAction shall then be used to remove actions from the valid pool. 

    public ArrayList<String> getValidActions(Player ply) {
        Room castingOffice = this.getBoard().getCastingOffice();
        Room trailers = this.getBoard().getTrailers();
        Room playerRoom = ply.getPlayerRoom();
        ArrayList<String> actions = new ArrayList<String>();
        actions.add("end");
        if (!ply.getPlayerInRole()) {
           actions.add("move");
           
            if (playerRoom != castingOffice && playerRoom != trailers && playerRoom.getRoomScene() != null){
                if (getAvailableRoles(playerRoom.getRoomScene()).size() != 0) {
                    actions.add("take role");
                }
            }
        } else {
            Scene plyScene = ply.getPlayerRoom().getRoomScene();
            if (plyScene != null && plyScene.getShotsRemaining() > 0) {
                actions.add("act");
                actions.add("rehearse");
            }
        }

        int maxRank = 6;
        if (playerRoom == castingOffice && ply.getPlayerRank() < maxRank) {
            actions.add("upgrade");
        }

        return actions;
    }

    public int getNumberOfScenesRemaining() {
        return this.numberOfScenesRemaining;
    }

    public void dealScenes(int numScenes) {
        ArrayList<Room> rooms = this.gameBoard.getRooms();
        
        // It should be noted that not all rooms are contained within the board object
        // rooms.size() should be equivalent to the number of rooms in which a player can actually
        // act. The board should deal 10 scene cards at one time because there are 10 rooms.

        assert rooms.size() == numScenes : "Scenes not being dealt to every room.";
        ArrayList<Scene> randomScenes = this.createRandomScenes(numScenes);

        for (int i = 0; i < numScenes; i++) {
            Scene roomScene = randomScenes.get(i);
            Room dealRoom = rooms.get(i);
            assert roomScene != null : "Do not assign null scenes to a room object";
            assignSceneToRoom(roomScene, dealRoom);
        }

        this.numberOfScenesRemaining = numScenes;
    }

}