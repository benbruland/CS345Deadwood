public class Role {

    private int roleID;
    private String roleName;
    private boolean roleAvailable;
    private boolean isOnCardRole;

    //TODO Implement Role Constructor
    public Role() {

    }

    public boolean getRoleAvailable() {
        return this.roleAvailable;
    }

    //TODO Implement SetRoleAvailable
    public void setRoleAvailable(boolean isAvailable) {
        this.roleAvailable = isAvailable;
    }

}
