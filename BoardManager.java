import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;

public class BoardManager{

    /* Primitive Attributes */
    private int numPlayers;
    private int dayOfGame;
    private int numberOfDays;

    /* Non-Primitive Attributes */
    private Board gameBoard;
    private Player activePlayer;


    //TODO Implement Constructors
    public BoardManager() {
        this.numPlayers = 0;
        this.dayOfGame = 1;
        this.numberOfDays = 3;
    }

    public BoardManager(int numPlyrs) {
        assert numPlyrs >= 2 && numPlyrs <= 8 : "Invalid number of players: " + numPlyrs ;
        this.numPlayers = numPlyrs;
        this.numberOfDays = numPlyrs > 3 ? 4 : 3;
        this.dayOfGame = 1; 
        this.initBoard(numPlyrs);
        this.activePlayer = chooseFirstPlayer();
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

        newPlayer.setPlayerRoom(this.gameBoard.getTrailers());
        return newPlayer;
    }

    public ArrayList<Card> getDeck() {
        return this.getBoard().getDeck();
    }

    // Sets up the board for the given number of players
    // Responsible for the creation of the gameBoard object
    private void initBoard(int numPlayers) {
        XMLParser parser = new XMLParser();
        this.gameBoard = parser.parseBoard();
        Board board = this.gameBoard;

        ArrayList<Player> players = new ArrayList<Player>();
        
        for (int i = 0; i < numPlayers; i++) {
            Player newPlayer = makeInitialPlayer(numPlayers, i);
            players.add(newPlayer);
        }
        
        board.setPlayers(players);
        this.dealScenes(10);
        // ArrayList<Room> rooms = this.getBoard().getRooms();
        // for (int i = 0; i < rooms.size(); i++) {
        //     rooms.get(i).getRoomScene().printScene();
        // }
    }

    public Player getActivePlayer() {
        return this.activePlayer;
    }

    public int getCurrentDay(){
        return this.dayOfGame;
    }

    /* Increments the day counter, checks for end of game, and returns winning player if game over and null if not. */
    public Player cycleGameDay() {
        assert this.dayOfGame >= 1 : "Invalid game day number: " + this.dayOfGame;
        assert this.dayOfGame <= this.numberOfDays : "Invalid game day " + this.dayOfGame + " should be between 1 and " + this.numberOfDays;
        this.dayOfGame += 1;
        
        /* Checking to see if game is over, no need to reset board for next day if so */
        if(this.dayOfGame > this.numberOfDays){
            Player p = scoreGame();
            return p;
        }
        /* Removing scenes from previous day */
        ArrayList<Room> rms = this.gameBoard.getBoardRooms();
        for(Room rm : rms){
            this.removeSceneFromRoom(rm);
        }

        /* Dealing scenes to each room */
        this.dealScenes(10);


        /* Returning players to Trailer room */
        for(Player plyr : this.gameBoard.getPlayers()){
            plyr.setPlayerRoom(this.gameBoard.getTrailers());
        }
        return null;
    }

    /* Assumes that only one winner and will return the first player obj with "highest score", called in cycleGameDay() */
    public Player scoreGame() {
        ArrayList<Player> plyrs = gameBoard.getPlayers();
        Player winner = null;
        int maxScore = 0;

        for(Player plyr : plyrs){
            int temp = plyr.getPlayerCredits() + plyr.getPlayerDollars() + (plyr.getPlayerRank() * 5);
            if(temp > maxScore){
                maxScore = temp;
                winner = plyr;
            }    
        }
        return winner;
    }

    // Returns a random integer in range [min, max]
    private int randInt(int min, int max) {

        Random randGen = new Random();
        int randomNum = randGen.nextInt((max - min) + 1) + min;
    
        return randomNum;
    }

