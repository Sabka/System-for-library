
import java.sql.SQLException;
import java.sql.Timestamp;
/**
 *
 * @author sabinka
 */
public class DeliveryManager 
{
    /**
    * manageReservations
    * send all reserved copies to library
    * @throws SQLException
    */
    public static void manageReservations() throws SQLException
    {
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
                    System.out.println("Announcement for reader " + r.getReaderId() + ": Copy with id " + c.getId() + " has been delivered to library." );
                }
                
            }
            
        }
    }
    
    public static void manageReturned() throws SQLException
    {
        
    }
    
    
    
}
