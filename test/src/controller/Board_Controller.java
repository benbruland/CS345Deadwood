package controller;

import javafx.collections.ObservableList;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Board_Controller {

    private VBox gameData;
    private VBox playerData;
    private GridPane trailer;
    private GridPane castingoffice;
    private GridPane trainstation;
    private GridPane saloon;
    private GridPane secrethideout;
    private GridPane generalstore;
    private GridPane jail;
    private GridPane bank;
    private GridPane mainstreet;
    private GridPane hotel;
    private GridPane ranch;
    private GridPane church;
    private Label currPlayer;


    public static BoardManager boardManager = null;
    public static Board_Controller boardController = null;
    public Map<String, ImageView> scenes = new HashMap<String, ImageView>();
    public Map<String, ImageView> shots = new HashMap<String, ImageView>();
    public Map<String, Image> playerMarkers = new HashMap<String, Image>();
    public Map<String, GridPane> roomPanes = new HashMap<String, GridPane>();
    public Map<String, boolean[][]> roomPositions = new HashMap<String, boolean[][]>();
    public static Stage main;
    public static Parent root;
    public static Pane mainFrame;

    public Board_Controller(){};

    public static Board_Controller getInstance(){
        if(boardController == null){
            boardController = new Board_Controller();
            return boardController;
        }
        return boardController;
    }

    public void start(Stage mainStage, int nump, ArrayList<String> names) throws Exception{
        System.out.println("in start");
        main = mainStage;
        this.boardManager = BoardManager.getInstance(nump, names);
        Board b = boardManager.getBoard();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/board.fxml"));
        root = (Parent) loader.load();
        loader.setController(boardController);
        mainFrame = new Pane();
        mainFrame.getChildren().add(root);

        ArrayList<Player> plyrs = b.getPlayers();
        /* First player decided randomly */
        Player currPlayer = boardManager.getActivePlayer();
        setRoomPanes();
        int playerIndex = -1;
        for(int i = 0; i < nump; i++){
            createPlayerMarker(plyrs.get(i), i);
            if (plyrs.get(i) == currPlayer){
                playerIndex = i;
            }
        }
        assert playerIndex != -1: "Unable to find randomly chosen first player.";
        ArrayList<Card> deck = boardManager.getDeck();
        boardManager.shuffleDeck(deck);
        initialize();

        Stage gameStage = new Stage();
        gameStage.setResizable(false);
        gameStage.initStyle(StageStyle.DECORATED);
        gameStage.setX(0);
        gameStage.setY(0);
        gameStage.setWidth(1500);
        gameStage.setHeight(900);
        gameStage.initModality(Modality.WINDOW_MODAL);
        javafx.scene.Scene game = new Scene(mainFrame, 1400, 1000);
        gameStage.setTitle("Deadwood");
        gameStage.setScene(game);
        gameStage.show();

    }

    public void generateMovePrompt(ActionEvent event){
        ArrayList<String> neighboringRooms = boardManager.getActivePlayer().getPlayerRoom().getNeighborNames();
        VBox vb = new VBox(12);
        vb.setAlignment(Pos.CENTER);
        vb.setPrefSize(234, 200);
        Label prompt = new Label("Where would you like to move?");
        prompt.setStyle("-fx-text-fill:WHITE;");
        prompt.setPrefSize(260, 28);
        prompt.setAlignment(Pos.TOP_CENTER);
        prompt.setFont(Font.font("Sylfaen", 18));
        vb.getChildren().add(prompt);

        for(String s : neighboringRooms){
            Button bt = new Button(s);
            bt.setAlignment(Pos.CENTER);
            bt.setPrefSize(200, 25);
            bt.setFont(Font.font("Sylfaen", 18));
            bt.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String desiredRoom = s;
                    gameData.getChildren().remove(vb);
                    boardManager.doMoveIo(desiredRoom);
                }
            });
            vb.getChildren().add(bt);
        }
        gameData.getChildren().add(vb);
    }

    //TODO Implement takearole()

    public void setPositionOpen(String roomName, int col, int row){
        boolean[][] temp = roomPositions.get(roomName);
        temp[col][row] = false;
        roomPositions.put(roomName, temp);
    }

    public void setPositionOccupied(String roomName, int col, int row){
        boolean[][] temp = roomPositions.get(roomName);
        temp[col][row] = true;
        roomPositions.put(roomName, temp);
    }

    public void setAllNotOccupied(boolean[][] roomPositions){
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++){
                roomPositions[i][j] = false;
            }
        }
    }

    public void setShotCounters() throws Exception{
        ArrayList<Room> rooms = boardManager.getBoard().getRooms();
        for (Room room : rooms){
            ArrayList<GuiData> shotCoordinates = room.getShotPositions();
            Collections.reverse(shotCoordinates);
            GuiData data;
            String roomName = room.getRoomName();

            for(int i = 1; i <= shotCoordinates.size(); i++){
                data = shotCoordinates.get(i-1);
                ImageView shotImg = new ImageView(new Image("/resources/imgs/shot.png", data.getWidth(),data.getWidth(), false, false));
                shotImg.setX(data.getX());
                shotImg.setY(data.getY());
                shots.put(sanitizeRoomName(roomName)+i, shotImg);
                mainFrame.getChildren().add(shotImg);
            }
        }
        System.out.println("Shots set");
    }

    public void removeShotCounter(String roomName, int shotNum){
        ImageView img = shots.get(sanitizeRoomName(roomName)+shotNum);
        if(img != null){
            img.setImage(null);
        }
        System.out.println("Shots removed: #" + shotNum);
    }

    public void setScenes(){
        ArrayList<Room> roomList = boardManager.getBoard().getBoardRooms();
        for(Room room : roomList) {
            GuiData sceneCoordinates = room.getGuiData();
            String roomName = sanitizeRoomName(room.getRoomName()) + "scene";
            ImageView sceneImg = new ImageView(new Image("/resources/imgs/CardBack.jpg", sceneCoordinates.getWidth(),sceneCoordinates.getHeight(), false, false));
            sceneImg.setX(sceneCoordinates.getX());
            sceneImg.setY(sceneCoordinates.getY());
            scenes.put(roomName, sceneImg);
            mainFrame.getChildren().add(sceneImg);
        }
    }

    public void flipCardImage(String imgTitle, String roomName){
        String sceneName = sanitizeRoomName(roomName)+"scene";
        assert scenes.get(sceneName) != null: "Scene cannot be removed from map, is not in map";
        GuiData data = boardManager.getBoard().getRoomByName(roomName).getGuiData();
        ImageView img = new ImageView(new Image("/resources/imgs/"+imgTitle, data.getWidth(), data.getHeight() , false, true));
        img.setX(data.getX());
        img.setY(data.getY());
        scenes.put(sceneName, img);
        mainFrame.getChildren().add(img);
    }

    public void removeCardImage(String roomName){
        String sceneName = sanitizeRoomName(roomName) + "scene";
        ImageView img = scenes.get(sceneName);
        if (img != null){
            img.setImage(null);
            scenes.put(sceneName, img);
        }
        else{
            System.out.println("Scene image is null");
        }
    }

    public void setRoomPanes(){
        ArrayList<Room> rms = boardManager.getBoard().getAllRooms();
        GridPane gp;
        boolean[][] occupiedSpaces;
        String roomName;
        for(Room rm : rms){
            roomName = sanitizeRoomName(rm.getRoomName());
            occupiedSpaces = new boolean[4][2];
            setAllNotOccupied(occupiedSpaces);
            roomPositions.put(roomName, occupiedSpaces);
            switch(roomName){
                case "office":
                    gp = new GridPane();
                    gp.setLayoutX(25);
                    gp.setLayoutY(465);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                case "trailer":
                    gp = new GridPane();
                    gp.setLayoutX(1000);
                    gp.setLayoutY(280);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                case "saloon":
                    gp = new GridPane();
                    gp.setLayoutX(725);
                    gp.setLayoutY(180);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                case "bank":
                    gp = new GridPane();
                    gp.setLayoutX(725);
                    gp.setLayoutY(450);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                case "trainstation":
                    gp = new GridPane();
                    gp.setLayoutX(50);
                    gp.setLayoutY(50);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                case "jail":
                    gp = new GridPane();
                    gp.setLayoutX(250);
                    gp.setLayoutY(150);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                case "hotel":
                    gp = new GridPane();
                    gp.setLayoutX(975);
                    gp.setLayoutY(730);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                case "ranch":
                    gp = new GridPane();
                    gp.setLayoutX(225);
                    gp.setLayoutY(600);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                case "church":
                    gp = new GridPane();
                    gp.setLayoutX(1050);
                    gp.setLayoutY(260);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                case "secrethideout":
                    gp = new GridPane();
                    gp.setLayoutX(245);
                    gp.setLayoutY(810);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                case "generalstore":
                    gp = new GridPane();
                    gp.setLayoutX(375);
                    gp.setLayoutY(235);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                case "mainstreet":
                    gp = new GridPane();
                    gp.setLayoutX(775);
                    gp.setLayoutY(80);
                    gp.setPrefSize(90, 180);
                    roomPanes.put(roomName, gp);
                    mainFrame.getChildren().add(gp);
                    break;
                default:
                    System.out.println("Invalid roomName: " + roomName);
                    break;
            }
        }
    }

    public void setSidePanel(){
        gameData = new VBox(10);
        gameData.setLayoutX(1198);
        gameData.setPrefSize(302, 450);
        gameData.setStyle("-fx-background-color: lightgreen");
        gameData.setStyle("-fx-border-color: black");
        currPlayer = new Label("");
        currPlayer.setAlignment(Pos.TOP_CENTER);
        currPlayer.setFont(Font.font ("Sylfaen", 18));
        gameData.getChildren().add(currPlayer);

        playerData = new VBox();
        playerData.setAlignment(Pos.BOTTOM_CENTER);
        playerData.setPrefSize(300, 450);
        playerData.setLayoutX(1198);
        playerData.setLayoutY(450);
        playerData.setStyle("-fx-background-color: lightcoral");
        playerData.setStyle("-fx-border-color: black");

        HBox topRow = new HBox();
        Button moveBut = new Button("Move");
        moveBut.setFont(Font.font("Sylfaen", 18));
        moveBut.setPrefSize(100, 100);
        moveBut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                moveBut.setDisable(true);
                generateMovePrompt(event);
                moveBut.setDisable(false);
            }
        });

        Button takeRoleBut = new Button("Take a role");
        takeRoleBut.setFont(Font.font("Sylfaen", 16));
        takeRoleBut.setPrefSize(100, 100);
