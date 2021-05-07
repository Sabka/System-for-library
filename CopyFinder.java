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
public class CopyFinder 
{
    private static final CopyFinder INSTANCE = new CopyFinder();

    public static CopyFinder getINSTANCE() {
        return INSTANCE;
    }
    
    
    private CopyFinder(){}

    
    /**
     * find and return copy with id
    * @param id id of a copy
    * @return instance of found fee, null if fee with entered id does not exists

     * @throws java.sql.SQLException
    */
    public Copy findById(int id) throws SQLException {

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM copies WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Copy b = new Copy();

                    b.setId(r.getInt("id"));
                    b.setBookId(r.getInt("book_id"));
                    b.setState(r.getDouble("state"));
                    b.setAvailableDistantly(r.getBoolean("available_distantly"));
                    b.setInLibrary(r.getBoolean("in_library"));
                    b.setStockId(r.getInt("stock_id"));
                    b.setCategory(r.getInt("category"));
                    
                    if (r.next()) {
                        throw new RuntimeException("More than one row was returned");
                    }

                    return b;
                } else {
                    return null;
                }
            }
        }
    }
    
    /**
    * find all copies of a book
     * @param inputBookId
    * @return list of all copies of book
     * @throws java.sql.SQLException
    */
    public List<Copy> findCopiesOfBook(int inputBookId) throws SQLException {

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM copies WHERE book_id = ?")) 
        {
            s.setInt(1, inputBookId);
            try (ResultSet r = s.executeQuery()) {

                List<Copy> elements = new ArrayList<>();

                while (r.next()) {
                    Copy b = new Copy();

                    b.setId(r.getInt("id"));
                    b.setBookId(r.getInt("book_id"));
                    b.setState(r.getDouble("state"));
                    b.setAvailableDistantly(r.getBoolean("available_distantly"));
                    b.setInLibrary(r.getBoolean("in_library"));
                    b.setStockId(r.getInt("stock_id"));
                    b.setCategory(r.getInt("category"));
                    elements.add(b);
                }

                return elements;
            }
        }
    }
    
    /**
    * find all copies in DB
    * @return list of all copies
     * @throws java.sql.SQLException
    */
    public List<Copy> findAll() throws SQLException
    {
        List<Copy> res = new ArrayList();
        
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM copies")) 
        {

            try (ResultSet r = s.executeQuery()) 
            {
                while(r.next()) 
                {
                    Copy b = new Copy();

                    b.setId(r.getInt("id"));
                    b.setBookId(r.getInt("book_id"));
                    b.setState(r.getDouble("state"));
                    b.setAvailableDistantly(r.getBoolean("available_distantly"));
                    b.setInLibrary(r.getBoolean("in_library"));
                    b.setStockId(r.getInt("stock_id"));
                    b.setCategory(r.getInt("category"));
                    
                    res.add(b);
                }
            }
        }
        return res;
    }
    
    public List<Copy> findInLib() throws SQLException
            {
        List<Copy> res = new ArrayList();
        
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM copies where in_library = true")) 
        {

            try (ResultSet r = s.executeQuery()) 
            {
                while(r.next()) 
                {
                    Copy b = new Copy();

                    b.setId(r.getInt("id"));
                    b.setBookId(r.getInt("book_id"));
                    b.setState(r.getDouble("state"));
                    b.setAvailableDistantly(r.getBoolean("available_distantly"));
                    b.setInLibrary(r.getBoolean("in_library"));
                    b.setStockId(r.getInt("stock_id"));
                    b.setCategory(r.getInt("category"));
                    
                    res.add(b);
                }
            }
        }
        return res;
    }
}
