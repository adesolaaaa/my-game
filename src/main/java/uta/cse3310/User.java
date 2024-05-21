package uta.cse3310;

public class User {
    public String name;
    // 0 outside of a game
    public int color;
    public int currentGameScore;
    public int totalScore;
    // null when user is disconnected
    public transient Connection socket;
    // null when not playing a game
    public transient Game currentGame;
    public transient Point selectedPoint;

    public void addToCurrentScore(int score){
        currentGameScore += score;
    }
    public void addGameScoreToTotalScore() {
        totalScore += currentGameScore;
        currentGameScore = 0;
    }
    public void setColor(int color){
        this.color = color;
    }
}
