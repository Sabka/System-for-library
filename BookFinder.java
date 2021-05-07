package RDG;

import MAIN.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
     * @param inputId - id of a Book
     * @return whether there is any available copy of book
     * @throws java.sql.SQLException
    */
    public static boolean checkAvailability(int inputId) throws SQLException
    {
        try(PreparedStatement s = DBContext.getConnection().prepareStatement("select num_avail_cpy_of_book(?, ?)"))
        {
            s.setInt(1, inputId);
            s.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            try(ResultSet r = s.executeQuery())
            {
                r.next();
                return r.getInt(1) > 0;
            }
        }
    }
    
    private BookFinder(){}
    
    /**
    * find authors of a book
     * @param b - id of a book
     * @throws java.sql.SQLException
    */
    public void findAuthors(Book b) throws SQLException
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT author_id  FROM book_authors WHERE book_id = ?")) 
        {
            s.setInt(1, b.getId());
            ResultSet r = s.executeQuery();
            List<Author> a = new ArrayList();
            while(r.next())
            {
                a.add(new Author(r.getInt(1)));
                
            }
            b.setAuthors(a);
        }
    }
    
    /**
    * find book by its id
     * @param id -  id of a Book
    * @return object of found Book
     * @throws java.sql.SQLException
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
                    
                    findAuthors(b);

                    return b;
                } else {
                    return null;
                }
            }
        }
    }
    
    /**
    * find books by its title
     * @param title - book title
    * @return found Book
     * @throws java.sql.SQLException
    */
    public List<Book> findByTitle(String title) throws SQLException {

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM books WHERE title similar to '%"+ title +"%'")) {

            try (ResultSet r = s.executeQuery()) {
                
                List<Book> elements = new ArrayList<>();
                while (r.next()) {
                    Book b = new Book();

                    b.setId(r.getInt("id"));
                    b.setTitle(r.getString("title"));
                    findAuthors(b);
                    elements.add(b);
                }
                return elements;
            }
        }
    }
    
    /**
    * find books written by author
    * @param authorLastName last name of author
    * @return list of Books
     * @throws java.sql.SQLException
    */
    public List<Book> findByAuthor(String authorLastName) throws SQLException {

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("select * from books b join book_authors ba on b.id = ba.book_id join authors a on a.id = ba.author_id where last_name = ?")) {
            s.setString(1, authorLastName);

            try (ResultSet r = s.executeQuery()) {
                
                List<Book> elements = new ArrayList<>();
                while (r.next()) {
                    Book b = new Book();

                    b.setId(r.getInt(1));
                    b.setTitle(r.getString("title"));
                    findAuthors(b);
                    elements.add(b);
                }
                return elements;
            }
        }
    }
    
    /**
    * find all books in DB
    * @return list of Books
     * @throws java.sql.SQLException
    */
    public List<Book> findAll() throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM books")) {
            try (ResultSet r = s.executeQuery()) {

                List<Book> elements = new ArrayList<>();

                while (r.next()) {
                    Book b = new Book();

                    b.setId(r.getInt("id"));
                    b.setTitle(r.getString("title"));
                    findAuthors(b);
                    elements.add(b);
                }

                return elements;
            }
        }
    }
    
    
    /**
    * find maximum of book ids  in DB
    * @return maximum id
     * @throws java.sql.SQLException
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
