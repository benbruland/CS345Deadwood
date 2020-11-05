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

	public ArrayList<Player> getPlayersInRoom(){
		return this.playersInRoom;
	}
	
	public Scene getRoomScene(){
		return this.roomScene;
	}

	public boolean addPlayerToRoom(Player plyr){ 
		if(!playersInRoom.contains(plyr)){
			playersInRoom.add(plyr);
			return true;
		}
		return false;
	}

	public boolean removePlayerToRoom(Player plyr){ 
		/* ArrayList.remove(Object obj) returns true if it finds obj in list and it is removed, else false.
		    If there are duplicates, removes first occurence of obj and returns true. */
		return playersInRoom.remove(plyr);
	}
}
