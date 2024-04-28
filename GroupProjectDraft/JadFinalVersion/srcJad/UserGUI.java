import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class UserGUI implements Runnable {
    private User currentUser; //current user using the GUI
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    private String implementingUserEmail;

    //left panel
    private JLabel userFriendListLabel;
    private JComboBox friendList;
    private JButton showFriendDetail;
    private JComboBox searchUserResultsList;
    private JTextField searchUserText;
    private JButton searchButton;

    private JButton addOrRemoveButton;
    private JButton blockOrUnblockButton;

    //right panel
    private JTextArea chatArea;
    private Map<String, String> conversations;
    private JScrollPane scrollPane;
    private JTextField inputDialogue;
    private JButton sendMessageButton;
    private JButton deleteLastMessageButton;
    private String withoutLastMessage;

    public UserGUI(BufferedReader bfr, PrintWriter pw) {
        //this is only for testing
        //in real examples, currentUser and its friends, blocked and chats should be also stored in client side
        //(messaging software can still see own data and even if there is no connection)
        //(also it decreases the level of complexity)
        //and update from time to time

        try {
            String emailCurrentUser = bfr.readLine();
            currentUser = new User(emailCurrentUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //
        System.out.println("Email from UserGUI: " + currentUser.getEmail());
        //pass reader and writer from client thread to GUI, so GUI can talk to main thread(the server)
        bufferedReader = bfr;
        printWriter = pw;
        //main page components
        implementingUserEmail = null;
        //friends list
        userFriendListLabel = new JLabel("Friends: ");
        String[] friendListALL = new String[currentUser.getFriendsList().size() + currentUser.getBlockedList().size()];
        for (int i = 0; i < currentUser.getFriendsList().size(); i++) {
            friendListALL[i] = currentUser.getFriendsList().get(i);
        }
        int temp = friendListALL.length - 1;
        for (int i = 0; i < currentUser.getBlockedList().size(); i++) {
            friendListALL[i + temp] = currentUser.getBlockedList().get(i);
        }
        friendList = new JComboBox<>(friendListALL);
        friendList.addActionListener(friendListActionListener);
        showFriendDetail = new JButton("Show Friend Detail");
        showFriendDetail.addActionListener(showFriendDetailActionListener);
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
        //send messages
        chatArea = new JTextArea();
        chatArea.setTabSize(100);
        chatArea.setEditable(false);
        scrollPane = new JScrollPane(chatArea);
        conversations = new HashMap<>();

        inputDialogue = new JTextField(20);
        sendMessageButton = new JButton("Send");
        sendMessageButton.addActionListener(sendMessageActionListener);

        deleteLastMessageButton = new JButton("Delete Last Message");
        deleteLastMessageButton.addActionListener(deleteLastMessageActionListener);
        withoutLastMessage = null;
    }


    @Override
    public void run() {
        //set the frame
        JFrame jFrame = new JFrame("messaging software");
        jFrame.setSize(1000, 400);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        jFrame.pack();
        jFrame.setVisible(true);

        //set the panel
        JPanel jPanel = new JPanel(new BorderLayout());
        jFrame.setContentPane(jPanel);
        JPanel leftPanel = new JPanel(new GridLayout(3, 1));
        JPanel leftUpPanel = new JPanel();
        JPanel leftCenterPanel = new JPanel();
        JPanel leftDownPanel = new JPanel();
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel centerCenterPanel = new JPanel(new BorderLayout());
        JPanel centerDownPanel = new JPanel();
        //messaging page

        leftUpPanel.add(userFriendListLabel);
        leftUpPanel.add(friendList);
        leftUpPanel.add(showFriendDetail);
        leftCenterPanel.add(searchUserResultsList);
        leftCenterPanel.add(searchUserText);
        leftCenterPanel.add(searchButton);
        leftDownPanel.add(addOrRemoveButton);
        leftDownPanel.add(blockOrUnblockButton);
        leftPanel.add(leftUpPanel);
        leftPanel.add(leftCenterPanel);
        leftPanel.add(leftDownPanel);
        jPanel.add(leftPanel, BorderLayout.WEST);


        //chatArea is already in scrollPane
        centerCenterPanel.add(scrollPane, BorderLayout.CENTER);
        centerDownPanel.add(inputDialogue);
        centerDownPanel.add(sendMessageButton);
        centerDownPanel.add(deleteLastMessageButton);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
        centerPanel.add(centerDownPanel, BorderLayout.SOUTH);
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
                    if (!inputDialogue.getText().isEmpty()) {
                        System.out.println("sending: " + inputDialogue.getText() + " to " + implementingUserEmail + " , if possible.");
                        printWriter.write("[sending message command],,," + implementingUserEmail + ",,,[message],,," + inputDialogue.getText());
                        printWriter.println();
                        printWriter.flush();
                        //it is up to server side to check friendList and blockList to if the sending can work
                        //if can just add the message to that specific file
//                        String conversation = conversations.getOrDefault(implementingUserEmail, "");
//                        conversation += "You: " + inputDialogue.getText() + "\n";
//                        conversations.put(implementingUserEmail, conversation);
                        String conversation = "";
                        try {
                            while (bufferedReader.readLine().equals("CONTINUE")) {
                                conversation = conversation.concat("\n" + bufferedReader.readLine());
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        conversation = conversation;
                        conversations.put(implementingUserEmail, conversation);
                        chatArea.setText(conversations.getOrDefault(implementingUserEmail, ""));
                        inputDialogue.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Cannot send empty sentence", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    };

    ActionListener addOrRemoveButtonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addOrRemoveButton) {


                boolean operatedOnce = false; //ensure the "add or remove" process only happens one time
                int confirm;

                if (implementingUserEmail == null) {
                    JOptionPane.showMessageDialog(null, "Please choose a user first", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    String emailToRemove = null;
                    for (String aEmail : currentUser.getFriendsList()) {
                        if (aEmail.equals(implementingUserEmail) && !operatedOnce) {
                            confirm = JOptionPane.showConfirmDialog(null, "This user is already your friend, do you want to remove friend" + aEmail + "?", "Remove friend",
                                    JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                emailToRemove = aEmail;
                                printWriter.write("[remove friend command]" + aEmail);
                                printWriter.println();
                                printWriter.flush();
                            }
                            operatedOnce = true;
                        }
                    }
                    if (emailToRemove != null) {
                        currentUser.removeFriend(implementingUserEmail);
                        friendList.removeItem(emailToRemove);
                    }
                    if (!operatedOnce) {
                        confirm = JOptionPane.showConfirmDialog(null, "This user is currently not your friend, do you sure want to add friend" + implementingUserEmail + "?", "Add friend",
                                JOptionPane.YES_NO_OPTION);
                        currentUser.addFriend(implementingUserEmail);
                        printWriter.write("[add friend command]" + implementingUserEmail);
                        printWriter.println();
                        printWriter.flush();
                        friendList.addItem(implementingUserEmail);
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
                                currentUser.unblockUser(aEmail); //do not need
                                printWriter.write("[unblock user command]" + aEmail);
                                printWriter.println();
                                printWriter.flush();
                            }
                            operatedOnce = true;
                        }
                    }
                    if (!operatedOnce) {
                        confirm = JOptionPane.showConfirmDialog(null, "This user is currently not blocked by you, do you want to block" + implementingUserEmail + "?", "Block user",
                                JOptionPane.YES_NO_OPTION);
                        currentUser.blockUser(implementingUserEmail); //do not need
                      printWriter.write("[block user command]" + implementingUserEmail);
                      printWriter.println();
                      printWriter.flush();
                    }
                }


            }
        }
    };
    ActionListener friendListActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == friendList) {


                implementingUserEmail = (String) friendList.getSelectedItem();
                if (implementingUserEmail != null) {
                    chatArea.setText(conversations.getOrDefault(implementingUserEmail, ""));
                }


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
                    printWriter.write("[search user command],,," + searchUserText.getText());
                    printWriter.println();
                    printWriter.flush();
                }

                String results = "null";
                try {
                    results = bufferedReader.readLine();
                    System.out.println(results);
                    searchUserResultsList.removeAllItems();
                    String[] resultsOfSearchInArray = results.split("/");
                    for (String r : resultsOfSearchInArray) {
                        searchUserResultsList.addItem(r);
                    }
//                    searchUserResultsList = new JComboBox<>(results.split("/"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //error occurs when reading searching results
                    searchUserResultsList = new JComboBox<>();
                }


            }
        }
    };
    ActionListener searchUserResultListActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == searchUserResultsList) {


                implementingUserEmail = (String) searchUserResultsList.getSelectedItem();
                if (implementingUserEmail != null) {
                    chatArea.setText(conversations.getOrDefault(implementingUserEmail, ""));
                }


            }
        }
    };
    ActionListener deleteLastMessageActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == deleteLastMessageButton) {
                printWriter.write("[delete last message],,," + implementingUserEmail);
                printWriter.println();
                printWriter.flush();

                String conversation = "";
                try {
                    while (bufferedReader.readLine().equals("CONTINUE")) {
                        conversation = conversation.concat("\n" + bufferedReader.readLine());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                conversation = conversation;
                conversations.put(implementingUserEmail, conversation);
                chatArea.setText(conversations.getOrDefault(implementingUserEmail, ""));
                inputDialogue.setText("");
            }


        }
    };
    ActionListener showFriendDetailActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {


            implementingUserEmail = (String) friendList.getSelectedItem();
            if (implementingUserEmail != null) {
                //for connecting, this part should be communicating with server and get the user information with implementingUserEmail
                printWriter.write("[show friend detail command],,," + implementingUserEmail);
                printWriter.println();
                printWriter.flush();
                String[] details = new String[3];
                try {
                    details[0] = bufferedReader.readLine();
                    details[1] = bufferedReader.readLine();
                    details[2] = bufferedReader.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                User implementingUser;
                try {
                    implementingUser = new User(implementingUserEmail);
                } catch (InvalidInputException ex) {
                    throw new RuntimeException(ex);
                }
                String detail = details[0] +
                        "\n" + details[1] +
                        "\n" + details[2];
//                try {
//                    detail = bufferedReader.readLine();
//                    details = detail.split("/");
//                } catch (IOException exception) {
//                    details = null;
//                }

                if (details != null) {
                    JOptionPane.showMessageDialog(null,
                            details[0]
                                    + "\n" + details[2]
                                    + "\n" + details[1], "Detail Information",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "An error occurred when finding the friend's detail information, please try again later", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }


        }
    };
}
