import java.util.ArrayList;

public class Player {

    /* Primitive Attributes */
    private int playerRank;
    private int playerCredits;
    private int playerDollars;
    private int rehearseBonus;
    private int playerID;
    private boolean playerInRole;

    /* Non-Primitive Attributes */
    private Role playerRole;
    private Card playerCard;
    private Room playerRoom;
    // private Board boardRef TODO: Need a board ref to use .getTrailers() & .getCastingOffice()

    /* No args constructor, for case where 4 or less players */
    public Player() {
        this.playerRank = 1;
        this.playerCredits = 0;
        this.playerDollars = 0;
        this.rehearseBonus = 0;
        //this.playerID = ; TODO: Not sure how to initialize playerID
        this.playerInRole = false;
        this.playerRole = null;
        this.playerCard = null;
        //this.playerRoom = Trailer; TODO: change Trailer to proper way of referencing Trailer Room
    }

    /* 5 or 6 player case, set credits to passed in param */ 
    public Player(int credits) {
        this.playerRank = 1;
        this.playerCredits = credits;
        this.playerDollars = 0;
        this.rehearseBonus = 0;
        //this.playerID = ; TODO: Not sure how to initialize playerID
        this.playerInRole = false;
        this.playerRole = null;
        this.playerCard = null;
        //this.playerRoom = getTrailers(); TODO: need Board object ref
    }

    /* 7 or 8 player case, set rank and credits to passed in params */ 
    public Player(int credits, int rank) {
        this.playerRank = rank;
        this.playerCredits = credits;
        this.playerDollars = 0;
        this.rehearseBonus = 0;
        //this.playerID = ; TODO: Not sure how to initialize playerID
        this.playerInRole = false;
        this.playerRole = null;
        this.playerCard = null;
        //this.playerRoom = getTrailers(); TODO: need Board object ref
    }

    /*
        ======== PRIVATE METHODS ========
     */

    // TODO Implement Upgrade()
    private boolean upgrade(int selection){
         if(this.playerRoom == this.playerCard.getCastingOffice()){ // TODO: Add board ref? 
            if(selection >= 2 && selection <= 6){  
                if(this.getPlayerCredits() >= getCreditUpgradeCost(selection) && this.getPlayerDollars() >= getDollarUpgradeCost(selection)){ // TODO: Add board ref? 
                    this.playerRank = selection;
                    this.setPlayerCredits(this.getPlayerCredits()-getCreditUpgradeCost(selection));
                    this.setPlayerDollars(this.getPlayerDollars()-getDollarUpgradeCost(selection));
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

    // TODO Implement Rehearse()
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

    // TODO Implement Move()
    private boolean move() {
        return false;
    }

    // TODO Discuss purpose of work()
    private void work() {

    }

    // TODO Implement ChooseRole()
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

    // TODO Implement AddRehearsalBonus()
    public void addRehearsalBonus() {

    }
}
