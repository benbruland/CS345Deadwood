import java.util.ArrayList;
import java.util.Random;

public class BoardManager {

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

    public BoardManager(int numPlys) {
        assert numPlys >= 2 && numPlys <= 8 : "Invalid number of players: " + numPlys ;
        this.numPlayers = numPlys;
        this.numberOfDays = numPlys > 3 ? 4 : 3;
        this.dayOfGame = 1; 
        this.initBoard(numPlys);
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
        Player newPlayer = new Player();

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
        ArrayList<Room> rooms = this.getBoard().getRooms();
        for (int i = 0; i < rooms.size(); i++) {
            rooms.get(i).getRoomScene().printScene();
        }
    }

    public Player getActivePlayer() {
        return this.activePlayer;
    }

    //TODO Implement CycleGameDay()
    public void cycleGameDay() {
        assert this.dayOfGame >= 1 : "Invalid game day number: " + this.dayOfGame;
        assert this.dayOfGame <= this.numberOfDays : "Invalid game day " + this.dayOfGame + " should be between 1 and " + this.numberOfDays;
    }

    //TODO Implement ScoreGame()
    // Should return the winner of the game's player object.
    public Player scoreGame() {
        return null;
    }

    // Returns a random integer in range [min, max]
    private int randInt(int min, int max) {

        Random randGen = new Random();
        int randomNum = randGen.nextInt((max - min) + 1) + min;
    
        return randomNum;
    }

    //TODO Implement PayBonuses()
    public void payBonuses(Scene sceneToPayout) {

    }

    //TODO discuss awardPlayer()
    public void awardPlayer() {

    }


    public void setActivePlayer(Player ply) {
        this.activePlayer = ply;
    }

    //TODO Implement DoPlayerTurn
    public void doPlayerTurn(Player ply) {
        
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

    //TODO implement createRandomScenes
    
    /*
        Select 10 random cards, assign them to scene objects.
        Remove these cards from the pool deck
    */
    
    private void removeCardFromDeck(int cardIndex) {
        int originalDeckSize = this.getDeck().size();
        this.getBoard().getDeck().remove(cardIndex);

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
        ArrayList<Room> rooms = this.getBoard().getRooms();
        
        // It should be noted that not all rooms are contained within the board object
        // rooms.size() should be equivalent to the number of rooms in which a player can actually
        // act. The board manager should deal 10 scene cards at one time because there are 10 rooms.

        assert rooms.size() == numScenes : "Scenes not being dealt to every room.";
        ArrayList<Scene> randomScenes = this.createRandomScenes(numScenes);

        for (int i = 0; i < numScenes; i++) {
            Scene roomScene = randomScenes.get(i);
            Room dealRoom = rooms.get(i);
            assignSceneToRoom(roomScene, dealRoom);
        }
    }
}