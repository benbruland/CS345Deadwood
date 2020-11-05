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
		this.playersInScene = new ArrayList<Player>(); // Initialized as empty list until players join Scene 
	}

	/*
	 * ====== PRIVATE METHODS ======
	 */

	/*
	 * ====== PUBLIC METHODS ======
	 */

	public ArrayList<Role> getOnCardRoles(){
		// on card roles stored in the scene xml data
		ArrayList<Role> onCard = new ArrayList<Role>();
		for (Role rl : roles){
			if(rl.isOnCardRole) //TODO: figure out how to make this visible
				onCard.add(rl);
		}
		return onCard;
	}

	public ArrayList<Role> getOffCardRoles(){
		// off card roles stored in the board xml data
		ArrayList<Role> offCard = new ArrayList<Role>();
		for (Role rl : roles){
			if(!rl.isOnCardRole) //TODO: figure out how to make this visible
				offCard.add(rl);
		}
		return offCard;
	}

	public ArrayList<Player> getPlayersOnCard(int roleID){
		ArrayList<Player> playersOnCard = new ArrayList<Player>();
		for (Player plyr : playersInScene){
			if (plyr.playerRole != null && plyr.playerRole.isOnCardRole == true){ //TODO: figure out how to make this visible
				playersOnCard.add(plyr);
			}
		}
		return playersOnCard;
	}

	public ArrayList<Player> getPlayersOffCard(int roleID){
		ArrayList<Player> playersOffCard = new ArrayList<Player>();
		for (Player plyr : playersInScene){
			if (plyr.playerRole != null && plyr.playerRole.isOnCardRole == false){ //TODO: figure out how to make this visible
				playersOffCard.add(plyr);
			}
		}
		return playersOffCard;
	}

	public ArrayList<Player> getPlayersInScene(int sceneID){
		return this.playersInScene;
	}

	// Should this method have return type boolean and return true on success and false otherwise?
	public void removeShotCounter(){
		this.shotsRemaining -= 1;
	}

	public int getShotCount(){
		return this.shotsRemaining;
	}

	//TODO: Implement finishScene
	public void finishScene(){

	}
}
