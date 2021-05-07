package TS;


import MAIN.DBContext;
import RDG.Fee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.postgresql.util.PSQLException;


/**
 *
 * @author sabinka
 */
public class FeeManager 
{
    /**
    * create new timestamp which is entered days earlier than entered timestamp
     * @param t - old timestamp
     * @param days - number of days
     * @return created timestamp
    */
    public static Timestamp getEarlierTimestamp(Timestamp t, int days)
    {
        Timestamp tmp = new Timestamp(t.getTime());
        for(int i=0; i<days; i++)
        {
            tmp = new Timestamp(tmp.getTime() - 24*60*60*1000);
        }
        return tmp;
    }
    
    /**
    * create fees for readers who hadn't returned rented books in time 
     * @param date - date of check
     * @return list of announcements for readers
     * @throws java.sql.SQLException
    */
    public static List<FeeAnnouncement> feesForNotReturnedCopies(Timestamp date) throws SQLException
    {
        while(true)
        {
            try
            {
                DBContext.getConnection().setAutoCommit(false);
                DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

                List<FeeAnnouncement> res = new ArrayList();
                PreparedStatement s = DBContext.getConnection().prepareStatement
                ("select reader_id, copy_id, id,  5 as amount, 1 as delay from rentals  where returned is null and date_to "
                        + "between ?  and ? "
                        + "union "
                        + "select reader_id, copy_id,  id, 10 as amount, 7 as delay from rentals  where returned is null and date_to "
                        + "between ? and ? "
                        + "union\n"
                        + "select reader_id, copy_id,  id, 20 as amount, 30 as delay from rentals  where returned is null and date_to "
                        + "between ?  and ?");


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
                            f.setDelay(r.getInt("delay"));
                            
                            f.insert();

                            res.add(new FeeAnnouncement(r.getInt("reader_id"), r.getInt("copy_id"), r.getInt("id"), r.getInt("amount")));

                        }
                    }
                    return res;
            }
            catch(PSQLException e)
            {
                // opakuj transakciu
            }
            finally
            {
                DBContext.getConnection().commit();
                DBContext.getConnection().setAutoCommit(true);
            }
        }   
    }
    
    /**
    * transaction for paying entered fees
     * @param fees - get list of fees to become payed
     * @throws java.sql.SQLException
    */
    public static void payFees(List<Fee> fees) throws SQLException
    {
        try
        {
            DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            DBContext.getConnection().setAutoCommit(false);
            for(Fee f:fees) f.pay();
            
        }
        finally
        {
            DBContext.getConnection().commit();
            DBContext.getConnection().setAutoCommit(true);
        }
    }
    
    /**
    * count summary amount of fees reader want to pay
     * @param readersFees - readers actual fees
     * @return sum
     * @throws java.lang.Exception
    */
    public static double countSum(List<Fee> readersFees) throws Exception
    {
        double sum = 0;
        for(Fee f: readersFees) sum += f.getAmount();
        if(sum == 0) throw new Exception("No valid ids were entered.");
        return sum;
    }
    
    /**
    * find only valid fees from entered fees
     * @param readersFees - readers actual fees
     * @param fIds - list of fee ids which had been entered
     * @return list of valid fees
    */
    public static List<Fee> validFees(List<Fee> readersFees, List<Integer> fIds)
    {
        return readersFees.stream().filter(f -> fIds.contains(f.getId())).collect(Collectors.toList());
    }
    
}