    public void payBonuses(Scene sceneToPayout) {
        ArrayList<Integer> dieRolls = new ArrayList<Integer>();
			for(int i = 0; i < this.activePlayer.getPlayerCard().getBudget(); i++){
				dieRolls.add(this.activePlayer.rollDice(6));
			}
			Collections.sort(dieRolls);
			int topRolePayout = 0;
			int middleRolePayout = 0;
			int bottomRolePayout = 0;
			int j = 0;

			/* Loop collecting on card bonuses into top, middle, and bottom */
			for(int i = dieRolls.size() - 1; i >= 0; i--){
				j++;
				if(j == 1){
					topRolePayout += dieRolls.get(i);
				}
				else if (j == 2){
					middleRolePayout += dieRolls.get(i);
				}
				else{
					bottomRolePayout += dieRolls.get(i);
					j = 0;
				}
			}

			/* Giving bonuses to on card players */
			for(Player plyr : this.activePlayer.getPlayerRoom().getRoomScene().getPlayersOnCard()){
				if(plyr.getRole().getRoleID() == 2){ //Highest Ranking Role
					plyr.setPlayerDollars(plyr.getPlayerDollars() + topRolePayout);
				}
				else if(plyr.getRole().getRoleID() == 1){ //Middle Ranking Role
					plyr.setPlayerDollars(plyr.getPlayerDollars() + middleRolePayout);
				}
				else { //Lowest Ranking Role
					plyr.setPlayerDollars(plyr.getPlayerDollars() + bottomRolePayout);
				}
			}

			/* Giving bonuses to off card players */
			for(Player plyr : this.activePlayer.getPlayerRoom().getRoomScene().getPlayersOffCard()){
				plyr.setPlayerDollars(plyr.getPlayerDollars() + plyr.getRole().getRoleLevel());
			}
    }

    public void awardPlayer(Player plyr) {
        if(plyr.getRole().getIsOnCardRole()){ // On card role
            plyr.setPlayerCredits(plyr.getPlayerCredits() + 2);
            plyr.getPlayerRoom().getRoomScene().removeShotCounter();
        }
        else { // Off card role
            plyr.setPlayerDollars(plyr.getPlayerCredits() + 1);
            plyr.setPlayerCredits(plyr.getPlayerCredits() + 1);
            plyr.getPlayerRoom().getRoomScene().removeShotCounter();
        }
    }


    public void setActivePlayer(Player plyr) {
        this.activePlayer = plyr;
    }

