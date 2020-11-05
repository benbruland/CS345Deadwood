import java.util.ArrayList;

public class Scene {

	/* Primitive Attributes */
	private String sceneName, sceneDescription;
	private int sceneID;
	private int sceneBudget;
	private int shotsRemaining;
	private int totalShots;
	
	/* Non-primitive Attributes */
	private ArrayList<Role> roles;
	private ArrayList<Player> playersInScene;

	public Scene(){

	}
	
	public Scene(String sceneN, String sceneD, int id, int budget, int shotsR, int totalS){
		this.sceneName = sceneN;
		this.sceneDescription = sceneD;
		this.sceneID = id;
		this.sceneBudget = budget;
		this.shotsRemaining = shotsR;
		this.totalShots = totalS;
	}

	/*
	 * ====== PRIVATE METHODS ======
	 */

	/*
	 * ====== PUBLIC METHODS ======
	 */

	//TODO Implement getOnCardRoles
	public ArrayList<Role> getOnCardRoles(){
		// on card roles stored in the 

		// Change return statement
		return roles;
	}

	//TODO Implement getOffCardRoles
	public ArrayList<Role> getOffCardRoles(){
		// off card roles stored in the board xml data

		// Change return statement
		return roles;
	}

	//TODO Implement getPlayersOnCard
	public ArrayList<Player> getPlayersOnCard(int roleID){
	
		// Change return statement
		return playersInScene;
	}

	//TODO Implement getPlayersOffCard
	public ArrayList<Player> getPlayersOffCard(int roleID){

		// Change return statement
		return playersInScene;
	}

	public ArrayList<Player> getPlayersInScene(int sceneID){
		
		return this.playersInScene;
	}

	public void removeShotCounter(){
		this.shotsRemaining -= 1;
	}

	public int getShotCount(){
		return this.shotsRemaining;
	}

	//TODO Implement finishScene
	public void finishScene(){

	}
}
