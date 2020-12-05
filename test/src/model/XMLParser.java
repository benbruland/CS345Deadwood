//Authors: Benjamin Bruland, Lukas McIntosh
package model;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.ArrayList;

public class XMLParser {

    private static String cardDocument = "model/XML/cards.xml";
    private static String boardDocument = "model/XML/board.xml";

    private static int expectedNumberOfRanks = 5;

    public Document getDocFromFile(String filename)
            throws ParserConfigurationException{
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;

            try{
                doc = db.parse(filename);
            } catch (Exception ex){
                System.out.println("XML parse failure");
                ex.printStackTrace();
            }
            return doc;
        } // exception handling

    }

    
    private String getAttributeByName(Node n, String attribute) {
        return n.getAttributes().getNamedItem(attribute).getNodeValue();
    }

    //Pass this method a role node from the XML Document builder,
    //creates a role object from the XML and returns it
    private Role createRole(Node n, boolean onCard, int roleId) {
        
        
        String roleName = getAttributeByName(n, "name");
        int roleLevel = Integer.parseInt(getAttributeByName(n, "level"));
        NodeList children = n.getChildNodes();
        int listSize = children.getLength();
        String areaType = "area";
        String lineType = "line";
        String line = "default line";
        GuiData data = new GuiData();
        for (int i = 0; i < listSize; i++) {
            Node child = children.item(i);
            if (child.getNodeName().equals(areaType)) {
                data = parseGuiData(child);
            } else if (child.getNodeName().equals(lineType)) {
                line = child.getTextContent();
            }
        }

        Role newRole = new Role(roleId, roleLevel, roleName, onCard, data);
        newRole.setGuiData(data);
        newRole.setLine(line);
        return newRole;
    }

    //Creates a card by reading XML stored in XML/cards.xml
    //pass this method an XML node, it returns a Card object
    private Card createCard(Node card) {
        int sceneID = 0;
        int roleIDCounter = 0;
        int budget = Integer.parseInt(getAttributeByName(card, "budget"));
        String cardName = getAttributeByName(card, "name");
        String imgName = getAttributeByName(card, "img");
        boolean rolesOnCard = true;
        ArrayList<Role> cardRoles = new ArrayList<>();
        
        NodeList cardData = card.getChildNodes();
        int numChildren = cardData.getLength();
        String sceneType = "scene";
        String roleType = "part";

        for (int i = 0; i < numChildren; i++) {
            //Child contains: <scene> <part> <line> and <area> tags
            Node child = cardData.item(i);
            String childType = child.getNodeName();

            if (sceneType.equals(childType)) {

                sceneID = Integer.parseInt(getAttributeByName(child, "number"));
           
            } else if (roleType.equals(childType)) {
                //Role id is intended to be an index into the array list cardRoles
                Role newRole = createRole(child, rolesOnCard, roleIDCounter);
                roleIDCounter++;
                cardRoles.add(newRole);
            }
        }
        //public Card(int cardBudget, int cardSceneId, String card, Role[] roles)
        Card newCard = new Card(budget, sceneID, cardName, cardRoles);
        newCard.setImage(imgName);
        return newCard;
    }

    private GuiData parseGuiData(Node areaNode) {
        int height = Integer.parseInt(getAttributeByName(areaNode, "h"));
        int width = Integer.parseInt(getAttributeByName(areaNode, "w"));
        int x = Integer.parseInt(getAttributeByName(areaNode, "x"));
        int y = Integer.parseInt(getAttributeByName(areaNode, "y"));
        GuiData data = new GuiData(height, width, x, y);
        return data;
    }

    public ArrayList<Card> readCardData(Document cardDoc) {
        ArrayList<Card> deck = new ArrayList<>();
        try {

            Element rootCard = cardDoc.getDocumentElement();
            NodeList cards = rootCard.getElementsByTagName("card");
            int numCards = cards.getLength();
            
            for (int i = 0; i < numCards; i++) {
                Node card = cards.item(i);
                Card newCard = createCard(card);
                deck.add(newCard);
            }
        } catch(Exception e) {
            System.out.println("XML parsing exception");
            e.printStackTrace();
        }
        return deck;
    }

    // Returns an array list of strings which represent the names
    // of the neighboring rooms to this room object.
    
    private ArrayList<String> getRoomNeighbors(Node neighborNode) {
        ArrayList<String> neighbors = new ArrayList<String>();
        NodeList neighborList = neighborNode.getChildNodes();
        int listSize = neighborList.getLength();
        for (int i = 0; i < listSize; i++) {
             Node child = neighborList.item(i);
             String nodeType = child.getNodeName();
             if (nodeType.equals("neighbor")) {
                String name = getAttributeByName(child, "name");
                neighbors.add(name);
             }
        }
        return neighbors;
    }

    // Pass in a <parts> tag to this function to parse
    // off card roles from board.xml
    private ArrayList<Role> getRoomOffCardRoles(Node partsNode) {
        NodeList parts = partsNode.getChildNodes();
        ArrayList<Role> roomRoles = new ArrayList<Role>();
        int offCardRoleIDCounter = 0;
        int listSize = parts.getLength();

        for (int i = 0; i < listSize; i++) {
            Node child = parts.item(i);
            String childType = child.getNodeName();
            if (childType.equals("part")) {
                roomRoles.add(createRole(child, false, offCardRoleIDCounter));
                offCardRoleIDCounter++;
            }
        }
        return roomRoles;

    }

    // pass in a <set> object to parse a Room object from
    // board.xml
    Room createRoom(Node roomNode, String roomName) {
        NodeList roomData = roomNode.getChildNodes();
        ArrayList<String> neighbors = new ArrayList<String>();
        ArrayList<Role> offCardRoles = new ArrayList<Role>();
        int numTakes = 0;
        boolean hasTakes = false;
        int listSize = roomData.getLength();
        GuiData data = new GuiData();
        ArrayList<GuiData> shotsData = new ArrayList<GuiData>();

        for (int i = 0; i < listSize; i++) {
            Node child = roomData.item(i);
            String childType = child.getNodeName();
            
            switch (childType) {
                case "neighbors":
                    neighbors = getRoomNeighbors(child);
                    break;
                case "parts":
                    offCardRoles = getRoomOffCardRoles(child);
                    break;
                case "takes":
                    hasTakes = true;
                    NodeList takes = child.getChildNodes();
                    for (int j = 0; j < takes.getLength(); j++) {
                        Node take = takes.item(j);
                        if  (take.getNodeName() != "#text") {
                            NodeList shotPositions = take.getChildNodes();
                            for (int k = 0; k < shotPositions.getLength(); k++) {
                                Node position = shotPositions.item(k);
                                if (position.getNodeName().equals("area")) {
                                    shotsData.add(parseGuiData(position));
                                }
                            }
                            numTakes++;
                        }
                    }
                    break;
                case "area":
                    data = parseGuiData(child);
                    break;
                default:
            }

        }

        //This is for the special case of trailers and office which have no takes, but it
        numTakes = hasTakes ? numTakes : 0;
        
        Room newRoom = new Room(0, roomName, null, neighbors, offCardRoles, numTakes);
        newRoom.setGuiData(data);
        newRoom.setShotPositions(shotsData);
        return newRoom;
    }

    // Creates and fills room objects from
    public Board parseBoard() {
        ArrayList<Card> deck = new ArrayList<Card>();
        ArrayList<Room> boardRooms = new ArrayList<Room>();;
        Room castingOffice = new Room();
        Room trailers = new Room();
        int[][] costs = new int[expectedNumberOfRanks][expectedNumberOfRanks];
        try {
            Document boardDoc = getDocFromFile(boardDocument);
            Document cardDoc = getDocFromFile(cardDocument);
            deck = readCardData(cardDoc);
            boardRooms = createBoardRooms(boardDoc);
            castingOffice = readRoomData(boardDoc, "office");
            trailers = readRoomData(boardDoc, "trailer");
            costs = readUpgradeCosts(boardDoc);
            
        } catch(Exception e) {
            System.out.println("XML parsing exception");
            e.printStackTrace();
        }
        Board gameBoard = new Board(boardRooms, deck, costs, castingOffice, trailers);        
        return gameBoard;
    }

    public ArrayList<Room> createBoardRooms(Document boardDoc) {
        ArrayList<Room> boardRooms = new ArrayList<>();
        try {
            Element boardRoot = boardDoc.getDocumentElement(); 
            NodeList boardSets = boardRoot.getElementsByTagName("set");
            int listSize = boardSets.getLength();
            
            for (int i = 0; i < listSize; i++) {
                Node child = boardSets.item(i);
                String roomName = getAttributeByName(child, "name");
                Room newRoom = createRoom(child, roomName);
                boardRooms.add(newRoom);
            }

        } catch (Exception e) {
            System.out.println("XML parsing exception");
            e.printStackTrace();
        }
        return boardRooms;
    }

    public Room readRoomData(Document boardDoc, String roomName) {
        Room room = new Room();
        try {
            Element boardRoot = boardDoc.getDocumentElement(); 
            NodeList boardSets = boardRoot.getElementsByTagName(roomName);
            room = createRoom(boardSets.item(0), roomName);
        } catch (Exception e) {
            System.out.println("XML parsing exception");
            e.printStackTrace();
        }
        return room;
    }

    private int[] parseCurrencyCosts(NodeList list, String type) {
        int[] costs = new int[expectedNumberOfRanks];
        int costIndex = 0;
        int listSize = list.getLength();

        for (int i = 0; i < listSize; i++) {
            Node cost = list.item(i);
            if (!cost.getNodeName().equals("#text") && getAttributeByName(cost, "currency").equals(type)) {
                costs[costIndex] = Integer.parseInt(getAttributeByName(cost, "amt"));
                costIndex++;
            }
        }
        return costs;
    }

    public int[][] readUpgradeCosts(Document boardDoc) {
        int[] creditCosts = new int[expectedNumberOfRanks];
        int[] dollarCosts = new int[expectedNumberOfRanks];

        try {
            Element boardRoot = boardDoc.getDocumentElement(); 
            NodeList boardSets = boardRoot.getElementsByTagName("office");
            Node officeRoom = boardSets.item(0);
            NodeList costs = officeRoom.getChildNodes();
            int listSize = costs.getLength();

            for (int i = 0; i < listSize; i++) {
                Node child = costs.item(i);
                if (child.getNodeName().equals("upgrades")) {
                    NodeList upgradeCosts = child.getChildNodes();
                    creditCosts = parseCurrencyCosts(upgradeCosts, "credit");
                    dollarCosts = parseCurrencyCosts(upgradeCosts, "dollar");
                }
            }

        } catch (Exception e) {
            System.out.println("XML parsing exception");
            e.printStackTrace();
        }

        int[][] costMatrix = {creditCosts, dollarCosts};
        return costMatrix;
    }

    public Room readCastingOfficeData(Document boardDoc) {
       return readRoomData(boardDoc, "office");
    }

    public Room readTrailersData(Document boardDoc) {
        return readRoomData(boardDoc, "trailer");
    }
}