    /* Prompts player for turn choices, returns true if game over and false if not */
    public boolean doPlayerTurn(Player plyr) {
        Scanner playerChoice = new Scanner(System.in);
        String action;
        boolean validChoice;
        boolean retVal;
        if(plyr.getPlayerInRole()){
            action = "";
            validChoice = false;
            while(!validChoice && action != "exit"){
                System.out.println("Type one of the following options to perform for your turn:\n\t 1. Rehearse\n\t 2. Act\n");
                action = playerChoice.next();
                switch(action){
                    case "EXIT":
                    case "Exit":
                    case "exit":
                    case "E":
                    case "e":
                        action = "exit";
                        validChoice = true;
                        break;
                    case "Rehearse":
                    case "rehearse":
                    case "1":
                    case "1.":
                        retVal = plyr.performRehearse();
                        if(retVal){
                            System.out.println("Rehearse successful.");
                        }
                        else {
                            System.out.println("Rehearse unsuccessful.");
                        }
                        validChoice = true;
                        break;
                    case "Act":
                    case "act":
                    case "2.":
                    case "2":
                        retVal = plyr.performAct();
                        if(retVal){
                            System.out.println("Act successful.");
                            awardPlayer(plyr);
                            /* Last shot counter removed */
                            if(plyr.getPlayerRoom().getRoomScene().getShotCount() == 0){
                                System.out.println("Last shot counter for scene removed!");
                                /* At least one player occupies on card role */
                                if(!plyr.getPlayerRoom().getRoomScene().getPlayersOnCard().isEmpty()){
                                    System.out.println("Paying bonuses to players on finished scene.");
                                    payBonuses(plyr.getPlayerRoom().getRoomScene());
                                }
                                else{
                                    System.out.println("Scene has no on-card players, so no bonuses will be paid.");
                                }
                                /* Only one scene remaining */
                                if(plyr.getPlayerRoom().getRoomScene().getScenesRemaining() == 1){
                                    /* Returns winner if game over and null if not */
                                    Player winner = cycleGameDay();
                                    if (winner != null){
                                        return true;
                                    }
                                    System.out.println("=====");
                                    System.out.println("DAY " + getCurrentDay());
                                    System.out.println("=====");
                                }
                            }
                        }
                        else {
                            System.out.println("Act unsuccessful.");
                            if(!plyr.getRole().getIsOnCardRole()){
                                System.out.println("Player has off card role and is award one dollar.");
                                plyr.setPlayerDollars(plyr.getPlayerDollars() + 1);
                            }
                        }
                        validChoice = true;
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        }
        else{
            System.out.println("Type one of the following options to perform for your turn:\n\t 1. Move\n\t 2. Upgrade\n\t 3. Take Role");
            action = playerChoice.nextLine();
            validChoice = false;
            while(!validChoice){
                switch(action){
                    case "Move":
                    case "move":
                    case "1.":
                    case "1":

                        ArrayList<Room> allRms = this.gameBoard.getBoardRooms();
                        ArrayList<String> adjRms = plyr.getPlayerRoom().getNeighbors();
                        for(Room rm: allRms){
                            if(adjRms.contains(rm.getRoomName())){
                                System.out.println("RoomID: " + rm.getRoomID() + ", Room Name: " + rm.getRoomName());
                            }
                        }
                        System.out.println("Type the roomID of the room you would like to move to from the list above: ");
                        action = playerChoice.nextLine();
                        for(int i = 0; i < allRms.size(); i++){
                            if(adjRms.contains(allRms.get(i).getRoomName()) && allRms.get(i).getRoomID() == Integer.parseInt(action)){
                                Room desiredRoom = allRms.get(i);
                                System.out.println("Performing move on player to room " + allRms.get(i).getRoomName());
                                validChoice = plyr.performMove(desiredRoom);
                                break;
                            }
                        }
                        if(validChoice){
                            System.out.println("Player succesfully moved to specified room.");
                        }
                        else { 
                            System.out.println("Invalid room selection, please select a valid option for player turn.");
                        }
                        break;

                    case "Upgrade":
                    case "upgrade":
                    case "2.":
                    case "2":
                        
                        boolean validUpgrade = false;
                        String upgradeChoice = "";
                        int selection;
                        while(!validUpgrade && upgradeChoice != "exit"){
                            System.out.println("Type which upgrade you would like from the options below: \n\t 2. Rank 2, Costs 4 dollars & 5 credits \n\t 3. Rank 3, Costs 10 dollars & 10 credits \n\t 4. Rank 4, Costs 18 dollars & 15 credits \n\t 5. Rank 5, Costs 28 dollars & 20 credits \n\t 6. Rank 2, Costs 40 dollars & 25 credits \n\t (Exit to quit)");
                            upgradeChoice = playerChoice.nextLine();
                            switch(upgradeChoice){
                                case "EXIT":
                                case "Exit":
                                case "exit":
                                case "E":
                                case "e":
                                    upgradeChoice = "exit";
                                    validUpgrade = true;
                                    break;                                
                                case "2.":
                                case "2":
                                    selection = 2;
                                    validUpgrade = plyr.performUpgrade(selection, this.gameBoard.getCreditUpgradeCost(selection), this.gameBoard.getDollarUpgradeCost(selection));
                                    if(!validUpgrade){
                                        System.out.println("Invalid upgrade; Not enough dollars or credits for upgrade");
                                    }
                                    break;
                                case "3.":
                                case "3":
                                    selection = 3;
                                    validUpgrade = plyr.performUpgrade(selection, this.gameBoard.getCreditUpgradeCost(selection), this.gameBoard.getDollarUpgradeCost(selection));
                                    if(!validUpgrade){
                                        System.out.println("Invalid upgrade; Not enough dollars or credits for upgrade");
                                    }
                                    break;
                                case "4.":
                                case "4":
                                    selection = 4;
                                    validUpgrade = plyr.performUpgrade(selection, this.gameBoard.getCreditUpgradeCost(selection), this.gameBoard.getDollarUpgradeCost(selection));
                                    if(!validUpgrade){
                                        System.out.println("Invalid upgrade; Not enough dollars or credits for upgrade");
                                    }
                                    break;
                                case "5.":
                                case "5":
                                    selection = 5;
                                    validUpgrade = plyr.performUpgrade(selection, this.gameBoard.getCreditUpgradeCost(selection), this.gameBoard.getDollarUpgradeCost(selection));
                                    if(!validUpgrade){
                                        System.out.println("Invalid upgrade; Not enough dollars or credits for upgrade");
                                    }
                                    break;
                                case "6.":
                                case "6":
                                    selection = 6;
                                    validUpgrade = plyr.performUpgrade(selection, this.gameBoard.getCreditUpgradeCost(selection), this.gameBoard.getDollarUpgradeCost(selection));
                                    if(!validUpgrade){
                                        System.out.println("Invalid upgrade; Not enough dollars or credits for upgrade");
                                    }
                                    break;
                                default:
                                    System.out.println("Invalid selection, please enter a valid choice.");
                            }
                        }
                        if(validUpgrade){
                            System.out.println("Upgrade successful.");
                            validChoice = true;
                        }
                        else {
                            System.out.println("Upgrade unsuccessful, please select another option for player turn.");
                        }
                        break;
                    case "Take Role":
                    case "take role":
                    case "Take":
                    case "take":
                    case "Role":
                    case "role":
                    case "3.":
                    case "3":
                        try{    
                            ArrayList<Role> offCard = plyr.getPlayerRoom().getOffCardRoles();
                            ArrayList<Role> onCard = plyr.getPlayerCard().getRoles();
                            ArrayList<Integer> roleIDs = new ArrayList<Integer>();
                            System.out.println("ON CARD ROLES");
                            for(Role rl : onCard){
                                roleIDs.add(rl.getRoleID());
                                System.out.println("RoleID: " + rl.getRoleID() + ", Rank: " + rl.getRoleLevel() + ", Title: " + rl.getRoleName() + ", Line: " + rl.getRoleLine());
                            }
                            System.out.println("OFF CARD ROLES");
                            for(Role rl : offCard){
                                roleIDs.add(rl.getRoleID());
                                System.out.println("RoleID: " + rl.getRoleID() + ", Rank: " + rl.getRoleLevel() + ", Title: " + rl.getRoleName() + ", Line: " + rl.getRoleLine());
                            }
                            System.out.println("Type desired role's roleID from list of roles above: ");
                            action = playerChoice.nextLine();
                            boolean validID = plyr.performChooseRole(Integer.parseInt(action));
                            if(validID){
                                System.out.println("Valid roleID, player is now working role with roleID = " + action);
                                System.out.println("Player now has opportunity to act/rehearse.");
                                validChoice = false;
                                while(!validChoice && action != "exit"){
                                    System.out.println("Type one of the following options to perform for your turn:\n\t 1. Rehearse\n\t 2. Act\n\t (Exit to quit)");
                                    action = playerChoice.nextLine();
                                    switch(action){
                                        case "EXIT":
                                        case "Exit":
                                        case "exit":
                                        case "E":
                                        case "e":
                                            action = "exit";
                                            validChoice = true;
                                            break;
                                        
                                        case "Rehearse":
                                        case "rehearse":
                                        case "1":
                                        case "1.":
                                            retVal = plyr.performRehearse();
                                            if(retVal){
                                                System.out.println("Rehearse successful.");
                                            }
                                            else {
                                                System.out.println("Rehearse unsuccessful.");
                                            }
                                            validChoice = true;
                                            break;
                                        case "Act":
                                        case "act":
                                        case "2.":
                                        case "2":
                                            retVal = plyr.performAct();
                                            if(retVal){
                                                System.out.println("Act successful.");
                                                awardPlayer(plyr);
                                                /* Last shot counter removed */
                                                if(plyr.getPlayerRoom().getRoomScene().getShotCount() == 0){
                                                    System.out.println("Last shot counter for scene removed!");
                                                    /* At least one player occupies on card role */
                                                    if(!plyr.getPlayerRoom().getRoomScene().getPlayersOnCard().isEmpty()){
                                                        System.out.println("Paying bonuses to players on finished scene.");
                                                        payBonuses(plyr.getPlayerRoom().getRoomScene());
                                                    }
                                                    else{
                                                        System.out.println("Scene has no on-card players, so no bonuses will be paid.");
                                                    }
                                                    /* Only one scene remaining */
                                                    if(plyr.getPlayerRoom().getRoomScene().getScenesRemaining() == 1){
                                                        /* Returns winner if game over and null if not */
                                                        Player winner = cycleGameDay();
                                                        if (winner != null){
                                                            return true;
                                                        }
                                                        System.out.println("=====");
                                                        System.out.println("DAY " + getCurrentDay());
                                                        System.out.println("=====");
                                                    }
                                                }
                                            }
                                            else {
                                                System.out.println("Act unsuccessful.");
                                                if(!plyr.getRole().getIsOnCardRole()){
                                                    System.out.println("Player has off card role and is award one dollar.");
                                                    plyr.setPlayerDollars(plyr.getPlayerDollars() + 1);
                                                }
                                            }
                                            validChoice = true;
                                            break;
                                        default:
                                            System.out.println("Invalid choice, please try again.");
                                    }

                                }
                            }
                            else {
                                System.out.println("Invalid roleID, role unavailable, or player rank not high enough for role.");
                                validChoice = false;
                            }
                        } catch (NullPointerException e){
                            System.out.println("No roles in this room.");
                            break;
                        }
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
                if(!validChoice){
                    System.out.println("Type one of the following options to perform for your turn:\n\t 1. Move\n\t 2. Upgrade\n\t 3. Take Role");
                    action = playerChoice.nextLine();
                }
            }
        }
        System.out.println("Scanner closed");
        playerChoice.close();
        return false;
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
            randomScenes.add(randScene);
            this.removeCardFromDeck(i);
        }

        assert randomScenes.size() == numScenes : 
                "Random scenes ArrayList not filled size: " 
                        + randomScenes.size() + " desired size: " + numScenes;

        assert this.getDeck().size() == originalDeckSize - numScenes : "Items not correctly removed from deck.";
        return randomScenes;
    }

    // This function ensures that the scene's pointer to its room
    // and the room's pointer to its scene are respectively correct / synced.
    private void assignSceneToRoom(Scene scene, Room room) {
        scene.setSceneRoom(room);
        room.setRoomScene(scene);
        scene.setShotsRemaining(room.getNumTakes());
    }

    // This function ensures that the scene's pointer to its room
    // and the room's pointer to its scene are respectively correct / synced.
    private void removeSceneFromRoom(Room room) {
        Scene roomScene = room.getRoomScene();
        room.setRoomScene(null);
        roomScene.setSceneRoom(null);
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
        
        return availableRoles;
    }

    private void dealScenes(int numScenes) {
        ArrayList<Room> rooms = this.gameBoard.getRooms();
        
        // It should be noted that not all rooms are contained within the board object
        // rooms.size() should be equivalent to the number of rooms in which a player can actually
        // act. The board should deal 10 scene cards at one time because there are 10 rooms.

        assert rooms.size() == numScenes : "Scenes not being dealt to every room.";
        ArrayList<Scene> randomScenes = this.createRandomScenes(numScenes);

        for (int i = 0; i < numScenes; i++) {
            Scene roomScene = randomScenes.get(i);
            Room dealRoom = rooms.get(i);
            assignSceneToRoom(roomScene, dealRoom);
        }
        /* resets scenes remaining counter in Scene.java */
        randomScenes.get(0).newDay();
    }

}