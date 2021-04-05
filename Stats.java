
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    
    
}
