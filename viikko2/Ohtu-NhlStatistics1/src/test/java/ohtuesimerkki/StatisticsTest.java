package ohtuesimerkki;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author gabriel
 */
public class StatisticsTest
{

    Statistics stats;
    Reader readerStub = new Reader()
    {

        public List<Player> getPlayers()
        {
            ArrayList<Player> players = new ArrayList<Player>();

            players.add(new Player("Semenko", "EDM", 4, 12));
            players.add(new Player("Lemieux", "PIT", 45, 54));
            players.add(new Player("Kurri", "EDM", 37, 53));
            players.add(new Player("Yzerman", "DET", 42, 56));
            players.add(new Player("Gretzky", "EDM", 35, 89));

            return players;
        }
    };
    
    @Before
    public void setUp()
    {
        stats = new Statistics(readerStub);
    }
    
    @Test
    public void playerListContainsKurri()
    {
        Player kurri = stats.search("Kurri");
        assertEquals(kurri.getName(), "Kurri");
        
    }
    
    @Test
    public void nullIsReturnedIfPlayerNotOnList()
    {
        Player selanne = stats.search("Selanne");
        assertNull(selanne);
    }
    
    @Test
    public void numberOfPlayersInTeamIsCorrect()
    {
        List<Player> team = stats.team("EDM");
        assertEquals(team.size(), 3);
    }
    
    @Test
    public void pointsLeaderIsCorrect()
    {
        List<Player> pointsLeaders = stats.topScorers(1);
        assertEquals(pointsLeaders.get(0).getName(), "Gretzky");
    }
    
    @Test
    public void negativeArgumentReturnsEmptyTopScorerList()
    {
        List<Player> pointsLeaders = stats.topScorers(-1);
        assertEquals(pointsLeaders.size(), 0);
    }

}
