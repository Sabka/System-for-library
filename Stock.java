package RDG;


import MAIN.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Stock 
{
    private Integer id;
    private String adress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }


    /**
     * Insert new row to table stocks in DB.
     */
    public void insert() throws SQLException
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO stocks (adress) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, adress);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    /**
     * Update row in table stocks in DB.
     */
    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE stocks SET adress = ? WHERE id = ?")) {
            s.setString(1, adress);
            s.setInt(2, id);
            s.executeUpdate();
        }
    }

    /**
     * Delete row from table stocks in DB.
     */
    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM stocks WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

    @Override
    public String toString() {
        return "Stock{" + "id=" + id + ", adress=" + adress + '}';
    }

    
    
    
}
