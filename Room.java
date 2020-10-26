public class Room {

	/* Private Attributes */
	private int roomID;
	private String roomName;
	private Room[] adjacentRooms;
	private Scene roomScene;
	private Player[] playersInRoom;
	
	/* Public Attributes*/

    
	//TODO Implement Room Constructor
    	public Room() {

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
