package TS;


import STATS.Announcement;
import RDG.ReservationFinder;
import RDG.Reservation;
import RDG.CopyFinder;
import RDG.Copy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    */
    public static List<Announcement> manageReservations() throws SQLException
    {
        List<Announcement> res = new ArrayList();
        ReservationFinder rf = ReservationFinder.getINSTANCE();
        for(Reservation r:rf.findAllActiveReservationsWithUndeliveredCopies())
        {
            Copy c = CopyFinder.getINSTANCE().findById(r.getCopyId());
            // magicky presun
            c.setInLibrary(true);
            c.update();
            Announcement a = new Announcement();
            a.setReaderId(r.getReaderId());
            a.setCopyId(r.getCopyId());
            res.add(a);
            
        }
        return res;
    }
    
    /**
    * send all returned copies to stocks
     * @throws java.sql.SQLException
    */
    public static void manageReturned() throws SQLException 
    {
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
}
