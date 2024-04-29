import javax.swing.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Menu implements Runnable {
    private BufferedReader reader;
    private PrintWriter writer;

    public void run() {
        String confirmation = null;
        try {

            confirmation = reader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (confirmation.equals("Connection Established")) {
            System.out.println("New Connection formed");
        }
        ArrayList<User> userlist = new ArrayList<>();
        Database data = new Database(userlist);
        User user = new User();
//        Scanner scanner = new Scanner(System.in);
        String emailInput = "";
        String passwordInput = "";
        ArrayList<String> email = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("LoginDetails.txt"));
            ArrayList<String> logInList = new ArrayList<>();
            String line = bfr.readLine();
            while (line != null) {
                logInList.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            //Login data format:
            //jkaraki@purdue.edu,1234
            //dnquan@purdue.edu,Tree
            //.
            //.
            //.
            for (String s : logInList) {
                String[] userLogIn = s.split(",");
                String e = userLogIn[0];
                String p = userLogIn[1];
                if (e.contains("@") && !e.contains(" ") && !p.isEmpty()) {
                    String[] emailElements = e.split("@");
                    if (emailElements.length == 2 && !emailElements[0].isEmpty() && emailElements[1].equals("purdue.edu")) {
                        email.add(e);
                        password.add(p);
                    } else {
                        throw new InvalidInputException("Invalid Input!");
                    }
                } else {
                    throw new InvalidInputException("Invalid Input!");
                }
            }
            //[DONE]Insert Welcome page with option to login or sign up and based on user choice, change account value
            welcomeMessage();
            boolean validUser = false; //turns true when user is logged in/ signed up
            do {
                int account = accountInput(); // 1 when user wants to log in, 2 when user wants to sign up, and 3 when user wants to exit
                if (account == 1) {
                    emailInput = loginEmailInput();
                    passwordInput = loginPasswordInput();

                    if (email.contains(emailInput) && password.get(email.indexOf(emailInput)).equals(passwordInput)) {
                        validUser = true;
                    } else {
                        int retry = 0;
                        if (!email.contains(emailInput)) {
                            retry = errorRetryInput("User with this email does not exist");
                        } else if (!password.get(email.indexOf(emailInput)).equals(passwordInput)) {
                            retry = errorRetryInput("Password and email do not match");
                        }
                        // retry is 0 for yes, is 1 for no
                        
                        if (retry == 1) {
                            break;
                        }
                        //[DONE]Provide user options to retry, sign up, or end program and update account accordingly
                    }
                } else if (account == 2) {
                    emailInput = loginEmailInput();
                    passwordInput = signupPasswordInput();
                    String majorInput = majorInput();
                    String nameInput = fullNameInput();
                    String restrictionInput = inputRestrictMessage(); //"all" or "friends"
                    if (!email.contains(emailInput)) {
                        if (emailInput.contains("@") && !emailInput.contains(" ") && !passwordInput.isEmpty()) {
                            String[] emailElements = emailInput.split("@");
                            if (emailElements.length == 2 && !emailElements[0].isEmpty()
                                    && emailElements[1].equals("purdue.edu")) {
                                email.add(emailInput);
                                password.add(passwordInput);
                                rewriteLoginDetails(email, password);
                            } else {
                                throw new InvalidInputException("Invalid Input!");
                            }
                        } else {
                            throw new InvalidInputException("Invalid Input!");
                        }
                        validUser = true;
                        createUserFile(emailInput, passwordInput, majorInput, nameInput, restrictionInput);
                    } else {
                        int retry = errorRetryInput("This email is already tied to an account. " +
                                "Please use a different email.");
                        
                        if (retry == 1) {
                            break;
                        }
                        //Provide user options to retry, log in, or end program and update account accordingly
                    }
                }
            } while (!validUser);
        } catch (IOException e) {
            throw new RuntimeException();
        } catch (InvalidInputException r) {

        }
        //User data format (each user has their own file with their respective data)

        //The file names would be jkaraki@purdue.edu and would be formatted as:
        //jkaraki@purdue.edu,1234
        //Jad
        //Industrial Engineering
        //Friends:dngquan@purdue.edu,yash@purdue.edu,purduePete@purdue.edu...
        //Blocked:cs180@purdue.edu,csdep@purdue.edu...
        //WhoCanMessage:all

        //here is another example:
        //edey@purdue.edu,DoubleDouble
        //Zach
        //Computer Science
        //Friends:dngquan@purdue.edu,yash@purdue.edu,purduePete@purdue.edu...
        //Blocked:Empty
        //WhoCanMessage:friends
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(new File(emailInput)));
            ArrayList<String> userData = new ArrayList<>();
            String l = bfr.readLine();
            while (l != null) {
                userData.add(l);
                l = bfr.readLine();
            }
            ArrayList<String> friends = new ArrayList<>();
            ArrayList<String> blocked = new ArrayList<>();
            ArrayList<Chat> chats = new ArrayList<>();
            String[] friendString = userData.get(3).substring(8).split(",");
            String[] blockedString = userData.get(4).substring(8).split(",");
            if (!friendString[0].equals("Empty")) {
                for (int i = 0; i < friendString.length; i++) {
                    friends.add((friendString[i]));
                    chats.add(new Chat(emailInput.concat("_with_").concat(friendString[i])));
                    //create constructor for users based on info in file
                    //do the same for chats based on chat files
                    //this for loop will initialize a list of users and corresponding chats
                }
            }
            if (!blockedString[0].equals("Empty")) {
                for (int i = 0; i < blockedString.length; i++) {
                    blocked.add((blockedString[i]));
                    chats.add(new Chat(emailInput.concat("_with_").concat(blockedString[i])));
                    //create constructor for users based on info in file
                    //do the same for chats based on chat files
                    //this for loop will initialize a list of users and corresponding chats
                }
            }
//            for(String u: userData) {
//                System.out.println(u);
//            }
//            System.out.println(passwordInput);
//            System.out.println(emailInput);
//            System.out.println(friends);
//            System.out.println(blocked);
//            System.out.println(chats);
            user = new User(userData.get(1), passwordInput, emailInput, userData.get(2), friends,
                    blocked, chats, userData.get(5).split(":")[1].equals("friends"));
            //Write new data to file either now or at end of program
            //Show Options to exit(4), modify user information(3), search for users(2), or chat(1)
            String choice = "";


            writer.write("Call UserGUI");
            writer.println();
            writer.flush();
            writer.write(user.getEmail());
            writer.println();
            writer.flush();
            do {
                choice = reader.readLine();
                if (choice.contains("[sending message command]")) {
                    Chat chat = new Chat(user.getEmail() + "_with_" + choice.split(",,,")[1]);
                    if (chat.addAMessage(choice.split(",,,")[3], user)) {
                        System.out.println("FLALSO#$UBFR");
                    }
                    for (String s : chat.getMessages()) {
                        writer.write("CONTINUE");
                        writer.println();
                        writer.flush();
                        writer.write(s);
                        writer.println();
                        writer.flush();
                    }
                    writer.write("ENDING CONVERSATION");
                    writer.println();
                    writer.flush();
                } else if (choice.contains("[remove friend command]")) {
                    user.removeFriend(new User(choice.split("command]")[1]));
                } else if (choice.contains("[unblock user command]")) {
                    user.unblockUser(new User(choice.split("command]")[1]));
                } else if (choice.contains("[add friend command]")) {
                    user.addFriend(new User(choice.split("command]")[1]));
                } else if (choice.contains("[block user command]")) {
                    user.blockUser(new User(choice.split("command]")[1]));
                } else if (choice.contains("[delete last message]")) {
                    Chat chat = new Chat(user.getEmail() + "_with_" + choice.split(",,,")[1]);
                    for (int i = 0; i < chat.getMessages().size() - 1; i++) {
                        writer.write("CONTINUE");
                        writer.println();
                        writer.flush();
                        writer.write(chat.getMessages().get(i));
                        writer.println();
                        writer.flush();
                    }
                    chat.deleteRecentMessage();
                    writer.write("ENDING CONVERSATION");
                    writer.println();
                    writer.flush();
                } else if (choice.contains("[show friend detail command]")) {
                    User userViewer = new User(choice.split(",,,")[1]);
                    writer.write("Name: " + userViewer.getName());
                    writer.println();
                    writer.flush();
                    writer.write("Major: " + userViewer.getMajor());
                    writer.println();
                    writer.flush();
                    writer.write("Email: " + userViewer.getEmail());
                    writer.println();
                    writer.flush();
                } else if (choice.contains("[search user command]")) {
                    String output = "";
                    for (String e : email) {
                        if (e.contains(choice.split(",,,")[1])) {
                            output = output.concat("/" + e);
                            System.out.println(e);
                        }
                    }
                    writer.write(output.substring(1));
                    writer.println();
                    writer.flush();
                }
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        farewellMessage();
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void rewriteLoginDetails(ArrayList<String> emails, ArrayList<String> passwords) throws
            InvalidInputException {
        String output = "";
        for (int i = 0; i < emails.size(); i++) {
            output = output.concat("\n" + emails.get(i) + "," + passwords.get(i));
        }
        if (!output.isEmpty()) {
            output = output.substring(1);
            try (PrintWriter out = new PrintWriter("LoginDetails.txt")) {
                out.print(output);
            } catch (Exception e) {
                throw new InvalidInputException("Problem updating LoginDetails.txt");
            }
        }
    }

    public void createUserFile(String email, String password, String major, String name, String restriction)
            throws InvalidInputException {
        //jkaraki@purdue.edu,1234
        //Jad
        //Industrial Engineering
        //Friends:dngquan@purdue.edu,yash@purdue.edu,purduePete@purdue.edu...
        //Blocked:cs180@purdue.edu,csdep@purdue.edu...
        //WhoCanMessage:all
        String output = "";
        output = output.concat("\n" + email + "," + password);
        output = output.concat("\n" + name);
        output = output.concat("\n" + major);
        output = output.concat("\nFriends:Empty");
        output = output.concat("\nBlocked:Empty");
        output = output.concat("\nWhoCanMessage:" + restriction);
        if (!output.isEmpty()) {
            output = output.substring(1);
            try (PrintWriter out = new PrintWriter(email)) {
                out.print(output);
            } catch (Exception e) {
                throw new InvalidInputException("Problem creating user file");
            }
        }
    }

    public static void welcomeMessage() {
        JOptionPane.showMessageDialog(null, "Welcome to Messaging Software!",
                "Messaging Software", JOptionPane.INFORMATION_MESSAGE);
    }

    public static int accountInput() {
        int inp;
        inp = JOptionPane.showConfirmDialog(null, "Do you have an existing account?",
                "Messaging Software", JOptionPane.YES_NO_CANCEL_OPTION);
        return inp + 1;
    }

    public static String loginEmailInput() {
        String inp;
        do {
            inp = JOptionPane.showInputDialog(null, "Please enter your Purdue email.",
                    "Messaging Software", JOptionPane.QUESTION_MESSAGE);
            if ((inp.isEmpty()) || ((!inp.contains("@purdue.edu")))) {
                JOptionPane.showMessageDialog(null, "Please enter a valid Purdue email with @purdue.edu.", "Messaging Software",
                        JOptionPane.ERROR_MESSAGE);
            } //end if
        } while ((inp == null) || ((!inp.contains("@purdue.edu"))));

        return inp;
    }

    public static String loginPasswordInput() {
        String inp;
        inp = JOptionPane.showInputDialog(null, "Please enter your password.",
                "Messaging Software", JOptionPane.QUESTION_MESSAGE);
        return inp;
    }

    public static String majorInput() {
        String inp;
        inp = JOptionPane.showInputDialog(null, "Please enter your major.",
                "Messaging Software", JOptionPane.QUESTION_MESSAGE);
        return inp;
    }

    public static String fullNameInput() {
        String inp;
        inp = JOptionPane.showInputDialog(null, "Please enter your full name.",
                "Messaging Software", JOptionPane.QUESTION_MESSAGE);
        return inp;
    }

    public static String signupPasswordInput() {
        String inp;
        boolean valid = false;
        do {
            inp = JOptionPane.showInputDialog(null,
                    "Please make a password with between 5 and 20 characters.",
                    "Messaging Software", JOptionPane.QUESTION_MESSAGE);
            if ((inp == null) || ((inp.length() < 5)) || (inp.length() > 20)) {
                JOptionPane.showMessageDialog(null,
                        "Please make sure to enter a password with " +
                                "between 5 and 20 characters", "Messaging Software", JOptionPane.ERROR_MESSAGE);
            } else {
                valid = true;
            }

        } while (!valid);

        return inp;
    }

    public static int errorRetryInput(String error) {
        int inp;
        JOptionPane.showMessageDialog(null, error, "Messaging Software",
                JOptionPane.ERROR_MESSAGE);
        inp = JOptionPane.showConfirmDialog(null, "Would you like to retry? " +
                        "\n (If you select No, the program will end)",
                "Messaging Software", JOptionPane.YES_NO_OPTION);
        return inp;
    }

    public static String inputRestrictMessage() {
        int inp;
        inp = JOptionPane.showConfirmDialog(null, "Would you like to receive " +
                        "messages from everyone? \n (If you select No, only your friends will be able to message you)",
                "Messaging Software", JOptionPane.YES_NO_OPTION);
        if (inp == 0) {
            return "all";
        } else {
            return "friends";
        }
    }

    public static void farewellMessage() {
        JOptionPane.showMessageDialog(null, "Farewell!",
                "Messaging Software", JOptionPane.INFORMATION_MESSAGE);
    }
}
