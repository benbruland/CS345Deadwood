package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

import java.util.*;


public class Board_Controller {

    private static BoardManager boardManager = null;
    private static Board_Controller boardController = null;
    private Map<String, ImageView> scenes = new HashMap<String, ImageView>();
    private Map<String, ImageView> cardBacks = new HashMap<String, ImageView>();
    private Map<String, ImageView> shots = new HashMap<String, ImageView>();
    private Map<String, ImageView> playerMarkers = new HashMap<String, ImageView>();
    private Map<String, GridPane> roomPanes = new HashMap<String, GridPane>();
    private Map<String, boolean[][]> roomPositions = new HashMap<String, boolean[][]>();
    private Map<String, Label> playerScore = new HashMap<String, Label>();
    private HashMap<String, Button> buttonMap = new HashMap<String, Button>();
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

    private void setButtonStates(boolean buttonIsOn, String ... buttonIds) {
        for (String id: buttonIds) {
            this.buttonMap.get(id).setDisable(!buttonIsOn);
        }
    }

    private void setButtonStates(boolean buttonIsOn, ArrayList<String> buttonIds) {
        for (String id: buttonIds) {
            this.buttonMap.get(id).setDisable(!buttonIsOn);
        }
    }

    public void doMoveIo(ArrayList<String> actionList, String roomChoice) {
        Player ply = boardManager.getActivePlayer();
        if (ply.getPlayerInRole()) {
            showDialog("Move Failure", "The Player may not move, because the player is in a role.");
            return;
        }
        removeFromRoom(ply);
        moveToRoom(ply, roomChoice);
        Room roomOfChoice = boardManager.getBoard().getRoomByName(roomChoice);
        ply.move(roomOfChoice);
        boardManager.registerAction(actionList, ply, "move");
        updateButtonStates(actionList);
        /* if scene card is face down */
        Room plyRoom = ply.getPlayerRoom();
        if (!ply.getPlayerRoom().getFlippedOver()){
            String roomName = sanitizeRoomName(ply.getPlayerRoom().getRoomName());
            flipCardImage(plyRoom.getRoomScene().getImageTitle(), plyRoom.getRoomName());
            plyRoom.setFlippedOver(true);
            GridPane roomPane = roomPanes.get(roomName);
            roomPane.toFront();
            roomPanes.put(roomName, roomPane);
        }
    }

    public void generateMovePrompt(ArrayList<String> actionList, VBox gameData, ActionEvent event){
        System.out.println("in move prompt");
        ArrayList<String> neighboringRooms = boardManager.getActivePlayer().getPlayerRoom().getNeighborNames();
        VBox vb = makePrompt("Where would you like to move?", 5);

        for(String s : neighboringRooms){
            Button bt = new Button(s);
            bt.setAlignment(Pos.CENTER);
            bt.setPrefSize(200, 20);
            bt.setFont(Font.font("Sylfaen", 16));
            bt.setOnAction(e -> {

                String desiredRoom = s;
                gameData.getChildren().remove(vb);
                doMoveIo(actionList, desiredRoom);

            });
            vb.getChildren().add(bt);
        }
        gameData.getChildren().add(vb);
    }

