public class Role {

    private int roleID;
    private int roleLevel;
    private String roleName;
    private boolean roleAvailable = true;
    private boolean isOnCardRole;
    private String line;
    private GuiData rolePosition;

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
        System.out.println("Role name = " + this.roleName);
        System.out.println("\t * Role level = " + this.roleLevel);
        System.out.println("\t * Role Line = " + this.line);
        System.out.println("\t * Role ID = " + this.roleID);
        String offCard = this.isOnCardRole ? "yes" : "no";
        System.out.println("\t * Role is on card role: " + offCard);
        System.out.println("\t * Role Available : " + this.roleAvailable);
        
        System.out.println("GUI Data: ");
        this.rolePosition.printGuiData();

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

}
