package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board_Controller {

//    @FXML
//    private Pane guiContainer;
//
//    @FXML
//    private ImageView trainstation1;
//
//    @FXML
//    private ImageView trainstation2;
//
//    @FXML
//    private ImageView trainstation3;
//
//    @FXML
//    private ImageView saloon1;
//
//    @FXML
//    private ImageView saloon2;
//
//    @FXML
//    private ImageView jail1;
//
//    @FXML
//    private ImageView church1;
//
//    @FXML
//    private ImageView church2;
//
//    @FXML
//    private ImageView secrethideout1;
//
//    @FXML
//    private ImageView secrethideout2;
//
//    @FXML
//    private ImageView secrethideout3;
//
//    @FXML
//    private ImageView mainstreet1;
//
//    @FXML
//    private ImageView mainstreet2;
//
//    @FXML
//    private ImageView mainstreet3;
//
//    @FXML
//    private ImageView bank1;
//
//    @FXML
//    private ImageView ranch1;
//
//    @FXML
//    private ImageView ranch2;
//
//    @FXML
//    private ImageView generalstore1;
//
//    @FXML
//    private ImageView generalstore2;
//
//    @FXML
//    private ImageView hotel1;
//
//    @FXML
//    private ImageView hotel2;
//
//    @FXML
//    private ImageView hotel3;
//
//    @FXML
//    private ImageView saloonscene;
//
//    @FXML
//    private ImageView mainstreetscene;
//
//    @FXML
//    private ImageView secrethideoutscene;
//
//    @FXML
//    private ImageView jailscene;
//
//    @FXML
//    private ImageView bankscene;
//
//    @FXML
//    private ImageView churchscene;
//
//    @FXML
//    private ImageView hotelscene;
//
//    @FXML
//    private ImageView ranchscene;
//
//    @FXML
//    private ImageView trainstationscene;
//
//    @FXML
//    private ImageView generalstorescene;

    public static BoardManager boardManager = null;
    public static Board_Controller boardController = null;
    public Map<String, ImageView> scenes = new HashMap<String, ImageView>();
    public Map<String, ImageView> shots = new HashMap<String, ImageView>();
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
        initialize();

        ArrayList<Player> plyrs = b.getPlayers();
        /* First player decided randomly */
        Player currPlayer = boardManager.getActivePlayer();
        int np = names.size();
        int playerIndex = -1;
        for(int i = 0; i < np; i++){
            plyrs.get(i).setName(names.get(i));
            if (plyrs.get(i) == currPlayer){
                playerIndex = i;
            }
        }
        assert playerIndex != -1: "Unable to find randomly chosen first player.";
        ArrayList<Card> deck = boardManager.getDeck();
        boardManager.shuffleDeck(deck);

        Stage gameStage = new Stage();
        gameStage.setResizable(false);
        gameStage.initStyle(StageStyle.DECORATED);
        gameStage.initModality(Modality.APPLICATION_MODAL);
        gameStage.setX(0);
        gameStage.setY(0);
        gameStage.setWidth(1400);
        gameStage.setHeight(1000);
        gameStage.initModality(Modality.WINDOW_MODAL);
        //Parent newRoot = FXMLLoader.load(getClass().getResource("/resources/board.fxml"));
        javafx.scene.Scene game = new Scene(mainFrame, 1400, 1000);
        gameStage.setTitle("Deadwood");
        gameStage.setScene(game);
        gameStage.show();

    }

    public void setShotCounters() throws Exception{
        ArrayList<Room> rooms = boardManager.getBoard().getRooms();
        for (Room room : rooms){
            ArrayList<GuiData> shotCoordinates = room.getShotPositions();
            GuiData data;
            String roomName = room.getRoomName();
            switch(roomName){
                case "Saloon":
                    data = shotCoordinates.get(0);
                    ImageView saloon2 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    saloon2.setX(data.getX());
                    saloon2.setY(data.getY());
                    shots.put("saloon2", saloon2);
                    mainFrame.getChildren().add(saloon2);
                    data = shotCoordinates.get(1);
                    ImageView saloon1 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    saloon1.setX(data.getX());
                    saloon1.setY(data.getY());
                    shots.put("saloon1", saloon1);
                    mainFrame.getChildren().add(saloon1);
                    break;
                case "Hotel":
                    data = shotCoordinates.get(0);
                    ImageView hotel3 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    hotel3.setX(data.getX());
                    hotel3.setY(data.getY());
                    shots.put("hotel3", hotel3);
                    mainFrame.getChildren().add(hotel3);
                    data = shotCoordinates.get(1);
                    ImageView hotel2 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    hotel2.setX(data.getX());
                    hotel2.setY(data.getY());
                    shots.put("hotel2", hotel2);
                    mainFrame.getChildren().add(hotel2);
                    data = shotCoordinates.get(2);
                    ImageView hotel1 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    hotel1.setX(data.getX());
                    hotel1.setY(data.getY());
                    shots.put("hotel2", hotel2);
                    mainFrame.getChildren().add(hotel1);
                    break;
                case "Secret Hideout":
                    data = shotCoordinates.get(0);
                    ImageView secrethideout3 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    secrethideout3.setX(data.getX());
                    secrethideout3.setY(data.getY());
                    shots.put("secrethideout3", secrethideout3);
                    mainFrame.getChildren().add(secrethideout3);
                    data = shotCoordinates.get(1);
                    ImageView secrethideout2 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    secrethideout2.setX(data.getX());
                    secrethideout2.setY(data.getY());
                    shots.put("secrethideout2", secrethideout2);
                    mainFrame.getChildren().add(secrethideout2);
                    data = shotCoordinates.get(2);
                    ImageView secrethideout1 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    secrethideout1.setX(data.getX());
                    secrethideout1.setY(data.getY());
                    shots.put("secrethideout1", secrethideout1);
                    mainFrame.getChildren().add(secrethideout1);
                    break;
                case "Ranch":
                    data = shotCoordinates.get(0);
                    ImageView ranch2 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    ranch2.setX(data.getX());
                    ranch2.setY(data.getY());
                    shots.put("ranch2", ranch2);
                    mainFrame.getChildren().add(ranch2);
                    data = shotCoordinates.get(1);
                    ImageView ranch1 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    ranch1.setX(data.getX());
                    ranch1.setY(data.getY());
                    shots.put("ranch1", ranch1);
                    mainFrame.getChildren().add(ranch1);
                    break;
                case "Train Station":
                    data = shotCoordinates.get(0);
                    ImageView trainstation3 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    trainstation3.setX(data.getX());
                    trainstation3.setY(data.getY());
                    shots.put("trainstation3", trainstation3);
                    mainFrame.getChildren().add(trainstation3);
                    data = shotCoordinates.get(1);
                    ImageView trainstation2 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    trainstation2.setX(data.getX());
                    trainstation2.setY(data.getY());
                    shots.put("trainstation2", trainstation2);
                    mainFrame.getChildren().add(trainstation2);
                    data = shotCoordinates.get(2);
                    ImageView trainstation1 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    trainstation1.setX(data.getX());
                    trainstation1.setY(data.getY());
                    shots.put("trainstation1", trainstation1);
                    mainFrame.getChildren().add(trainstation1);
                    break;
                case "Church":
                    data = shotCoordinates.get(0);
                    ImageView church2 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    church2.setX(data.getX());
                    church2.setY(data.getY());
                    shots.put("church2", church2);
                    mainFrame.getChildren().add(church2);
                    data = shotCoordinates.get(1);
                    ImageView church1 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    church1.setX(data.getX());
                    church1.setY(data.getY());
                    shots.put("church1", church1);
                    mainFrame.getChildren().add(church1);
                    break;
                case "Bank":
                    data = shotCoordinates.get(0);
                    ImageView bank1 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    bank1.setX(data.getX());
                    bank1.setY(data.getY());
                    shots.put("bank1", bank1);
                    mainFrame.getChildren().add(bank1);
                    break;
                case "General Store":
                    data = shotCoordinates.get(0);
                    ImageView generalstore2 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    generalstore2.setX(data.getX());
                    generalstore2.setY(data.getY());
                    shots.put("generalstore2", generalstore2);
                    mainFrame.getChildren().add(generalstore2);
                    data = shotCoordinates.get(1);
                    ImageView generalstore1 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    generalstore1.setX(data.getX());
                    generalstore1.setY(data.getY());
                    shots.put("generalstore1", generalstore1);
                    mainFrame.getChildren().add(generalstore1);
                    break;
                case "Jail":
                    data = shotCoordinates.get(0);
                    ImageView jail1 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    jail1.setX(data.getX());
                    jail1.setY(data.getY());
                    shots.put("jail1", jail1);
                    mainFrame.getChildren().add(jail1);
                    break;
                case "Main Street":
                    data = shotCoordinates.get(0);
                    ImageView mainstreet3 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    mainstreet3.setX(data.getX());
                    mainstreet3.setY(data.getY());
                    shots.put("mainstreet3", mainstreet3);
                    mainFrame.getChildren().add(mainstreet3);
                    data = shotCoordinates.get(1);
                    ImageView mainstreet2 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    mainstreet2.setX(data.getX());
                    mainstreet2.setY(data.getY());
                    shots.put("mainstreet2", mainstreet2);
                    mainFrame.getChildren().add(mainstreet2);
                    data = shotCoordinates.get(2);
                    ImageView mainstreet1 = new ImageView(new Image("/resources/imgs/shot.png", 47,47, false, false));
                    mainstreet1.setX(data.getX());
                    mainstreet1.setY(data.getY());
                    shots.put("mainstreet1", mainstreet1);
                    mainFrame.getChildren().add(mainstreet1);
                    break;
                default:
                    System.out.println("Invalid room, nothing shots to set");
                    break;
            }
        }
        System.out.println("Shots set");
    }

    public void setScenes(){
        ArrayList<Room> roomList = boardManager.getBoard().getRooms();
        for(Room room : roomList) {
            GuiData sceneCoordinates = room.getGuiData();
            String roomName = room.getRoomName();

            switch (roomName){
                case "Saloon":
                    ImageView saloonscene = new ImageView(new Image("/resources/imgs/CardBack.jpg", sceneCoordinates.getWidth(),sceneCoordinates.getHeight(), false, false));
                    saloonscene.setX(sceneCoordinates.getX());
                    saloonscene.setY(sceneCoordinates.getY());
                    scenes.put("saloonscene", saloonscene);
                    mainFrame.getChildren().add(saloonscene);
                    break;
                case "Train Station":
                    ImageView trainstationscene = new ImageView(new Image("/resources/imgs/CardBack.jpg", sceneCoordinates.getWidth(),sceneCoordinates.getHeight(), false, false));
                    trainstationscene.setX(sceneCoordinates.getX());
                    trainstationscene.setY(sceneCoordinates.getY());
                    scenes.put("trainstationscene", trainstationscene);
                    mainFrame.getChildren().add(trainstationscene);
                    break;
                case "Ranch":
                    ImageView ranchscene = new ImageView(new Image("/resources/imgs/CardBack.jpg", sceneCoordinates.getWidth(),sceneCoordinates.getHeight(), false, false));
                    ranchscene.setX(sceneCoordinates.getX());
                    ranchscene.setY(sceneCoordinates.getY());
                    scenes.put("ranchscene", ranchscene);
                    mainFrame.getChildren().add(ranchscene);
                    break;
                case "Bank":
                    ImageView bankscene = new ImageView(new Image("/resources/imgs/CardBack.jpg", sceneCoordinates.getWidth(),sceneCoordinates.getHeight(), false, false));
                    bankscene.setX(sceneCoordinates.getX());
                    bankscene.setY(sceneCoordinates.getY());
                    scenes.put("bankscene", bankscene);
                    mainFrame.getChildren().add(bankscene);
                    break;
                case "Church":
                    ImageView churchscene = new ImageView(new Image("/resources/imgs/CardBack.jpg", sceneCoordinates.getWidth(),sceneCoordinates.getHeight(), false, false));
                    churchscene.setX(sceneCoordinates.getX());
                    churchscene.setY(sceneCoordinates.getY());
                    scenes.put("churchscene", churchscene);
                    mainFrame.getChildren().add(churchscene);
                    break;
                case "Secret Hideout":
                    ImageView secrethideoutscene = new ImageView(new Image("/resources/imgs/CardBack.jpg", sceneCoordinates.getWidth(),sceneCoordinates.getHeight(), false, false));
                    secrethideoutscene.setX(sceneCoordinates.getX());
                    secrethideoutscene.setY(sceneCoordinates.getY());
                    scenes.put("secrethideoutscene", secrethideoutscene);
                    mainFrame.getChildren().add(secrethideoutscene);
                    break;
                case "Main Street":
                    ImageView mainstreetscene = new ImageView(new Image("/resources/imgs/CardBack.jpg", sceneCoordinates.getWidth(),sceneCoordinates.getHeight(), false, false));
                    mainstreetscene.setX(sceneCoordinates.getX());
                    mainstreetscene.setY(sceneCoordinates.getY());
                    scenes.put("mainstreetscene", mainstreetscene);
                    mainFrame.getChildren().add(mainstreetscene);
                    break;
                case "General Store":
                    ImageView generalstorescene = new ImageView(new Image("/resources/imgs/CardBack.jpg", sceneCoordinates.getWidth(),sceneCoordinates.getHeight(), false, false));
                    generalstorescene.setX(sceneCoordinates.getX());
                    generalstorescene.setY(sceneCoordinates.getY());
                    scenes.put("generalstorescene", generalstorescene);
                    mainFrame.getChildren().add(generalstorescene);
                    break;
                case "Hotel":
                    ImageView hotelscene = new ImageView(new Image("/resources/imgs/CardBack.jpg", sceneCoordinates.getWidth(),sceneCoordinates.getHeight(), false, false));
                    hotelscene.setX(sceneCoordinates.getX());
                    hotelscene.setY(sceneCoordinates.getY());
                    scenes.put("hotelscene", hotelscene);
                    mainFrame.getChildren().add(hotelscene);
                    break;
                case "Jail":
                    ImageView jailscene = new ImageView(new Image("/resources/imgs/CardBack.jpg", sceneCoordinates.getWidth(),sceneCoordinates.getHeight() , false, false));
                    jailscene.setX(sceneCoordinates.getX());
                    jailscene.setY(sceneCoordinates.getY());
                    scenes.put("jailscene", jailscene);
                    mainFrame.getChildren().add(jailscene);
                    break;
                default:
                    System.out.println("Invalid room name: " + roomName);
                    break;
            }
        }
    }

    /* Singleton implementation */
