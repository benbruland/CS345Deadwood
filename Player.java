import java.util.ArrayList;
import java.util.Random;

public class Player {

    static int playerCounter = 0;

    /* Primitive Attributes */
    private int playerRank;
    private int playerCredits;
    private int playerDollars;
    private int rehearseBonus;
    private int playerID;
    private boolean playerInRole;
    private String playerName;

    /* Non-Primitive Attributes */
    private Role playerRole;
    private Card playerCard;
    private Room playerRoom;

    /* No args constructor */
    public Player() {

    }

    /* Constructor for case where 4 or less players */
    public Player(Room trailer, String nm) {
        this.playerRank = 1;
        this.playerCredits = 0;
        this.playerDollars = 0;
        this.rehearseBonus = 0;
        this.playerID = ++playerCounter;
        this.playerInRole = false;
        this.playerRole = null;
        this.playerCard = null;
        this.playerRoom = trailer;
        this.playerName = nm;
    }

    /* 5 or 6 player case, set credits to passed in param */ 
    public Player(Room trailer, String nm, int credits) {
        assert credits >= 0 : "Credits may not be negative";
        this.playerRank = 1;
        this.playerCredits = credits;
        this.playerDollars = 0;
        this.rehearseBonus = 0;
        this.playerID = ++playerCounter;
        this.playerInRole = false;
        this.playerRole = null;
        this.playerCard = null;
        this.playerRoom = trailer;
        this.playerName = nm;
    }

    /* 7 or 8 player case, set rank and credits to passed in params */ 
    public Player(Room trailer, String nm, int credits, int rank) {
        assert credits >= 0 : "credits may not be negative";
        assert rank >= 1 && rank <= 6 : "Invalid rank, ranks must be between 1 and 6";
        this.playerRank = rank;
        this.playerCredits = credits;
        this.playerDollars = 0;
        this.rehearseBonus = 0;
        this.playerID = ++playerCounter;
        this.playerInRole = false;
        this.playerRole = null;
        this.playerCard = null;
        this.playerRoom = trailer;
        this.playerName = nm;
    }

    /*
        ======== PRIVATE METHODS ========
     */

    private boolean upgrade(int selection, int[] creditCosts, int[] dollarCosts){
         if(this.playerRoom.getRoomName().equals("castingOffice")){ 
            if(selection >= 2 && selection <= 6){  
                if(this.playerRank < selection-1 && this.playerCredits >= creditCosts[selection-1] && this.playerDollars >= dollarCosts[selection-1]){ 
                    this.playerRank = selection;
                    this.setPlayerCredits(this.playerCredits-creditCosts[selection-1]);
                    this.setPlayerDollars(this.playerDollars-dollarCosts[selection-1]);
                    return true; 
                }
                else{
                    System.out.println("Insufficient number of credits or dollars for selected upgrade.");
                    return false;
                }
            }
            else{
                System.out.println("user selection is out of the acceptable bound 2 and 6, inclusive.");
                return false;
            }
        }
        else { 
            System.out.println("Player must be in Casting Office in order to upgrade.");
            return false;
        }
    }

    private boolean rehearse() {
        if(playerInRole && (this.rehearseBonus < this.playerCard.getBudget() - 1)){
            this.rehearseBonus += 1;
            return true;
        }
        else{
            System.out.println("Player is unable to rehearse.");
            return false;
        }
    }

    private boolean move(Room desiredRoom) {
        ArrayList<String> adjRooms = playerRoom.getNeighbors();
        boolean validRoom = false;
        if(adjRooms.contains(desiredRoom.getRoomName())){
            validRoom = true;
        }
        if(validRoom){
            this.playerRoom = desiredRoom; // TODO: edit player card / scene?  
            return true;
        }
        else{
            return false;
        }
    }

    // TODO Discuss purpose of work()
    private void actRole() {
        if(this.playerInRole){
            int playerRoll = this.rollDice(6) + this.addRehearsalBonus();
            if(this.playerRole.getIsOnCardRole()){  // on card role
                if (playerRoll > this.playerCard.getBudget()){
                    // Success
                    this.playerCredits += 2;
                    this.playerRoom.getRoomScene().removeShotCounter();
                    if(this.playerRoom.getRoomScene().getShotCount()  == 0){
                        this.playerRoom.getRoomScene().finishScene();
                        return;
                    }
                }
                else {
                    // Failure
                    return;
                }
            }
            else {                                  // off card role
                if (playerRoll > this.playerCard.getBudget()){
                    // Success
                    this.playerCredits += 1;
                    this.playerCredits += 1;
                    this.playerRoom.getRoomScene().removeShotCounter();
                    if(this.playerRoom.getRoomScene().getShotCount()  == 0){
                        this.playerRoom.getRoomScene().finishScene();
                        return;
                    }
                }
                else {
                    // Failure
                    this.playerDollars += 1;
                    return;
                }
            }
        }

    }

    private boolean chooseRole(int id) {
        if(!this.playerInRole){
            ArrayList<Role> roleList = this.playerCard.getRoles();
            for(Role rl : roleList){
                if (rl.getRoleID() == id && rl.getRoleAvailable()){
                    this.playerInRole = true;
                    this.playerRole = rl;
                    this.rehearseBonus = 0;
                    rl.setRoleAvailable(false);
                    //TODO: Reset player card to new scene?
                    return true;
                }
                else{
                    System.out.println("Role unavailable.");
                    return false;
                }
            }
        }
        /*TODO: Is this logic correct? Could you hypothetically take a role with a lesser rank while 
                *staying on the Scene, or do you have to stay on Role until Scene is wrapped? */
        System.out.println("Player currently in Role, cannot take another on Role while in Role.");
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

    public Role getRole() {
        return this.playerRole;
    }

    public void setPlayerCredits(int numCredits) {
        this.playerCredits = numCredits;
    }

    public void setPlayerDollars(int numDollars) {
        this.playerDollars = numDollars;
    }

    public int getPlayerDollars() {
        return this.playerDollars;
    }

    public int getPlayerCredits() {
        return this.playerCredits;
    }

    public void setRank(int rank) {
        assert rank >= 1 && rank <= 6 : "Invalid rank, ranks must be between 1 and 6";
        this.playerRank = rank;
    }

    public void setPlayerRoom(Room room) {
        this.playerRoom = room;
    }

    public void setPlayerCard(Card crd){
        this.playerCard = crd;
    }

    public int rollDice(int numFaces) {
        return randInt(1, numFaces);
    }

    public int addRehearsalBonus() {
        return this.rehearseBonus;
    }
}
