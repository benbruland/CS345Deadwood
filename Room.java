public class Room {

	/* Primitive Attributes */
	private int roomID;
	private String roomName;

	/* Non-primitive Attributes*/
	private Scene roomScene;	
	private Room[] adjacentRooms;
	private Player[] playersInRoom;
	

	public Room(int id, String name, Scene scene, Room[] roomList, Player[] playerList) {
		this.roomID = id;
		this.roomName = name;
		this.roomScene = scene;
		this.adjacentRooms = roomList;
		this.playersInRoom = playerList;
	}

	/*
	 * ====== PRIVATE METHODS ======
	 */


	/*
	 * ====== PUBLIC METHODS ======
	 */

	//TODO Implement playersInRoom
	public Player[] playersInRoom(){
	
		return this.playersInRoom;
	}
	
	public Scene getRoomScene(){
		return this.roomScene;
	}

	//TODO Implement addPlayerToRoom
	public boolean addPlayerToRoom(int playerID){
		return false;
	}

	//TODO Implement removePlayerFromRoom
	public boolean removePlayerFromRoom(int playerID){
		return false;
	}
}
