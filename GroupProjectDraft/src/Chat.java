import java.util.ArrayList;

public class Chat {
    private User userA;
    private User userB;
    private ArrayList<String> messageList;

    public Chat() {
        userA = new User();
        userB = new User();
        messageList = null;
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }

    public ArrayList<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<String> messageList) {
        this.messageList = messageList;
    }

    public void addMessage(String message, User user) {
        if (message != null && (user.equals(userA) || user.equals(userB))) {
            messageList.add(user.getEmail() + ":" + message);
        }
    }

    public void deleteMessage(String message) {
        //currently doesn't account for messages that are the same
        messageList.remove(message);
    }
}
