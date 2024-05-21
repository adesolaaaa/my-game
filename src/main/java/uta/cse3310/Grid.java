package uta.cse3310;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.HashMap;

public class Grid {
    public ArrayList<WordLocation> wordIndices = new ArrayList<>();
    private HashMap<String, Integer> directionCounts = new HashMap<>();
    public GridItem[][] grid;

    private static long seed = 1234567L;                    //Generates a consitant for repeatablility of bugs
    public static boolean useSeed = false;
    public static boolean debugFill = false;
    public transient Random random = useSeed ? new Random(seed) : new Random();
    //private Random random = new Random(seed);//

    static {
        var testSeed = System.getenv("TEST_GRID");
        if (testSeed != null)
            seed = Integer.parseInt(testSeed);
        if (System.getenv("USE_SEED") != null)
            useSeed = true;
    }

    private Grid() {}

    public static Grid createGrid(int rowNumber, int columnNumber) {
        var grid = new Grid();
        grid.grid = new GridItem[rowNumber][columnNumber];
        // Initialize each grid item with a space characterx
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                grid.grid[i][j] = new GridItem(' '); 
            }
        }

        return grid;
    }

    public WordLocation addWord(String word) {
        int attempts = 100;  // Limit the number of placement attempts
        while (attempts-- > 0) {
            int row = random.nextInt(grid.length);
            int col = random.nextInt(grid[0].length);
            int horizontal;
            int vertical;

            do {
                horizontal = random.nextInt(3) - 1; // -1, 0, or 1
                vertical = random.nextInt(3) - 1;
            } while (horizontal == 0 && vertical == 0); // both can not be zero

            if (canPlaceWord(word, row, col, horizontal, vertical)) {
                String directionKey = horizontal + "," + vertical;
                directionCounts.put(directionKey, directionCounts.getOrDefault(directionKey, 0) + 1);//count the orentaions 
                var location = new WordLocation(word, new Point(row, col), null);
                placeWord(word, row, col, horizontal, vertical, location);
                wordIndices.add(location);
                return location;
            }
        }
        return null;  
    }

    private void placeWord(String word, int row, int col, int horizontal , int vertical, WordLocation location) {
        //vertical or horizontil are the direction the word should be placed
        for (int i = 0; i < word.length(); i++) {
            int currentCol = col + (i * horizontal);
            int currentRow = row + (i * vertical);
          
            var item = grid[currentRow][currentCol];
            item.wordCount++;
            item.letter = word.charAt(i);
            location.letters.add(item);
            location.end = new Point(currentRow, currentCol);
        }
    }
    
    private boolean canPlaceWord(String word, int row, int col, int horizontal, int vertical) {
        for (int i = 0; i < word.length(); i++) {
            int currentRow = row + (i * vertical);
            int currentCol = col + (i * horizontal);

            // Check bounds for the word we are trying to place
            if (currentRow < 0 || currentRow >= grid.length || currentCol < 0 || currentCol >= grid[0].length) {
                return false;
            }

            //when looking for somewhere to put the word we will check its it empty or the letter is the same as what we need
            if (grid[currentRow][currentCol].letter != ' ' && grid[currentRow][currentCol].letter != word.charAt(i)) {
                return false; 
            }
        }

        return true; // No conflicts the word can be placed
    }

    public void fillEmptySpaces() {
        int counter = 0;
        ArrayList<Character> alphabet = new ArrayList<>();
        //Maybe change character to a class to keep track of different letters being used
        for (char c = 'a'; c <= 'z'; c++) {
            alphabet.add(c);
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].letter == ' ') {  // Assuming ' ' represents an empty space
                    char randomChar = alphabet.get(random.nextInt(20));

                    grid[i][j].letter = randomChar;
                    if(counter++ < 26){
                       Collections.shuffle(alphabet,random);
                       counter = 0 ;
                    }
                    if(debugFill){
                        grid[i][j].letter = '_';  // Fill with _ for debugging
                    }
                }
            }
        }
    }

    // takes all the characters it the grid and puts them in a hash map the key is the letter an th value is the number of occurances
    public HashMap<Character, Integer> countCharacterFrequencies() {
        HashMap<Character, Integer> frequencyMap = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char currentChar = grid[i][j].letter;
                if(currentChar != ' ' || currentChar != '_'){
                    frequencyMap.put(currentChar, frequencyMap.getOrDefault(currentChar, 0) + 1);
                }
            }
        }
        return frequencyMap;
    }

    //This is chi-squared fuction this is not the distrobution of each letter but it is the deveation fron the expected 
    // the expected woudl be the total number of letter / 26 all the letters in the alphabet.
    public double calculatechisquared() {
        HashMap<Character, Integer> actualFrequencies = countCharacterFrequencies();
        double chiSquared = 0.0;
        double expected =  Game.totalCells/26;

        for (char ch = 'a'; ch <= 'z'; ch++) {
            double observed = actualFrequencies.getOrDefault(ch, 0);

            if (expected != 0) {
                chiSquared += Math.pow(observed - expected, 2) / expected;
            }
        }
        System.out.println(actualFrequencies.toString());
        return chiSquared;
    }
    //standard deviation on our word directioncounts
    public double calcstddiveation(HashMap<String, Integer> directionCounts){
        double sum = 0.0;
        for(double values: directionCounts.values() ){
            sum += values;
        }
        double mean = sum / directionCounts.size();

        double stddeveation = 0.0;
        for(double values : directionCounts.values()){
            stddeveation += Math.pow(values - mean, 2);
        }

        return Math.sqrt(stddeveation/directionCounts.size());
    }

    public void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j].letter + " "); 
            }
            System.out.println(); 
        }
        for(WordLocation location : wordIndices){
            System.out.println(location.word);
        }
        System.out.println("\nWord Locations:");
        for (WordLocation location : wordIndices) {
            System.out.println("Word at (" + location.start.x + ", " + location.start.y + 
                               ") to (" + location.end.x + ", " + location.end.y + ")");
        }
        System.out.println("standard deviation"+calcstddiveation(directionCounts));
    }

    public WordLocation checkStartEnd(Point start, Point end) {
        for (int i = 0; i < wordIndices.size(); i++) {
            var word = wordIndices.get(i);
            if (start.equals(word.start) && end.equals(word.end)) {
                return wordIndices.get(i);
            }
        }
        return null;
    }

    public void addSelection(Point point, int color) {
        grid[point.x][point.y].selectedBy.add(color);
    }

    public void removeSelection(Point point, int color) {
        //Get size of the selectedBy arrayList. If the color (number) is in the list remove.
        grid[point.x][point.y].selectedBy.remove((Object)color);
    }

    //Used for gameover seqeucne if result is true.
    public boolean checkWordList() {
        return wordIndices.size() == 0;
    }
    
}
