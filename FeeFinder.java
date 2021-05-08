package RDG;


import MAIN.DBContext;
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
     * @return instance of found fee, null if fee with entered id does not exists
     * @throws java.sql.SQLException
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
                    f.setDelay(r.getInt("delay"));
                    
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
    
    
    /**
    * find all active fees of reader
    * @param rId id of a reader
     * @return list of fees
     * @throws java.sql.SQLException
    */
    public List<Fee> findUnpayedByReaderID(int rId) throws SQLException
    {
        List<Fee> res = new ArrayList();
        
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM fees where reader_id = ? and closed != true and amount > 0.0")) 
        {
            s.setInt(1, rId);

            try (ResultSet r = s.executeQuery()) 
            {
                while(r.next()) 
                {
                    Fee f = new Fee();

                    f.setId(r.getInt("id"));
                    f.setReaderId(r.getInt("reader_id"));
                    f.setAmount(r.getDouble("amount"));
                    f.setClosed(r.getBoolean("closed"));
                    f.setDelay(r.getInt("delay"));
                    
                    res.add(f);
                }
            }
        }
        return res;
    }
    
    /**
    * find all fees
    * 
     * @return list of fees
     * @throws java.sql.SQLException
    */
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
                    f.setAmount(r.getDouble("amount"));
                    f.setClosed(r.getBoolean("closed"));
                    f.setDelay(r.getInt("delay"));
                    
                    res.add(f);
                }
            }
        }
        return res;
    }

    
}
