public class Scene {

	/* Primitive Attributes */
	private int sceneID;
	private int sceneBudget;
	private int shotsRemaining;
	private int totalShots;
	private String sceneName;
	
	/* Non-primitive Attributes */
	private Role[] roles;
	private Player[] playersInScene;

    
	//TODO Implement Scene Constructor
    	public Scene(){

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
