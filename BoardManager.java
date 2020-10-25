public class BoardManager {

    /* Primitive Attributes */
    private int numPlayers;
    private int dayOfGame;

    /* Non-Primitive Attributes */
    private Board gameBoard;
    private Player activePlayer;


    //TODO Implement Constructors
    public BoardManger() {

    }

    /*
        ========= Public Methods ==========
    */


    //TODO Implement CycleGameDay()
    public cycleGameDay() {

    }

    //TODO Implement ScoreGame()
    public Player scoreGame() {
        return null;
    }

    int rollDice(int numFaces) {
        return (int) ((Math.random() * (numFaces - 1)) + 1);
    }

    //TODO Implement PayBonuses()
    public void payBonuses(Scene sceneToPayout) {

    }

    //TODO discuss awardPlayer()
    public void awardPlayer() {

    }

    //TODO Implement DoPlayerTurn
    public void doPlayerTurn(Player ply) {

    }
}