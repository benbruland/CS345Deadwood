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
        System.out.println("Role level = " + this.roleLevel);
        System.out.println("Role ID = " + this.roleID);
    }
    public boolean getRoleAvailable() {
        return this.roleAvailable;
    }


    public void setRoleAvailable(boolean isAvailable) {
        this.roleAvailable = isAvailable;
    }

}
