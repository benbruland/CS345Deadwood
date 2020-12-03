//Authors: Benjamin Bruland, Lucas McIntosh
package model;
import java.util.ArrayList;
import java.util.Random;

public class Player {

    /* Primitive Attributes */
    private int playerRank;
    private int playerCredits;
    private int playerDollars;
    private int rehearseBonus;
    private int playerID;
    private int[] playerCoords = new int[2];
    private boolean playerInRole;
    private String playerColor;
    private Role playerRole;
    private String playerName;

    // This BoardManager object is responsible for 
    // controling the state of all game objects.
    // The board manager is created in Deadwood.java
    private static BoardManager boardManager = BoardManager.getInstance();

    /* Non-Primitive Attributes */

    /* Player constructor,  custom game settings set in BoardManager.java 
     *       - Taken care of:
     *          - playerName, playerRoom, playerCard
     * 
     */ 
    public Player(int id) {
        this.playerRank = 1;
        this.playerCredits = 0;
        this.playerDollars = 0;
        this.rehearseBonus = 0;
        this.playerID = id;
        this.playerInRole = false;
        this.playerColor = boardManager.colorID(id);
    }

    /*
        ======== PRIVATE METHODS ========
     */

    public void printPlayer() {
        Scene playerScene = this.getPlayerRoom().getRoomScene();
        
        System.out.println("\t* Name: " + this.playerName);
        System.out.println("\t* Credits: "+ this.playerCredits);
        System.out.println("\t* Dollars: "+ this.playerDollars);
        System.out.println("\t* Rank: "+ this.playerRank);
        System.out.println("\t* Room: " + this.getPlayerRoom().getRoomName());
        if (playerScene != null) {
            System.out.println("Shots remaining: " + playerScene.getShotsRemaining());
        }
        
        if (this.playerInRole) {
            this.playerRole.printRole();
        } else {
            System.out.println("Role: Not in a role.");
        }

        
    }

    // This function will use either credits or dollars to charge the cost of the rank.
    // whichever argument is sufficient to charge the cost of the rank will be selected.
    public boolean upgrade(int selection, int dollars, int credits) {
        boolean success = false;
        Board gameBoard = boardManager.getBoard();
        Room playerRoom = this.getPlayerRoom();
        Room castingOffice = gameBoard.getCastingOffice();
        int creditCost = gameBoard.getCreditUpgradeCost(selection);
        int dollarCost = gameBoard.getDollarUpgradeCost(selection);
        if (playerRoom == castingOffice && selection > this.playerRank) {
            success = dollars >= dollarCost || credits >= dollarCost;
            if (credits >= creditCost) {
                this.playerCredits = this.playerCredits - creditCost;
            } else if (dollars >= dollarCost) {
                this.playerDollars = this.playerDollars - dollarCost;
            }
        }
       
        this.playerRank = success ? selection : this.playerRank;
        return success;
    }

    public boolean rehearse() {
        Card playerCard = this.getPlayerCard();
        boolean success = this.playerInRole && (this.rehearseBonus < playerCard.getBudget() - 1);
        this.rehearseBonus = success? this.rehearseBonus + 1 : this.rehearseBonus;

        return success;
    }

    public boolean move(Room desiredRoom) {
        Room playerRoom = this.getPlayerRoom();
        ArrayList<Room> neighbors = playerRoom.getNeighbors();
        if (neighbors.contains(desiredRoom)) {
            playerRoom.removePlayerFromRoom(this);
            desiredRoom.addPlayerToRoom(this);
        }
        return false;
    }

    /* act assumes that active player has a role, and that BoardManager will handle all checking for on-card vs. off-card role */
    public boolean act() {
        Scene playerScene = this.getPlayerRoom().getRoomScene();
        int budget = playerScene.getSceneCard().getBudget();
        int playerRoll = this.rollDice(6) + this.addRehearsalBonus();
        boolean success = playerRoll >= budget;
        boardManager.awardPlayer(success, this);
        boolean sceneFinished = playerScene.getShotsRemaining() > 0;
        this.setPlayerInRole(sceneFinished);
        return success;
    }

