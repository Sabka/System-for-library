
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author sabinka
 */
class CategoryFinder
{
    private static final CategoryFinder  INSTANCE = new CategoryFinder ();

    public static CategoryFinder  getINSTANCE() {
        return INSTANCE;
    }
 
    private CategoryFinder (){}
    
    /**
     * find and return category with id
    */
    public Category findById(int id) throws SQLException {

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM book_categories WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Category c = new Category();

                    c.setId(r.getInt("id"));
                    c.setCatName(r.getString("cat_name"));
                    c.setPeriod(r.getInt("period"));
                    
                    if (r.next()) {
                        throw new RuntimeException("More than one row was returned");
                    }

                    return c;
                } else {
                    return null;
                }
            }
        }
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
    
    
}
