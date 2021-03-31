
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
    * print stat about Book availability in this year, for each book in DB
    */
    public static void bookAvailability(int year) throws SQLException
    {
        
        PreparedStatement p = DBContext.getConnection().prepareStatement("select distinct(id), num(id) as avail from books");
        ResultSet r = p.executeQuery();
        while(r.next())
        {
            System.out.println("book:" + r.getInt("id") + " avgAvaiDays:" + r.getInt("avail")/12);
            
        }
        
    
    }
    
    
}
