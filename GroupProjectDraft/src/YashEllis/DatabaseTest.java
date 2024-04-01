import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * A framework to run public test cases for Group Project.
 *
 * <p>Purdue University -- CS18000 -- Spring 2021</p>
 *
 * @author J Morris Purdue CS
 * @version Feb 20, 2024
 */

@RunWith(Enclosed.class)
public class DatabaseTest {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    public static class TestCase {

        private static final String INFILE = "input.txt";

        @Test(timeout = 1000)
        public void BadDataExceptionDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = InvalidInputException.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `InvalidInputException` is `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `InvalidInputException` is NOT `abstract`!",
                    Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `InvalidInputException` extends `Exception`!",
                    Exception.class, superclass);
            Assert.assertEquals("Ensure that `InvalidInputException` implements no interfaces!",
                    0, superinterfaces.length);
        }

        @Test(timeout = 1000)
        public void ImpossibleChangeException() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = InvalidInputException.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `ImpossibleChangeException` is `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `ImpossibleChangeException` is NOT `abstract`!",
                    Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `ImpossibleChangeException` extends `Exception`!",
                    Exception.class, superclass);
            Assert.assertEquals("Ensure that `ImpossibleChangeException` implements no interfaces!",
                    0, superinterfaces.length);
        }

        @Test(timeout = 1000)
        public void UserClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = User.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `User` is `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `User` is NOT `abstract`!",
                    Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `User` extends `Object`!",
                    Object.class, superclass);
            Assert.assertEquals("Ensure that `User` implements no interfaces!",
                    0, superinterfaces.length);
        }

        @Test(timeout = 1000)
        public void runTestFoundationDatabaseOut() {
            try {

                User u1 = new User("personA", "1234", "personA@purdue.edu", "IE",
                        new ArrayList<>(), null, null, true);
                User u2 = new User("personB", "1234", "personB@purdue.edu", "ECE",
                        new ArrayList<User>() {{
                            add(u1);
                        }}, null, null, true);
                User u3 = new User("personC", "1234", "personC@purdue.edu", "CS",
                        new ArrayList<User>() {{
                            add(u1);
                            add(u2);
                        }}, null, null, true);
                User u4 = new User("personD", "2222", "personD@purdue.edu", "ME",
                        null, null, null, true);
                Chat c = new Chat(u1, u2);
                c.addAMessage("hello", u1);
                Database db = new Database(new ArrayList<User>() {{
                    add(u1);
                    add(u2);
                    add(u3);
                    add(u4);
                }});
                if (!db.login("personA@purdue.edu", "1234")) {
                    Assert.assertTrue("Log in was successful, but did not go through", false);
                }
                if (db.login("personA@purdue.edu", "1111")) {
                    Assert.assertTrue("Log in was not successful, but did go through", false);
                }
                if(!db.usersEmailSearch("personD@purdue.edu").equals(u4)) {
                    Assert.assertTrue("Returned wrong user based on email", false);
                }
                if(!db.usersMajorSearch("personD@purdue.edu").get(0).equals(u4)) {
                    Assert.assertTrue("Returned wrong user based on major", false);
                }
                if(!db.usersNameSearch("personC").get(0).equals(u3)) {
                    Assert.assertTrue("Returned wrong user based on name", false);
                }
                if(!db.modifyUser(u2, u3) && db.usersEmailSearch("personB@purdue.edu").equals(u2)) {
                    Assert.assertTrue("Modified user incorrectly", false);
                }
            } catch (InvalidInputException e) {
                Assert.assertTrue("Error while attempting to create user/database", false);
            } catch (Exception e) {
                Assert.assertTrue("Error while attempting to run one or more methods", false);
            }
        }

    }

}
