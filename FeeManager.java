package TS;


import MAIN.DBContext;
import RDG.Copy;
import RDG.CopyFinder;
import RDG.Fee;
import RDG.ReaderFinder;
import RDG.Rental;
import RDG.Reservation;
import RDG.ReservationFinder;
import UI.InputChecker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 *
 * @author sabinka
 */
public class FeeManager 
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
        
        
        DBContext.getConnection().setAutoCommit(false);
        DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        
        List<FeeAnnouncement> res = new ArrayList();
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
            DBContext.getConnection().setAutoCommit(true);
        }
        
        return res;
        
    }
    
    
    public static void payFees(List<Fee> fees) throws SQLException
    {
        try
        {
            DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            DBContext.getConnection().setAutoCommit(false);
            fees.forEach(f-> 
            {
                f.setClosed(true);
                try {
                    f.update();
                } catch (SQLException ex) {
                    Logger.getLogger(FeeManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
        }
        finally
        {
            DBContext.getConnection().commit();
            DBContext.getConnection().setAutoCommit(true);
        }
    }
    
    public static double countSum(List<Fee> readersFees, List<Integer> fIds) throws Exception
    {
        double sum = 0;
        System.out.println(fIds);
        readersFees = readersFees.stream().filter(f -> fIds.contains(f.getId())).collect(Collectors.toList());
        for(Fee f: readersFees) sum += f.getAmount();
        if(sum == 0) throw new Exception("No valid ids were entered.");
        return sum;
    }
    
    public static List<Fee> validFees(List<Fee> readersFees, List<Integer> fIds)
    {
        return readersFees.stream().filter(f -> fIds.contains(f.getId())).collect(Collectors.toList());
    }
    
}
