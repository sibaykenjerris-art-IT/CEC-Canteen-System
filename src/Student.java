public class Student extends User {
    public Student(String username, String fullName){ super(username, fullName); }
    @Override public String getRole(){ return "Student / Customer"; }
}
