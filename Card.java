public class Card {
    private int cardBudget;
    private String cardName;
    private int cardScene;
    private ArrayList<Role> cardRoles;

    public Card() {
        this.cardBudget = 0;
        this.cardName = "default";
        this.cardScene = -1;
    }

    public Card(int cardBudget, int cardSceneId, String card, Role[] roles) {
        this.cardBudget = cardBudget;
        this.cardScene = cardScene;
        this.cardName = card;
        this.cardRoles = roles;
    }

    public Role[] getRoles() {
        return this.cardRoles;
    }

    public int getBudget() {
        return this.cardBudget;
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

    public void setCardRoles(Role[] roles) {
        this.cardRoles = roles;
    }

    public void setCardName(String name) {
        this.cardName = name;
    }

    public void setCardBudget(int budget) {
        this.cardBudget = budget;
    }
}