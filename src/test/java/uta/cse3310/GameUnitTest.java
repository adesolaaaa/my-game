package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.ArrayList;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class GameUnitTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GameUnitTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GameUnitTest.class );
    }

    //Testing a player selecting a point
    public void testGame1()
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
        Point point = new Point();
        point.x = 2;
        point.y = 2;

        testGame.input( testPlayers.get(0), point);
        testGame.input( testPlayers.get(0), point);

        assertTrue(testPlayers.get(0).selectedPoint == null);
    }
}