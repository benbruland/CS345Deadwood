import java.util.ArrayList;

public class Room {

	/* Primitive Attributes */
	private int roomID;
	private String roomName;

	/* Non-primitive Attributes*/
	private Scene roomScene;	
	private Room[] adjacentRooms; // non-ArrayList
	private ArrayList<Player> playersInRoom;
	
	/* No argument constructor*/
	public Room(){

	}

	//TODO: Implement special constructor for Trailer/Casting Office ???
	// public Room(int id, String name){

	// }

	/* Constructor for basic acting rooms */
	public Room(int id, String name, Scene scene, Room[] roomList) {
		this.roomID = id;
		this.roomName = name;
		this.roomScene = scene;
		this.adjacentRooms = roomList;
		this.playersInRoom = new ArrayList<Player>();
	}

	/*
	 * ====== PRIVATE METHODS ======
	 */


	/*
	 * ====== PUBLIC METHODS ======
	 */

	//TODO Implement playersInRoom
	public ArrayList<Player> playersInRoom(){
	
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
