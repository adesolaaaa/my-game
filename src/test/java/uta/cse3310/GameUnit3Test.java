package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.ArrayList;

public class GameUnit3Test extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GameUnit3Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GameUnit3Test.class );
    }

    //Testing adding to a players score on gameover
    public void testGame3()
    {
        ArrayList<User> testPlayers = new ArrayList<User>(); 
        User p1 = new User();
        p1.name = "Test Player 1";
        p1.socket = new TestConnection();
        User p2 = new User();
        p2.name = "Test Player 2";
        p2.socket = new TestConnection();

        testPlayers.add(p1);
        testPlayers.add(p2);

        Grid.useSeed = true;
        Game testGame = new Game(testPlayers);
        p1.addToCurrentScore(20);
        p2.addToCurrentScore(10);
        assertTrue(p1.currentGameScore == 20);
        assertTrue(p2.currentGameScore == 10);

        testGame.gameOver();

        assertTrue(testGame.gameOver == true);
        assertTrue(p1.totalScore == 20);
        assertTrue(p2.totalScore == 10);
        
    }
}
