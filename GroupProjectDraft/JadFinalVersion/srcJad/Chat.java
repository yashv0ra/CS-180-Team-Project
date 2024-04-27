import java.io.*;
import java.util.ArrayList;

/**
 * The class for accessing and editing the chat file between 2 users.
 * <p>
 * Purdue University -- CS18000 -- Spring 2024 -- Team Project Phase 1
 *
 * @author Yash Vora, Ellis Sioukas, Zack Wang, Jad Karaki
 * @version April 1, 2024
 */
public class Chat {
    private ArrayList<String> messages;
    private ArrayList<User> whoSentTheMessage;
    private User user1;
    private User user2;
    private String fileNameForTheTwoUser;
    private PrintWriter pw1;

    public Chat(User user1, User user2) throws InvalidInputException {
        try {
            this.user1 = user1;
            this.user2 = user2;
            fileNameForTheTwoUser = user1.getEmail() + "_with_" + user2.getEmail();
            // the below line clears the file, which is a problem right now
            PrintWriter pw = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser)));
            pw1 = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser), true));
            pw1.println("Chat between " + user1.getEmail() + " & " + user2.getEmail());
            messages = new ArrayList<>();
            whoSentTheMessage = new ArrayList<>();
        } catch (Exception e) {
            throw new InvalidInputException("Invalid Input");
        }
    }

    public Chat(String filename) throws IOException, InvalidInputException {
        fileNameForTheTwoUser = filename;
        pw1 = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser), true));
        try {
            user1 = new User(filename.split("_with_")[0]);
        } catch (Exception e) {
            user1 = new User();
            user1.setEmail(filename.split("_with_")[0]);
        }
        try {
            user2 = new User(filename.split("_with_")[1]);
        } catch (Exception e) {
            user2 = new User();
            user2.setEmail(filename.split("_with_")[1]);
        }
        ArrayList<String> list = new ArrayList<>();
        File f = new File(filename);
        BufferedReader bfr = null;
        if (!f.exists()) {
            try {
                fileNameForTheTwoUser = user1.getEmail() + "_with_" + user2.getEmail();
                // the below line clears the file, which is a problem right now
                PrintWriter pw = new PrintWriter(new FileWriter((fileNameForTheTwoUser)));
                pw1 = new PrintWriter(new FileWriter(new File(fileNameForTheTwoUser), true));
                pw1.println("Chat between " + user1.getEmail() + " & " + user2.getEmail());
                messages = new ArrayList<>();
                whoSentTheMessage = new ArrayList<>();
            } catch (Exception e) {
                throw new InvalidInputException("Invalid Input");
            }
        } else {
            try {
                FileReader fr = new FileReader(f);
                bfr = new BufferedReader(fr);
                String line = bfr.readLine();
                while (line != null) {
                    list.add(line);
                    line = bfr.readLine();
                }
                bfr.close();
            } catch (Exception e) {
                throw new InvalidInputException("Invalid Input");
            } finally {
                if (bfr != null) {
                    try {
                        bfr.close();
                    } catch (IOException e) {
                        throw new InvalidInputException("Invalid Input");
                    }
                }
            }
            messages = list;
        }
    }

    public synchronized boolean addAMessage(String message, User whichUser) {
        try {
            if (user1.blocked(user2.getEmail()) || user2.blocked(user1.getEmail())) {
                pw1.println("<Blocked Message>");
                pw1.flush();
            } else if (!user1.canMessage(user2) || !user2.canMessage(user1)) {
                pw1.println("<Please add this user as a friend to message>");
                pw1.flush();
            } else {
                pw1.println(whichUser.getEmail() + ": " + message);
                pw1.flush();
                messages.add(whichUser.getEmail() + ": " + message);
                whoSentTheMessage.add(whichUser);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public synchronized void deleteMessage(String message) throws ImpossibleChangeException {
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
            throw new ImpossibleChangeException("Invalid Input");
        }
    }

    public synchronized void deleteMessage(int input) throws ImpossibleChangeException {
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
            throw new ImpossibleChangeException("Invalid Change");
        }
    }

    public void deleteRecentMessage() throws InvalidInputException, FileNotFoundException {
        ArrayList<String> list = new ArrayList<>();
        File f = new File(fileNameForTheTwoUser);
        BufferedReader bfr = null;
        try {
            FileReader fr = new FileReader(f);
            bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            while (line != null) {
                list.add(line);
                line = bfr.readLine();
            }
            bfr.close();
        } catch (Exception e) {
            throw new InvalidInputException("Invalid Input");
        } finally {
            if (bfr != null) {
                try {
                    bfr.close();
                } catch (IOException e) {
                    throw new InvalidInputException("Invalid Input");
                }
            }
        }
        try {
            list.remove(list.size() - 1);
            messages = list;
            PrintWriter writer = new PrintWriter(fileNameForTheTwoUser);
            writer.print("");
            writer.close();
            for(String s: messages) {
                pw1.println(s);
                pw1.flush();
            }
        } catch (IndexOutOfBoundsException e) {

        }

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
