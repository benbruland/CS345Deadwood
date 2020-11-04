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
    private Card createCard(Node card) {
        return null;
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
            }
        } catch(Exception e) {
            System.out.println("XML parse failure");
            e.printStackTrace();
        }
        //TODO: Fill in return value
        return null;
    }
}