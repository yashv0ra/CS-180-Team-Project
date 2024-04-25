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
        Scanner scanner = new Scanner(System.in);
        String emailInput = "";
        String passwordInput = "";
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("LoginDetails.txt"));
            ArrayList<String> logInList = new ArrayList<>();
            String line = bfr.readLine();
            while (line != null) {
                logInList.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            ArrayList<String> email = new ArrayList<>();
            ArrayList<String> password = new ArrayList<>();
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
            //Insert Welcome page with option to login or sign up and based on user choice, change account value
            int account = 0; // 1 when user wants to log in, 2 when user wants to sign up, and 3 when user wants to exit
            boolean validUser = false; //turns true when user is logged in/ signed up
            do {
                if (account == 1) {
                    //Let user input both
                    if (email.contains(emailInput) && password.get(email.indexOf(emailInput)).equals(passwordInput)) {
                        validUser = true;
                    } else {
                        if (!email.contains(emailInput)) {
                            throw new InvalidInputException("User with this email does not exist");
                        } else if (password.get(email.indexOf(emailInput)).equals(passwordInput)) {
                            throw new InvalidInputException("Password and email do not match");
                        }
                        //Provide user options to retry, sign up, or end program and update account accordingly
                    }
                } else if (account == 2) {
                    emailInput = "";
                    passwordInput = "";
                    String majorInput = "";
                    String nameInput = "";
                    String restrictionInput = ""; //"all" or "friends"
                    //Let user input all of the above
                    if (!email.contains(emailInput)) {
                        if (emailInput.contains("@") && !emailInput.contains(" ") && !passwordInput.isEmpty()) {
                            String[] emailElements = emailInput.split("@");
                            if (emailElements.length == 2 && !emailElements[0].isEmpty()
                                    && emailElements[1].equals("purdue.edu")) {
                                email.add(emailInput);
                                password.add(passwordInput);
                                rewriteLoginDetails(email, password);
                                //Synchronize the two things above
                            } else {
                                throw new InvalidInputException("Invalid Input!");
                            }
                        } else {
                            throw new InvalidInputException("Invalid Input!");
                        }
                        validUser = true;
                        createUserFile(emailInput, passwordInput, majorInput, nameInput, restrictionInput);
                    } else {
                        if (email.contains(emailInput)) {
                            //throw new InvalidInputException("User with this email already exists");
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

        //The file names would be jkaraki@purdue.eduDataFile and would be formatted as:
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
            BufferedReader bfr = new BufferedReader(new FileReader(emailInput.concat("DataFile")));
            ArrayList<String> userData = new ArrayList<>();
            String l = bfr.readLine();
            while (l != null) {
                userData.add(l);
                l = bfr.readLine();
            }
            ArrayList<String> friends = new ArrayList<>();
            ArrayList<String> blocked = new ArrayList<>();
            ArrayList<Chat> chats = new ArrayList<>();
            String[] friendString = userData.get(3).substring(7).split(",");
            String[] blockedString = userData.get(4).substring(7).split(",");
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
            user = new User(userData.get(1), passwordInput, emailInput, userData.get(2), friends,
                    blocked, chats, userData.get(5).split(":")[2].equals("friends"));
            //Write new data to file either now or at end of program
            //Show Options to exit(4), modify user information(3), search for users(2), or chat(1)
            String choice = "";
            do {
                choice = reader.readLine();
                if (choice.contains("[sending message command]")) {
                    Chat chat = new Chat(user.getEmail() + "_with_" + choice.split(",,,")[1]);
                    chat.addAMessage(choice.split(",,,")[choice.length() - 1], user);
                    //allow user to pick who to talk to and allow them to send messages
                } else if (choice.contains("[remove friend command]")) {
                    user.removeFriend(new User(choice.split("command]")[1]));
                } else if (choice.contains("[unblock user command]")) {
                    user.unblockUser(new User(choice.split("command]")[1]));
                } else if (choice.contains("[block user command]")) {
                    user.blockUser(new User(choice.split("command]")[1]));
                }
            } while (true);
        } catch (Exception e) {

        }

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

    public void rewriteLoginDetails(ArrayList<String> emails, ArrayList<String> passwords) throws InvalidInputException {
        String output = "";
        for(int i = 0; i < emails.size(); i++) {
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
        output = output.concat("\n" + email);
        output = output.concat("\n" + name);
        output = output.concat("\n" + major);
        output = output.concat("\nFriends:Empty");
        output = output.concat("\nBlocked:Empty");
        output = output.concat("\nWhoCanMessage:" + restriction);
        if (!output.isEmpty()) {
            output = output.substring(1);
            try (PrintWriter out = new PrintWriter(email + ".txt")) {
                out.print(output);
            } catch (Exception e) {
                throw new InvalidInputException("Problem creating user file");
            }
        }
    }
}
