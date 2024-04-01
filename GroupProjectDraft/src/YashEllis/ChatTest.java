import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class ChatTest {

    @Test
    public void testAddAMessage() {
        try {

            User user1 = new User("Alice", "password", "alice@purdue.edu", "Computer Science",
                    new ArrayList<>(), null, null, false);
            User user2 = new User("Bob", "password", "bob@purdue.edu", "Electrical Engineering",
                    new ArrayList<>(), null, null, false);

            Chat chat = new Chat(user1, user2);
            chat.addAMessage("Hello, Bob!", user1);
            assertEquals(1, chat.getMessages().size());
        } catch (Exception e) {
            System.out.println("Error Adding Message");
        }
    }

    @Test
    public void testDeleteMessageByContent() {
        try {
            User user1 = new User("Alice", "password", "alice@purdue.edu", "Computer Science",
                    new ArrayList<>(), null, null, false);
            User user2 = new User("Bob", "password", "bob@purdue.edu", "Electrical Engineering",
                    new ArrayList<>(), null, null, false);

            Chat chat = new Chat(user1, user2);
            chat.addAMessage("Hello, Bob!", user1);
            chat.deleteMessage("Hello, Bob!");
            assertEquals(0, chat.getMessages().size());
        } catch (Exception e) {
            System.out.println("Error Deleting Message");
        }
    }
}
