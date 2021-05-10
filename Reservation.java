package RDG;


import MAIN.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

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
    private boolean rented;

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }
    
    
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
    
    /**
     * set dateTo based on dateFrom, dateTo = dateFrom + 3 days
     * @throws java.sql.SQLException
     */
    public void autosetDateTo() throws SQLException
    {
        dateTo = new Timestamp(dateFrom.getTime() + (3 * 24 * 60 * 60 * 1000));
    }

    /**
     * Insert new row to table reservations in DB.
     * @throws java.sql.SQLException
     */
    public void insert() throws SQLException
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO reservations (date_from, date_to, reader_id, copy_id, rented) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) 
        {
            s.setTimestamp(1, dateFrom);
            s.setTimestamp(2, dateTo);
            s.setInt(3, readerId);
            s.setInt(4, copyId);
            s.setBoolean(5, rented);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) 
            {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    /**
     * Update row in table reservations in DB.
     * @throws java.sql.SQLException
     */
    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE reservations SET date_from = ?, date_to = ?, copy_id = ?, reader_id = ?, rented = ? WHERE id = ?")) {
            s.setTimestamp(1, dateFrom);
            s.setTimestamp(2, dateTo);
            
            if(copyId == 0 || copyId == null) s.setNull(3, Types.INTEGER);
            else s.setInt(3, copyId);
            
            if(readerId == 0 || readerId == null) s.setNull(4, Types.INTEGER);
            else s.setInt(4, readerId);
            
            s.setBoolean(5, rented);
            s.setInt(6, id);
            s.executeUpdate();
        }
    }


    /**
     * Delete row from table reservations in DB.
     * @throws java.sql.SQLException
     */
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
        return "Reservation{" + "DEFAULT_PERIOD=" + DEFAULT_PERIOD + ", id=" + id + ", dateFrom=" + dateFrom + ", dateTo=" + dateTo + ", readerId=" + readerId + ", copyId=" + copyId + ", rented=" + rented + '}';
    }
}
