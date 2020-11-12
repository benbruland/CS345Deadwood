import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class BoardManager {

    /* Primitive Attributes */
    private int numPlayers;
    private int dayOfGame;
    private int numberOfDays;

    /* Non-Primitive Attributes */
    private Board gameBoard;
    private Player activePlayer;


    //TODO Implement Constructors
    public BoardManager() {
        this.numPlayers = 0;
        this.dayOfGame = 1;
        this.numberOfDays = 3;
    }

    public BoardManager(int numPlys) {
        assert numPlys >= 2 && numPlys <= 8 : "Invalid number of players: " + numPlys ;
        this.numPlayers = numPlys;
        this.numberOfDays = numPlys > 3 ? 4 : 3;
        this.dayOfGame = 1; 
        this.initBoard(numPlys);
        this.activePlayer = chooseFirstPlayer();
    }

    private Player chooseFirstPlayer() {
       
        assert this.gameBoard != null : "Gameboard not initialized before choosing first player";
        assert this.gameBoard.getPlayers().size() > 0 : "Gameboard players array not filled";
       
        int playerIndex = randInt(0, this.numPlayers - 1);
        return this.gameBoard.getPlayers().get(playerIndex); 
    }

    private Player makeInitialPlayer(int numPlayers, int playerID) {
        Player newPlayer = new Player();

        switch(numPlayers) {
            case 5:
                newPlayer.setPlayerCredits(2);
            case 6:
                newPlayer.setPlayerCredits(4);
            case 7:
                newPlayer.setRank(2);
            case 8:
                newPlayer.setRank(2);
        }

        newPlayer.setPlayerRoom(this.gameBoard.getTrailers());
        return newPlayer;
    }
    
    public void initBoard(int numPlayers) {
        XMLParser parser = new XMLParser();
        this.gameBoard = parser.parseBoard();
        Board board = this.gameBoard;

        ArrayList<Player> players = new ArrayList<Player>();
        
        for (int i = 0; i < numPlayers; i++) {
            Player newPlayer = makeInitialPlayer(numPlayers, i + 1);
            players.add(newPlayer);
        }
        
        board.setPlayers(players);

    }

    public Player getActivePlayer() {
        return this.activePlayer;
    }

    //TODO Implement CycleGameDay()
    public void cycleGameDay() {
        assert this.dayOfGame >= 1 : "Invalid game day number: " + this.dayOfGame;
        assert this.dayOfGame <= this.numberOfDays : "Invalid game day " + this.dayOfGame + " should be between 1 and " + this.numberOfDays;
    }

    //TODO Implement ScoreGame()
    public Player scoreGame() {
        return null;
    }

    // Returns a random integer in range [min, max]
    private int randInt(int min, int max) {

        Random randGen = new Random();
        int randomNum = randGen.nextInt((max - min) + 1) + min;
    
        return randomNum;
    }

    private int rollDice(int numFaces) {
        return randInt(1, numFaces);
    }

    //TODO Implement PayBonuses()
    public void payBonuses(Scene sceneToPayout) {

    }

    //TODO discuss awardPlayer()
    public void awardPlayer() {

    }


    public void setActivePlayer(Player ply) {
        this.activePlayer = ply;
    }

    //TODO Implement DoPlayerTurn
    public void doPlayerTurn(Player ply) {
        
    }

}