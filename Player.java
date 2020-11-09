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
    private Room playerRoom;

    /* No args constructor, for case where 4 or less players */
    public Player() {
        this.playerRank = 1;
        this.playerCredits = 0;
        this.playerDollars = 0;
        this.rehearseBonus = 0;
        //this.playerID = ; TODO: Not sure how to initialize playerID
        this.playerInRole = false;
        this.playerRole = null;
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
        //this.playerRoom = Trailer; TODO: change Trailer to proper way of referencing Trailer Room
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
        //this.playerRoom = Trailer; TODO: change Trailer to proper way of referencing Trailer Room
    }

    /*
        ======== PRIVATE METHODS ========
     */

    // TODO Implement Upgrade()
    private boolean upgrade() {
        /* Outline:
         *  if (playerRoom == CastingOffice){
         *      prompt player with upgrade menu and wait for selection
         *      if(player.getPlayerCredits() >= selection.creditCost && player.getPlayerDollars() >= selection.dollarCost){
         *          player.playerRank = selection.rank;
         *          player.setPlayerCredits(player.getPlayerCredits()-selection.creditCost);
         *          player.setPlayerDollars(player.getPlayerDollars()-selection.dollarCost);
         *          return true; 
         *      }
         *      else {
         *          System.out.println("Insufficient number of credits or dollars for selected upgrade.");
         *          return false;
         *      }
         *  }
         *  else { 
         *      System.out.println("Player must be in Casting Office in order to upgrade.");
         *      return false;
         *  }
         */

        return false;
    }

    // TODO Implement Rehearse()
    private boolean rehearse() {
        if(playerInRole && (this.rehearseBonus < this.playerScene.getSceneBudget() - 1)){
            this.rehearseBonus += 1;
            return true;
        }
        else{
            System.out.println("Player is unabale to rehearse.");
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
    private boolean chooseRole() {
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
