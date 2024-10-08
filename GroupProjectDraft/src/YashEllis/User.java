import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * An interface for accessing the User class
 * <p>
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
    public ArrayList<String> friendsList = new ArrayList<>();
    public ArrayList<String> blockedList = new ArrayList<>();
    public ArrayList<Chat> chatList = new ArrayList<>();
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
        this.friendsList = new ArrayList<>();
        this.blockedList = new ArrayList<>();
        this.chatList = new ArrayList<>();
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
                this.friendsList = new ArrayList<>();
            } else {
                String[] friends1 = friends.split(",");
                friendsList = new ArrayList<>();
                this.friendsList.addAll(Arrays.asList(friends1));
            }
            String blocks = bfr.readLine().substring(8);
            if (blocks.equals("Empty")) {
                this.blockedList = new ArrayList<>();
            } else {
                String[] blocks1 = blocks.split(",");
                this.blockedList.addAll(Arrays.asList(blocks1));
            }
            String[] lastline = bfr.readLine().split(":");
            if (lastline[1].equals("all")) {
                this.restrictMessage = false;
            } else if (lastline[1].equals("friends")) {
                this.restrictMessage = true;
            }

        } catch (IOException e) {
            throw new InvalidInputException("Invalid Input!");

        }
    }

    public void rewriteUserFile() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(email, false));
        bw.write(email + "," + password);
        bw.write("\n" + name);
        bw.write("\n" + major);
        String friendsString = "\nFriends:";
        String blockedString = "\nBlocked:";
        if (friendsList.isEmpty()) {
            friendsString = friendsString.concat("Empty.");
        } else {
            for (String f : friendsList) {
                friendsString = friendsString.concat(f + ",");
            }
        }
        if (blockedList.isEmpty()) {
            blockedString = blockedString.concat("Empty.");
        } else {
            for (String f : blockedList) {
                blockedString = blockedString.concat(f + ",");
            }
        }
        bw.write(friendsString.substring(0, friendsString.length() - 1));
        bw.write(blockedString.substring(0, blockedString.length() - 1));
        if (restrictMessage) {
            bw.write("\nWhoCanMessage:friends");
        } else {
            bw.write("\nWhoCanMessage:all");
        }
        bw.close();
    }
    public boolean writeToFile() {
        //edey@purdue.edu,DoubleDouble
        //Zach
        //Computer Science
        //Friends:dngquan@purdue.edu,yash@purdue.edy,purduePete@purdue.edu...
        //Blocked:Empty
        //WhoCanMessage:friends
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(new File(email)));
            pw.write(email + "," + password + "\n");
            pw.write(name + "\n");
            pw.write(major + "\n");
            pw.write("Friends:");
            if (friendsList == null) {
                pw.write("Empty");
            } else {
                for (int i = 0; i < friendsList.size(); i++) {
                    pw.write(friendsList.get(i));
                    if (i != friendsList.size() - 1) {
                        pw.write(",");
                    }
                }
            }
            pw.write("\n" + "Blocked:");
            if (blockedList == null) {
                pw.write("Empty");
            } else {
                for (int i = 0; i < blockedList.size(); i++) {
                    pw.write(blockedList.get(i));
                    if (i != blockedList.size() - 1) {
                        pw.write(",");
                    }
                }
            }
            if (restrictMessage) {
                pw.write("\n WhoCanMessage:friends");
            } else {
                pw.write("\n WhoCanMessage:all");
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public void addFriend(User a) {
        if (!friendsList.contains(a.getEmail()) && !blockedList.contains(a.getEmail())) {
            friendsList.add(a.getEmail());
        }
        writeToFile();
    }
    public void addFriend(String email) {
        if (!friendsList.contains(email) && !blockedList.contains(email)) {
            friendsList.add(email);
        }
        writeToFile();
    }

    public void removeFriend(User a) {
        friendsList.remove(a.getEmail());
        blockedList.remove(a.getEmail());
        writeToFile();
    }
    public void removeFriend(String email) {
        friendsList.remove(email);
        blockedList.remove(email);
        writeToFile();
    }

    public void blockUser(User a) {
        if (!blockedList.contains(a.getEmail())) {
            blockedList.add(a.getEmail());
        }
        friendsList.remove(a.getEmail());
        writeToFile();
    }
    public void blockUser(String email) {
        if (!blockedList.contains(email)) {
            blockedList.add(email);
        }
        friendsList.remove(email);
        writeToFile();
    }

    public void unblockUser(User a) {
        blockedList.remove(a.getEmail());
        writeToFile();
    }
    public void unblockUser(String email) {
        blockedList.remove(email);
        writeToFile();
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
        writeToFile();
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
        writeToFile();
    }

    public void setBlockedList(ArrayList<String> blockedList) {
        this.blockedList = blockedList;
        writeToFile();
    }

    public void setChatList(ArrayList<Chat> chatList) {
        this.chatList = chatList;
        writeToFile();
    }

    public void setRestrictMessage(boolean restrictMessage) {
        this.restrictMessage = restrictMessage;
        writeToFile();
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
        writeToFile();
    }

    public void setPassword(String password) {
        this.password = password;
        writeToFile();
    }

    public void setMajor(String major) {
        this.major = major;
        writeToFile();
    }
    public boolean compareTo(User user) {
        return this.email.equals(user.email);
    }
}
