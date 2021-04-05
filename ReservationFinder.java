
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReservationFinder 
{
    private static final ReservationFinder  INSTANCE = new ReservationFinder ();

    public static ReservationFinder  getINSTANCE() {
        return INSTANCE;
    }
 
    private ReservationFinder (){}


    /**
     *  find all reservations in DB
     * @return list of found reservations
     */
    public List<Reservation> findAll() throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM reservations")) {
            try (ResultSet r = s.executeQuery()) {

                List<Reservation> elements = new ArrayList<>();

                while (r.next()) {
                    Reservation b = new Reservation();

                    b.setId(r.getInt("id"));
                    b.setDateFrom(r.getTimestamp("date_from"));
                    b.setDateTo(r.getTimestamp("date_to"));
                    b.setCopyId(r.getInt("copy_id"));
                    b.setReaderId(r.getInt("reader_id"));
                    b.setRented(r.getBoolean("rented"));

                    elements.add(b);
                }

                return elements;
            }
        }
    }
    
    /**
    * find active reservation of reader
    */
    public List<Reservation> findReadersActiveReservations(int rId) throws SQLException 
    {
        List<Reservation> res = new ArrayList();
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM reservations WHERE reader_id = ? and rented = false and date_to >= ?")) {
            s.setInt(1, rId);
            s.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            Boolean nejake = false;
            try (ResultSet r = s.executeQuery()) 
            {
                while (r.next()) 
                {
                    nejake = true;
                    Reservation b = new Reservation();

                    b.setId(r.getInt("id"));
                    b.setDateFrom(r.getTimestamp("date_from"));
                    b.setDateTo(r.getTimestamp("date_to"));
                    b.setCopyId(r.getInt("copy_id"));
                    b.setReaderId(r.getInt("reader_id"));
                    b.setRented(r.getBoolean("rented"));

                    res.add(b);
                }
            }
            
        }
        return res;
    }
    
    /**
     *  find reservation by id
     */
    public Reservation findById(int id) throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM reservations where id = ?"))
        {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()) {


                if(r.next()) {
                    Reservation b = new Reservation();

                    b.setId(r.getInt("id"));
                    b.setDateFrom(r.getTimestamp("date_from"));
                    b.setDateTo(r.getTimestamp("date_to"));
                    b.setCopyId(r.getInt("copy_id"));
                    b.setReaderId(r.getInt("reader_id"));
                    b.setRented(r.getBoolean("rented"));
                    return b;

                }

                return null;
               
            }
        }
    }
}
