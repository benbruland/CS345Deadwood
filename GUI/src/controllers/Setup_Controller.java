package controllers;


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
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class Setup_Controller {

    @FXML
    private Slider numPlayers;
    public static int playerCount;

    private Stage setup = new Stage();
    public static List<TextField> nameList = new ArrayList<TextField>();

    public void handleGameSetup(ActionEvent event) throws Exception {
        Parent numPlayers = FXMLLoader.load(getClass().getResource("../resources/numberPlayers.fxml"));
        Scene numpPrompt = new Scene(numPlayers, 500, 100);
        Stage main = (Stage) ((Node) event.getSource()).getScene().getWindow();
        main.close();
        setup.setTitle("Deadwood Game Setup");
        setup.setScene(numpPrompt);
        setup.show();
    }

    public void processPlayers(ActionEvent event) throws Exception{
        playerCount = (int) numPlayers.getValue();
        initializeTF();
        Stage prevWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        prevWindow.close();
        Parent playerNames = FXMLLoader.load(getClass().getResource("../resources/playerNames.fxml"));
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
                    startGame(event, (ArrayList) nameList);
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
        prevWindow.show();

    }

    // TODO: Add fields for player data at bottom.
    public void startGame(ActionEvent event, ArrayList<TextField> userInput) throws Exception {
        List<String> lst = new ArrayList<String>();
        for (int i = 0; i < userInput.size(); i++){
            lst.add(userInput.get(i).getText());
        }
        Parent newRoot = FXMLLoader.load(getClass().getResource("../resources/board_gui.fxml"));
        Stage setupWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        setupWindow.close();
        Stage gameStage = new Stage();
        gameStage.setResizable(false);
        gameStage.initStyle(StageStyle.DECORATED);
        gameStage.initModality(Modality.APPLICATION_MODAL);
        gameStage.setX(0);
        gameStage.setY(0);
        gameStage.setWidth(1200);
        gameStage.setHeight(900);
        gameStage.initModality(Modality.WINDOW_MODAL);
        Scene game = new Scene(newRoot, 1200,900);
        gameStage.setTitle("Deadwood");
        gameStage.setScene(game);
        gameStage.show();


    }

    private void initializeTF() {
        for(int i = 1; i <= playerCount; i++){
            TextField name = new TextField("Player " + i);
            nameList.add(name);
        }
    }

}