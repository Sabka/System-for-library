import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author sabinka
 */

public class Category 
{
    private int id;
    private String catName;
    private int period;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }


    /**
     * print all book categories
     */
    public static void findAll() throws SQLException
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM book_categories")) 
        {
            try (ResultSet r = s.executeQuery()) 
            {
                while(r.next())
                {
                    Category c = new Category();
                    c.setId(r.getInt("id"));
                    c.setCatName(r.getString("cat_name"));
                    c.setPeriod(r.getInt("period"));
                    System.out.println(c);
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", catName=" + catName + ", period=" + period + '}';
    }
}
