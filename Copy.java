
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Copy 
{
    
    private final Double CRITICAL_STATE = 50.0;
    
    private Integer id;
    private Integer bookId;
    private Double state;
    private boolean availableDistantly;
    private boolean inLibrary;
    private Integer stockId; 
    private Integer category;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getState() {
        return state;
    }

    public void setState(Double state) {
        this.state = state;
    }
    
    public boolean isAvailableDistantly() {
        return availableDistantly;
    }

    public void setAvailableDistantly(boolean availableDistantly) {
        this.availableDistantly = availableDistantly;
    }

    public boolean isInLibrary() {
        return inLibrary;
    }

    public void setInLibrary(boolean inLibrary) {
        this.inLibrary = inLibrary;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
    
    
    
    public boolean isAvailable() throws SQLException
    {
        int cnt = 0;
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT count(id) FROM copies WHERE id = ? and state > ? and id not in (select copy_id from rentals where returned is null) and id not in (select copy_id from reservations)")) 
        {
            s.setInt(1, id);
            s.setDouble(2, CRITICAL_STATE);
            try (ResultSet r = s.executeQuery()) 
            {
                r.next();
                cnt = r.getInt(1);
            }
        }
        return cnt > 0;
    }

    public void insert() throws SQLException 
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO copies (book_id, state, available_distantly, in_library, stock_id) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, bookId);
            s.setDouble(2, state);
            s.setBoolean(3, availableDistantly);
            s.setBoolean(4, inLibrary);
            s.setInt(5, stockId);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    
    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }
        
        if(category == null)
        {
            try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE copies SET book_id = ?, state = ?, available_distantly = ?, in_library = ? , stock_id = ?, category = NULL WHERE id = ?")) 
            {
                s.setInt(1, bookId);
                s.setDouble(2, state);
                s.setBoolean(3, availableDistantly);
                s.setBoolean(4, inLibrary);
                s.setInt(5, stockId);
                s.setInt(6, id);

                s.executeUpdate();
            }
            
        }
        else
        {
            try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE copies SET book_id = ?, state = ?, available_distantly = ?, in_library = ? , stock_id = ?, category = ? WHERE id = ?")) 
            {
                s.setInt(1, bookId);
                s.setDouble(2, state);
                s.setBoolean(3, availableDistantly);
                s.setBoolean(4, inLibrary);
                s.setInt(5, stockId);
                s.setInt(6, category);
                s.setInt(7, id);

                s.executeUpdate();
            }
            
        }
        
    }
    
    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM copies WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

    @Override
    public String toString() {
        return "Copy{" + "id=" + id + ", bookId=" + bookId + ", state=" + state + ", availableDistantly=" + availableDistantly + ", inLibrary=" + inLibrary + ", stockId=" + stockId + ", category=" + (category==0?"null":category) + '}';
    }

    
    
    
    
    
    
}
