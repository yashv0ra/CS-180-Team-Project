import java.util.ArrayList;

public class User implements UserInterface{
    private String name;
    private int password;
    private String email;
    private String major;

    public User (String name, int password, String email, String major) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.major = major;
    }
    public User () {
        name = "";
        password = 0;
        email = "";
        major = "";
    }

    public String getName() {
        return name;
    }
    public int getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getMajor() {
        return major;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(int password) {
        this.password = password;
    }
    public void setMajor(String major) {
        this.major = major;
    }
}