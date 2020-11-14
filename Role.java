public class Role {

    private int roleID;
    private int roleLevel;
    private String roleName;
    private boolean roleAvailable = true;
    private boolean isOnCardRole;
    private String line;

    public Role() {

    }

    public Role(int id, int level, String name, boolean isOnCard) {
        this.roleID = id;
        this.roleLevel = level;
        this.roleName = name;
        this.isOnCardRole = isOnCard;
    }

    public void printRole() {
        System.out.println("Role name = " + this.roleName);
        System.out.println("\t * Role level = " + this.roleLevel);
        System.out.println("\t * Role ID = " + this.roleID);
        String offCard = this.isOnCardRole ? "yes" : "no";
        System.out.println("\t * Role is on card role: " + offCard);
        System.out.println("\t * Role Available : " + this.roleAvailable);
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

    public void setRoleAvailable(boolean isAvailable) {
        this.roleAvailable = isAvailable;
    }

}
