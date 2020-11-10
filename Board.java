import java.util.ArrayList;
import java.util.Random; 

public class Board {

	/* Private Attributes */

	//This is the card deck
	private ArrayList<Scene> boardScenes;
	
	//List of board room objects as parsed from board.xml
	private ArrayList<Room> boardRooms;

	//
	private ArrayList<Player> players;

	private Room castingOffice;
	private Room trailers;
	
	private int[] dollarUpgradeCosts = {4, 10, 18, 28, 40}; // changed this from ArrayList to int[] as int[i] is better than 
	private int[] creditUpgradeCosts = {5, 10, 15, 20, 25}; // .get(i) and both lists are constant 
	
	/* Public Attributes*/

    
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

	public int getCreditUpgradeCost(int targetLevel) {
		return this.creditUpgradeCosts[targetLevel-1];
	}

	public int getDollarUpgradeCost(int targetLevel) {
		return this.dollarUpgradeCosts[targetLevel-1];
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

}
