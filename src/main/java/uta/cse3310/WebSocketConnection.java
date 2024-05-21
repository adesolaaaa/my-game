package uta.cse3310;

import org.java_websocket.WebSocket;

public class WebSocketConnection implements Connection {
    private WebSocket socket;

    public WebSocketConnection(WebSocket socket) {
        this.socket = socket;
    }

    @Override
    public void send(String message) {
        socket.send(message);
    }

    @Override
    public User getAttachedUser() {
        return socket.getAttachment();
    }

    @Override
    public void setAttatchedUser(User user) {
        socket.setAttachment(user);
    }

    @Override
    public boolean isForObject(Object o) {
        return socket == o;
    } 
}
