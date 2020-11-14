import java.util.ArrayList;
import java.util.Random;

public class Player {

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
        this.playerRole = null;
        this.playerCard = null;
    }

    /*
        ======== PRIVATE METHODS ========
     */

    private boolean upgrade(int selection, int creditCost, int dollarCost){
         if(this.playerRoom.getRoomName().equals("castingOffice")){ 
            if(selection >= 2 && selection <= 6){  
                if(this.playerRank < selection-1 && this.playerCredits >= creditCost && this.playerDollars >= dollarCost){ 
                    this.playerRank = selection;
                    this.setPlayerCredits(this.playerCredits-creditCost);
                    this.setPlayerDollars(this.playerDollars-dollarCost);
                    return true; 
                }
                else{
                    return false;
                }
            }
            else{
               return false;
            }
        }
        else { 
            return false;
        }
    }

    private boolean rehearse() {
        if(playerInRole && (this.rehearseBonus < this.playerCard.getBudget() - 1)){
            this.rehearseBonus += 1;
            return true;
        }
        else{
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
            this.playerRoom = desiredRoom; 
            this.playerCard = this.playerRoom.getRoomScene().getSceneCard();  
            this.rehearseBonus = 0;
            return true;
        }
        else{
            return false;
        }
    }

    /* act assumes that active player has a role, and that BoardManager will handle all checking for on-card vs. off-card role */
    private boolean act() {
        int playerRoll = this.rollDice(6) + this.addRehearsalBonus();
        if (playerRoll > this.playerCard.getBudget()){
            // Success
            return true;
        }
        else {
            // Failure
            return false;
        }
    }

    private boolean chooseRole(int id, boolean isOnCard) {
        if(!this.playerInRole){
            if(isOnCard){
                ArrayList<Role> roleList = this.playerRoom.getRoomScene().getOnCardRoles();
                for(Role rl : roleList){
                    if (rl.getRoleID() == id && rl.getRoleAvailable()){
                        this.playerInRole = true;
                        this.playerRole = rl;
                        this.rehearseBonus = 0;
                        rl.setRoleAvailable(false);
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
            else{
                ArrayList<Role> roleList = this.playerRoom.getRoomScene().getOffCardRoles();
                for(Role rl : roleList){
                    if (rl.getRoleID() == id && rl.getRoleAvailable()){
                        this.playerInRole = true;
                        this.playerRole = rl;
                        this.rehearseBonus = 0;
                        rl.setRoleAvailable(false);
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
        }
        /*TODO: Is this logic correct? Could you hypothetically take a role with a lesser rank while 
                *staying on the Scene, or do you have to stay on Role until Scene is wrapped? */
        // System.out.println("Player currently in Role, cannot take another on Role while in Role.");
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

    public boolean performUpgrade(int selection, int creditCost, int dollarCost){
        boolean retVal = upgrade(selection, creditCost, dollarCost);
        return retVal;
    }

    public boolean performRehearse(){
        boolean retVal = rehearse();
        return retVal;
    }

    public boolean performAct(){
        boolean retVal = act();
        return retVal;
    }

    public boolean performChooseRole(int id, boolean isOnCard){
        boolean retVal = chooseRole(id, isOnCard);
        return retVal;
    }

    public boolean performMove(Room desiredRoom){
        boolean retVal = move(desiredRoom);
        return retVal;
    }

    public Role getRole() {
        return this.playerRole;
    }

    public String getName(){
        return this.playerName;
    }

    public Card getPlayerCard() {
        return this.playerCard;
    }

    public Room getPlayerRoom(){
        return this.playerRoom;
    }

    public boolean getPlayerInRole() {
        return this.playerInRole;
    }

    public int getPlayerRank(){
        return this.playerRank;
    }

    public void setPlayerInRole(boolean tof) {
        this.playerInRole = tof;
        return;
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

    public void setName(String nm){
        this.playerName = nm;
    }

    public void setPlayerRoom(Room room) {
        this.playerRoom = room;
    }

    public void setPlayerRole(Role rl){
        this.playerRole = rl;
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
