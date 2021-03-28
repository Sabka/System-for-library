import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author sabinka
 */
public class BookFinder 
{
    private static final BookFinder INSTANCE = new BookFinder();

    public static BookFinder getINSTANCE() {
        return INSTANCE;
    }
    
    private static boolean checkAvailability(int inputId) throws SQLException
    {
        try(PreparedStatement s = DBContext.getConnection().prepareStatement("select count(id) from copies where book_id = ? and id not in (select copy_id from rentals where returned is null) and id not in (select copy_id from reservations)"))
        {
            s.setInt(1, inputId);
            try(ResultSet r = s.executeQuery())
            {
                r.next();
                return r.getInt(1) > 0;
            }
        }
    }
    
    private BookFinder(){}
    
    public Book findById(int id) throws SQLException {

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM books WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Book b = new Book();

                    b.setId(r.getInt("id"));
                    b.setTitle(r.getString("title"));
                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }

                    return b;
                } else {
                    return null;
                }
            }
        }
    }
    
    public Book findByTitle(String title) throws SQLException {

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM books WHERE title = ?")) {
            s.setString(1, title);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Book b = new Book();

                    b.setId(r.getInt("id"));
                    b.setTitle(r.getString("title"));
                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    System.out.println(((checkAvailability(b.getId())?"":"not ")) + "available " + b);
                    return b;
                } else {
                    return null;
                }
            }
        }
    }
    
    public List<Book> findByAuthor(String authorLastName) throws SQLException {
        
        Integer authorId = 0;

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("select * from books where id in (SELECT book_id FROM book_authors WHERE author_id in (SELECT id from authors where last_name = ?))")) {
            s.setString(1, authorLastName);

            try (ResultSet r = s.executeQuery()) {
                
                List<Book> elements = new ArrayList<>();
                while (r.next()) {
                    Book b = new Book();

                    b.setId(r.getInt("id"));
                    b.setTitle(r.getString("title"));
                    elements.add(b);
                }
                
                for(Book tmp:elements)
                {
                    System.out.println((checkAvailability(tmp.getId())?"":"not ") + "available " + tmp);
                }
                return elements;
            }
        }
    }
    
    public List<Book> findAll() throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM books")) {
            try (ResultSet r = s.executeQuery()) {

                List<Book> elements = new ArrayList<>();

                while (r.next()) {
                    Book b = new Book();

                    b.setId(r.getInt("id"));
                    b.setTitle(r.getString("title"));
                    elements.add(b);
                }

                return elements;
            }
        }
    }
    
    public int maxIndex() throws SQLException
    {
        int res = -1;
        List<Book> all = BookFinder.getINSTANCE().findAll();
        for(Book b: all)
        {
            res = Math.max(res, b.getId());
        }
        return res;
    }
    

    
}
