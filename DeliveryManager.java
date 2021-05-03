package TS;

import MAIN.DBContext;
import STATS.Announcement;
import RDG.ReservationFinder;
import RDG.Reservation;
import RDG.CopyFinder;
import RDG.Copy;
import UI.InputChecker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.postgresql.util.PSQLException;
/**
 *
 * @author sabinka
 */
public class DeliveryManager 
{
    /**
    * send all reserved copies to library
     * @return list of announcements for readers
     * @throws java.sql.SQLException
     * @throws java.io.IOException
    */
    public static List<Announcement> manageReservations() throws SQLException, IOException, Exception
    {
        while(true)
        {
            try
            {
                DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                DBContext.getConnection().setAutoCommit(false);
                
                List<Announcement> res = new ArrayList();
                ReservationFinder rf = ReservationFinder.getINSTANCE();
                for(Reservation r:rf.findAllActiveReservationsWithUndeliveredCopies())
                {
                    
                    // dummy nacitavanie pre ucely testovania serializacie
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    br.readLine();
                    
                    if(!InputChecker.checkCopy(r.getCopyId()))
                    {
                        throw new Exception("[[Announcement for reader " + r.getReaderId()+ " : Something went wrong during the delivery of copy with id "+r.getCopyId() +". Please create a new reservation.]]");
                    }
                    Copy c = CopyFinder.getINSTANCE().findById(r.getCopyId());
                    // magicky presun
                    c.setInLibrary(true);
                    c.update();
                    System.err.println("update");
                    Announcement a = new Announcement();
                    a.setReaderId(r.getReaderId());
                    a.setCopyId(r.getCopyId());
                    res.add(a);

                }
                return res;
                
            }
            catch(PSQLException e)
            {
                // opakuj transakciu, lebo nastala chyba (zrejme vymazana copy)
                System.err.println("opakujem");
            }
            finally
            {
                DBContext.getConnection().commit();
                DBContext.getConnection().setAutoCommit(true);
                
            }
        }
        
    }
    
    /**
    * send all returned copies to stocks
     * @throws java.sql.SQLException
    */
    public static void manageReturned() throws SQLException 
    {
        while(true)
        {
            try
            {
                DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                DBContext.getConnection().setAutoCommit(false);
                
                CopyFinder cf = CopyFinder.getINSTANCE();
                for(Copy tmp_c : cf.findAll())
                {
                    if(tmp_c.isInLibrary())
                    {
                        if(tmp_c.isAvailable())
                        {
                            tmp_c.setInLibrary(false); // magicky presun do skladu
                            tmp_c.update();
                        }
                    }
                }

            }
            catch(PSQLException e)
            {
                // opakuj transakciu, lebo nastala chyba (zrejme vymazana copy)
                System.err.println("opakujem man ret");
            }
            finally
            {
                DBContext.getConnection().commit();
                DBContext.getConnection().setAutoCommit(true);
                
            }
        }

    }
}