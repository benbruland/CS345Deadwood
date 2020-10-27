public class BoardManager {

    /* Primitive Attributes */
    private int numPlayers;
    private int dayOfGame;

    /* Non-Primitive Attributes */
    private Board gameBoard;
    private Player activePlayer;


    public static void main(String[] args) {
        Board b = new Board();
        BoardManager bm = new BoardManager();
        Player p = new Player();
        Role r = new Role();
        Room rm  = new Room();
        Scene s = new Scene();
        System.out.println("Hello from main!");
    }


    //TODO Implement Constructors
    public BoardManager() {

    }

    /*
        ========= Public Methods ==========
    */


    //TODO Implement CycleGameDay()
    public void cycleGameDay() {
        
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