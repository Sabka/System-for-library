
import java.sql.SQLException;
import java.sql.Timestamp;
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
    */
    public static List<Announcement> manageReservations() throws SQLException
    {
        List<Announcement> res = new ArrayList();
        ReservationFinder rf = ReservationFinder.getINSTANCE();
        CopyFinder cf = CopyFinder.getINSTANCE();
        for(Reservation r:rf.findAll())
        {
            if(!r.isRented() && r.getDateTo().after(new Timestamp(System.currentTimeMillis()))) // aktivna rezervacia
            {
                Copy c = cf.findById(r.getCopyId());
                if(!c.isInLibrary())
                {
                    // magicky presun
                    c.setInLibrary(true);
                    c.update();
                    Announcement a = new Announcement();
                    a.setReaderId(r.getReaderId());
                    a.setCopyId(c.getId());
                    //System.out.println("Announcement for reader " + r.getReaderId() + ": Copy with id " + c.getId() + " has been delivered to library." );
                    res.add(a);
                }
                
            }
            
        }
        return res;
    }
    
    /**
    * send all returned copies to stocks
    */
    static void manageReturned() throws SQLException 
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
