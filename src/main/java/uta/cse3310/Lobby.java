package uta.cse3310;

import java.util.ArrayList;

public class Lobby {
    public ArrayList<User> usersInLobby = new ArrayList<>();
    public int playerSize;

    public Lobby(int size) {
        playerSize = size;
    }

    public Game tryMakeGame() {
        //create new game and add players in player list to lobby
        
        if(usersInLobby.size() >= playerSize){
            Game newinstance = new Game(usersInLobby);

            usersInLobby.clear();
            return newinstance;
        }
        
        return null;
    }
    void addUser(User user) {
        usersInLobby.add(user);
    }
    void removeUser(User user) {
        usersInLobby.remove(user);
    }
}
