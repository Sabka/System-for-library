
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 *
 * @author sabinka
 */
public class Stats 
{
    
    /**
    * count bookAvailabilityStats and return list of Stat1Rows with counted stats
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

    static List<Stats2Row> getDelayStats(int n) throws SQLException 
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
            System.out.println(r.getDouble("amount"));
            if((int)r.getDouble("amount") == 5)
            {
                tmp.setX(1);
            }
            else if((int)r.getDouble("amount") == 10)
            {
                tmp.setX(7);
            }
            else
            {
               tmp.setX(30); 
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
