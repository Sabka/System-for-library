
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author sabinka
 */
public class ReaderFinder 
{
    private static final ReaderFinder INSTANCE = new ReaderFinder();

    public static ReaderFinder getINSTANCE() {
        return INSTANCE;
    }
    
    private ReaderFinder(){}
    
   
    public Reader findById(int id) throws SQLException {

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM readers WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Reader rr = new Reader();

                    rr.setId(r.getInt("id"));
                    rr.setFirstName(r.getString("first_name"));
                    rr.setLastName(r.getString("last_name"));
                    rr.setValidTil(r.getTimestamp("valid_til"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }

                    return rr;
                } else {
                    return null;
                }
            }
        }
    }
    
    public List<Reader> findAll() throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM readers")) {
            try (ResultSet r = s.executeQuery()) {

                List<Reader> elements = new ArrayList<>();

                while (r.next()) {
                    Reader rr = new Reader();

                    rr.setId(r.getInt("id"));
                    rr.setFirstName(r.getString("first_name"));
                    rr.setLastName(r.getString("last_name"));
                    rr.setValidTil(r.getTimestamp("valid_til"));
                    
                    elements.add(rr);
                }

                return elements;
            }
        }
    }
    
    /**
    * hasOpenedFees
    * check whether reader with this id has unpayed fees
    * @throws SQLException
    */
    public boolean hasOpenedFees(int id) throws SQLException
    {
        try(PreparedStatement s = DBContext.getConnection().prepareStatement("select sum(amount) from fees where reader_id = ? and closed  = false"))
        {
            s.setInt(1, id); 
            try (ResultSet r = s.executeQuery())
            {
                r.next();
                if(r.getInt("sum") > 0) return true;
            }   
        }
        return false;
    }
}
