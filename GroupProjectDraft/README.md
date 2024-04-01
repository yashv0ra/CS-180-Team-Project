Class Overview:

This project contains 6 classes named Chat.java, Database.java, UserInterface.java, User.java, 
ImpossibleChangeException.java, and InvalidInputException.java.

The Chat class will contain fields and methods representing messages and messaging between two users. 

The Database class will contain methods and fields related to users including how to modify a user, 
search up a user, and email and password info.

The UserInterface class will contatin all fields for the User class.

The User class will contain fields and methods relating to user info and comparing two users.

ImpossibleChangeException is a custom Exception for when an invalid change is being made.

InvalidInputException is a custom Exception for when an invalid input is being inputted.


Class Descriptions:

The Chat class contains 5 fields: A string array list named "messages" A user array list named "whoSentTheMessage", 
two users named "User1" and "User2", and a string named "fileNameForTheTwoUser". Each of the 5 fields has its' own 
getter and setter methods. Additionally the class contains a chat constructor, a boolean method named "addAMessage", 
a void method named "deleteMessage" a boolean method named isRestricted, and a test case that tests for invalid inputs 
in a new chat object. The addAMessage and deleteMessage methods both have a String parameter named "message" and addAMessage
also has a user parameter named "whichUser". The two methods add and delete messages between two users. isRestricted has 
two Profile parameters which are named "A" and "B". The isRestricted method will check if a user is restricted by 
another user. The test case throws an InvalidInputException if an invalid input is added to a user object.

The Database class contains 1 field: An user array list named "listOfUsers". Database also contains a database constructor, a
boolean method named "login", a user array list named "usersMajorSearch", a user array list named "usersNameSearch", 
a user array list named "usersEmailSearch", two boolean methods named "ModifyUser" and a String method named "toString".
The three search methods each have a string named "login", "name", and "email" respectively. Each of the three methods searches
their respective string(ex: usersNameSearch searches for name). The first of the two ModifyUser methods has two user parameters named 
"aimUser" and "changed". This method changes the information of the user. The second ModifyUser method has two user array list parameters
named "usersNeedToBeChanged" and "changed" and also throws an ImpossibleChangeException. This method changes a user array list. The
toString method has an int parameter named index and the method prints the toString of the Database class.
