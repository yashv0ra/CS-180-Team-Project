import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    public void runClient(BufferedReader r, PrintWriter w) throws IOException, InvalidInputException {
        ArrayList<User> userlist = new ArrayList<>();
        Database data = new Database(userlist);
        User user = new User();
        Scanner scanner = new Scanner(System.in);
        String emailInput = "";
        String passwordInput = "";
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
        int account = 0; // 1 when user wants to log in, 2 when user wants to sign in, and 3 when user wants to exit
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
                //Let user input both
                if (!email.contains(emailInput)) {
                    if (emailInput.contains("@") && !emailInput.contains(" ") && !passwordInput.isEmpty()) {
                        String[] emailElements = emailInput.split("@");
                        if (emailElements.length == 2 && !emailElements[0].isEmpty()
                                && emailElements[1].equals("purdue.edu")) {
                            email.add(emailInput);
                            password.add(passwordInput);
                            //Synchronize the two things above
                        } else {
                            throw new InvalidInputException("Invalid Input!");
                        }
                    } else {
                        throw new InvalidInputException("Invalid Input!");
                    }
                    validUser = true;
                } else {
                    if (email.contains(emailInput)) {
                        //throw new InvalidInputException("User with this email already exists");
                    }
                    //Provide user options to retry, log in, or end program and update account accordingly
                }
            }
        } while (!validUser);
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
        //Friends:dngquan@purdue.edu,yash@purdue.edy,purduePete@purdue.edu...
        //Blocked:Empty
        //WhoCanMessage:friends

        bfr = new BufferedReader(new FileReader(emailInput.concat("DataFile")));
        ArrayList<String> userData = new ArrayList<>();
        String l = bfr.readLine();
        while (l != null) {
            userData.add(l);
            l = bfr.readLine();
        }
        ArrayList<User> friends = new ArrayList<>();
        ArrayList<User> blocked = new ArrayList<>();
        ArrayList<Chat> chats = new ArrayList<>();
        String[] friendString = userData.get(3).substring(7).split(",");
        String[] blockedString = userData.get(4).substring(7).split(",");
        if(!friendString[0].equals("Empty")) {
            for(int i = 0; i < friendString.length; i ++) {
                friends.add(new User(friendString[i]));
                chats.add(new Chat(emailInput.concat("_with_").concat(friendString[i])));
                //create constructor for users based on info in file
                //do the same for chats based on chat files
                //this for loop will initialize a list of users and corresponding chats
            }
        }
        if(!blockedString[0].equals("Empty")) {
            for(int i = 0; i < blockedString.length; i ++) {
                blocked.add(new User(blockedString[i]));
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
        int choice = 0;
        do {
            choice = scanner.nextInt();
            if (choice == 1) {
                user.canMessage(user);
                //allow user to pick who to talk to and allow them to send messages
            } else if (choice == 2) {
                //search input is:
                String searchInput = "";
                data.usersNameSearch(user.getName());
                data.usersMajorSearch(user.getMajor());
                data.usersEmailSearch(user.getEmail());
                user.addFriend(user);
                user.removeFriend(user);
                user.blockUser(user);
                user.unblockUser(user);
                //allow user to search for others based on major, name, or email and add, remove, or block people
            } else if (choice == 3) {
                //data.modifyUser(); needs to be void?
                user.changeRestriction();
                //allow user to modify name, message restriction, or password
            }
        } while (!(choice == 4));
    }
}