//    private static final Board_Controller bc = new Board_Controller();
//
//    private Board_Controller(){};
//
//    public Board_Controller getBCInstance(){
//        return bc;
//    }

    /* TODO */

    public void removeShotCounter(String roomName, int shotNum){
        ImageView img;
        switch(roomName) {
            case "Saloon":

                if ((img = shots.get("saloon2")) != null){
                    img.setImage(null);
                }
                break;
            case "Hotel":
                if ((img = shots.get("hotel3")) != null){
                    img.setImage(null);
                }
                else if ((img = shots.get("hotel2")) != null) {
                    img.setImage(null);
                }
                else if ((img = shots.get("hotel1")) != null){
                    img.setImage(null);
                }
                else{
                    System.out.println("All shots removed for this scene.");
                }
                break;
            case "Secret Hideout":
                if ((img = shots.get("secrethideout3")) != null){
                    img.setImage(null);
                }
                else if ((img = shots.get("secrethideout2")) != null) {
                    img.setImage(null);
                }
                else if ((img = shots.get("secrethideout1")) != null){
                    img.setImage(null);
                }
                else{
                    System.out.println("All shots removed for this scene.");
                }
                break;
            case "Ranch":
                if ((img = shots.get("ranch2")) != null){
                    img.setImage(null);
                }
                break;
            case "Train Station":
                if ((img = shots.get("trainstation3")) != null){
                    img.setImage(null);
                }
                else if ((img = shots.get("trainstation2")) != null) {
                    img.setImage(null);
                }
                else if ((img = shots.get("trainstation1")) != null){
                    img.setImage(null);
                }
                else{
                    System.out.println("All shots removed for this scene.");
                }
                break;
            case "Church":
                if ((img = shots.get("church2")) != null){
                    img.setImage(null);
                }
                break;
            case "Bank":
                if ((img = shots.get("bank1")) != null){
                    img.setImage(null);
                }
                break;
            case "General Store":
                if ((img = shots.get("generalstore3")) != null){
                    img.setImage(null);
                }
                else if ((img = shots.get("generalstore2")) != null) {
                    img.setImage(null);
                }
                else if ((img = shots.get("generalstore1")) != null){
                    img.setImage(null);
                }
                else{
                    System.out.println("All shots removed for this scene.");
                }
                break;
            case "Jail":
                if ((img = shots.get("jail1")) != null){
                    img.setImage(null);
                }
                else{
                    System.out.println("All shots removed for this scene.");
                }
                break;

            case "Main Street":
                if ((img = shots.get("mainstreet3")) != null){
                    img.setImage(null);
                }
                else if ((img = shots.get("mainstreet2")) != null) {
                    img.setImage(null);
                }
                else if ((img = shots.get("mainstreet1")) != null){
                    img.setImage(null);
                }
                else{
                    System.out.println("All shots removed for this scene.");
                }
                break;
            default:
                System.out.println("Invalid room name: " + roomName);
                break;
        }
    }

    public void flipCardImage(String imgTitle, String roomName){
        ImageView img;
        GuiData data = boardManager.getBoard().getRoomByName(roomName).getGuiData();
        switch(roomName){
            case "Saloon":
                assert scenes.remove("saloonscene") != null: "Scene cannot be removed from map, is not in map";
                img = new ImageView(new Image("/resources/imgs/"+imgTitle, data.getWidth(), data.getHeight() , false, true));
                img.setX(data.getX());
                img.setY(data.getY());
                scenes.put("saloonscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Hotel":
                assert scenes.remove("hotelscene") != null: "Scene cannot be removed from map, is not in map";
                img = new ImageView(new Image("/resources/imgs/"+imgTitle, data.getWidth(), data.getHeight(), false, true));
                img.setX(data.getX());
                img.setY(data.getY());
                scenes.put("hotelscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Secret Hideout":
                assert scenes.remove("secrethideoutscene") != null: "Scene cannot be removed from map, is not in map";
                img = new ImageView(new Image("/resources/imgs/"+imgTitle, data.getWidth(), data.getHeight() , false, true));
                scenes.put("secrethideoutscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Ranch":
                assert scenes.remove("ranchscene") != null: "Scene cannot be removed from map, is not in map";
                img = new ImageView(new Image("/resources/imgs/"+imgTitle, data.getWidth(), data.getHeight() , false, true));
                img.setX(data.getX());
                img.setY(data.getY());
                scenes.put("ranchscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Train Station":
                assert scenes.remove("trainstationscene") != null: "Scene cannot be removed from map, is not in map";
                img = new ImageView(new Image("/resources/imgs/"+imgTitle, data.getWidth(), data.getHeight() , false, true));
                img.setX(data.getX());
                img.setY(data.getY());
                scenes.put("trainstationscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Church":
                assert scenes.remove("churchscene") != null: "Scene cannot be removed from map, is not in map";
                img = new ImageView(new Image("/resources/imgs/"+imgTitle, data.getWidth(), data.getHeight() , false, true));
                img.setX(data.getX());
                img.setY(data.getY());
                scenes.put("churchscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Bank":
                assert scenes.remove("bankscene") != null: "Scene cannot be removed from map, is not in map";
                img = new ImageView(new Image("/resources/imgs/"+imgTitle, data.getWidth(), data.getHeight() , false, true));
                img.setX(data.getX());
                img.setY(data.getY());
                scenes.put("bankscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "General Store":
                assert scenes.remove("generalstorescene") != null: "Scene cannot be removed from map, is not in map";
                img = new ImageView(new Image("/resources/imgs/"+imgTitle, data.getWidth(), data.getHeight(), false, true));
                img.setX(data.getX());
                img.setY(data.getY());
                scenes.put("generalstorescene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Jail":
                assert scenes.remove("jailscene") != null: "Scene cannot be removed from map, is not in map";
                img = new ImageView(new Image("/resources/imgs/"+imgTitle, data.getWidth(), data.getHeight() , false, true));
                img.setX(data.getX());
                img.setY(data.getY());
                scenes.put("jailscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Main Street":
                assert scenes.remove("mainstreetscene") != null: "Scene cannot be removed from map, is not in map";
                img = new ImageView(new Image("/resources/imgs/"+imgTitle, data.getWidth(), data.getHeight(), false, true));
                img.setX(data.getX());
                img.setY(data.getY());
                scenes.put("mainstreetscene", img);
                mainFrame.getChildren().add(img);
                break;
            default:
                System.out.println("Invalid room, nothing to flip over");
                break;
        }
    }

    public void removeCardImage(String roomName){
        ImageView img;
        switch(roomName){
            case "Train Station":
                img = scenes.get("trainstationscene");
                img.setImage(null);
                assert scenes.remove("trainstationscene") != null: "Scene cannot be removed from map, is not in map";
                scenes.put("trainstationscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Secret Hideout":
                img = scenes.get("secrethideoutscene");
                img.setImage(null);
                assert scenes.remove("secrethideoutscene") != null: "Scene cannot be removed from map, is not in map";
                scenes.put("secrethideoutscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "General Store":
                img = scenes.get("generalstorescene");
                img.setImage(null);
                assert scenes.remove("generalstorescene") != null: "Scene cannot be removed from map, is not in map";
                scenes.put("generalstorescene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Main Street":
                img = scenes.get("mainstreetscene");
                img.setImage(null);
                assert scenes.remove("mainstreetscene") != null: "Scene cannot be removed from map, is not in map";
                scenes.put("mainstreetscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Hotel":
                img = scenes.get("hotelscene");
                img.setImage(null);
                assert scenes.remove("hotelscene") != null: "Scene cannot be removed from map, is not in map";
                scenes.put("hotelscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Saloon":
                img = scenes.get("saloonscene");
                img.setImage(null);
                assert scenes.remove("saloonscene") != null: "Scene cannot be removed from map, is not in map";
                scenes.put("saloonscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Church":
                img = scenes.get("churchscene");
                img.setImage(null);
                assert scenes.remove("churchscene") != null: "Scene cannot be removed from map, is not in map";
                scenes.put("churchscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Ranch":
                img = scenes.get("ranchscene");
                img.setImage(null);
                assert scenes.remove("ranchscene") != null: "Scene cannot be removed from map, is not in map";
                scenes.put("ranchscene", img);
                mainFrame.getChildren().add(img);
                break;
            case "Jail":
//                img = scenes.get("jailscene");
//                img.setImage(null);
//                assert scenes.remove("jailscene") != null: "Scene cannot be removed from map, is not in map";
//                scenes.put("jailscene", img);
//                mainFrame.getChildren().add(img);
                if ((img = scenes.get("jailscene")) != null){
                    img.setImage(null);
                    scenes.remove("jailscene");
                }
                else{
                    System.out.println("Scene image is null");
                }
                break;
            case "Bank":
                img = scenes.get("bankscene");
                img.setImage(null);
                assert scenes.remove("bankscene") != null: "Scene cannot be removed from map, is not in map";
                scenes.put("bankscene", img);
                mainFrame.getChildren().add(img);
                break;
            default:
                System.out.println("Invalid roomName: " + roomName);
                break;
        }
    }

    @FXML
    public void initialize() throws Exception{
        System.out.println("In initialize method Board_Controller");
        Board_Controller boardController = Board_Controller.getInstance();
        System.out.println("after Board_Controller.getInstance");
        boardController.setShotCounters();
        System.out.println("after setShotCounters");
        boardController.setScenes();
        System.out.println("after setScenes");
        boardController.removeShotCounter("Saloon", 2);
        System.out.println("after shot counter removed");
        boardController.flipCardImage("01.png", "Saloon");
        System.out.println("after card image flipped");
        boardController.removeShotCounter("Jail", 1);
        boardController.removeCardImage("Jail");
        System.out.println("after shot AND card image removal");

    }

}

