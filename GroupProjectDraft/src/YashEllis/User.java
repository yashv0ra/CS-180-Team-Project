public class User {
    private String name;
    private String password;
    private String email;
    private String major;

    public User(String name, String password, String email, String major) throws InvalidInputException {
        //Checks for no empty contents
        if (name.isEmpty() || password.isEmpty() || email.isEmpty() || major.isEmpty()) {
            throw new InvalidInputException("Invalid Input!");
        }
        //check for valid email
        if(email.contains("@") && !email.contains(" ")) {
            String[] emailElements = email.split("@");
            if (emailElements.length == 2 && !emailElements[0].isEmpty() && emailElements[1].equals("purdue.edu")) {
                this.email = email;
            } else {
                throw new InvalidInputException("Invalid Input!");
            }
        } else {
            throw new InvalidInputException("Invalid Input!");
        }
        this.name = name;
        this.password = password;
        this.major = major;
    }

    public User() {
        name = "";
        password = "";
        email = "";
        major = "";
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMajor(String major) {
        this.major = major;
    }
    public boolean compareTo(User user) {
        return name.equals(user.name) && password.equals(user.password) &&
                email.equals(user.email) && major.equals(user.major);

    }
}
