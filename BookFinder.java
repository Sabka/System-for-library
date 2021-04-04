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
    
    /**
    * check whether any copy of book with inputId is available in current time
    */
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
    
    /**
    * find book by its id
    * @return object of found Book
    */
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
    
    /**
    * find books by its title
    * @return found Book
    */
    public List<Book> findByTitle(String title) throws SQLException {

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM books WHERE title similar to '%"+ title +"%'")) {

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
    
    /**
    * find books written by author
    * @param authorLastName last name of author
    * @return list of Books
    */
    public List<Book> findByAuthor(String authorLastName) throws SQLException {

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
    
    /**
    * find all books in DB
    * @return list of Books
    */
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
    
    
    /**
    * find maximum of book ids  in DB
    * @return maximum id
    */
    public int maxIndex() throws SQLException
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM books order by id desc limit 1")) 
        {
            try (ResultSet r = s.executeQuery()) 
            {
                if(r.next())
                {
                    return r.getInt("id");
                }
              
            }
        }
        return -1;
    }

    
}
