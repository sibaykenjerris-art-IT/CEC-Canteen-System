public class Staff extends User {
    public Staff(String username, String fullName){ super(username, fullName); }
    @Override public String getRole(){ return "Canteen Staff"; }
}
