
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationFinder 
{
    private static final ReservationFinder  INSTANCE = new ReservationFinder ();

    public static ReservationFinder  getINSTANCE() {
        return INSTANCE;
    }
 
    private ReservationFinder (){}
    
    
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
    
}
