import java.util.ArrayList;
import java.util.Scanner;

public class Deadwood {

    // Checks that a player's name does not contain spaces or non-alphanumeric chars
    // This just makes the printing neater
    public static boolean playerNameValid(String name) {
        String regex = "^[a-zA-Z0-9]+$";
        int nameStrings = name.split(" ").length;
        int maxLen = 16;
        return name.length() < maxLen && name.matches(regex) && nameStrings == 1;
    }


    public static String getValidPlayerName(Scanner nameScanner) {
        String name = nameScanner.nextLine();
        while (!playerNameValid(name)) {
            System.out.println("\t * Names may have at most 16 chars, must be alphanumeric, and may contain no spaces.");
            System.out.print("\t * name: ");
            name = nameScanner.nextLine();
        }
        return name;
    }

    public static ArrayList<String> collectPlayerNames() {
        
        String endCmd = "done";
        int numberOfPlayers = 0;
        int maxPlayers = 8;
        int minPlayers = 2;
        ArrayList<String> names = new ArrayList<String>();
        Scanner nameScanner = new Scanner(System.in);
        String name = "";

        System.out.println("Enter \"done\" to finish entering player names.");
        
        while (numberOfPlayers < minPlayers || numberOfPlayers < maxPlayers && !name.toLowerCase().equals(endCmd)) {
            System.out.printf("Enter a name for player %d\n \t * name: ", numberOfPlayers + 1);
            name = getValidPlayerName(nameScanner);
            
            if (!name.toLowerCase().equals(endCmd)) {
                names.add(name);
            }
            numberOfPlayers = names.size();
        } 

        return names;
    }
    
    public static void main(String[] args) {
        ArrayList<String> playerNames = collectPlayerNames();
        int numberOfPlayers = playerNames.size();
        BoardManager manager = new BoardManager(numberOfPlayers);
        ArrayList<Player> plyrs = manager.getBoard().getPlayers();
        /* First player decided randomly */
        Player currPlayer = manager.getActivePlayer();
        int playerIndex = -1;
        for(int i = 0; i < numberOfPlayers; i++){
            plyrs.get(i).setName(playerNames.get(i));
            if (plyrs.get(i) == currPlayer){
                playerIndex = i;
            }
        }
        assert playerIndex != -1: "Unable to find randomly chosen first player.";
        ArrayList<Card> deck = manager.getDeck();
        manager.shuffleDeck(deck);

        boolean endOfGame = false;
        while(!endOfGame){
            System.out.println("Player " + ((playerIndex % numberOfPlayers) + 1) + "'s turn");
            endOfGame = manager.doPlayerTurn(currPlayer);
            // Increment playerIndex and change polayer turns
            if(!endOfGame){
                manager.setActivePlayer(plyrs.get(++playerIndex % numberOfPlayers));
                currPlayer = manager.getActivePlayer();
            }
        }
        System.out.println("Final day completed, player " + (playerIndex + 1) + " is the winner!");
    }
}