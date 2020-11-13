import java.util.Collections;
import java.util.ArrayList;

public class Scene {

	/* Primitive Attributes */
	private int shotsRemaining;
	
	/* Non-primitive Attributes */
	private Card sceneCard;
	private Room sceneRoom;
	private ArrayList<Player> playersInScene;

	public Scene() {
		
	}

	public Scene(int shotsR) {
		this.shotsRemaining = shotsR;
		this.playersInScene = new ArrayList<Player>(); // Initialized as empty list until players join Scene 
	}
	
	public Scene(int shotsR, int totalS, Card scene) {
		this.sceneCard = scene;
		this.shotsRemaining = shotsR;
		this.playersInScene = new ArrayList<Player>(); // Initialized as empty list until players join Scene 
	}

	public void printScene() {
		System.out.println("Scene Name: " + this.sceneCard.getName());
		System.out.println("Scene Room: " + this.sceneRoom.getRoomName());
		System.out.println("Scene Roles: ");
		System.out.println("Offcard Roles: ");
		ArrayList<Role> offCards = this.getOffCardRoles();
		int listSize = offCards.size();
		for (int i = 0; i < listSize; i++) {
			offCards.get(i).printRole();
		}
		System.out.println("On Card Roles: ");
		ArrayList<Role> onCards = this.getOnCardRoles();
		listSize = onCards.size();
		for (int i = 0; i < listSize; i++) {
			onCards.get(i).printRole();
		}

	}

	/*
	 * ====== PUBLIC METHODS ======
	 */

	public ArrayList<Role> getOnCardRoles() {
		// on card roles stored in the scene xml data
		ArrayList<Role> roles = this.sceneCard.getRoles();
		return roles;
	}

	public ArrayList<Role> getOffCardRoles() {
		return this.sceneRoom.getOffCardRoles();
	}

	public ArrayList<Player> getPlayersOnCard() {
		
		ArrayList<Player> playersOnCard = new ArrayList<Player>();

		for (Player plyr : playersInScene) {
			if (plyr.getRole().getIsOnCardRole()) { 
				playersOnCard.add(plyr);
			}
		}

		return playersOnCard;
	}

	public ArrayList<Player> getPlayersOffCard() {
		ArrayList<Player> playersOffCard = new ArrayList<Player>();
		for (Player plyr : playersInScene) {
			if (!plyr.getRole().getIsOnCardRole()) {
				playersOffCard.add(plyr);
			}
		}
		return playersOffCard;
	}

	public ArrayList<Player> getPlayersInScene(int sceneID) {
		return this.playersInScene;
	}

	// Should this method have return type boolean and return true on success and false otherwise? 
	public void removeShotCounter() {
		this.shotsRemaining -= 1;
		assert this.shotsRemaining >= 0 : "Shots remaining should be non-negative";
	}

	public int getShotCount() {
		assert shotsRemaining >= 0 : "Shots remaining should be non-negative.";
		return this.shotsRemaining;
	}

	public Card getSceneCard() {
		return this.sceneCard;
	}

	public void finishScene() {
		/* Setting all roles refs associated to scene to unavailable / not working */
		for(Player plyr : this.playersInScene){
			plyr.setPlayerInRole(false);
			plyr.setPlayerRole(null);
		}
		for(Role rl : this.getOffCardRoles()){
			rl.setRoleAvailable(false);
		}
		for(Role rl : this.getOnCardRoles()){
			rl.setRoleAvailable(false);
		}
	}

	public void setShotsRemaining(int shots) {
		this.shotsRemaining = shots;
	}

	public void setSceneCard(Card card) {
		this.sceneCard = card;
	}

	public void setSceneRoom(Room room) {
		this.sceneRoom = room;
	}

	public Room getSceneRoom() {
		return this.sceneRoom;
	}
}
