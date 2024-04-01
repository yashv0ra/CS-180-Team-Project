import java.util.ArrayList;
public interface UserInterface {
    public void addFriend(User user);
    public void removeFriend(User user);
    public void blockUser(User user);
    public void unblockUser(User user);
    public void changeRestriction();
    public boolean isBlocked(User A);

    public String getName();
    public String getPassword();
    public String getEmail();
    public String getMajor();
    public ArrayList<User> getFriendsList();
    public ArrayList<User> getBlockedList();
    public ArrayList<Chat> getChatList();
    public boolean isRestrictMessage();
    public void setName(String name);
    public void setPassword(String password);
    public void setEmail(String email);
    public void setMajor(String major);
    public void setFriendsList(ArrayList<User> friendsList);
    public void setBlockedList(ArrayList<User> blockedList);
    public void setChatList(ArrayList<Chat> chatList);
    public void setRestrictMessage(boolean restrictMessage);
    public boolean compareTo(User user);
    //only check email
    public boolean modifyUser(User user);
    //public String toString();
}
