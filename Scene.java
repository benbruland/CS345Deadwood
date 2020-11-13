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

	public void finishScene() {
		ArrayList<Player> playersOnCard = this.getPlayersOnCard();
		ArrayList<Player> playersOffCard = this.getPlayersOffCard();
		ArrayList<Role> onCardRole = this.getOnCardRoles();
		ArrayList<Role> offCardRole = this.getOffCardRoles();

		if(playersOnCard.isEmpty()){ // No player's working on card role, so no payout
			
			/* Setting all roles refs associated to scene to unavailable / not working */
			for(Player plyr : this.playersInScene){
				plyr.setPlayerInRole(false);
			}
			for(Role rl : offCardRole){
				rl.setRoleAvailable(false);
			}
			for(Role rl : onCardRole){
				rl.setRoleAvailable(false);
			}
			return;
		}
		else { // At least one on card role for scene
			ArrayList<Integer> dieRolls = new ArrayList<Integer>();
			for(int i = 0; i < this.sceneCard.getBudget(); i++){
				dieRolls.add(this.playersInScene.get(0).rollDice(6));
			}
			Collections.sort(dieRolls);
			int topRolePayout = 0;
			int middleRolePayout = 0;
			int bottomRolePayout = 0;
			int j = 0;

			/* Loop collecting on card bonuses into top, middle, and bottom */
			for(int i = dieRolls.size() - 1; i >= 0; i--){
				j++;
				if(j == 1){
					topRolePayout += dieRolls.get(i);
				}
				else if (j == 2){
					middleRolePayout += dieRolls.get(i);
				}
				else{
					bottomRolePayout += dieRolls.get(i);
					j = 0;
				}
			}

			/* Giving bonuses to on card players */
			for(Player plyr : playersOnCard){
				if(plyr.getRole().getRoleID() == 2){ //Highest Ranking Role
					plyr.setPlayerDollars(plyr.getPlayerDollars() + topRolePayout);
				}
				else if(plyr.getRole().getRoleID() == 1){ //Middle Ranking Role
					plyr.setPlayerDollars(plyr.getPlayerDollars() + middleRolePayout);
				}
				else if(plyr.getRole().getRoleID() == 0){ //Lowest Ranking Role
					plyr.setPlayerDollars(plyr.getPlayerDollars() + bottomRolePayout);
				}
			}

			/* Giving bonuses to off card players */
			for(Player plyr : playersOffCard){
				plyr.setPlayerDollars(plyr.getPlayerDollars() + plyr.getRole().getRoleLevel());
			}

			/* Setting all roles refs associated to scene to unavailable / not working */
			for(Player plyr : this.playersInScene){
				plyr.setPlayerInRole(false);
			}
			for(Role rl : offCardRole){
				rl.setRoleAvailable(false);
			}
			for(Role rl : onCardRole){
				rl.setRoleAvailable(false);
			}
			return;
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
