//Authors: Benjamin Bruland, Lukas McIntosh
package model;

public class Role {

    private int roleID;
    private int roleLevel;
    private String roleName;
    private boolean roleAvailable = true;
    private boolean isOnCardRole;
    private String line;
    private GuiData rolePosition;


    // This BoardManager object is responsible for 
    // controling the state of all game objects.
    // The board manager is created in Deadwood.java
    private static BoardManager boardManager;

    public Role() {

    }

    public Role(int id, int level, String name, boolean isOnCard, GuiData data) {
        this.roleID = id;
        this.roleLevel = level;
        this.roleName = name;
        this.isOnCardRole = isOnCard;
        this.rolePosition = data;
    }

    public void printRole() {

        String offCard = this.isOnCardRole ? "on card" : "off card";
        System.out.printf("Role: %-30sRole Type: %-15sLevel: %-1d\n", this.roleName, offCard, this.roleLevel);
        System.out.println("Role gui data: ");
        
        
        

    }

    public boolean getRoleAvailable() {
        return this.roleAvailable;
    }
    
    public int getRoleID() {
        return this.roleID;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getRoleLine() {
        return this.line;
    }

    public int getRoleLevel() {
        return this.roleLevel;
    }

    public boolean getIsOnCardRole() {
        return this.isOnCardRole;
    }


    public GuiData getGuiData() {
        return this.rolePosition;
    }
    public String getLine() {
        return this.line;
    }
    
    public void setLine(String roleLine) {
        this.line = roleLine;
    }

    public void setGuiData(GuiData data) {
        this.rolePosition = data;
    }

    public void setRoleAvailable(boolean isAvailable) {
        this.roleAvailable = isAvailable;
    }
    
    public void setBoardManager(BoardManager mngr) {
		this.boardManager = mngr;
	}

	public BoardManager getBoardManager() {
		return this.boardManager;
	}

}
