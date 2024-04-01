import java.util.ArrayList;
public interface DatabaseInterface {
    public boolean login(String email, String password);
    public ArrayList<User> usersMajorSearch (String major);
    public ArrayList<User> usersNameSearch (String name);
    public User usersEmailSearch (String email);
    public boolean modifyUser(User aimUser, User changed);
    public boolean modifyUser(ArrayList<User> usersNeedToBeChanged, ArrayList<User> changed)
                throws ImpossibleChangeException;
    public String toString(int index);
}
