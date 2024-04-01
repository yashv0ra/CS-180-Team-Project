import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testAddFriend() {
        try {
            User user1 = new User("Alice", "password", "alice@purdue.edu", "Computer Science",
                    new ArrayList<>(), null, null, true);
            User user2 = new User("Bob", "password", "bob@purdue.edu", "Electrical Engineering",
                    new ArrayList<>(), null, null, true);

            user1.addFriend(user2);
            assertTrue(user1.getFriendsList().contains(user2));
        } catch (Exception e) {
            System.out.println("Problem with adding friend");
        }
    }

    @Test
    public void testRemoveFriend() {
        try {
            User user1 = new User("Alice", "password", "alice@purdue.edu", "Computer Science",
                    new ArrayList<>(), null, null, true);
            User user2 = new User("Bob", "password", "bob@purdue.edu", "Electrical Engineering",
                    new ArrayList<>(), null, null, true);

            user1.addFriend(user2);
            user1.removeFriend(user2);
            assertFalse(user1.getFriendsList().contains(user2));
        } catch (Exception e) {
            System.out.println("Problem with removing friend");
        }
    }

    @Test
    public void testBlockUser() {
        try {
            User user1 = new User("Alice", "password", "alice@purdue.edu", "Computer Science",
                    new ArrayList<>(), null, null, true);
            User user2 = new User("Bob", "password", "bob@purdue.edu", "Electrical Engineering",
                    new ArrayList<>(), null, null, true);

            user1.blockUser(user2);
            assertTrue(user1.getBlockedList().contains(user2));

        } catch (Exception e) {
            System.out.println("Problem with blocking user");
        }
    }

    @Test
    public void testUnblockUser() {
        try {
            User user1 = new User("Alice", "password", "alice@purdue.edu", "Computer Science",
                    new ArrayList<>(), null, null, true);
            User user2 = new User("Bob", "password", "bob@purdue.edu", "Electrical Engineering",
                    new ArrayList<>(), null, null, true);

            user1.blockUser(user2);
            user1.unblockUser(user2);
            assertFalse(user1.getBlockedList().contains(user2));
        } catch (Exception e) {
            System.out.println("Problem with unblocking user");
        }
    }
}

