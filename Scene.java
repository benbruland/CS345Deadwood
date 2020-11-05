public class Scene {

	/* Primitive Attributes */
	private String sceneName, sceneDescription;
	private int sceneID;
	private int sceneBudget;
	private int shotsRemaining;
	private int totalShots;
	
	/* Non-primitive Attributes */
	private Role[] roles;
	private Player[] playersInScene;

	public Scene(){
		
	}
	
	public Scene(String sceneN, String sceneD, int id, int budget, int shotsR, int totalS, Role[] roleList, Player[] playerList){
		this.sceneName = sceneN;
		this.sceneDescription = sceneD;
		this.sceneID = id;
		this.sceneBudget = budget;
		this.shotsRemaining = shotsR;
		this.totalShots = totalS;
		this.roles = roleList;
		this.playersInScene = playerList;
	}

	/*
	 * ====== PRIVATE METHODS ======
	 */

	/*
	 * ====== PUBLIC METHODS ======
	 */

	//TODO Implement getOnCardRoles
	public Role[] getOnCardRoles(){
		
		// Change return statement
		return roles;
	}

	//TODO Implement getOffCardRoles
	public Role[] getOffCardRoles(){

		// Change return statement
		return roles;
	}

	//TODO Implement getPlayersOnCard
	public Player[] getPlayersOnCard(int roleID){
	
		// Change return statement
		return playersInScene;
	}

	//TODO Implement getPlayersOffCard
	public Player[] getPlayersOffCard(int roleID){

		// Change return statement
		return playersInScene;
	}

	public Player[] getPlayersInScene(int sceneID){
		
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
