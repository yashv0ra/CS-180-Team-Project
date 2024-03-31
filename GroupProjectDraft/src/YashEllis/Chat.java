import java.io.*;
import java.util.ArrayList;

public class Chat {
    private ArrayList<String> messages;
    private ArrayList<User> whoSentTheMessage;
    private User user1;
    private User user2;
    private String fileNameForTheTwoUser;

    public Chat(User user1, User user2) {
        messages = new ArrayList<>();
        whoSentTheMessage = new ArrayList<>();
        this.user1 = user1;
        this.user2 = user2;
        fileNameForTheTwoUser = user1.getEmail() + "_with_" + user2.getEmail();

        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser)))){
            pw.close();

        } catch (IOException e) {

        }
    }

    public boolean addAMessage(String message, User whichUser) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser)))){
            pw.print(whichUser.getEmail() + ": ");
            pw.println(message);

        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public void deleteMessage(String message) {
        //currently doesn't account for messages that are the same
        int cnt = 0;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).equals(message)) {
                break;
            }
            cnt++;
        }
        messages.remove(message);
        whoSentTheMessage.get(cnt);
    }

    public static void main(String[] args) throws InvalidInputException {
        User u1 = new User("personA", "1234", "personA@purdue.edu", "eco");
        User u2 = new User("personB", "1234", "personB@purdue.edu", "eco");

        Chat c = new Chat(u1, u2);
        c.addAMessage("hello", u1);

    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public ArrayList<User> getWhoSentTheMessage() {
        return whoSentTheMessage;
    }

    public String getFileNameForTheTwoUser() {
        return fileNameForTheTwoUser;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public void setWhoSentTheMessage(ArrayList<User> whoSentTheMessage) {
        this.whoSentTheMessage = whoSentTheMessage;
    }

    public void setFileNameForTheTwoUser(String fileNameForTheTwoUser) {
        this.fileNameForTheTwoUser = fileNameForTheTwoUser;
    }
}
