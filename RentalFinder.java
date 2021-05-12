package RDG;


import MAIN.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * @throws java.sql.SQLException - incorrect query
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
    
    /**
     *  find all active rentals of a reader
     * @param rId - reader id
     * @return list of found rentals
     * @throws java.sql.SQLException - incorrect query
     */
    public List<Rental> findReadersActiveRentals(int rId) throws SQLException
    {
        List<Rental> res = new ArrayList();
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM rentals WHERE reader_id = ? and returned is null")) {
            s.setInt(1, rId);

            try (ResultSet r = s.executeQuery()) 
            {
                while (r.next()) 
                {
                    Rental b = new Rental();

                    b.setId(r.getInt("id"));
                    b.setDateFrom(r.getTimestamp("date_from"));
                    b.setDateTo(r.getTimestamp("date_to"));
                    b.setCopyId(r.getInt("copy_id"));
                    b.setReaderId(r.getInt("reader_id"));
                    b.setReturned(r.getTimestamp("returned"));

                    res.add(b);
                }
            }
            
        }
        return res;
    }

    /**
     *  find rental by its id
     * @param rId
     * @return found rental/ null if not exists
     * @throws java.sql.SQLException - incorrect query
     */
    public Rental findById(int rId) throws SQLException 
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM rentals where id = ?")) 
        {
            s.setInt(1, rId);
            try (ResultSet r = s.executeQuery()) 
            {
                if(r.next()) {
                    Rental b = new Rental();

                    b.setId(r.getInt("id"));
                    b.setDateFrom(r.getTimestamp("date_from"));
                    b.setDateTo(r.getTimestamp("date_to"));
                    b.setCopyId(r.getInt("copy_id"));
                    b.setReaderId(r.getInt("reader_id"));
                    b.setReturned(r.getTimestamp("returned"));
                    return b;

                }
                else
                {
                    return null;   
                }
            }
        }
    }

    
    
}