    public void createUpgradeWindow(ActionEvent event){
        Stage upgradeWindow = new Stage();
        upgradeWindow.setResizable(false);
        upgradeWindow.setTitle("Upgrade Rank");
        upgradeWindow.initStyle(StageStyle.DECORATED);
        upgradeWindow.initModality(Modality.APPLICATION_MODAL);
        Pane root = new Pane();
        ImageView bg = new ImageView(new Image("/resources/imgs/CardBack.jpg", 600, 400, false, false));
        bg.setOpacity(0.75);
        root.getChildren().add(bg);

        HBox topBar = new HBox(100);
        topBar.setPadding(new Insets(0, 65, 0 , 0));
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPrefSize(600, 0);
        Label creditLabel = new Label("Credits");
        creditLabel.setAlignment(Pos.TOP_CENTER);
        creditLabel.setFont(Font.font("Sylfaen", 28));
        Label dollarLabel = new Label("Dollars");
        dollarLabel.setAlignment(Pos.TOP_CENTER);
        dollarLabel.setFont(Font.font("Sylfaen", 28));
        topBar.getChildren().addAll(dollarLabel, creditLabel);
        root.getChildren().add(topBar);

        VBox rankBox = new VBox(15);
        rankBox.setLayoutX(45);
        rankBox.setLayoutY(50);
        ToggleGroup rankGrp = new ToggleGroup();
        rankBox.setAlignment(Pos.TOP_CENTER);
        ArrayList<RadioButton> buttonList = createRankButtons(rankGrp, 6);
        rankBox.getChildren().addAll(buttonList);
        root.getChildren().addAll(rankBox);


        int[] dollarCosts = boardManager.getBoard().getDollarUpgradeCostList();
        int[] creditCosts = boardManager.getBoard().getCreditUpgradeCostList();

        VBox dollarCostBox = new VBox(15);
        dollarCostBox.setPadding(new Insets(25, 0, 0, 0));
        dollarCostBox.setAlignment(Pos.TOP_CENTER);
        dollarCostBox.setLayoutX(405);
        dollarCostBox.setLayoutY(25);
        dollarCostBox.setPrefSize(180, 100);
        ArrayList<Label> labelDollarList = createLabelNodes(dollarCosts, 6);
        dollarCostBox.getChildren().addAll(labelDollarList);
        root.getChildren().add(dollarCostBox);


        VBox creditCostBox = new VBox(15);
        creditCostBox.setPadding(new Insets(25, 0, 0, 0));
        creditCostBox.setAlignment(Pos.TOP_CENTER);
        creditCostBox.setLayoutX(225);
        creditCostBox.setLayoutY(25);
        creditCostBox.setPrefSize(180, 100);
        ArrayList<Label> labelCreditList = createLabelNodes(creditCosts, 6);
        creditCostBox.getChildren().addAll(labelCreditList);
        root.getChildren().addAll(creditCostBox);

        ToggleGroup selectionGrp = new ToggleGroup();
        RadioButton creditBut = new RadioButton("Credits");
        creditBut.setFont(Font.font("Sylfaen", 20));
        creditBut.setLayoutX(210);
        creditBut.setLayoutY(335);
        creditBut.setToggleGroup(selectionGrp);
        RadioButton dollarBut = new RadioButton("Dollars");
        dollarBut.setLayoutX(340);
        dollarBut.setLayoutY(335);
        dollarBut.setFont(Font.font("Sylfaen", 20));
        dollarBut.setToggleGroup(selectionGrp);
        Label payPrompt = new Label("Pay with                         or ");
        payPrompt.setFont(Font.font("Sylfaen", 18));
        payPrompt.setLayoutX(135);
        payPrompt.setLayoutY(340);;


        Button submitBut = new Button("Submit Upgrade");
        submitBut.setFont(Font.font("Sylfaen", 12));
        submitBut.setLayoutY(370);
        submitBut.setPrefSize(600, 30);
        submitBut.setOnAction(e -> { try{
                                        processUpgrade(e,((RadioButton)rankGrp.getSelectedToggle()).getText(),
                                                ((RadioButton)selectionGrp.getSelectedToggle()).getText());
                                    } catch(Exception excep){
                                        showDialog("Invalid Upgrade",
                                                "Rank selection and payment type buttons must be selected.");}});
        root.getChildren().addAll(payPrompt, dollarBut, creditBut, submitBut);
        Scene upgradeScene = new Scene(root, 600, 400);
        upgradeWindow.setScene(upgradeScene);
        upgradeWindow.show();
    }

