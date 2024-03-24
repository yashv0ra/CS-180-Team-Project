import java.util.ArrayList;

public class Profile extends User {
    public ArrayList<User> friendsList;
    public ArrayList<User> blockedList;
    public ArrayList<Chat> chatList;
    boolean restrictMessage; //if true, allows to send messages to all users
                            // if false, restricts to only friends

    public Profile(String name, String password, String email, String major, ArrayList<User> friendsList,
                   ArrayList<User> blockedList, ArrayList<Chat> chatList, boolean restrictMessage)
            throws InvalidInputException {
        super(name, password, email, major);
        ArrayList<User> temp = friendsList;
        temp.retainAll(blockedList); //creates array list with common elements without altering arrays
        if (temp.equals(new ArrayList<User>())) {
            this.friendsList = friendsList;
            this.blockedList = blockedList;
            this.chatList = chatList;
            this.restrictMessage = restrictMessage;
        } else {
            throw new InvalidInputException("Invalid Input!");
        }
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

    public void checkChats() {
        //checks if chats are shared between this user and other users
        for (Chat c : chatList) {
            if (!((super.equals(c.getUserA()) || super.equals(c.getUserB()))
            && !c.getUserA().equals(c.getUserB()))) {
                chatList.remove(c);
            }
        }
    }

    public void changeRestriction() {
        restrictMessage = !restrictMessage;
    }

}
