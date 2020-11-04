import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLParser {
    // building a document from the XML file
    // returns a Document object after loading the book.xml file.
    public static Document getDocFromFile(String filename)
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
    public static Card[] readCardData(Document d) {

        Element rootCard = d.getDocumentElement();

        NodeList cards = rootCard.getElementsByTagName("card");
        int numCards = cards.getLength();
        
        for (int i = 0; i < numCards; i++) {
            Node card = cards.item(i);
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            System.out.printf("Card %d name = %s\n", i+1, cardName);
        }

        //TODO: Fill in return value
        return null;
    }
    public static void main(String[] args) {
        try {
            Document d = getDocFromFile("XML/cards.xml");
            Card[] cards = readCardData(d);
        } catch(Exception e) {
            System.out.print("Exception: "+ e.toString());
        }
       
    }

}