    private ArrayList<Label> createLabelNodes(int[] upgradeCost, int maxRank){
        Player plyr = boardManager.getActivePlayer();
        int playerRank = plyr.getPlayerRank();
        ArrayList<Label> labelList = new ArrayList<Label>();
        for(int i = playerRank + 1; i <= maxRank; i++){
            Label label = new Label(Integer.toString(upgradeCost[i-2]));
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font("Sylfaen", 28));
            labelList.add(label);
        }
        return labelList;
    }

    private ArrayList<RadioButton> createRankButtons(ToggleGroup grp, int maxRank){
        Player plyr = boardManager.getActivePlayer();
        int playerRank = plyr.getPlayerRank();
        ArrayList<RadioButton> buttonList = new ArrayList<RadioButton>();
        for(int i = playerRank + 1; i <= maxRank; i++){
            RadioButton rankBut = new RadioButton("Rank " + i);
            rankBut.setAlignment(Pos.CENTER);
            rankBut.setFont(Font.font("Sylfaen", 28));
            rankBut.setToggleGroup(grp);
            buttonList.add(rankBut);
        }
        return buttonList;
    }

    private void processUpgrade(ActionEvent e, String rankString, String currencySelection){
        int rankSelection = Integer.parseInt(rankString.replaceAll("[a-zA-Z\\s+]", ""));
        Player plyr = boardManager.getActivePlayer();
        Stage prevWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
        int dollars = currencySelection == "Dollars" ? plyr.getPlayerDollars() : 0;
        int credits = currencySelection == "Credits" ? plyr.getPlayerCredits() : 0;
        boolean success = plyr.upgrade(rankSelection, dollars, credits);
        updateSinglePlayerScore(plyr);
        if(success){
            showDialog("Successful Upgrade", "Player rank upgraded to rank " + plyr.getPlayerRank());
            removeFromRoom(plyr);
            playerMarkers.remove(plyr.getName());
            String color = plyr.getPlayerColor();
            String diceImage = "/resources/imgs/" + color.charAt(0) + plyr.getPlayerRank() + ".png";
            System.out.println(diceImage);
            ImageView imgV = new ImageView(new Image(diceImage, 45, 45 , false, true));
            playerMarkers.put(plyr.getName(), imgV);
            moveToRoom(plyr, "office");
            prevWindow.close();
        }
        else{
            showDialog("Error upgrading", "Player does not have sufficient currency to upgrade to rank " + rankSelection);
        }
    }

    private void showDialog(String title, String message) {
        Dialog<String> dialog = new Dialog<String>();
        dialog.setTitle(title);
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
    }

    private void toggleButtons(ArrayList<Button> buttons, boolean buttonIsOn) {
        for (Button btn: buttons) {
            btn.setDisable(!buttonIsOn);
        }
    }

    private void disableButtons(String ... buttonIds) {
        ArrayList<Button> buttonsToDisable = getButtonsByIds(buttonIds);
        toggleButtons(buttonsToDisable, false);
    }

    private void enableButtons(String ... buttonIds) {
        ArrayList<Button> buttonsToEnable = getButtonsByIds(buttonIds);
        toggleButtons(buttonsToEnable, true);
    }

    private void updateButtonStates(ArrayList<String> actionList) {
        toggleButtons(getButtonsByIds("move","act","rehearse","takerole","upgrade","end"), false);
        setButtonStates(true, actionList);
    }

    private void doTakeRoleIo(ArrayList<String> actionList, VBox gameData, GridPane roleButtonContainer, VBox rolePrompt, Role plyRole) {
        Player ply = boardManager.getActivePlayer();
        actionList = boardManager.registerAction(actionList, ply, "takerole");
        updateButtonStates(actionList);
        String dialogMessage = "This role is too high in rank for you";
        if (!ply.getPlayerInRole() && ply.playerCanTakeRole(plyRole)) {
            boardManager.setPlayerRole(ply, plyRole);
            setPlayerMarkerToRole(ply, plyRole);
            dialogMessage = "Successfully took role: " + plyRole.getRoleName();
        } else {
            enableButtons("takerole");
        }

        showDialog("Take Role Attempt", dialogMessage);
        gameData.getChildren().remove(roleButtonContainer);
        gameData.getChildren().remove(rolePrompt);
    }

    public void setPlayerMarkerToRole(Player plyr, Role rl){
        String playerName = plyr.getName();
        removeFromRoom(plyr);
        ImageView imgV = playerMarkers.get(playerName);
        GuiData data;
        if(rl.getIsOnCardRole()){
            data = rl.getGuiData();
            GuiData sceneData = plyr.getPlayerRoom().getGuiData();
            imgV.setX(data.getX()+sceneData.getX());
            imgV.setY(data.getY()+sceneData.getY());
            imgV.toFront();
        }
        else{
            data = rl.getGuiData();
            imgV.setX(data.getX());
            imgV.setY(data.getY());
            imgV.toFront();
        }
        mainFrame.getChildren().add(imgV);
    }

    public void removePlayerMarkerFromRole(Player plyr){
        mainFrame.getChildren().remove(playerMarkers.get(plyr.getName()));
    }

    private VBox makePrompt(String promptStr, int vSpace) {
        VBox promptContainer = new VBox(vSpace);
        promptContainer.setAlignment(Pos.TOP_CENTER);
        Label prompt = new Label(promptStr);
        prompt.setPrefSize(250, 28);
        prompt.setAlignment(Pos.TOP_CENTER);
        prompt.setFont(Font.font("Sylfaen", 16));
        prompt.setStyle("-fx-text-fill: white");
        promptContainer.getChildren().add(prompt);
        return promptContainer;
    }

    private GridPane makeRoleBox(ArrayList<Role> roles) {
        GridPane roleContainer = new GridPane();
        roleContainer.setAlignment(Pos.CENTER);
        roleContainer.setPrefSize(234, 200);
        return roleContainer;
    }


    public void takeRoleButtonHandler(ArrayList<String> actionList, ArrayList<Button> buttons, VBox gameData, ActionEvent event) {
        Player activePly = boardManager.getActivePlayer();
        Room plyRoom = activePly.getPlayerRoom();
        disableButtons("end", "move");


        if (!plyRoom.roomHasScene()) {
            showDialog("No roles", "There are no roles in the casting office, or the trailers.");
            return;
        }

        ArrayList<Role> roles = boardManager.getAvailableRoles(plyRoom.getRoomScene());
        ObservableList children = gameData.getChildren();
        Font roleFont = Font.font("Sylfaen", 12);
        VBox rolePrompt = makePrompt("Choose a Role:", 12);
        GridPane roleContainer = makeRoleBox(roles);
        children.addAll(rolePrompt, roleContainer);

        int roleCounter = 0;
        for (Role plyRole: roles) {
            Button roleButton = new Button(plyRole.getRoleName());
            roleButton.setAlignment(Pos.CENTER);
            int len = plyRole.getRoleName().length();
            roleButton.setPrefSize(200, 10);
            roleButton.setFont(roleFont);
            roleContainer.add(roleButton, roleCounter % 2, roleCounter/2);
            roleButton.setOnAction(e -> doTakeRoleIo(actionList, gameData, roleContainer, rolePrompt,plyRole));
            roleCounter++;
        }
    }

    private ArrayList<Button> getButtonsByIds(String ... ids) {
        ArrayList<Button> requestedButtons = new ArrayList<Button>();
        for (String id: ids) {
            requestedButtons.add(this.buttonMap.get(id));
        }
        return requestedButtons;
    }

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
        int height = roomPositions.length;
        int width =  roomPositions[0].length;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
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

            for(int i = 0; i < shotCoordinates.size(); i++){
                data = shotCoordinates.get(i);
                ImageView shotImg = new ImageView(new Image("/resources/imgs/shot.png", data.getWidth(),data.getWidth(), false, false));
                shotImg.setX(data.getX());
                shotImg.setY(data.getY());
                shots.put(sanitizeRoomName(roomName)+(i+1), shotImg);
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
            cardBacks.put(sanitizeRoomName(room.getRoomName()), sceneImg);
            mainFrame.getChildren().add(sceneImg);
        }
    }

    public void flipCardImage(String imgTitle, String roomName){
        String sceneName = sanitizeRoomName(roomName)+"scene";
        assert cardBacks.get(sceneName) != null: "Scene cannot be removed from map, is not in map";
        ImageView cardBack = cardBacks.get(sanitizeRoomName(roomName));
        if(cardBack != null) {
            cardBack.setImage(null);
            cardBacks.put(sanitizeRoomName(roomName), cardBack);
        }
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
            System.out.println("In removeCardImage if statement");
            img.setImage(null);
            scenes.put(sceneName, img);
        }
        else{
            System.out.println("Scene image is null");
        }
    }

    public void setRoomPanes(){
        ArrayList<Room> rms = boardManager.getBoard().getAllRooms();
        boolean[][] occupiedSpaces;
        String roomName;
        for(Room rm : rms) {
            GridPane gp = new GridPane();
            roomName = sanitizeRoomName(rm.getRoomName());
            occupiedSpaces = new boolean[4][2];
            setAllNotOccupied(occupiedSpaces);
            roomPositions.put(roomName, occupiedSpaces);
            switch(roomName) {
                case "office":
                    gp.setLayoutX(25);
                    gp.setLayoutY(465);
                    break;
                case "trailer":
                    gp.setLayoutX(1000);
                    gp.setLayoutY(280);
                    break;
                case "saloon":
                    gp.setLayoutX(725);
                    gp.setLayoutY(180);
                    break;
                case "bank":
                    gp.setLayoutX(725);
                    gp.setLayoutY(450);
                    break;
                case "trainstation":
                    gp.setLayoutX(50);
                    gp.setLayoutY(50);
                    break;
                case "jail":
                    gp.setLayoutX(250);
                    gp.setLayoutY(150);
                    break;
                case "hotel":
                    gp.setLayoutX(975);
                    gp.setLayoutY(730);
                    break;
                case "ranch":
                    gp.setLayoutX(225);
                    gp.setLayoutY(600);
                    break;
                case "church":
                    gp.setLayoutX(730);
                    gp.setLayoutY(645);
                    break;
                case "secrethideout":
                    gp.setLayoutX(245);
                    gp.setLayoutY(810);
                    break;
                case "generalstore":
                    gp.setLayoutX(375);
                    gp.setLayoutY(235);
                    break;
                case "mainstreet":
                    gp.setLayoutX(775);
                    gp.setLayoutY(80);
                    break;
                default:
                    System.out.println("Invalid roomName: " + roomName);
                    break;
            }
            roomPanes.put(roomName, gp);
            mainFrame.getChildren().add(gp);
        }
    }

    private VBox createPlayerDataPane() {
        VBox playerData = new VBox();
        playerData.setAlignment(Pos.TOP_CENTER);
        playerData.setPrefSize(300, 450);
        playerData.setLayoutX(1198);
        playerData.setLayoutY(450);
        playerData.setStyle("-fx-border-color: white");
        Label prompt = new Label("Player Data");
        prompt.setAlignment(Pos.TOP_CENTER);
        prompt.setFont(Font.font ("Sylfaen", 48));
        prompt.setStyle("-fx-text-fill: white");
        GridPane playerInfoContainer = new GridPane();
        playerInfoContainer.setAlignment(Pos.CENTER);
        playerInfoContainer.setVgap(10);
        playerInfoContainer.setPadding(new Insets(10, 5, 10, 5));
        ArrayList<Player> playerList = boardManager.getBoard().getPlayers();
        int numPlayers = playerList.size();
        int row;
        for(row = 0; row < numPlayers; row++){
            Player plyr = playerList.get(row);
            String plyrColor = plyr.getPlayerColor();
            String plyrName = plyr.getName();
            Label playerInfo = new Label(plyrName + ", Dollars: " + plyr.getPlayerDollars() + ", Credits: " + plyr.getPlayerCredits());
            playerInfo.setFont(Font.font("Sylfaen", FontWeight.BOLD, 16));
            playerInfo.setStyle("-fx-text-fill: " + plyrColor);
            playerScore.put(plyrName, playerInfo);
            playerInfoContainer.add(playerInfo, 0, row);
        }
        playerData.getChildren().addAll(prompt, playerInfoContainer);
        return playerData;
    }

    private VBox createGameDataPane() {
        VBox gameData = new VBox(10);
        gameData.setLayoutX(1198);
        gameData.setPrefSize(302, 450);
        gameData.setStyle("-fx-border-color: white");
        Label currPlayer = new Label("");
        currPlayer.setAlignment(Pos.TOP_CENTER);
        currPlayer.setFont(Font.font ("Sylfaen", 18));
        gameData.getChildren().add(currPlayer);
        return gameData;
    }

    ArrayList<Button> createActionButtons(ArrayList<String> actions) {
        Font buttonFont = Font.font("Sylfaen", 15);
        ArrayList<Button> actionButtons = new ArrayList<Button>();

        for (String action: actions) {
            Button newButton = new Button(action);
            newButton.setPrefSize(150,75);
            newButton.setFont(buttonFont);
            actionButtons.add(newButton);
        }

        return actionButtons;
    }

    public void updateActivePlayer(Player prevPlyr, Player plyr, int fontSize){
        String playerName = prevPlyr.getName();
        Label lbl = playerScore.get(playerName);
        lbl.setAlignment(Pos.TOP_CENTER);
        lbl.setFont(Font.font("Sylfaen", 16));
        playerScore.put(playerName, lbl);

        playerName = plyr.getName();
        lbl = playerScore.get(playerName);
        lbl.setAlignment(Pos.TOP_CENTER);
        lbl.setFont(Font.font("Sylfaen", fontSize));
        playerScore.put(playerName, lbl);
    }

    public void updateSinglePlayerScore(Player plyr){
        String playerName = plyr.getName();
        Label lbl = playerScore.get(playerName);
        lbl.setText(playerName + ", Dollars: " + plyr.getPlayerDollars() + ", Credits: " + plyr.getPlayerCredits());
        playerScore.put(playerName, lbl);
    }

    public void moveButtonHandler(ArrayList<String> actionList, ActionEvent e, VBox gameData) {
        disableButtons("end");
        generateMovePrompt(actionList, gameData, e);
    }

    public void endButtonHandler(ArrayList<String> actionList) {
        Player prevPlyr = boardManager.getActivePlayer();
        boardManager.gotoNextPlayer();
        Player plyr = boardManager.getActivePlayer();
        updateActivePlayer(prevPlyr, plyr, 20);
        actionList = boardManager.getValidActions(plyr);
        updateButtonStates(actionList);
    }



    public void actButtonHandler(ArrayList<String> actionList) {
        Player activePly = boardManager.getActivePlayer();
        boardManager.doActIo(activePly);
        actionList = boardManager.registerAction(actionList, activePly, "act");
        updateButtonStates(actionList);
    }


    public void rehearseButtonHandler(ArrayList<String> actionList) {
        Player activePly = boardManager.getActivePlayer();
        Room playerRoom = activePly.getPlayerRoom();
        actionList = boardManager.registerAction(actionList, activePly, "rehearse");
        int rehearseBonus = activePly.getRehearsalBonus();
        if (rehearseBonus < playerRoom.getRoomScene().getSceneCard().getBudget() - 1) {
            activePly.setRehearsalBonus(rehearseBonus + 1);
            rehearseBonus = activePly.getRehearsalBonus();
            showDialog("Rehearsal Bonus", "Rehearsal Bonus Now: " + rehearseBonus);
        } else {
            showDialog("Rehearsal Bonus", "Player may not rehearse further.");
        }
        updateButtonStates(actionList);
    }

    public void setSidePanel() {
        ArrayList<String> buttonKeys = new ArrayList<String>(Arrays.asList("takerole","move","act","rehearse","upgrade","end"));
        ArrayList<Button> buttons = createActionButtons(buttonKeys);
        Player activePly = boardManager.getActivePlayer();
        ArrayList<String> actionList = boardManager.getValidActions(activePly);
        addKeysValues(buttonMap, buttonKeys, buttons);
        VBox gameData = createGameDataPane();
        VBox playerData = createPlayerDataPane();
        HBox topRow = new HBox();

        getButtonById("move").setOnAction(e -> {
            disableButtons("move");
            moveButtonHandler(actionList, e, gameData);
        });

        getButtonById("end").setOnAction(e -> {
            endButtonHandler(actionList);
        });

        getButtonById("takerole").setOnAction(e -> {
            disableButtons("takerole");
            takeRoleButtonHandler(actionList, buttons, gameData, e);
        });

        getButtonById("upgrade").setOnAction(e -> {
                disableButtons("upgrade");
                createUpgradeWindow(e);
                boardManager.registerAction(actionList, activePly, "upgrade");
            }
        );

        getButtonById("act").setOnAction(e -> {
                disableButtons("act");
                actButtonHandler(actionList);
            }
        );

        getButtonById("rehearse").setOnAction(e -> {
                disableButtons("rehearse");
                rehearseButtonHandler(actionList);
            }
        );

        topRow.getChildren().addAll(getButtonsByIds("move","upgrade","takerole"));
        HBox botRow = new HBox();
        disableButtons("rehearse","upgrade","takerole","act");
        botRow.getChildren().addAll(getButtonsByIds("rehearse","act","end"));
        gameData.getChildren().addAll(topRow, botRow);
        mainFrame.getChildren().addAll(gameData, playerData);
    }

    private void addKeysValues(HashMap<String,Button> map, ArrayList<String> keys, ArrayList<Button> values) {
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }
    }


    public void moveToRoom(Player plyr, String roomName) {
        try{
            ImageView imgV;
            GridPane gp;
            String sanRoomName = sanitizeRoomName(roomName);
            if ((imgV = playerMarkers.get(plyr.getName())) != null){
                if ((gp = roomPanes.get(sanRoomName)) != null){
                    int[] coordinates = getOpenPositionInRoom(sanRoomName);
                    setPositionOccupied(sanRoomName, coordinates[0], coordinates[1]);
                    gp.add(imgV, coordinates[0], coordinates[1]);
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
        ImageView playerMarker;
        String diceImage = "/resources/imgs/" + color.charAt(0) + plyr.getPlayerRank() + ".png";
        System.out.println(diceImage);
        playerMarker = new ImageView(new Image(diceImage, 45, 45 , false, true));
        playerMarkers.put(plyr.getName(), playerMarker);
        moveToRoom(plyr, "trailer");
    }

    public String sanitizeRoomName(String roomName){
        return roomName.toLowerCase().replace(" ", "");
    }

    private Button getButtonById(String id) {
        return this.buttonMap.get(id);
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
        updateActivePlayer(boardManager.getActivePlayer(), boardManager.getActivePlayer(), 20);
        System.out.println("After updating active player initially");
    }
}

