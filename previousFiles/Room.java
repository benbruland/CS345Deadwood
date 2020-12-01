import java.util.ArrayList;

public class Room {


	    // This BoardManager object is responsible for 
    // controling the state of all game objects.
    // The board manager is created in Deadwood.java
    private static BoardManager boardManager = BoardManager.getInstance();

	/* Primitive Attributes */
	private int roomID;
	private int numTakes;
	private String roomName;
	private GuiData data;
	private ArrayList<GuiData> shotPositions;
	/* Non-primitive Attributes*/
	
	//All on-card roles stored in scenes, these are randomly assigned.
	private Scene roomScene;	

	//Off-card roles associated with specific sets / rooms
	private	ArrayList<Role> offCardRoles;
	
	private ArrayList<Player> playersInRoom;
	private ArrayList<String> neighbors;
	
	/* No argument constructor*/
	public Room() {

	}
	
	public Room(int id, String name) {
		assert name != "" : "Name must be non-empty";
		assert id >= 0 : "room id must be a valid index into boardRooms";
		this.roomID = id;
		this.roomName = name;
	}

	/* Constructor for basic acting rooms */
	public Room(int id, String name, Scene scene, ArrayList<String> roomList, ArrayList<Role> offCards, int takes) {
		this.roomID = id;
		this.roomName = name;
		this.roomScene = scene;
		this.neighbors = roomList;
		this.offCardRoles = offCards;
		this.numTakes = takes;
		assert takes >= 0 && takes < 4 : "Invalid number of shots for room : " + name;
		this.playersInRoom = new ArrayList<Player>();
	}

	/*
	 * ====== PRIVATE METHODS ======
	 */


	/*
	 * ====== PUBLIC METHODS ======
	 */
	
	public void printRoom() {
		System.out.println();
		
		System.out.println("Room Name: " + this.roomName);
		System.out.println("Room ID: " + this.roomID);
		System.out.println("Num Takes : " + this.numTakes);
		System.out.println("Room Off Card Roles: ");
		
		int listSize = this.offCardRoles.size();
		System.out.println("Number Offcard roles: " + listSize);
		for (int i = 0; i < listSize; i++) {
			System.out.println("======================================");
			this.offCardRoles.get(i).printRole();
			System.out.println("======================================");
		}
		System.out.println("Room gui data:");
		
		this.data.printGuiData();
		System.out.println("\t Shot Position Data");
		for (int i = 0; i < this.shotPositions.size(); i++) {
			this.shotPositions.get(i).printGuiData();
		}

		System.out.println("Room Neighbors: ");
		listSize = this.neighbors.size();
		
		for (int i = 0; i < listSize; i++) {
			System.out.printf("\t* %s\n", this.neighbors.get(i));
		}
		
		System.out.println();
	}

	public int getRoomID() { //TODO: FIX -> All RoomIDs returning 0 at this point
		return this.roomID;
	}

	public int getNumTakes() {
		return this.numTakes;
	}


	public String getRoomName() {
		return this.roomName;
	}

	public ArrayList<Role> getOffCardRoles() {
		return this.offCardRoles;
	}

	//Assumes the room has been assigned a scene and roles
	public ArrayList<Role> getAllRoles() {

		Card roomCard = this.getRoomScene().getSceneCard();
		ArrayList<Role> offCardRoles = this.getOffCardRoles();
        ArrayList<Role> onCardRoles = roomCard.getRoles();
        ArrayList<Role> roomRoles = new ArrayList<Role>();
		roomRoles.addAll(onCardRoles);
		roomRoles.addAll(offCardRoles);
		return roomRoles;
	}

	public ArrayList<Player> getPlayersOffCard() {
		ArrayList<Player> players = this.getPlayersInRoom();
		ArrayList<Role> offCardRoles = this.getOffCardRoles();
		ArrayList<Player> playersOffCard = new ArrayList<Player>();
		for (Player ply: players) {
			if (ply.getPlayerInRole() && offCardRoles.contains(ply.getRole())) {
				playersOffCard.add(ply);
			}
		}

		return playersOffCard;
	}

	public ArrayList<Player> getPlayersOnCard() {
		ArrayList<Player> players = this.getPlayersInRoom();
		ArrayList<Role> onCardRoles = this.getRoomScene().getSceneCard().getRoles();
		ArrayList<Player> playersOnCard = new ArrayList<Player>();
		for (Player ply: players) {
			if (ply.getPlayerInRole() && onCardRoles.contains(ply.getRole())) {
				playersOnCard.add(ply);
			}
		}

		return playersOnCard;
	}

	public ArrayList<Room> getNeighbors() {
		BoardManager manager = BoardManager.getInstance();
		ArrayList<Room> rooms = manager.getBoard().getRooms();
		ArrayList<Room> roomNeighbors = new ArrayList<Room>();
		
		Room castingOffice = manager.getBoard().getCastingOffice();
		Room trailers = manager.getBoard().getTrailers();
		ArrayList<String> neighborNames = this.neighbors;

		for (int i = 0; i < rooms.size(); i++) {
			if (neighborNames.contains(rooms.get(i).getRoomName())) {
				roomNeighbors.add(rooms.get(i));
			}
		}

		if (castingOffice.getNeighborNames().contains(this.roomName)) {
			roomNeighbors.add(castingOffice);
		}

		if (trailers.getNeighborNames().contains(this.getRoomName())) {
			roomNeighbors.add(trailers);
		}
		
		return roomNeighbors;

	}

	public ArrayList<String> getNeighborNames() {
		return this.neighbors;
	}

	 public ArrayList<Player> getPlayersInRoom() {
		return this.playersInRoom;
	}
	
	public void setNumTakes(int takes) {
		this.numTakes = takes;
	}

	public void setOffCardRoles(ArrayList<Role> offCard) {
		this.offCardRoles = offCard;
	}

	public void setNeighbors(ArrayList<String> neighboringRooms) {
		this.neighbors = neighboringRooms;
	}

	public void setNameTrailer(Room trailer){
		this.roomName = "Trailer";
	}

	public void setNameCastingOffice(Room castingOffice){
		this.roomName = "Casting Office";
	}

	public Scene getRoomScene() {
		return this.roomScene;
	}

	public void setRoomScene(Scene s) {
		this.roomScene = s;
	}

	public boolean addPlayerToRoom(Player plyr) { 
		boolean playerInRoom = playersInRoom.contains(plyr);
		if(!playerInRoom) {
			playersInRoom.add(plyr);
		}
		return playerInRoom;
	}

	public boolean roomContainsPlayer(int plyId) {
		ArrayList<Player> plys = BoardManager.getInstance().getBoard().getPlayers();
		Player ply = plys.get(plyId);
		return this.playersInRoom.contains(ply);
	}

	public boolean roomContainsPlayer(Player ply) {
		return this.playersInRoom.contains(ply);
	}

	public boolean removePlayerFromRoom(Player plyr) { 
		/* ArrayList.remove(Object obj) returns true if it finds obj in list and it is removed, else false.
			If there are duplicates, removes first occurence of obj and returns true. */
		return playersInRoom.remove(plyr);
	}

	public GuiData getGuiData() {
		return this.data;
	}

	public void setGuiData(GuiData d) {
		this.data = d;
	}

	public ArrayList<GuiData> getShotPositions() {
		return this.shotPositions;
	}

	public void setShotPositions(ArrayList<GuiData> posData) {
		this.shotPositions = posData;
	}

	public void setBoardManager(BoardManager mngr) {
		this.boardManager = mngr;
	}

	public BoardManager getBoardManager() {
		return this.boardManager;
	}

}
