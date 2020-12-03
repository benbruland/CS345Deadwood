import controller.Board_Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main extends Application {

    @FXML
    private Slider numPlayers;

    @FXML
    private VBox numplayercontainer;

    private ArrayList<TextField> nameList = new ArrayList<TextField>();

    private Stage mainStage;


    @Override
    public void init() throws Exception {
        System.out.println("Before runtime\n======================");
    }

    @Override
    public void stop() throws Exception {
        System.out.println("======================\nAfter runtime");
    }

    @Override
    public void start(Stage window) throws Exception{
        mainStage = window;
        Parent root = FXMLLoader.load(getClass().getResource("resources/deadwood_setup.fxml"));
        Button bt = new Button("Game Setup");
        bt.setLayoutX(208);
        bt.setLayoutY(304);
        bt.setPrefSize(185.0, 54.0);
        bt.setFont(Font.font("Sylfaen", 27));
        bt.setStyle("-fx-background-color: white");
        bt.setStyle("-fx-border-color: black");
        bt.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent event) {
                   try {
                       handleGameSetup(event);
                   }
                   catch (Exception e){
                       e.printStackTrace();
                   }
               }
        });
        Pane main = new Pane();
        main.getChildren().addAll(root, bt);
        Scene gameSetup = new Scene(main, 600, 400);

        mainStage.setTitle("Deadwood Game Setup");
        mainStage.setResizable(false);
        mainStage.initStyle(StageStyle.DECORATED);
        mainStage.setScene(gameSetup);
        mainStage.show();
    }

    public void handleGameSetup(ActionEvent event) throws Exception {
        Parent numberplayers = FXMLLoader.load(getClass().getResource("/resources/number_players.fxml"));
        Stage main = (Stage) ((Node) event.getSource()).getScene().getWindow();
        main.close();
        Pane pn = new Pane();
        pn.getChildren().add(numberplayers);
        mainStage = new Stage();
        Scene numpPrompt = new Scene(pn, 470, 150);
        mainStage.setTitle("Number of Players");
        mainStage.setScene(numpPrompt);
        mainStage.setResizable(false);
        mainStage.show();
    }

    public void processPlayers(ActionEvent event) throws Exception{
        int playerCount = (int) numPlayers.getValue();
        initializeTF(playerCount);
        Stage prevWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent playerNames = FXMLLoader.load(getClass().getResource("/resources/player_names.fxml"));
        StackPane main = new StackPane();
        main.getChildren().addAll(playerNames);

        VBox layout = new VBox(8);
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(300);
        layout.getChildren().add(new Label("Enter player names: "));
        for(int k = 0; k < nameList.size(); k++){
            layout.getChildren().add(nameList.get(k));
        }
        Button start = new Button("Start Game");
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    prevWindow.close();
                    startGame(event, (ArrayList<TextField>) nameList);
                } catch (java.lang.Exception e){
                    System.out.println("java.lang.Exception: " + e.toString());
                }
            }
        });
        layout.getChildren().add(start);
        main.getChildren().add(layout);

        Scene namePrompt = new Scene(main, 600, 400);
        prevWindow.setTitle("Deadwood Game Setup");
        prevWindow.setScene(namePrompt);
        prevWindow.setResizable(false);
        prevWindow.show();

    }

    // TODO: Add fields for player data at bottom.
    public void startGame(ActionEvent event, ArrayList<TextField> userInput) throws Exception {
        try {
            ArrayList<String> lst = new ArrayList<String>();
            for (int i = 0; i < userInput.size(); i++) {
                lst.add(userInput.get(i).getText());
            }

            /* TODO: finish debugging the setup process for Deadwood */
            int np = (int) numPlayers.getValue();
            Board_Controller bc = Board_Controller.getInstance();
            mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            bc.start(mainStage, np, lst);


//            Board b = manager.getBoard();
//            ArrayList<Player> plyrs = b.getPlayers();
//            /* First player decided randomly */
//            Player currPlayer = manager.getActivePlayer();
//            int playerIndex = -1;
//            for(int i = 0; i < np; i++){
//                plyrs.get(i).setName(lst.get(i));
//                if (plyrs.get(i) == currPlayer){
//                    playerIndex = i;
//                }
//            }
//            assert playerIndex != -1: "Unable to find randomly chosen first player.";
//            ArrayList<Card> deck = manager.getDeck();
//            manager.shuffleDeck(deck);

//            boolean endOfGame = false;
//            Scanner playerChoice = new Scanner(System.in);
//            while(!endOfGame){
//
//                manager.doPlayerTurn(currPlayer);
//                currPlayer = manager.getActivePlayer();
//
//                if (manager.getNumberOfScenesRemaining() == 1) {
//                    ArrayList<Player> players = manager.getBoard().getPlayers();
//                    for (Player ply: players) {
//                        ply.setPlayerRoom(manager.getBoard().getTrailers());
//                    }
//                    endOfGame = manager.cycleGameDay();
//                    System.out.println("Day ended, all players returned to trailers. New scenes dealt.");
//                    System.out.println("Game Day: " + manager.getCurrentDay());
//                }
//
//                if(!endOfGame){
//                    manager.setActivePlayer(plyrs.get(++playerIndex % np));
//                    currPlayer = manager.getActivePlayer();
//                }
//            }
//            System.out.println("Game winner(s): ");
//            ArrayList<Player> winners = manager.scoreGame();
//            for (Player ply: winners) {
//                System.out.printf("Player: %s Score: %d", ply.getName(), manager.calculatePlayerScore(ply));
//            }
//            playerChoice.close();
            System.out.println("Back in Main");

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initializeTF(int nump) {
        for(int i = 1; i <= nump; i++){
            TextField name = new TextField("Player " + i);
            nameList.add(name);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
