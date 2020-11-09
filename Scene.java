import java.util.ArrayList;

public class Scene {

	/* Primitive Attributes */
	private int shotsRemaining;
	private int totalShots;
	
	/* Non-primitive Attributes */
	private Card sceneCard; //unitialized
	private ArrayList<Role> roles; //unitialized
	private ArrayList<Player> playersInScene;

	public Scene() {

	}

	public Scene(int shotsR, int totalS){
		this.shotsRemaining = shotsR;
		this.totalShots = totalS;
		this.playersInScene = new ArrayList<Player>(); // Initialized as empty list until players join Scene 
	}
	
	public Scene(int shotsR, int totalS, Card scene){
		this.sceneCard = scene;
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

		ArrayList<Role> roles = this.sceneCard.getRoles();
		ArrayList<Role> onCardRoles = new ArrayList<>();
		
		for (Role rl : roles){
			if(rl.getIsOnCardRole())
				onCardRoles.add(rl);
		}
		
		return onCardRoles;
	}

	public ArrayList<Role> getOffCardRoles(){
		// off card roles stored in the board xml data
		ArrayList<Role> roles = this.sceneCard.getRoles();
		ArrayList<Role> offCardRoles = new ArrayList<>();
		for (Role rl : roles){
			if(!rl.getIsOnCardRole())
				offCardRoles.add(rl);
		}
		return offCardRoles;
	}

	public ArrayList<Player> getPlayersOnCard() {
		
		ArrayList<Player> playersOnCard = new ArrayList<Player>();

		for (Player plyr : playersInScene){
			if (plyr.getRole().getIsOnCardRole()) { 
				playersOnCard.add(plyr);
			}
		}

		return playersOnCard;
	}

	public ArrayList<Player> getPlayersOffCard(int roleID){
		ArrayList<Player> playersOffCard = new ArrayList<Player>();
		for (Player plyr : playersInScene){
			if (!plyr.getRole().getIsOnCardRole()) {
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
