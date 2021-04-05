
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author sabinka
 */
public class Fee 
{
    public static final double EUROSPERDECRESEDPERCENT = 1.3;
    private int id;
    private int readerId;
    private double amount;
    private boolean closed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
    
    /**
     * Insert new row to table fees in DB.
     */
    public void insert() throws SQLException
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO fees (reader_id, amount, closed) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, readerId);
            s.setDouble(2, amount);
            s.setBoolean(3, closed);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        } 
    }

    @Override
    public String toString() {
        return "Fee{" + "id=" + id + ", readerId=" + readerId + ", amount=" + amount + ", closed=" + closed + '}';
    }
    
    
    
}
