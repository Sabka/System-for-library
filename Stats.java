
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sabinka
 */
public class Stats 
{
    public static boolean isTodayAvail(Book b, Timestamp t) throws SQLException
    {
        try(PreparedStatement p = DBContext.getConnection().prepareStatement(
                "select count(*) from copies where id = ? and id not in"
                        + "(select copy_id from reservations where date_from <= ? and date_to >= ?) and id not in"
                        + "(select copy_id from reservations where date_from <= ? and date_to >= ?)"))
        {
            p.setInt(1, b.getId());
            p.setTimestamp(2, t);
            p.setTimestamp(3, t);
            p.setTimestamp(4, t);
            p.setTimestamp(5, t);
            try(ResultSet r = p.executeQuery())
            {
                if(r.next())
                {
                    if(r.getInt("count") > 0) return true;
                }
                return false;
                
            }
        }
            
    }
    
    public static int averageAvailDays(Book b, int year) throws SQLException
    {
        int sum = 0;
       
        
        Timestamp t = new Timestamp(year-1900, 0, 1, 0, 0, 0, 0);
        Timestamp nxt;
        for(int month=2; month<=13; month++)
        {
            if(month == 13) nxt = new Timestamp(year+1-1900, 0, 1, 0, 0, 0, 0);
            else nxt = new Timestamp(year-1900, month-1, 1, 0, 0, 0, 0);
            
            while(t.before(nxt))
            {
                //System.out.println(t.toString() + nxt.toString());
                if(isTodayAvail(b, t))
                {
                    sum += 1; // nums[i] += 1
                } 
                t = new Timestamp(t.getTime() + 24*60*60*1000);
            }
        }
        
        return sum/12;
    }
    
    public static void bookAvailability(int year) throws SQLException
    {
        BookFinder bf = BookFinder.getINSTANCE();
        for(Book tmp:bf.findAll())
        {
            System.out.println("copy:" + tmp.getId() + " avgAvaiDays:" + averageAvailDays(tmp, year));
        }
    }
    
    
}