//        takeRoleBut.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//
//            }
//        });

        Button upgradeBut = new Button("Upgrade");
        upgradeBut.setFont(Font.font("Sylfaen", 18));
        upgradeBut.setPrefSize(100, 100);
//        upgradeBut.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//
//            }
//        });
        topRow.getChildren().addAll(moveBut, takeRoleBut, upgradeBut);

        HBox botRow = new HBox();

        Button actBut = new Button("Act");
        actBut.setFont(Font.font("Sylfaen", 28));
        actBut.setPrefSize(150, 100);
//        actBut.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                generateMovePrompt(event);
//            }
//        });

        Button rehearseBut = new Button("Rehearse");
        rehearseBut.setFont(Font.font("Sylfaen", 28));
        rehearseBut.setPrefSize(150, 100);
//        rehearseBut.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//
//            }
//        });
        botRow.getChildren().addAll(actBut, rehearseBut);
        gameData.getChildren().addAll(topRow, botRow);
        mainFrame.getChildren().addAll(gameData, playerData);
    }

    //Adding dummy comment

    public void moveToRoom(Player plyr, String roomName){
        try{
            Image img;
            GridPane gp;
            String sanRoomName = sanitizeRoomName(roomName);
            if ((img = playerMarkers.get(plyr.getName())) != null){
                if ((gp = roomPanes.get(sanRoomName)) != null){
                    int[] coordinates = getOpenPositionInRoom(sanRoomName);
                    setPositionOccupied(sanRoomName, coordinates[0], coordinates[1]);
                    gp.add(new ImageView(img), coordinates[0], coordinates[1]);
                    plyr.setPlayerCoordinates(coordinates);
                }
            }
        } catch(Exception e){
            System.out.println("Error in moveToRoom method");
            e.printStackTrace();
        }
    }

    public int[] getOpenPositionInRoom(String roomName){
        int[] coords = {-1, -1};
        boolean[][] roomPosition = roomPositions.get(roomName);
        for(int i = 0; i < 4; i ++){
            for(int j = 0; j < 2; j++){
                if(roomPosition[i][j] == false){
                    coords[0] = i;
                    coords[1] = j;
                    System.out.println("open space at position: (" + j + ", " + i + ")");
                    return coords;
                }
            }
        }
        return coords;
    }

    public void removeFromRoom(Player ply){
        String roomName = sanitizeRoomName(ply.getPlayerRoom().getRoomName());
        GridPane roomPane = roomPanes.get(roomName);
        int[] playerCoords = ply.getPlayerCoordinates();
        System.out.println("player coordinates: " + playerCoords.toString());
        ObservableList<Node> dicePane = roomPane.getChildren();
        for(Node n : dicePane){
            if((roomPane.getColumnIndex(n) == playerCoords[0] && roomPane.getRowIndex(n) == playerCoords[1])){
                roomPane.getChildren().remove(n);
                setPositionOpen(roomName, playerCoords[0], playerCoords[1]);
                roomPanes.put(roomName, roomPane);
                break;
            }
        }
    }

    public void createPlayerMarker(Player plyr, int num){
        String color = plyr.getPlayerColor();
        Image playerMarker;
        int row = num / 2;
        int col = num % 2;
        int coords[] = {row, col};

        switch (color){
            case "white":
                playerMarker = new Image("/resources/imgs/w"+plyr.getPlayerRank()+".png", 44, 44 , false, true);
                playerMarkers.put(plyr.getName(), playerMarker);
                moveToRoom(plyr, "trailer");
                break;
            case "green":
                playerMarker = new Image("/resources/imgs/g"+plyr.getPlayerRank()+".png", 44, 44 , false, true);
                playerMarkers.put(plyr.getName(), playerMarker);
                moveToRoom(plyr, "trailer");
                break;
            case "violet":
                playerMarker = new Image("/resources/imgs/v"+plyr.getPlayerRank()+".png", 44, 44 , false, true);
                playerMarkers.put(plyr.getName(), playerMarker);
                moveToRoom(plyr, "trailer");
                break;
            case "yellow":
                playerMarker = new Image("/resources/imgs/y"+plyr.getPlayerRank()+".png", 44, 44 , false, true);
                playerMarkers.put(plyr.getName(), playerMarker);
                moveToRoom(plyr, "trailer");
                break;
            case "orange":
                playerMarker = new Image("/resources/imgs/o"+plyr.getPlayerRank()+".png", 44, 44 , false, true);
                playerMarkers.put(plyr.getName(), playerMarker);
                moveToRoom(plyr, "trailer");
                break;
            case "blue":
                playerMarker = new Image("/resources/imgs/b"+plyr.getPlayerRank()+".png", 44, 44 , false, true);
                playerMarkers.put(plyr.getName(), playerMarker);
                moveToRoom(plyr, "trailer");
                break;
            case "pink":
                playerMarker = new Image("/resources/imgs/p"+plyr.getPlayerRank()+".png", 44, 44 , false, true);
                playerMarkers.put(plyr.getName(), playerMarker);
                moveToRoom(plyr, "trailer");
                break;
            case "red":
                playerMarker = new Image("/resources/imgs/r"+plyr.getPlayerRank()+".png", 44, 44 , false, true);
                playerMarkers.put(plyr.getName(), playerMarker);
                moveToRoom(plyr, "trailer");
                break;
            case "cyan":
                playerMarker = new Image("/resources/imgs/c"+plyr.getPlayerRank()+".png", 44, 44 , false, true);
                playerMarkers.put(plyr.getName(), playerMarker);
                moveToRoom(plyr, "trailer");
                break;
            default:
                System.out.println("Invalid color: " + color);
                break;
        }
    }

    public String sanitizeRoomName(String roomName){
        return roomName.toLowerCase().replace(" ", "");
    }

    public void initialize() throws Exception{
        System.out.println("In initialize method in Board_Controller");
        Board_Controller boardController = Board_Controller.getInstance();
        System.out.println("after Board_Controller.getInstance");
        boardController.setShotCounters();
        System.out.println("after setShotCounters");
        boardController.setScenes();
        System.out.println("after setScenes");
        boardController.setSidePanel();
        System.out.println("after setSidePanel");
//        boardController.removeShotCounter("Saloon", 2);
//        System.out.println("after shot counter removed");
//        boardController.flipCardImage("01.png", "Saloon");
//        System.out.println("after card image flipped");
//        boardController.removeShotCounter("Jail", 1);
//        boardController.removeCardImage("Jail");
//        System.out.println("after shot AND card image removal");

    }

}

