package STATS;


import MAIN.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author sabinka
 */
public class Stats 
{
    
    /**
    * count bookAvailabilityStats and return list of Stat1Rows with counted stats
     * @param year
     * @return list of stat1rows with info about each book availability in entered year
     * @throws java.sql.SQLException
    */
    public static List<Stat1Row> bookAvailability(int year) throws SQLException
    {
        
        PreparedStatement p = DBContext.getConnection().prepareStatement("select distinct(id), num(id, ?) as avail from books");
        p.setInt(1, year);
        ResultSet r = p.executeQuery();
        
        List<Stat1Row> res = new ArrayList();
        
        while(r.next())
        {
            Stat1Row tmp = new Stat1Row();
            tmp.setBookId(r.getInt("id"));
            tmp.setNumDays(r.getInt("avail"));
            res.add(tmp);
        }
        return res;
    }

    
    /**
    * find difference between real state and state with increased rental periods, increase from interval 1, n
     * @param n
     * @return list of stat2rows with info about delayStats from 1 to entered N
     * @throws java.sql.SQLException 
    */
    public static List<Stats2Row> getDelayStats(int n) throws SQLException 
    {
        List<Stats2Row> res = new ArrayList();
        PreparedStatement p = DBContext.getConnection().prepareStatement("select amount, count(*) from fees group by amount order by amount;");
        ResultSet r = p.executeQuery();
        
        // parse rows
        List<Stats2Row> list = new ArrayList();
        while(r.next())
        {
            Stats2Row tmp = new Stats2Row();
            tmp.setAmount(r.getDouble("amount") * r.getInt("count"));
            tmp.setNumFees(r.getInt("count"));
            switch ((int)r.getDouble("amount")) {
                case 5:
                    tmp.setX(1);
                    break;
                case 10:
                    tmp.setX(7);
                    break;
                default:
                    tmp.setX(30);
                    break; 
            }
            list.add(tmp);
        }
        
        
        // create output
        IntStream.range(1, n+1).forEach(d ->
                { 
                    Stats2Row tmp = new Stats2Row();
                    tmp.setX(d);
                    
                    double am = 0.0;
                    int num = 0;
                    for(Stats2Row l : list)
                    {
                        if(l.x <= d)
                        {
                            am += l.getAmount();
                            num += l.getNumFees();
                        }
                    }
                    
                    tmp.setAmount(am);
                    tmp.setNumFees(num);
                        
                    res.add(tmp);
                });
        
        
        return res;
        
    }

    
}
