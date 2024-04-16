import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class UserGUI implements Runnable{
    User currentUser; //current user using the GUI
    ArrayList<User> friendList;
    ArrayList<JButton> friendButtons;

    JTextField inputDialogue;
    JButton sendMessageButton;

    JButton loginButton;
    JTextField emailText;
    JTextField passwordText;
    String loginID;

    public UserGUI() {
        //this is only for testing
        ArrayList<User> fl = new ArrayList<>();
        try {
            User u1 = new User("friend1", "1234ab", "friend1@purdue.edu", "math", null, null, null, false);
            fl.add(u1);
            User u2 = new User("friend2", "abcdef", "friend2@purdue.edu", "physics", null, null, null, false);
            User u3 = new User("Zack Wang", "20000003134a", "wang5788@purdue.edu", "math", null, null, null, false);

            fl.add(u2);
            fl.add(u3);

        }catch (InvalidInputException e) {
            e.printStackTrace();
            System.out.println("wrong friend input");
            return;
        }
        //

        currentUser = new User();
        currentUser.setFriendsList(fl);

        //main page components
        friendList = currentUser.getFriendsList();
        friendButtons = new ArrayList<>();

        inputDialogue = new JTextField(20);
        sendMessageButton = new JButton("send");
        sendMessageButton.addActionListener(sendMessageActionListener);

        //login page components
        loginButton = new JButton("login");
        loginButton.addActionListener(loginActionListener);
        emailText = new JTextField(10);
        passwordText = new JTextField(10);
        loginID = "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new UserGUI());
    }
    @Override
    public void run() {
        JFrame jFrame = new JFrame("messaging software");
        jFrame.setVisible(true);
        jFrame.setSize(600, 400);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel(new BorderLayout());
        jFrame.setContentPane(jPanel);

        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JPanel centralPanel = new JPanel();


        //login page
        centralPanel.add(emailText);
        centralPanel.add(passwordText);
        centralPanel.add(loginButton);
        jPanel.add(centralPanel);
//        do {
//            JOptionPane.showMessageDialog(null, "please enter the correct username or password", "Error", JOptionPane.ERROR_MESSAGE);
//
//        } while (!loginID.equals("wang5788password:1234"));
        

        //messaging page
        jFrame.remove(centralPanel);
        for (User u : friendList) {
            JButton aFriend = new JButton(u.getName());
            aFriend.addActionListener(friendActionListener);
            friendButtons.add(aFriend);
            leftPanel.add(aFriend);
        }

        jPanel.add(leftPanel, BorderLayout.WEST);

        rightPanel.add(inputDialogue);
        rightPanel.add(sendMessageButton);
        jPanel.add(rightPanel, BorderLayout.CENTER);


    } //end of run


    ActionListener friendActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < friendButtons.size(); i++) {
                if (e.getSource() == friendButtons.get(i)) {
                    JOptionPane.showMessageDialog(null,
                            "Name: " + friendList.get(i).getName()
                                    + "\nEmail: " + friendList.get(i).getEmail()
                                    + "\nMajor: " + friendList.get(i).getMajor(), "Detail Information",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.out.println(friendList.get(i).getName());
                }
            }
        }
    };

    ActionListener sendMessageActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendMessageButton) {
                System.out.println(inputDialogue.getText());
            }
        }
    };

    ActionListener loginActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {
                System.out.println(emailText.getText() + passwordText.getText());
                loginID = emailText.getText() + "password:" + passwordText.getText();
            }
        }
    };











}
