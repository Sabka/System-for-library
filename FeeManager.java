package TS;


import MAIN.DBContext;
import RDG.Fee;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
     * @throws java.sql.SQLException - incorrect query
     * @throws Exception - message for user
    */
    public static List<FeeAnnouncement> feesForNotReturnedCopies(Timestamp date) throws SQLException, Exception
    {
        for(int i=0; i< 10; i++)
        {
            try
            {
                DBContext.getConnection().setAutoCommit(false);
                DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                // v zadani je ze predpokladame, ze operacia je spustena na konci dna
                // to je neskor ako posledna moznost knihu v dany den vratit
                // takze pocas transakcie predpokladam, ze sa uz data nemenia, 
                // lebo pracuje s datami z minulosti na zaklade ich datumu

                List<FeeAnnouncement> res = new ArrayList();
                PreparedStatement s = DBContext.getConnection().prepareStatement
                ("select reader_id, copy_id, id, 5 as amount, "
                        + "extract('day' from case when returned is null then now() else returned end - date_to) as delay  "
                        + "from rentals  where (returned is null and date_to between ?  and ? ) "
                        + "or (returned is not null and returned > date_to and extract('day' from ? - date_to)::int = 0)"
                        
                        + "union "
                        
                        + "select reader_id, copy_id, id, 10 as amount, "
                        + "extract('day' from case when returned is null then now() else returned end - date_to) as delay "
                        + "from rentals  where (returned is null and date_to between ? and ?) "
                        + "or (returned is not null and returned > date_to and extract('day' from ? - date_to)::int = 6)"
                        
                        
                        + "union "
                        
                        
                        + "select reader_id, copy_id, id, 20 as amount, "
                        + "extract('day' from case when returned is null then now() else returned end - date_to) as delay "
                        + "from rentals  where (returned is null and date_to between ? and ?) "
                        + "or (returned is not null and returned > date_to and extract('day' from ? - date_to)::int = 29)"
             
                );


                    s.setTimestamp(1, getEarlierTimestamp(date, 1));
                    s.setTimestamp(2, date);
                    s.setTimestamp(3, date);
                    
                    s.setTimestamp(4, getEarlierTimestamp(date, 7));
                    s.setTimestamp(5, getEarlierTimestamp(date, 6));
                    s.setTimestamp(6, getEarlierTimestamp(date, 6));
                    
                    s.setTimestamp(7, getEarlierTimestamp(date, 30));
                    s.setTimestamp(8, getEarlierTimestamp(date, 29));
                    s.setTimestamp(9, getEarlierTimestamp(date, 29));
                    

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
        throw new Exception("Something went wrong, please try again.");
       
    }
    
    /**
    * transaction for paying entered fees
     * @param fees - get list of fees to become payed
     * @param rID - readers id - to prevent wrong fee ids
     * @throws java.sql.SQLException - incorrect query
     * @throws Exception - message for user
    */
    public static void payFees(List<Fee> fees, int rID) throws SQLException, Exception
    {
        // podla mna to nema zmysel opakovat, ked sa nieco zmeni, lebo tym sa zrejme 
        // zmeni aj suma ktoru ma reader zaplatit, 
        // a teda opakovanie by bolo bud neuspesne alebo
        // nekonzistentne s touto sumou
        try
        {
            DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            // keby na 2 miestach platil reader rovnaky fee, tak nechceme, aby obe zbehli
            DBContext.getConnection().setAutoCommit(false);
            for(Fee f:fees)
            {
                //dummy nacitavanie pre ucely testovania serializacie
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                br.readLine();
                if(f.getReaderId() != rID) throw new Exception("fee " + f.getId() + "does not belogs to reader" + rID);
                if(f.isClosed()) throw new Exception("fee " + f.getId() + "has been already payed. Run oparation again.");
                f.pay();
            }
            
        }
        catch(PSQLException e)
        {
            throw new Exception("Something went wrong, please try again.");
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
     * @throws java.lang.Exception - message for user
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
