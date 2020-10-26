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
    //TODO private Room playerRoom;

    public Player() {

    }

    /*
        ======== PRIVATE METHODS ========
     */

    // TODO Implement Upgrade()
    private boolean upgrade() {
        return false;
    }

    // TODO Implement Rehearse()
    private boolean rehearse() {
        return false;
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
