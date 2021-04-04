
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RentalFinder 
{
    private static final RentalFinder  INSTANCE = new RentalFinder();

    public static RentalFinder  getINSTANCE() {
        return INSTANCE;
    }
 
    private RentalFinder (){}


    /**
     *  find all rentals in DB
     * @return list of found rentals
     */
    public List<Rental> findAll() throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM rentals")) {
            try (ResultSet r = s.executeQuery()) {

                List<Rental> elements = new ArrayList<>();

                while (r.next()) {
                    Rental b = new Rental();

                    b.setId(r.getInt("id"));
                    b.setDateFrom(r.getTimestamp("date_from"));
                    b.setDateTo(r.getTimestamp("date_to"));
                    b.setCopyId(r.getInt("copy_id"));
                    b.setReaderId(r.getInt("reader_id"));
                    b.setReturned(r.getTimestamp("returned"));

                    elements.add(b);
                }

                return elements;
            }
        }
    }
    
    
    
}
