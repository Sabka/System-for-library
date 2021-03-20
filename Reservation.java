
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author sabinka
 */
public class Reservation 
{ 
    private final Integer DEFAULT_PERIOD = 50;
    
    private Integer id;
    private Timestamp dateFrom;
    private Timestamp dateTo;
    private Integer readerId;
    private Integer copyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Timestamp dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Timestamp getDateTo() {
        return dateTo;
    }

    public void setDateTo(Timestamp dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    public Integer getCopyId() {
        return copyId;
    }

    public void setCopyId(Integer copyId) {
        this.copyId = copyId;
    }
    
    public void autosetDateTo() throws SQLException
    {
        int period = 0;
        if(copyId == null || dateFrom == null) return;
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT period FROM book_categories WHERE id = ?"))
        {
            s.setInt(1, CopyFinder.getINSTANCE().findById(copyId).getCategory());
            

            try (ResultSet r = s.executeQuery()) 
            {
                if(r.next())
                {
                   period = r.getInt("period");
                }
                else
                {
                    period = DEFAULT_PERIOD;
                }
            }
        }
        dateTo = new Timestamp(dateFrom.getTime() + (period * 24 * 60 * 60 * 1000));
        
    }
    
    public void insert() throws SQLException 
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO reservations (date_from, date_to, reader_id, copy_id) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) 
        {
            s.setTimestamp(1, dateFrom);
            s.setTimestamp(2, dateTo);
            s.setInt(3, readerId);
            s.setInt(4, copyId);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) 
            {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    
    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE reservations SET date_from = ?, date_to = ?, reader_id = ?, copy_id = ? WHERE id = ?")) {
            s.setTimestamp(1, dateFrom);
            s.setTimestamp(2, dateTo);
            s.setInt(3, readerId);
            s.setInt(4, copyId);
            s.setInt(5, id);

            s.executeUpdate();
        }
    }
    
    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM reservations WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

    @Override
    public String toString() {
        return "Reservation{" + "id=" + id + ", dateFrom=" + dateFrom + ", dateTo=" + dateTo + ", readerId=" + readerId + ", copyId=" + copyId + '}';
    }
    
    
    

    
}
