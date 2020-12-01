//Authors: Benjamin Bruland, Lucas McIntosh

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
        final BoardManager manager = BoardManager.getInstance(numberOfPlayers);
        ArrayList<Player> plyrs = manager.getBoard().getPlayers();
        Board b = manager.getBoard();
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
        Scanner playerChoice = new Scanner(System.in);
        while(!endOfGame){

            manager.doPlayerTurn(currPlayer);
            currPlayer = manager.getActivePlayer();

            if (manager.getNumberOfScenesRemaining() == 1) {
                ArrayList<Player> players = manager.getBoard().getPlayers();
                for (Player ply: players) {
                    ply.setPlayerRoom(manager.getBoard().getTrailers());
                }
                endOfGame = manager.cycleGameDay();
                System.out.println("Day ended, all players returned to trailers. New scenes dealt.");
                System.out.println("Game Day: " + manager.getCurrentDay());
            }

            if(!endOfGame){
                manager.setActivePlayer(plyrs.get(++playerIndex % numberOfPlayers));
                currPlayer = manager.getActivePlayer();
            }
        }
        System.out.println("Game winner(s): ");
        ArrayList<Player> winners = manager.scoreGame();
        for (Player ply: winners) {
            System.out.printf("Player: %s Score: %d", ply.getName(), manager.calculatePlayerScore(ply));
        }
        playerChoice.close();
    }
}