    private Card getPlayerCard() {
        Card playerCard = null;
        if (this.playerInRole) {
            playerCard = this.getPlayerRoom().getRoomScene().getSceneCard();
        }
        return playerCard;
    }

    private boolean chooseRole(int id, boolean isOnCard) {
        ArrayList<Role> roles = boardManager.getAvailableRoles(this.getPlayerRoom().getRoomScene());
        
        for (int i = 0; i < roles.size(); i++) {
            Role r = roles.get(i);
            if (r.getRoleID() == id && r.getIsOnCardRole() == isOnCard) {
                this.playerRole = r;
                return true;
            }
        }
        return false;
    }

    private int randInt(int min, int max) {

        Random randGen = new Random();
        int randomNum = randGen.nextInt((max - min) + 1) + min;
    
        return randomNum;
    }

    /*
        ======== PUBLIC METHODS ========
    */

    // Returns true if a player can take role plyRole
    // true means a player's level matches the role's level, 
    // the role isn't already taken, and the player isn't in a role.
    public boolean playerCanTakeRole(Role plyRole) {
        return !this.playerInRole && plyRole.getRoleAvailable() && this.playerRank >= plyRole.getRoleLevel();
    }

    public boolean performUpgrade(int selection, int creditCost, int dollarCost) {
        boolean retVal = upgrade(selection, creditCost, dollarCost);
        return retVal;
    }

    public boolean performRehearse() {
        boolean retVal = rehearse();
        return retVal;
    }

    public boolean performAct() {
        boolean retVal = act();
        return retVal;
    }

    public boolean performChooseRole(int id, boolean isOnCard) {
        boolean retVal = chooseRole(id, isOnCard);
        return retVal;
    }

    public boolean performMove(Room desiredRoom) {
        boolean retVal = move(desiredRoom);
        return retVal;
    }

    public Role getRole() {
        return this.playerRole;
    }

    public void setPlayerRoom(Room desiredRoom) {
        Room originalRoom = this.getPlayerRoom();
        originalRoom.removePlayerFromRoom(this);
        desiredRoom.addPlayerToRoom(this);
    }

    public Room getPlayerRoom() {
        return this.boardManager.getBoard().getPlayerRoom(this.playerID);
    }

    public String getName() {
        return this.playerName;
    }

    public boolean getPlayerInRole() {
        return this.playerInRole;
    }

    public int getPlayerRank() {
        return this.playerRank;
    }

    public void setPlayerInRole(boolean tof) {
        this.playerInRole = tof;
        return;
    }

    public void setPlayerRole(Role rol) {
        this.playerRole = rol;
    }

    public void setPlayerCredits(int numCredits) {
        this.playerCredits = numCredits;
    }

    public void setPlayerDollars(int numDollars) {
        this.playerDollars = numDollars;
    }

    public void setPlayerColor(String color){ this.playerColor = color;  }

    public String getPlayerColor(){ return this.playerColor; }

    public int getPlayerDollars() {
        return this.playerDollars;
    }

    public int getPlayerCredits() {
        return this.playerCredits;
    }

    public int[] getPlayerCoordinates(){
        return this.playerCoords;
    }

    public void setPlayerCoordinates(int[] coordinates){
        this.playerCoords[0] = coordinates[0];
        this.playerCoords[1] = coordinates[1];
    }

    public void setRank(int rank) {
        assert rank >= 1 && rank <= 6 : "Invalid rank, ranks must be between 1 and 6";
        this.playerRank = rank;
    }

    public void setName(String nm) {
        this.playerName = nm;
    }

    public void setRole(Role rl) {
        this.setPlayerInRole(rl != null);
        this.playerRole = rl;
    }

    public int rollDice(int numFaces) {
        return randInt(1, numFaces);
    }

    public int addRehearsalBonus() {
        return this.rehearseBonus;
    }

    public int getRehearsalBonus() {
        return this.rehearseBonus;
    }

    public void setRehearsalBonus(int bonus) {
        this.rehearseBonus = bonus;
    }

    public void setBoardManager(BoardManager mngr) {
		this.boardManager = mngr;
	}

	public BoardManager getBoardManager() {
		return this.boardManager;
	}

}
