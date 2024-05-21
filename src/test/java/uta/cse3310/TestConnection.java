package uta.cse3310;
import java.util.ArrayList;

public class TestConnection implements Connection {
    public ArrayList<String> sentMessages = new ArrayList<>();
    public User attachedUser;

    @Override
    public void send(String message) {
        sentMessages.add(message);
    }

    @Override
    public User getAttachedUser() {
        return attachedUser;
    }

    @Override
    public void setAttatchedUser(User user) {
        attachedUser = user;
    }

    @Override
    public boolean isForObject(Object o) {
        return false;
    }
}