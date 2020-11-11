import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

public class BoardManager {

    /* Primitive Attributes */
    private int numPlayers;
    private int dayOfGame;

    /* Non-Primitive Attributes */
    private Board gameBoard;
    private Player activePlayer;


    //TODO Implement Constructors
    public BoardManager() {

    }
    
    /*
        ========= Public Methods ==========
    */
   
    private Player makeInitialPlayer(int numPlayers, int playerID) {
        Player newPlayer = new Player();

        switch(numPlayers) {
            case 5:
                newPlayer.setPlayerCredits(2);
            case 6:
                newPlayer.setPlayerCredits(4);
            case 7:
        }
        return newPlayer;
    }

    public void initBoard(int numPlayers) {
        XMLParser parser = new XMLParser();
        this.gameBoard = parser.parseBoard();
        Board board = this.gameBoard;

        ArrayList<Player> players;
        
        for (int i = 0; i < numPlayers; i++) {
            
        }

    }

    public Player getActivePlayer() {
        return this.activePlayer;
    }

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


    public void setActivePlayer(Player ply) {
        this.activePlayer = ply;
    }

    //TODO Implement DoPlayerTurn
    public void doPlayerTurn(Player ply) {
        
    }

}