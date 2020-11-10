import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

public class XMLParser {
    // building a document from the XML file
    // returns a Document object after loading the book.xml file.
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
        
        Role newRole = new Role(roleId, roleLevel, roleName, onCard);

        return newRole;
    }

    //Creates a card by reading XML stored in XML/cards.xml
    //pass this method an XML node, it returns a Card object
    private Card createCard(Node card) {
        int sceneID = 0;
        int roleIDCounter = 0;
        int budget = Integer.parseInt(getAttributeByName(card, "budget"));
        String cardName = getAttributeByName(card, "name");
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
        return newCard;
    }

    public ArrayList<Card> readCardData() {
        ArrayList<Card> deck = new ArrayList<>();
        try {
            Document cardDoc = getDocFromFile("XML/cards.xml");
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

    Room createRoom(Node roomNode) {
        NodeList roomData = roomNode.getChildNodes();
        String roomName = getAttributeByName(roomNode, "name");
        ArrayList<String> neighbors = new ArrayList<String>();
        ArrayList<Role> offCardRoles = new ArrayList<Role>();
        int listSize = roomData.getLength();
        
        for (int i = 0; i < listSize; i++) {
            Node child = roomData.item(i);
            String childType = child.getNodeName();

            if (childType.equals("neighbors")) {
                neighbors = getRoomNeighbors(child);
            }
            
            if (childType.equals("parts")) {
                offCardRoles = getRoomOffCardRoles(child);
            }
        }

        Room newRoom = new Room(0, roomName, null, neighbors, offCardRoles);
        return newRoom;
    }


    //TODO: implement readBoardData()
    public ArrayList<Room> readBoardData() {
        ArrayList<Room> boardRooms = new ArrayList<>();
        try {
            Document boardDoc = getDocFromFile("XML/board.xml");
            Element boardRoot = boardDoc.getDocumentElement(); 
            boardRoot.normalize();
            NodeList boardSets = boardRoot.getElementsByTagName("set");
            int listSize = boardSets.getLength();
            
            for (int i = 0; i < listSize; i++) {
                Node child = boardSets.item(i);
                Room newRoom = createRoom(child);
                boardRooms.add(newRoom);
            }

        } catch (Exception e) {
            System.out.println("XML parsing exception");
            e.printStackTrace();
        }
        return boardRooms;
    }
}