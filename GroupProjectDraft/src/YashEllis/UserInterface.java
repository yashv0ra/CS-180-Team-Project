public interface UserInterface {
    public String getName();
    public String getPassword();
    public String getEmail();
    public String getMajor();
    public void setName(String name);
    public void setPassword(int password);
    public void setMajor(String major);
    public boolean compareTo(User user);
    //only check email
    public boolean modifyUser(User user);
    public String toString();

}
