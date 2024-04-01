import java.io.*;
import java.util.ArrayList;

public class Chat {
    private ArrayList<String> messages;
    private ArrayList<User> whoSentTheMessage;
    private User user1;
    private User user2;
    private String fileNameForTheTwoUser;
    private PrintWriter pw1;

    public Chat(User user1, User user2) {
        try {
            this.user1 = user1;
            this.user2 = user2;
            fileNameForTheTwoUser = user1.getEmail() + "_with_" + user2.getEmail();
            PrintWriter pw = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser)));
            pw1 = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser), true));
            pw1.println("Chat between " + user1.getEmail() + " & " + user2.getEmail());
            messages = new ArrayList<>();
            whoSentTheMessage = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addAMessage(String message, User whichUser) {
        try {
            if (user1.blocked(user2) || user2.blocked(user1)) {
                pw1.println("<Blocked Message>");
                pw1.flush();
            } else if (!user1.canMessage(user2) || !user2.canMessage(user1)) {
                pw1.println("<Please add this user as a friend to message>");
                pw1.flush();
            } else {
                pw1.println(whichUser.getEmail() + ": " + message);
                pw1.flush();
                messages.add(message);
                whoSentTheMessage.add(whichUser);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public void deleteMessage(String message) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser)));
            pw1 = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser), true));
            int cnt = 0;
            for (int i = 0; i < messages.size(); i++) {
                if (!messages.get(i).equals(message)) {
                    pw1.println(whoSentTheMessage.get(i).getEmail() + ": " + messages.get(i));
                    pw1.flush();
                } else {
                    pw1.println("<Deleted Message>");
                    pw1.flush();
                }
                cnt++;
            }
            messages.remove(message);
            whoSentTheMessage.remove(whoSentTheMessage.get(cnt - 1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteMessage(int input) {
        try {
            // this method records how many messages from the most recent message sent you would like to delete
            // For example, an input of 1 would delete the most recent message sent
            PrintWriter pw = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser)));
            pw1 = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser), true));
            int cnt = 0;
            int index = messages.size() - input;
            for (int i = 0; i < messages.size(); i++) {
                if (i != index) {
                    pw1.println(whoSentTheMessage.get(i).getEmail() + ": " + messages.get(i));
                    pw1.flush();
                } else {
                    pw1.println("<Deleted Message>");
                    pw1.flush();
                }
                cnt++;
            }
            messages.remove(messages.get(input));
            whoSentTheMessage.remove(whoSentTheMessage.get(cnt - 1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


//    public static void main(String[] args) throws InvalidInputException {
//        User u1 = new User("personA", "1234", "personA@purdue.edu", "eco",null,null,null,true);
//        User u2 = new User("personB", "1234", "personB@purdue.edu", "eco",null,null,null,true);
//
//        Chat c = new Chat(u1, u2);
//        c.addAMessage("hello", u1);
//        c.addAMessage("hi", u2);
//
//    }
    public static void main(String[] args) throws InvalidInputException {
        User u1 = new User("personA", "1234", "personA@purdue.edu", "IE",
                new ArrayList<User>(),null,null,true);
        User u2 = new User("personB", "1234", "personB@purdue.edu", "ECE",
                new ArrayList<User>() {{add(u1);}},null,null,true);
        User u3 = new User("personC", "1234", "personC@purdue.edu", "CS",
                new ArrayList<User>() {{add(u1);add(u2);}},null,null,true);
        User u4 = new User("personD", "2222", "personD@purdue.edu", "ME",
                null,null,null,true);
        u1.addFriend(u2);
        Chat c = new Chat(u1, u2);
        Chat d = new Chat(u2, u3);
        u2.blockUser(u3);
        d.addAMessage("No",u2);
        u2.unblockUser(u3);
        c.addAMessage("Yes", u2);
        u1.removeFriend(u2);
        u1.addFriend(u2);
        c.addAMessage("this is sent by person a", u1);
        c.addAMessage("this is sent by person b",u2);
        c.addAMessage("delet this",u1);
        c.deleteMessage(1);


        Database db = new Database(new ArrayList<User>() {{add(u1);add(u2);add(u3);add(u4);}});
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