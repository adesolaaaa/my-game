package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.ArrayList;

public class GameUnit2Test extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GameUnit2Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GameUnit2Test.class );
    }

    //Testing a player selecting the same point on the grid and different point 
    public void testGame2()
    {
        int port = 9880;
        App testApp = new App(port);
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
        testApp.activeGames.add(testGame);
        Point point = new Point();
        Point point2 = new Point();
        point.x = 2;
        point.y = 2;
        point2.x = 2;
        point2.y = 3;

        //Need to check highlighting of each points on the grid once slected.
        testGame.input( testPlayers.get(0), point);
        testGame.input( testPlayers.get(0), point2);

        assertTrue(testPlayers.get(0).selectedPoint == null);

        testGame.input( testPlayers.get(1), point);
        testGame.input( testPlayers.get(1), point);

        assertTrue(testPlayers.get(0).selectedPoint == null);
    }
}