import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class UserGUI implements Runnable{
    User currentUser; //current user using the GUI
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    String implementingUserEmail;

    //left panel
    JLabel userFriendListLabel;
    JComboBox friendList;
    
    JComboBox searchUserResultsList;
    JTextField searchUserText;
    JButton searchButton;
    
    JButton addOrRemoveButton;
    JButton blockOrUnblockButton;

    //right panel
    JTextField inputDialogue;
    JButton sendMessageButton;



    public UserGUI(BufferedReader bfr, PrintWriter pw) {
        //this is only for testing
        //in real examples, currentUser and its friends, blocked and chats should be stored in client side
        //(messaging software can still see own data and even if there is no connection)
        //also it decrease the level of complexity,
        //or maybe we can store the results in both client and serve side, and update from time to time
        ArrayList<String> fl = new ArrayList<>();
        ArrayList<String> bl = new ArrayList<>();
        fl.add("friend1@purdue.edu");
        fl.add("friend2@purdue.edu");
        fl.add("friend3@purdue.edu");
        fl.add("wang5788@purdue.edu");

        bl.add("blocked1@purdue.edu");
        bl.add("blocked2@purdue.edu");
        bl.add("someRandomPerson123@purdue.edu");
        currentUser = new User();
        //

        currentUser.setFriendsList(fl);
        currentUser.setBlockedList(bl);
        //pass reader and writer from client thread to GUI, so GUI can talk to main thread(the server)
        bufferedReader = bfr;
        printWriter = pw;
        //main page components
        implementingUserEmail = null;
        //friends list
        userFriendListLabel = new JLabel("Friends: ");
        String[] friendListALL = new String[currentUser.getFriendsList().size()];
        for (int i = 0; i < currentUser.getFriendsList().size(); i++) {
            friendListALL[i] = currentUser.getFriendsList().get(i);
        }
        friendList = new JComboBox<>(friendListALL);
        friendList.addActionListener(friendListActionListener);
        //functions
        searchUserResultsList = new JComboBox<>();
        searchUserResultsList.addActionListener(searchUserResultListActionListener);
        searchUserText = new JTextField(10);
        searchButton = new JButton("Search a User");
        searchButton.addActionListener(searchButtonActionListener);
        
        
        addOrRemoveButton = new JButton("ADD/REMOVE FRIEND");
        addOrRemoveButton.addActionListener(addOrRemoveButtonActionListener);
        blockOrUnblockButton = new JButton("BLOCK/UNBLOCK FRIEND");
        blockOrUnblockButton.addActionListener(blockOrUnblockButtonActionListener);
        //send message
        inputDialogue = new JTextField(20);
        sendMessageButton = new JButton("send");
        sendMessageButton.addActionListener(sendMessageActionListener);

    }


    @Override
    public void run() {
        //set the frame
        JFrame jFrame = new JFrame("messaging software");
        jFrame.setSize(600, 400);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);

        //set the panel
        JPanel jPanel = new JPanel(new BorderLayout());
        jFrame.setContentPane(jPanel);
        JPanel leftPanel = new JPanel(new GridLayout(3, 1));
        JPanel leftUpPanel = new JPanel();
        JPanel leftCenterPanel = new JPanel();
        JPanel leftDownPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        //messaging page
        
        leftUpPanel.add(userFriendListLabel);
        leftUpPanel.add(friendList);
        leftCenterPanel.add(searchUserResultsList);
        leftCenterPanel.add(searchUserText);
        leftCenterPanel.add(searchButton);
        leftDownPanel.add(addOrRemoveButton);
        leftDownPanel.add(blockOrUnblockButton);
        leftPanel.add(leftUpPanel);
        leftPanel.add(leftCenterPanel);
        leftPanel.add(leftDownPanel);
        jPanel.add(leftPanel, BorderLayout.WEST);

        centerPanel.add(inputDialogue);
        centerPanel.add(sendMessageButton);
        jPanel.add(centerPanel, BorderLayout.CENTER);


    } //end of run

    ActionListener sendMessageActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendMessageButton) {
                if (implementingUserEmail == null) {
                    JOptionPane.showMessageDialog(null, "Please choose a user first", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("sending: " + inputDialogue.getText() + " to " + implementingUserEmail);
                    printWriter.write(implementingUserEmail);
                    printWriter.flush();
                    printWriter.write(inputDialogue.getText());
                    printWriter.flush();
                }
            }
        }
    };

    ActionListener addOrRemoveButtonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addOrRemoveButton) {


                boolean operatedOnce = false; //ensure the add or remove process only happens one time
                int confirm;

                if (implementingUserEmail == null) {
                    JOptionPane.showMessageDialog(null, "Please choose a user first", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    for (String aEmail : currentUser.getFriendsList()) {
                        if (aEmail.equals(implementingUserEmail) && !operatedOnce) {
                            confirm = JOptionPane.showConfirmDialog(null, "This user is already your friend, do you want to remove friend" + aEmail + "?", "Remove friend",
                                    JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                currentUser.removeFriend(aEmail);
                            }
                            operatedOnce = true;
                        }
                    }
                    if (!operatedOnce) {
                        confirm = JOptionPane.showConfirmDialog(null, "This user is currently not your friend, do you sure want to add friend" + implementingUserEmail +"?", "Add friend",
                                JOptionPane.YES_NO_OPTION);
                        currentUser.addFriend(implementingUserEmail);
                    }
                }


            }
        }
    };

    ActionListener blockOrUnblockButtonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == blockOrUnblockButton) {


                boolean operatedOnce = false; //ensure the add or remove process only happens one time
                int confirm;

                if (implementingUserEmail == null) {
                    JOptionPane.showMessageDialog(null, "Please choose a user first", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    for (String aEmail : currentUser.getBlockedList()) {
                        if (aEmail.equals(implementingUserEmail) && !operatedOnce) {
                            confirm = JOptionPane.showConfirmDialog(null, "This user is already blocked by you, do you want to unblock " + aEmail + "?", "Unblock user",
                                    JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                currentUser.unblockUser(aEmail);
                            }
                            operatedOnce = true;
                        }
                    }
                    if (!operatedOnce) {
                        confirm = JOptionPane.showConfirmDialog(null, "This user is currently not blocked by you, do you want to block" + implementingUserEmail +"?", "Block user",
                                JOptionPane.YES_NO_OPTION);
                        currentUser.blockUser(implementingUserEmail);
                    }
                }


            }
        }
    };

    ActionListener friendListActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == friendList) {


                implementingUserEmail = (String)friendList.getSelectedItem();


            }
        }
    };

    ActionListener searchButtonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == searchButton) {


                if (searchUserText.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Please enter the key word to be searched", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("searching names that contains " + searchUserText.getText());
                    printWriter.write(searchUserText.getText());
                    printWriter.flush();
                }

                String results;
                try {
//                    results = bufferedReader.readLine();
                    
                    //suppose the result of search "ch" is in a string separated by "/"
                    //String results = "Charles/Charlotte/Chelsea/Chase/Christopher/Chandler/Chad/Cheryl/Cheyenne/Churchill";
                    results = "Charles/Charlotte/Chelsea/Chase/Christopher/Chandler/Chad/Cheryl/Cheyenne/Churchill";
                    //
                    
                    searchUserResultsList = new JComboBox(results.split("/"));
                } catch (Exception ex) {
                    //error occur when reading searching results
                    searchUserResultsList = new JComboBox<>();
                }


            }
        }
    };

    ActionListener searchUserResultListActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == searchUserResultsList) {


                implementingUserEmail = (String)searchUserResultsList.getSelectedItem();


            }
        }
    };





}
