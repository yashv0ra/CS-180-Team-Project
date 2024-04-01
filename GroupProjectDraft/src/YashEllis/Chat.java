import java.io.*;
import java.util.ArrayList;

public class Chat {
    private ArrayList<String> messages;
    private ArrayList<User> whoSentTheMessage;
    private User user1;
    private User user2;
    private String fileNameForTheTwoUser;

    public Chat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        fileNameForTheTwoUser = user1.getEmail() + "_with_" + user2.getEmail();
        if (isBlocked(user1, user2) || canMessage(user1, user2)) {
            messages = null;
            whoSentTheMessage = null;
        } else {
            messages = new ArrayList<>();
            whoSentTheMessage = new ArrayList<>();
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser)))){
            pw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public boolean isBlocked(User A, User B) {
        for (int i = 0; i < A.blockedList.size(); i++) {
            if (A.blockedList.get(i).compareTo(B)) {
                return true;
            }
        }
        for (int i = 0; i < B.blockedList.size(); i++) {
            if (B.blockedList.get(i).compareTo(A)) {
                return true;
            }
        }
        return false;
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

    public boolean canMessage(User A, User B) {
        boolean friendCheck1 = false;
        boolean friendCheck2 = false;
        for (int i = 0; i < A.friendsList.size(); i++) {
            if (A.friendsList.get(i).compareTo(B)) {
                friendCheck1 = true;
            }
        }
        for (int i = 0; i < B.friendsList.size(); i++) {
            if (B.friendsList.get(i).compareTo(A)) {
                friendCheck2 = true;
            }
        }
        if (friendCheck2 && friendCheck1) {
            return true;
        }
        if (A.isRestrictMessage() && !friendCheck1) {
            // if the user restricts messages from people they aren't friends with
            // and the other user is not your friend, then restrict
            return false;
        } else if (B.isRestrictMessage() && !friendCheck2) {
            // if the user restricts messages from people they aren't friends with
            // and the other user is not your friend, then restrict
            return false;
        } else {
            return true;
        }
    }

    public static void main(String[] args) throws InvalidInputException {
        User u1 = new User("personA", "1234", "personA@purdue.edu", "eco",null,null,null,true);
        User u2 = new User("personB", "1234", "personB@purdue.edu", "eco",null,null,null,true);

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