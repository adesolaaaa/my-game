package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Iterator;

public class AppUnitTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppUnitTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppUnitTest.class );
    }

    //Testing timer for single and multiple games
    public void testApp()
    {
        ArrayList<User> testPlayers = new ArrayList<User>(); 
        User p1 = new User();
        p1.name = "Test Player 1";
        p1.socket = new TestConnection();
        User p2 = new User();
        p2.name = "Test Player 2";
        p2.socket = new TestConnection();

        Game testGame = new Game(testPlayers);

        int port = 9880;

        System.out.println("testGame time: " + testGame.totalGameTime );
        assertTrue(testGame.totalGameTime == 300);
        App testApp = new App(port);
        testApp.activeGames.add(testGame);

        for(Game games: testApp.activeGames)
        {
            games.tick();
            games.tick();
            games.tick();
            games.tick();
            games.tick();
        }

        System.out.println("testGame time: " + testGame.totalGameTime);
        assertTrue(testGame.totalGameTime == 295);

        Game testGame2 = new Game(testPlayers);
        testApp.activeGames.add(testGame2);

        for(Game games: testApp.activeGames)
        {
            games.tick();
            games.tick();
            games.tick();
            games.tick();
            games.tick();
        }

        System.out.println("testGame time: " + testGame.totalGameTime);
        assertTrue(testGame.totalGameTime == 290);
        System.out.println("testGame time: " + testGame2.totalGameTime);
        assertTrue(testGame2.totalGameTime == 295);
    }
}