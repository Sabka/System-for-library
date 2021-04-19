package RDG;


import MAIN.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author sabinka
 */
public class Reader 
{
    private Integer id;
    private String firstName;
    private String lastName;
    private Timestamp validTil;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getValidTil() {
        return validTil;
    }

    public void setValidTil(Timestamp validTil) {
        this.validTil = validTil;
    }


    /**
     * Insert new row to table readers in DB.
     */
    public void insert() throws SQLException
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO readers (first_name, last_name, valid_til) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, firstName);
            s.setString(2, lastName);
            s.setTimestamp(3, validTil);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    /**
     * Update row in table readers in DB.
     */
    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE readers SET first_name = ?, last_name = ?, valid_til = ? WHERE id = ?")) {
            s.setString(1, firstName);
            s.setString(2, lastName);
            s.setTimestamp(3, validTil);
            s.setInt(4, id);

            s.executeUpdate();
        }
    }

    /**
     * Delete row from table books in DB.
     */
    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM readers WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

    @Override
    public String toString() {
        return "Reader{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", validTil=" + validTil + '}';
    }
    
    
}
