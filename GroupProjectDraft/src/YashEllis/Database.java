import java.util.ArrayList;

public class Database {
    private ArrayList<User> listOfUsers;

    public Database(ArrayList<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    public boolean login(String email, String password) {
        for (int i = 0; i < listOfUsers.size(); i++) {
            if (listOfUsers.get(i).getEmail().equals(email)) {
                if (listOfUsers.get(i).getPassword().equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    //for the major search, I am thinking we should do a scroll down menu on the GUI part instead of
    //letting the user freely input any word they want, in order to reduce the complexity of this program
    public ArrayList<User> usersMajorSearch (String major) {
        ArrayList<User> listThatMatches = new ArrayList<>();
        for (int i = 0; i < listOfUsers.size(); i++) {
            if (listOfUsers.get(i).getMajor().equals(major)) {
                listThatMatches.add(listOfUsers.get(i));
            }
        }
        if (listOfUsers.size() > 0) {
            return listThatMatches;
        } else {
            return null;
        }
    }

    //name should be in the form of first and last name's first letter cap and the rest is small case,
    //with a space between the first and last name.
    //"name" that the user want to search have to be from the first letter of the first or last name
    //for example, for search Zack Wang, the string can be "Za" or "wan"
    public ArrayList<User> usersNameSearch (String name) {
        ArrayList<User> listThatMatches = new ArrayList<>();

        if (name.length() > 1) {
            String toForm = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }

        for (int i = 0; i < listOfUsers.size(); i++) {
            if (listOfUsers.get(i).getName().contains(name)
                    || listOfUsers.get(i).getName().contains(toString())) {
                listThatMatches.add(listOfUsers.get(i));
            }
        }
        if (listOfUsers.size() > 0) {
            return listThatMatches;
        } else {
            return null;
        }
    }

    public User usersEmailSearch (String email) {
        User returnUser = null;

        if (email.contains("@")) {
            for (int i = 0; i < listOfUsers.size(); i++) {
                if (email.equals(listOfUsers.get(i).getEmail())) {
                    returnUser = listOfUsers.get(i);
                }
            }
        } else {
            String emailFull = email + "@purdue.edu";
            for (int i = 0; i < listOfUsers.size(); i++) {
                if (emailFull.equals(listOfUsers.get(i).getEmail())) {
                    returnUser = listOfUsers.get(i);
                }
            }
        }
        return returnUser;
    }

    public synchronized boolean modifyUser(User aimUser, User changed) {
        for (int i = 0; i < listOfUsers.size(); i++) {
            if (listOfUsers.get(i).compareTo(aimUser)) {
                listOfUsers.get(i).setName(changed.getName());
                listOfUsers.get(i).setMajor(changed.getMajor());
                return true;
            }
        }
        return false;
    }

    public synchronized boolean modifyUser(ArrayList<User> usersNeedToBeChanged, ArrayList<User> changed) throws ImpossibleChangeException{
        if (usersNeedToBeChanged.size() != changed.size()) {
            throw new ImpossibleChangeException("lists do not match");
        } else {
            for (int i = 0; i < usersNeedToBeChanged.size(); i++) {
                modifyUser(usersNeedToBeChanged.get(i), changed.get(i));
            }
        }
        return true;
    }

    //should this method the same or similar to the toString method?
//    public String toString(String email) {
//        usersEmailSearch(email);
//        String result = "name: " + listOfUsers.get(index).getName()
//                + "\nEmail: " + listOfUsers.get(index).getEmail()
//                + "\nMajor: " + listOfUsers.get(index).getMajor();
//        return result;
//    }

    //get the user index
//    public int userView(User user) {
//        for (int i = 0; i < listOfUsers.size(); i++) {
//            if ()
//        }
//    }


//For testing this class
    public static void main(String[] args) throws InvalidInputException {
        User u1 = new User("personA", "1234", "personA@purdue.edu", "IE",
                new ArrayList<>(),null,null,true);
        User u2 = new User("personB", "1234", "personB@purdue.edu", "ECE",
                new ArrayList<User>() {{add(u1);}},null,null,true);
        User u3 = new User("personC", "1234", "personC@purdue.edu", "CS",
                new ArrayList<User>() {{add(u1);add(u2);}},null,null,true);
        User u4 = new User("personD", "2222", "personD@purdue.edu", "ME",
                null,null,null,true);
        Chat c = new Chat(u1, u2);
        c.addAMessage("hello", u1);
        Database db = new Database(new ArrayList<User>() {{add(u1);add(u2);add(u3);add(u4);}});
    }

}
