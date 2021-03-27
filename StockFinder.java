
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class StockFinder 
{
    private static final StockFinder INSTANCE = new StockFinder();

    public static StockFinder getINSTANCE() {
        return INSTANCE;
    }
 
    private StockFinder(){}
    
    public Stock findById(int id) throws SQLException {

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM stocks WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Stock b = new Stock();

                    b.setId(r.getInt("id"));
                    b.setAdress(r.getString("adress"));
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
    
    public List<Stock> findAll() throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM stocks")) {
            try (ResultSet r = s.executeQuery()) {

                List<Stock> elements = new ArrayList<>();

                while (r.next()) {
                    Stock b = new Stock();

                    b.setId(r.getInt("id"));
                    b.setAdress(r.getString("adress"));
                    elements.add(b);
                }

                return elements;
            }
        }
    }
    
}
