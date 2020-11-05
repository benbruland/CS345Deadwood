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
            System.out.println("XML parse failure");
            e.printStackTrace();
        }
        return deck;
    }
}