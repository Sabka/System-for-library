
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author sabinka
 */
public class FeeFinder 
{
    
    private static final FeeFinder INSTANCE = new FeeFinder();

    public static FeeFinder getINSTANCE() {
        return INSTANCE;
    }
    
    
    private FeeFinder(){}

    
    /**
     * find and return fee with id
    * @param id id of a copy
    */
    public Fee findById(int id) throws SQLException {

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM fees WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Fee f = new Fee();

                    f.setId(r.getInt("id"));
                    f.setReaderId(r.getInt("reader_id"));
                    f.setAmount(r.getInt("amount"));
                    f.setClosed(r.getBoolean("closed"));
                    
                    if (r.next()) {
                        throw new RuntimeException("More than one row was returned");
                    }

                    return f;
                } else {
                    return null;
                }
            }
        }
    }
    
    public List<Fee> findUnpayedByReaderID(int rId) throws SQLException
    {
        List<Fee> res = new ArrayList();
        
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM fees where reader_id = ? and closed != true")) 
        {
            s.setInt(1, rId);

            try (ResultSet r = s.executeQuery()) 
            {
                while(r.next()) 
                {
                    Fee f = new Fee();

                    f.setId(r.getInt("id"));
                    f.setReaderId(r.getInt("reader_id"));
                    f.setAmount(r.getInt("amount"));
                    f.setClosed(r.getBoolean("closed"));
                    
                    res.add(f);
                }
            }
        }
        return res;
    }
    
    
    public List<Fee> findAll() throws SQLException
    {
        List<Fee> res = new ArrayList();
        
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM fees")) 
        {

            try (ResultSet r = s.executeQuery()) 
            {
                while(r.next()) 
                {
                    Fee f = new Fee();

                    f.setId(r.getInt("id"));
                    f.setReaderId(r.getInt("reader_id"));
                    f.setAmount(r.getInt("amount"));
                    f.setClosed(r.getBoolean("closed"));
                    
                    res.add(f);
                }
            }
        }
        return res;
    }

    
}
