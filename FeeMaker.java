
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
public class FeeMaker 
{
    public static Timestamp getEarlierTimestamp(Timestamp t, int days)
    {
        Timestamp tmp = new Timestamp(t.getTime());
        for(int i=0; i<days; i++)
        {
            tmp = new Timestamp(tmp.getTime() - 24*60*60*1000);
        }
        return tmp;
    }
    
    public static List<FeeAnnouncement> feesForNotReturnedCopies(Timestamp date) throws SQLException
    {
        List<FeeAnnouncement> res = new ArrayList();
        
        DBContext.getConnection().setAutoCommit(false);
        try (PreparedStatement s = DBContext.getConnection().prepareStatement
        ("select reader_id, copy_id, id,  5 as amount from rentals  where returned is null and date_to "
                + "between ?  and ? "
                + "union "
                + "select reader_id, copy_id,  id, 10 as amount from rentals  where returned is null and date_to "
                + "between ? and ? "
                + "union\n"
                + "select reader_id, copy_id,  id, 20 as amount from rentals  where returned is null and date_to "
                + "between ?  and ?")) 
        {
           
            s.setTimestamp(1, getEarlierTimestamp(date, 1));
            s.setTimestamp(2, date);
            s.setTimestamp(3, getEarlierTimestamp(date, 7));
            s.setTimestamp(4, getEarlierTimestamp(date, 6));
            s.setTimestamp(5, getEarlierTimestamp(date, 30));
            s.setTimestamp(6, getEarlierTimestamp(date, 29));
            
            try (ResultSet r = s.executeQuery()) 
            {
                while(r.next())
                {
                    Fee f = new Fee();
                    f.setReaderId(r.getInt("reader_id"));
                    f.setAmount(r.getInt("amount"));
                    f.setClosed(false);
                    f.insert();
                    
                    res.add(new FeeAnnouncement(r.getInt("reader_id"), r.getInt("copy_id"), r.getInt("id"), r.getInt("amount")));
                    
                    
                }
                
            }
        }
        finally
        {
            DBContext.getConnection().commit();
        }
        
        return res;
        
    }
    
}
