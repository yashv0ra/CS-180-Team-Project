Class Overview:

This project contains 6 classes named Chat.java, Database.java, UserInterface.java, User.java, 
ImpossibleChangeException.java, and InvalidInputException.java.

The Chat class will contain fields and methods representing messages and messaging between two users. 

The Database class will contain methods and fields related to users including how to modify a user, 
search up a user, and email and password info.

The UserInterface class will contatin all methods for the User class.

The User class will contain fields and methods relating to user info and comparing two users.

ImpossibleChangeException is a custom Exception for when an invalid change is being made.

InvalidInputException is a custom Exception for when an invalid input is being inputted.


Class Descriptions:

The Chat class contains 5 fields: A String ArrayList named "messages" A User ArrayList named "whoSentTheMessage", 
two users named "User1" and "User2", and a String named "fileNameForTheTwoUser". Each of the five fields has its' own 
getter and setter methods. Additionally the class contains a Chat constructor, a Boolean method named "addAMessage", 
a Void method named "deleteMessage" a Boolean method named isRestricted, and a test case that tests for invalid inputs 
in a new Chat object. The addAMessage and deleteMessage methods both have a String parameter named "message" and addAMessage
also has a User parameter named "whichUser". The two methods add and delete messages between two users. The isRestricted method has 
two Profile parameters which are named "A" and "B". The isRestricted method will check if a user is restricted by 
another user. The test case throws an InvalidInputException if an invalid input is added to a User object.

The Database class contains 1 field: An User ArrayList named "listOfUsers". Database also contains a Database constructor, a
Boolean method named "login", a User ArrayList named "usersMajorSearch", a User ArrayList named "usersNameSearch", 
a User ArrayList named "usersEmailSearch", two Boolean methods named "ModifyUser" and a String method named "toString".
The three search methods each have a String named "login", "name", and "email" respectively. Each of the three methods search
their respective String(ex: usersNameSearch searches for name). The first of the two ModifyUser methods has two User parameters named 
"aimUser" and "changed". This method changes the information of the user. The second ModifyUser method has two User ArrayList parameters
named "usersNeedToBeChanged" and "changed" and also throws an ImpossibleChangeException. This method changes a UserArray list. The
toString method has an Int parameter named index and the method prints the toString of the Database class.

The UserInterface class contains 10 methods all for the User class. The first 4 are String getter methods named "getName", "getPassword",
"getEmail", and "getMajor". Each of these methods gets one of the four User parameters being name, password, email, and major. 
The next four methods are all setters that correspond to the getters. They are named "setName", "setPassword", "setEmail", and "setMajor".
Each setter has a string parameter which are named "name", "password", "email", and "major" respectively. The final two methods are
a boolean method named "compareTo" with a User parameter named "user" and a boolean method named "modifyUser" with a User parameter 
named "user".

The User class contains 4 String fields named "name", "password", "email", and "major". The User class also has a User constructor which
uses the four class fields. There is also a User constructor which creates a new User object and sets all four variables to null. The rest of
the class is comprised of methods already mentioned in the UserInterface class description. The four getters return their respective variables
and the four setter methods set the value of those same four variables. Additionally, the compareTo method compares the emails of two users.

The ImpossibleChangeException class contains a ImpossibleChangeException constructor with a String paramater named "message".
The constructor returns a message whenever an ImpossibleChangeException is thrown.

The InvalidInputException class contains a InvalidInputException constructor with a String paramater named "message".
The constructor returns a message whenever an InvalidInputException is thrown.
