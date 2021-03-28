
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    public static List<Boolean> isTodayAvail(Timestamp t) throws SQLException
    {
        
        List<Boolean> res = new ArrayList<>();
        for(int i=0; i<=BookFinder.getINSTANCE().maxIndex(); i++) res.add(false);
        //System.out.println(BookFinder.getINSTANCE().maxIndex() + " " + res.size());
        try(PreparedStatement p = DBContext.getConnection().prepareStatement(
                "select distinct(id), case when (select is_avail_today(id, ?)) > 0 then true else false end as avail from books;"))
        {
            p.setTimestamp(1, t);
            try(ResultSet r = p.executeQuery())
            {
                while(r.next())
                {
                    res.set(r.getInt("id"), r.getBoolean("avail"));   
                }
                
            }
        }
            
        return res;
    }
    
    public static List<Integer> averageAvailDays(int year) throws SQLException
    {
        List<Integer> res = new ArrayList();
        for(int i=0; i<=BookFinder.getINSTANCE().maxIndex(); i++) res.add(0);
        
        Timestamp t = new Timestamp(year-1900, 0, 1, 0, 0, 0, 0);
        Timestamp nxt;
        for(int month=2; month<=13; month++)
        {
            if(month == 13) nxt = new Timestamp(year+1-1900, 0, 1, 0, 0, 0, 0);
            else nxt = new Timestamp(year-1900, month-1, 1, 0, 0, 0, 0);
            
            while(t.before(nxt))
            {
                System.out.println("Spracovavam: " + t);
                List<Boolean> tmp = isTodayAvail(t);
                for(int i=0; i<tmp.size(); i++)
                {
                    res.set(i, res.get(i)+(tmp.get(i)?1:0));
                }
                t = new Timestamp(t.getTime() + 24*60*60*1000);
            }
        }
        
        for(int i=0; i<res.size(); i++)
        {
            res.set(i, res.get(i)/12);
        }
        return res;
    }
    
    public static void bookAvailability(int year) throws SQLException
    {
        /*if(BookFinder.getINSTANCE().maxIndex() < 0)
        {
            System.out.println("No books in DB");
            return;
        }
        List<Integer> avd_res = averageAvailDays(year);
        System.out.println("hello world " + avd_res.size());*/
        
        PreparedStatement p = DBContext.getConnection().prepareStatement("select distinct(id), num(id) as avail from books");
        ResultSet r = p.executeQuery();
        while(r.next())
        {
            System.out.println("book:" + r.getInt("id") + " avgAvaiDays:" + r.getInt("avail")/12);
            
        }
        
        /*for(Book b: BookFinder.getINSTANCE().findAll())
        {
            System.out.println("book:" + b.getId() + " avgAvaiDays:" + avd_res.get(b.getId()));
        }*/
    }
    
    
}
