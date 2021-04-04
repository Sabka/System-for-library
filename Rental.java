
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


/**
 *
 * @author sabinka
 */
public class Rental 
{
    Integer id;
    Timestamp dateFrom;
    Timestamp dateTo;
    Timestamp returned;
    Integer readerId;
    Integer copyId;

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

    public Timestamp getReturned() {
        return returned;
    }

    public void setReturned(Timestamp returned) {
        this.returned = returned;
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
    
    /**
    * set dateTo based on book category and dateFrom
    */
    public void autosetDateTo() throws SQLException
    {
        Copy c = CopyFinder.getINSTANCE().findById(copyId);
        if(c.getCategory() == null)
        {
            dateTo = new Timestamp(dateFrom.getTime() + (3 * 24 * 60 * 60 * 1000));
        }
        else
        {
            Category cat = CategoryFinder.getINSTANCE().findById(c.getCategory());
            Timestamp tmp = new Timestamp(dateFrom.getTime());
            for(int i=0; i< cat.getPeriod(); i++)
            {
                tmp = new Timestamp(tmp.getTime()+ (1 * 24 * 60 * 60 * 1000));
            }
            dateTo = new Timestamp(tmp.getTime());
        }
    }
    
    /**
     * Insert new row to table rentals in DB.
     */
    public void insert() throws SQLException
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO rentals (date_from, date_to, reader_id, copy_id, returned) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) 
        {
            s.setTimestamp(1, dateFrom);
            s.setTimestamp(2, dateTo);
            s.setInt(3, readerId);
            s.setInt(4, copyId);
            s.setTimestamp(5, returned);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) 
            {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    /**
     * Update row in table rentals in DB.
     */
    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE rentals SET date_from = ?, date_to = ?, copy_id = ?, reader_id = ?, returned = ? WHERE id = ?")) {
            s.setTimestamp(1, dateFrom);
            s.setTimestamp(2, dateTo);
            s.setInt(3, copyId);
            s.setInt(4, readerId);
            s.setTimestamp(5, returned);
            s.setInt(6, id);
            s.executeUpdate();
        }
    }


    /**
     * Delete row from table rentals in DB.
     */
    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM rentals WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

    @Override
    public String toString() {
        return "Rentals{" + "id=" + id + ", dateFrom=" + dateFrom + ", dateTo=" + dateTo + ", returned=" + returned + ", readerId=" + readerId + ", copyId=" + copyId + '}';
    }
    
    
    
    
    
}
