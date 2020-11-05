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
    /*
        Card Tags:
            card name="Evil Wears a Hat" img="01.png" budget="4"
                 scene number="7"
    */
    // reads data from XML file and prints data
    
    private String getAttributeByName(Node n, String attribute) {
        return n.getAttributes().getNamedItem(attribute).getNodeValue();
    }

    private Role createRole(Node n, boolean onCard) {
        
        int roleId = -1; //TODO: figure out how to assign roles ids
        
        String roleName = getAttributeByName(n, "name");
        int roleLevel = Integer.parseInt(getAttributeByName(n, "level"));
        
        Role newRole = new Role(roleId, roleLevel, roleName, onCard);
        return newRole;
    }

    private Card createCard(Node card) {
        int sceneID = 999;
        int budget = Integer.parseInt(getAttributeByName(card, "budget"));
        String cardName = getAttributeByName(card, "name");
        boolean rolesOnCard = true;
        ArrayList<Role> cardRoles = new ArrayList<>();
        
        NodeList cardData = card.getChildNodes();
        int numChildren = cardData.getLength();
        String sceneType = "scene";
        String roleType = "part";

        for (int i = 0; i < numChildren; i++) {
            Node child = cardData.item(i);
            String childType = child.getNodeName();

            if (sceneType.equals(childType)) {
            
                sceneID = Integer.parseInt(getAttributeByName(child, "number"));
                System.out.println("Scene ID : " + sceneID);
            } else if (roleType.equals(childType)) {
            
                Role newRole = createRole(child, rolesOnCard);
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