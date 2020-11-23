import java.util.ArrayList;

public class Card {
    private int cardBudget;
    private String cardName;
    private int cardScene;
    private ArrayList<Role> cardRoles;
    private String cardImage;

    public Card() {
        this.cardBudget = 0;
        this.cardName = "default";
        this.cardScene = -1;
    }

    public Card(int cardBudget, int cardSceneId, String card, ArrayList<Role> roles) {
        this.cardBudget = cardBudget;
        this.cardScene = cardSceneId;
        this.cardName = card;
        this.cardRoles = roles;
    }

    public void printCard() {
        System.out.println("\n==================================");
        System.out.println("Card Name = " + this.cardName);
        System.out.println("\t* Card Budget = " + this.cardBudget);
        System.out.println("\t* Num roles = " + this.cardRoles.size());
        System.out.println("\t* Card Scene Number = " + this.cardScene);
        this.printCardRoles();
        System.out.println("==================================\n");
    }

    private void printCardRoles() {
        int numRoles = this.cardRoles.size();
        System.out.println("Card Roles: ");
        for (int i = 0; i < numRoles; i++) {
            System.out.println("Role [" + (i+1) + "] :" );
            this.cardRoles.get(i).printRole();
        }
    }

    public ArrayList<Role> getRoles() {
        return this.cardRoles;
    }

    public int getBudget() {
        return this.cardBudget;
    }

    public String getImage() {
        return this.cardImage;
    }

    public String getName() {
        return this.cardName;
    }

    public int getCardSceneId() {
        return this.cardScene;
    }

    public void setCardScene(int sceneId) {
        this.cardScene = sceneId;
    }

    public void setCardRoles(ArrayList<Role> roles) {
        this.cardRoles = roles;
    }

    public void setCardName(String name) {
        this.cardName = name;
    }

    public void setCardBudget(int budget) {
        this.cardBudget = budget;
    }

    public void setImage(String img) {
        this.cardImage = img;
    }
}