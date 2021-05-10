package RDG;


import MAIN.DBContext;
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
     * @throws java.sql.SQLException
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
     * @param rId - reader id
     * @return list of active reservations of a reader
     * @throws java.sql.SQLException
    */
    public List<Reservation> findReadersActiveReservations(int rId) throws SQLException 
    {
        List<Reservation> res = new ArrayList();
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM reservations WHERE reader_id = ? and rented = false and date_to >= ?")) {
            s.setInt(1, rId);
            s.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            try (ResultSet r = s.executeQuery()) 
            {
                while (r.next()) 
                {
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
     * find all active reservations whose copies are not in library yet
     * @return list of found reservations
     * @throws java.sql.SQLException
     */
    public List<Reservation> findAllActiveReservationsWithUndeliveredCopies() throws SQLException 
    {
        List<Reservation> res = new ArrayList();
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM reservations r JOIN copies c on c.id = r.copy_id WHERE r.rented = false AND r.date_to >= ? AND c.in_library = false AND r.reader_id is not null")) {
            s.setTimestamp(1, new Timestamp(System.currentTimeMillis()));

            try (ResultSet r = s.executeQuery()) 
            {
                while (r.next()) 
                {
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
     * find reservation by id
     * @param id - id of a reservation
     * @return instance of reservation or null if not exists 
     * @throws java.sql.SQLException
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
