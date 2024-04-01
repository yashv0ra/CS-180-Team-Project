import java.util.ArrayList;
public interface ChatInterface {
    public boolean addAMessage(String message, User whichUser);
    public void deleteMessage(String message);
    public boolean isRestricted(User A, User B);
    public User getUser1();
    public User getUser2();
    public ArrayList<String> getMessages();
    public ArrayList<User> getWhoSentTheMessage();
    public String getFileNameForTheTwoUser();
    public void setUser1(User user1);
    public void setUser2(User user2);
    public void setMessages(ArrayList<String> messages);
    public void setWhoSentTheMessage(ArrayList<User> whoSentTheMessage);
    public void setFileNameForTheTwoUser(String fileNameForTheTwoUser);
}
