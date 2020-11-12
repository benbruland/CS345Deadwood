import java.util.ArrayList;
public class Deadwood {
    public static void main(String[] args) {
        BoardManager manager = new BoardManager(2);
        ArrayList<Player> plys = manager.getBoard().getPlayers();
        ArrayList<Card> deck = manager.getBoard().getDeck();
        manager.getBoard().printDeck();
        manager.shuffleDeck(deck);
        manager.getBoard().printDeck();

    }
}