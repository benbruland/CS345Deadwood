import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random; 

public class Board {

	private class UpgradeCost {
		private int creditCost;
		private int dollarCost;
		public UpgradeCost() {

		}

		public UpgradeCost(int credits, int dollars) {
			this.creditCost = credits;
			this.dollarCost = dollars;
		}

		public int getCreditCost() {
			return this.creditCost;
		}

		public int getDollarCost() {
			return this.dollarCost;
		}
	}

	/* Private Attributes */

	//This is the card deck
	private ArrayList<Scene> boardScenes;
	
	//List of board room objects as parsed from board.xml
	private ArrayList<Room> boardRooms;

	//
	private ArrayList<Player> players;
	
	private ArrayList<UpgradeCost> costs;
	
	/* Public Attributes*/

    
	//TODO Implement Board Constructor
    public Board() {
		this.boardRooms = new ArrayList<Room>();
		this.boardScenes = new ArrayList<Scene>();
		this.players = new ArrayList<Player>();
	}
	
	public Board(ArrayList<Room> rooms, ArrayList<Scene> scenes, ArrayList<Player> plys) {
		this.boardRooms = rooms;
		this.boardScenes = scenes;
		this.players = plys;
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
}
