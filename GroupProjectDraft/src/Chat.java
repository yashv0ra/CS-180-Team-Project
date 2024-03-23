import java.util.ArrayList;

public class Chat {
    private ArrayList<String>[] messages;
    private User user1;
    private User user2;

    public Chat (ArrayList<String>[] messages, User user1, User user2) {
        this.messages = messages;
        this.user1 = user1;
        this.user2 = user2;
    }
    public Chat () {

    }



}
