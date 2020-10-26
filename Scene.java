public class Scene {

	/* Primitive Attributes */
	private int sceneID;
	private int sceneBudget;
	private int shotsRemaining;
	private int totalShots;

	/* Non-primitive Attributes */
	private String sceneName;
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

	}

	//TODO Implement getOffCardRoles
	public Role[] getOffCardRoles(){

	}

	//TODO Implement getPlayersOnCard
	public Player[] getPlayersOnCard(int roleID){

	}

	//TODO Implement getPlayersOffCard
	public Player[] getPlayersOffCard(int roleID){

	}

	//TODO Implement getPlayersInScene
	public Player[] getPlayersInScene(int sceneID){

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
