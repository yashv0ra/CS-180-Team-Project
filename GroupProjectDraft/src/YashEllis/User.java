import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
/**
 * An interface for accessing the User class
 *
 * Purdue University -- CS18000 -- Spring 2024 -- Team Project Phase 1
 *
 * @author Yash Vora, Ellis Sioukas, Zack Wang, Jad Karaki
 * @version April 1, 2024
 */
public class User {
    private String name;
    private String password;
    private String email;
    private String major;
    public ArrayList<String> friendsList;
    public ArrayList<String> blockedList;
    public ArrayList<Chat> chatList;
    boolean restrictMessage;
    public User(String name, String password, String email, String major, ArrayList<String> friendsList,
                   ArrayList<String> blockedList, ArrayList<Chat> chatList, boolean restrictMessage)
            throws InvalidInputException {
        //Checks for no empty contents
        if (name.isEmpty() || password.isEmpty() || email.isEmpty() || major.isEmpty()) {
            throw new InvalidInputException("Invalid Input!");
        }
        //check for valid email
        if (email.contains("@") && !email.contains(" ")) {
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
        if (friendsList == null) {
            this.friendsList = new ArrayList<>(1);
        } else {
            this.friendsList = friendsList;
        }
        if (blockedList == null) {
            this.blockedList = new ArrayList<>(1);
        } else {
            this.blockedList = blockedList;
        }
        if (chatList == null) {
            this.chatList = new ArrayList<>(1);
        } else {
            this.chatList = chatList;
        }
        this.restrictMessage = restrictMessage;
    }

    public User() {
        name = "";
        password = "";
        email = "";
        major = "";
        this.restrictMessage = false;
        this.friendsList = null;
        this.blockedList = null;
        this.chatList = null;
    }
    public User(String email) throws InvalidInputException {
        //edey@purdue.edu,DoubleDouble
        //Zach
        //Computer Science
        //Friends:dngquan@purdue.edu,yash@purdue.edy,purduePete@purdue.edu...
        //Blocked:Empty
        //WhoCanMessage:friends
        this.email = email;
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(email));
            String[] logindetails = bfr.readLine().split(","); // line1[0] is email and line1[1] = password
            this.password = logindetails[1];
            this.name = bfr.readLine();
            this.major = bfr.readLine();
            String friends = bfr.readLine().substring(8);
            if (friends.equals("Empty")) {
                this.friendsList = null;
            } else {
                String[] friends1 = friends.split(",");
                this.friendsList.addAll(Arrays.asList(friends1));
            }
            String blocks = bfr.readLine().substring(8);
            if (blocks.equals("Empty")) {
                this.blockedList = null;
            } else {
                String[] blocks1 = blocks.split(",");
                this.friendsList.addAll(Arrays.asList(blocks1));
            }
            String[] lastline = bfr.readLine().split(":");
            if (lastline[1].equals("all")) {
                this.restrictMessage = false;
            } else if (lastline[1].equals("friends")) {
                this.restrictMessage = true;
            }

        } catch(IOException e) {
            throw new InvalidInputException("Invalid Input!");

        }
    }
    public void addFriend(User a) {
        if (!friendsList.contains(a.getEmail()) && !blockedList.contains(a.getEmail())) {
            friendsList.add(a.getEmail());
        }
    }

    public void removeFriend(User a) {
        friendsList.remove(a.getEmail());
        blockedList.remove(a.getEmail());

    }

    public void blockUser(User a) {
        if (!blockedList.contains(a.getEmail())) {
            blockedList.add(a.getEmail());
        }
        friendsList.remove(a.getEmail());
    }

    public void unblockUser(User a) {
        blockedList.remove(a.getEmail());
    }

//    public void checkChats() {
//        //checks if chats are shared between this user and other users
//        for (Chat c : chatList) {
//            if (!((super.equals(c.getUser1()) || super.equals(c.getUser1()))
//                    && !c.getUser2().equals(c.getUser2()))) {
//                chatList.remove(c);
//            }
//        }
//    }

    public void changeRestriction() {
        restrictMessage = !restrictMessage;
    }
    public boolean canMessage(User a) {
        boolean friendCheck1 = false;
        boolean friendCheck2 = false;
        for (int i = 0; i < this.friendsList.size(); i++) {
            if (this.friendsList.get(i).equals(a.getEmail())) {
                friendCheck1 = true;
                break;
            }
        }
        for (int i = 0; i < a.friendsList.size(); i++) {
            if (a.friendsList.get(i).equals(a.getEmail())) {
                friendCheck2 = true;
                break;
            }
        }
        if (friendCheck2 && friendCheck1) {
            return true;
        }
        // if the user restricts messages from people they aren't friends with
        // and the other user is not your friend, then restrict
        return !a.isRestrictMessage() || friendCheck2;
    }
    public boolean blocked(String email) {
        for (int i = 0; i < this.blockedList.size(); i++) {
            if (this.blockedList.get(i).equals(email)) {
                return true;
            }
        }
        return false;
    }
    public ArrayList<String> getFriendsList() {
        return friendsList;
    }

    public ArrayList<String> getBlockedList() {
        return blockedList;
    }

    public ArrayList<Chat> getChatList() {
        return chatList;
    }

    public boolean isRestrictMessage() {
        return restrictMessage;
    }

    public void setFriendsList(ArrayList<String> friendsList) {
        this.friendsList = friendsList;
    }

    public void setBlockedList(ArrayList<String> blockedList) {
        this.blockedList = blockedList;
    }

    public void setChatList(ArrayList<Chat> chatList) {
        this.chatList = chatList;
    }

    public void setRestrictMessage(boolean restrictMessage) {
        this.restrictMessage = restrictMessage;
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
        return this.email.equals(user.email);
    }
}
