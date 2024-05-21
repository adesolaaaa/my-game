package uta.cse3310;

import java.util.ArrayList;

import uta.cse3310.Events.EventHolder;

import java.time.Duration;
import java.time.Instant;

public class Game {
    public ArrayList<User> players;
    public Grid grid;
    public transient int totalGameTime = 300; //Timer set for 5 minutes
    public boolean gameOver = false;
    public static int gridSize = 20; 
    public static int totalCells = gridSize * gridSize;
    public static int wordCountLimit = 10; // max total word in the grid or 60% whichever is first
    

    //Stats to be displayed for the user
    public static double maxDensity = 0.75;  //  maximum density (68%)
    public double timeToCreate;
    public double uniformity = -1;   // -1 because the value can be zero if the grid has a perfect distrobution
    public double density;
    public int intersections;
    //Can also maybe include the number directions the words are placed in.

    public Game(ArrayList<User> lobby)
    {
        var startingTime = Instant.now();
        int placedWordsCount = 0;
        int filledCells = 0;
        players = new ArrayList<User>(lobby);

        //Set colors
        for(int i = 0; i < players.size(); i++)
        {
            var player = players.get(i);
            player.setColor(i);
            player.currentGame = this;
            player.socket.send(App.gson.toJson(new EventHolder<>("UserResponse", player)));
        }

        grid = Grid.createGrid(gridSize, gridSize);

        while (true) {
            if ((double)filledCells / totalCells > maxDensity) {
                break;  // Stop adding words if limit or max density is reached
            }

            var word = App.wordsfromfile.get(grid.random.nextInt(App.wordsfromfile.size()));
            for (var alreadyPicked : grid.wordIndices) {
                if (word.equals(alreadyPicked.word)) {
                    continue;
                }
            }

            var added = grid.addWord(word);
            if (added != null) {
                placedWordsCount++;
                for (var item : added.letters) {
                    if (item.wordCount == 1) {
                        filledCells++;
                    }
                }
            } else {
                System.out.println("Failed to add word: " + word);
            }
        }
        
        grid.fillEmptySpaces();
        timeToCreate = Duration.between(startingTime, Instant.now()).toNanos() / 1e9d;
        System.out.println(timeToCreate);
        uniformity = grid.calculatechisquared();
        density = (double)filledCells / totalCells;
        for (var row : grid.grid) {
            for (var item : row) {
                if (item.wordCount > 1) {
                    intersections += item.wordCount;
                }
            }
        }
        System.out.println(uniformity);
        grid.printGrid();
    }

    public void gameOver() {
        //Add scores from game to each Users totalScore. Signal gameOver for App to remove the game.
        for(User temp: players)
        {
            temp.addGameScoreToTotalScore();
            temp.currentGame = null;
        }
        gameOver = true;
        sendUpdate();
        var gameOverEvent = App.gson.toJson(new EventHolder<>("GameOverResponse", null));
        for (var user : players) {
            user.socket.send(gameOverEvent);
        }
    }

    public void displayHint() {
        //Chooses random number between 0 to grid.wordList.length
        WordLocation random = grid.wordIndices.get(grid.random.nextInt(grid.wordIndices.size()));
        GridItem checkSelected = grid.grid[random.start.x][random.start.y];

        //Highlights start of a word. Might need to check if the word is currently selected by to get a unique word not selected by a player.
        while(checkSelected.foundBy.size() != 0) {
            random = grid.wordIndices.get(grid.random.nextInt(grid.wordIndices.size()));
            checkSelected = grid.grid[random.start.x][random.start.y];
        }
        grid.addSelection(random.start, 4); //4 will be the hint color
        sendUpdate();
    }

    public void validateAttempt(User attempter, Point start, Point end) {
        WordLocation wordFound = grid.checkStartEnd(start , end);

        if(wordFound != null)
        {
            attempter.addToCurrentScore(wordFound.word.length());

            for (var item : wordFound.letters) {
                item.foundBy.add(attempter.color);
            }

            grid.wordIndices.remove(wordFound);
            wordFound.letters.get(0).selectedBy.remove((Object)4);
        }

        //Remove selection from word grid.
        grid.removeSelection(start, attempter.color);
    }

    public void input(User user, Point selection) {
        //Change point in grid to selected/unselected
        System.out.println("Received input from Player " + user.name + " at point " + "(" + selection.x + ", " + selection.y + ")." );

        if(user.selectedPoint != null)
        {
            //point comes from the user. Assumed that the user has already made a selection
            System.out.println("Validating user: " + user.name + " selected points" + "(" + user.selectedPoint.x + ", " + user.selectedPoint.y + ") " + "and " + "(" + selection.x + ", " + selection.y + ").");
            validateAttempt(user, user.selectedPoint, selection);
            user.selectedPoint = null;
        }
        else
        {
            System.out.println("Highlighting point" + "(" + selection.x + ", " + selection.y + ") with the color " + user.color + "." );
            grid.addSelection(selection, user.color);
            user.selectedPoint = selection;
        }
        sendUpdate();
    }

    public void sendUpdate() {
        var event = App.gson.toJson(new EventHolder<>("GameResponse", this));
        for (var user : players) {
            user.socket.send(event);
            user.socket.send(App.gson.toJson(new EventHolder<>("UserResponse", user)));
        }
    }

    //Will be used for one timer that is implemented in App. Might have issue with the game time being negative or what to do with a game when it is finished.
    public void tick() {
        if(totalGameTime <= 0 || grid.checkWordList()) { 
            gameOver();
            return;
        }
        else if(((totalGameTime % 30) == 0) && (totalGameTime != 300)) {
            displayHint();
        }

        var event = App.gson.toJson(new EventHolder<>("TimerResponse", totalGameTime));

        for (var user : players) {
            user.socket.send(event);
        }

        totalGameTime--;
    }
}
