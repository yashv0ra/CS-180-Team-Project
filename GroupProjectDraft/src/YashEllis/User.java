import java.sql.Array;
import java.util.ArrayList;

public class User {
    private String name;
    private String password;
    private String email;
    private String major;
    public ArrayList<User> friendsList;
    public ArrayList<User> blockedList;
    public ArrayList<Chat> chatList;
    boolean restrictMessage;
    public User(String name, String password, String email, String major, ArrayList<User> friendsList,
                   ArrayList<User> blockedList, ArrayList<Chat> chatList, boolean restrictMessage) throws InvalidInputException {
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
        this.friendsList = new ArrayList<>(1);
        this.blockedList = new ArrayList<>(1);
        this.chatList = new ArrayList<>(1);
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
    public void addFriend(User user) {
        if (!friendsList.contains(user) && !blockedList.contains(user)) {
            friendsList.add(user);
        }
    }

    public void removeFriend(User user) {
        friendsList.remove(user);
        blockedList.remove(user);

    }

    public void blockUser(User user) {
        if (!blockedList.contains(user)) {
            blockedList.add(user);
        }
        friendsList.remove(user);
    }

    public void unblockUser(User user) {
        blockedList.remove(user);
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
    public ArrayList<User> getFriendsList() {
        return friendsList;
    }

    public ArrayList<User> getBlockedList() {
        return blockedList;
    }

    public ArrayList<Chat> getChatList() {
        return chatList;
    }

    public boolean isRestrictMessage() {
        return restrictMessage;
    }

    public void setFriendsList(ArrayList<User> friendsList) {
        this.friendsList = friendsList;
    }

    public void setBlockedList(ArrayList<User> blockedList) {
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
        return this.name.equals(user.name) && this.email.equals(user.email);

    }
}
