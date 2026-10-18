public abstract class User {
    private final String username, fullName;
    public User(String username, String fullName) { this.username=username; this.fullName=fullName; }
    public String getUsername(){ return username; }
    public String getFullName(){ return fullName; }
    public abstract String getRole();
}
