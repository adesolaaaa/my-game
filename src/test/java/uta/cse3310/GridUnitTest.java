package uta.cse3310;

import java.util.HashMap;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.time.Duration;
import java.time.Instant;

public class GridUnitTest extends TestCase
{
    private Grid grid;//10*10
    private Grid testgrid;//20*20
    //public ArrayList<WordLocation> wordIndices = new ArrayList<>();
    private static long seed = 1234567L;// this is the seed that is used but it 
    //is not set from the test and it is in the grid class itself
    public static double maxDensity = 0.75;  //  maximum density (68%)
    public double timeToCreate;
    public double uniformity = -1;   // -1 because the value can be zero if the grid has a perfect distrobution
    public double density;
    public int intersections;
    public int totalCells = 400;


    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GridUnitTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GridUnitTest.class );
    }


    protected void  setUp()
    {
        Grid.useSeed = true;
        Grid.debugFill = false;
        grid = Grid.createGrid(10, 10);
    }

    // simple test to make sure the grid is filled with spaces because this 
    // that is important for our fill grid and place word method
    public void testcreateGrid(){
    
        testgrid = Grid.createGrid(20, 20);
        //check col and row lenght to make sure it 20*20 
        assertTrue(testgrid.grid[0].length == 20);  
        assertTrue(testgrid.grid.length == 20);
        //make sure each space is filled with a space before any words are put in the grid
        for (int i = 0; i < testgrid.grid.length; i++) {
            for (int j = 0; j < testgrid.grid[i].length; j++) {
                assertTrue(testgrid.grid[i][j].letter == ' ');
            }
        }
    }

    // we test the add word beause the placeword and canplaceword are prvate methods
    //problems should propagrate down to here
    public void testaddWord(){
        //test if the addword will put a word that doesnt fit the grid in 
        assertNull(grid.addWord("thisisasupperlongwordsoitcantfit"));

        //should beable to put the word hello in the grid
        assertNotNull(grid.addWord("hello"));

        //make sure words are put in the indicies when a word is added
        assertNotNull(grid.wordIndices);
        assertFalse(grid.wordIndices.isEmpty());

        //with the seed we have this is the placement of the word hello in the grid
        //the hello will be in the same place everytime so we can look for specific points on the grid
        Point expectedstartpoint = new Point(3,6);
        Point expectedendpoint = new Point(7,2);
        
        boolean foundHello = false;
        for (WordLocation location : grid.wordIndices) {
            if ("hello".equals(location.word) &&
                expectedstartpoint.equals(location.start) &&
                expectedendpoint.equals(location.end)) {
                foundHello = true;
                break;
            }
        }
        // the word hello should be in the wordloaction and the points that its at
        assertTrue(foundHello);
        //check if the expected word and the word returned by the checkStartEnd is correct
        WordLocation expectedlocation  = new WordLocation("hello", expectedstartpoint, expectedendpoint);
        WordLocation result = grid.checkStartEnd(expectedstartpoint, expectedendpoint);
        assertEquals(expectedlocation.word, result.word);
        assertTrue(expectedstartpoint.equals(result.start));
        assertTrue(expectedendpoint.equals(result.end));
    }

    public void testfillEmptrySpaces(){
        grid.addWord("hello");
        grid.fillEmptySpaces();
        //make sure we have no spaces int he grid that are emptry or '_'
        for (int i = 0; i < grid.grid.length; i++) {
            for (int j = 0; j < grid.grid[i].length; j++) {
                assertTrue(grid.grid[i][j].letter != ' ' || grid.grid[i][j].letter != '_' );
            }
        }
        //make sure it has all 26 letters atleast once using the hashmap
        HashMap<Character, Integer> frequencyMap = grid.countCharacterFrequencies();
        assertEquals(26, frequencyMap.size());

    }

    public void test1fullplacement(){
        grid = Grid.createGrid(20, 20);
        int placedWordsCount = 0;
        int filledCells = 0;
        double timeToCreate = 0;
        var startingTime = Instant.now();
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

        //make sure the grid is filled to the right density
        assertTrue((double)filledCells / totalCells > maxDensity);
        //make sure the grid is made in under 1 second
        timeToCreate = Duration.between(startingTime, Instant.now()).toNanos() / 1e9d;
        assertTrue(timeToCreate<1);
        //the word list must have atleast 10 words
        assertTrue(grid.wordIndices.size()>10);
        assertTrue(placedWordsCount>10);


    }
    public void test2fullplacement(){
        grid = Grid.createGrid(20, 20);
        int placedWordsCount = 0;
        int filledCells = 0;
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
        for (var row : grid.grid) {
            for (var item : row) {
                if (item.wordCount > 1) {
                    intersections += item.wordCount;
                }
            }
        }
        //make sure we find intersections when we generate the grid
        assertTrue(intersections>10);

        //test if we get 8 different orentations

        

    }

}
