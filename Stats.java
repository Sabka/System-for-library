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
        
        PreparedStatement p = DBContext.getConnection().prepareStatement(
                "select book_id, (365 - count(distinct d))/12.0 as avail from\n" +
                "\n" +
                "(\n" +
                "    select id, date_from, date_to, copy_id from reservations\n" +
                "    union\n" +
                "    select id, date_from, case when returned is null then date_to else returned end as date_to, copy_id from rentals\n" +
                ") as data\n" +
                "join generate_series(\n" +
                "\n" +
                "    TIMESTAMP '2000-01-01 00:00:00'+ INTERVAL '1 year'*(?-2000),  -- 2021 za vstupny year\n" +
                "    TIMESTAMP '2000-12-31 00:00:00'+ INTERVAL '1 year'*(?-2000), \n" +
                "    INTERVAL '1 day') as seq(d)\n" +
                "\n" +
                "on d between date_from and date_to\n" +
                "\n" +
                "join copies c \n" +
                "on copy_id = c.id\n" +
                "\n" +
                "group by book_id\n" +
                "limit 100"
                );
        p.setInt(1, year);
        p.setInt(2, year);
        ResultSet r = p.executeQuery();
        
        List<Stat1Row> res = new ArrayList();
        
        while(r.next())
        {
            Stat1Row tmp = new Stat1Row();
            tmp.setBookId(r.getInt("book_id"));
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
        
        PreparedStatement p = DBContext.getConnection().prepareStatement(
                "select i, count(id), sum(amount) from fees \n" +
                "right join generate_series(1, ?) as seq(i) \n" +
                "on delay is not null and delay > i \n" +
                "group by i\n" +
                "order by i");
        p.setInt(1, n);
        ResultSet r = p.executeQuery();
        
        List<Stats2Row> list = new ArrayList();
        while(r.next())
        {
            Stats2Row tmp = new Stats2Row();
            tmp.setAmount(r.getDouble("sum"));
            tmp.setNumFees(r.getInt("count"));
            tmp.setX(r.getInt("i"));
            list.add(tmp);
        }
        
        return list;
        
    }

    
}
