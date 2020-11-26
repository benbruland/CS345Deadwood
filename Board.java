import java.util.ArrayList;
import java.util.Random; 

public class Board {

	/* Private Attributes */
	
	// This BoardManager object is responsible for 
    // controling the state of all game objects.
    // The board manager is created in Deadwood.java
    private static BoardManager boardManager = BoardManager.getInstance();
	
	//This is the card deck
	private ArrayList<Scene> boardScenes;
	
	//List of board room objects as parsed from board.xml
	private ArrayList<Room> boardRooms;
	
	// deck of all scene cards to be randomly assigned to scenes.
	private ArrayList<Card> deck;

	private ArrayList<Player> players;

	private Room castingOffice;
	private Room trailers;
	
	private int[] dollarUpgradeCosts;
	private int[] creditUpgradeCosts;
	
	/* Public Attributes*/

    
    public Board() {
		this.boardRooms = new ArrayList<Room>();
		this.boardScenes = new ArrayList<Scene>();
		this.players = new ArrayList<Player>();
	}
	
	public Board(ArrayList<Room> rooms, ArrayList<Card> cardDeck, int[][] costs, Room casting, Room trailer) {
		this.boardRooms = rooms;
		this.deck = cardDeck;
		this.dollarUpgradeCosts = costs[0];
		this.creditUpgradeCosts = costs[1];
		this.castingOffice = casting;
		this.trailers = trailer;
	}

	// Gets a random integer, assumes min integer to be 0
	private int randomNum(int max) {
		Random rand = new Random();
		return rand.nextInt(max);
	}

	// This method returns a randomly re-ordered version of the Scenes array list.
	// it does this by randomly swapping elements
	public void shuffleSceneOrder() {
		int numScenes = this.boardScenes.size();
		
		for (int i = 0; i < numScenes; i++) {
			
			int randItem = randomNum(numScenes);
			Scene temp = this.boardScenes.get(i);
			
			this.boardScenes.set(i, this.boardScenes.get(randItem)); 
			this.boardScenes.set(randItem, temp);
		}
	
	}
	
	public void printRooms() {
		System.out.println();
		ArrayList<Room> rooms = this.boardRooms;
		int listSize = rooms.size();
		System.out.println("=====================Board Rooms=====================");
		for (int i = 0; i < listSize; i++) {
			rooms.get(i).printRoom();
		}
		this.castingOffice.printRoom();
		this.trailers.printRoom();
		System.out.println("=====================================================");
    }

    public void printDeck() {
		System.out.println();
		ArrayList<Card> cardDeck = this.deck;
		int listSize = cardDeck.size();
		System.out.println("=====================Card Deck=====================");
		for (int i = 0; i < listSize; i++) {
			cardDeck.get(i).printCard();
		}
		System.out.println("===================================================");
		System.out.println();
	}
	
    public void printCosts() {
		System.out.println();
		System.out.println("Dollar Costs: ");
		for (int i = 0; i < dollarUpgradeCosts.length; i++) {
			System.out.printf("\t * Level %d costs: %d dollars.\n", i+2, this.dollarUpgradeCosts[i]);
		}

		System.out.println("Credit Costs: ");
		for (int i = 0; i < creditUpgradeCosts.length; i++) {
			System.out.printf("\t * Level %d costs: %d credits.\n", i+2, this.creditUpgradeCosts[i]);
		}
		System.out.println();
	}
	
    public void printBoard() {
		printRooms();
		printDeck();
		printCosts();
	}
	
	public Room getPlayerRoom(int playerId) {
		ArrayList<Room> rooms = this.boardRooms;
		Player ply = this.players.get(playerId);
		Room playerRoom = null;

		if (castingOffice.roomContainsPlayer(ply)) {
			playerRoom = castingOffice;
		} else if(trailers.roomContainsPlayer(ply)) {
			playerRoom = trailers;
		}

		if (playerRoom == null) {
			for (int i = 0; i < rooms.size() && playerRoom == null; i++) {
				if (rooms.get(i).roomContainsPlayer(ply)) {
					playerRoom = rooms.get(i);
				}	
			}
		}	

		return playerRoom;
	} 

	public Room getRoomByName(String roomName) {
		ArrayList<Room> rooms = this.getBoardRooms();
		Room desiredRoom = null;
		if (roomName.equals("office")) {
			desiredRoom = castingOffice;
		} else if(roomName.equals("trailer")) {
			desiredRoom = trailers;
		}
		for (int i = 0; i < rooms.size() && desiredRoom == null; i++) {
			if (rooms.get(i).getRoomName().equals(roomName)) {
				desiredRoom = rooms.get(i);
				return desiredRoom;
			}
		}
		return desiredRoom;
	}

	
	public ArrayList<Card> getDeck() {
		return this.deck;
	}
	
	public ArrayList<Scene> getBoardScenes(){
		return this.boardScenes;
	}

	public ArrayList<Room> getBoardRooms(){
		return this.boardRooms;
	}

	public Room getTrailers() {
		return this.trailers;
	}

	public Room getCastingOffice() {
		return this.castingOffice;
	}

	public ArrayList<Scene> getScenes(){
		return this.boardScenes;
	}
	
	public ArrayList<Player> getPlayers(){
		return this.players;
	}

	public ArrayList<Room> getRooms() {
		return this.boardRooms;
	}

	public void setRooms(ArrayList<Room> rooms) {
		this.boardRooms = rooms;
	}

	public void setPlayers(ArrayList<Player> plys) {
		this.players = plys;
	}

	public void setScenes(ArrayList<Scene> scns) {
		this.boardScenes= scns;
	}

	//Assumes that upgrade costs has been filled in
	public int getCreditUpgradeCost(int targetLevel) {
		return this.creditUpgradeCosts[targetLevel - 2];
	}

	public int getDollarUpgradeCost(int targetLevel) {
		return this.dollarUpgradeCosts[targetLevel - 2];
	}

	public void setDollarUpgradeCosts(int[] upgrades) {
		this.dollarUpgradeCosts = upgrades;
	}

	public void setCreditUpgradeCosts(int[] upgrades) {
		this.creditUpgradeCosts = upgrades;
	}

	public void setTrailers(Room trailer) {
		this.trailers = trailer;
	}

	public void setCastingOffice(Room casting) {
		this.castingOffice = casting;
	}

	public void setDeck(ArrayList<Card> cards) {
		this.deck = cards;
	}

	public void setBoardManager(BoardManager mngr) {
		this.boardManager = mngr;
	}

	public BoardManager getBoardManager() {
		return this.boardManager;
	}

	
}
