package uta.cse3310;

public interface Connection {
    void send(String message);
    User getAttachedUser();
    void setAttatchedUser(User user);
    boolean isForObject(Object o);
}