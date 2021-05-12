package RDG;

import MAIN.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author sabinka
 */
public class Book 
{
    private Integer id;
    private String title;
    private List<Author> authors;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
    
    

    /**
     * Insert new row to table books in DB.
     * @throws java.sql.SQLException - incorrect query
     */
    public void insert() throws SQLException
    {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO books (title) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, title);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
        for(Author a: authors)
        {
            try(PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO book_authors (book_id, author_id) VALUES (?, ?)"))
            {
                s.setInt(1, id);
                s.setInt(2, a.getId());
                s.executeUpdate();
            } 
        }
        
    }

    /**
     * Update row in table books in DB. 
     * @throws java.sql.SQLException - incorrect query
     */
    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE books SET title = ? WHERE id = ?")) {
            s.setString(1, title);
            s.setInt(2, id);
            s.executeUpdate();
        }
        
        try(PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM book_authors WHERE book_id = ?"))
        {
            s.setInt(1, id);
            s.executeUpdate();
        }
        
        for(Author a: authors)
        {
            try(PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO book_authors (book_id, author_id) VALUES (?, ?)"))
            {
                s.setInt(1, id);
                s.setInt(2, a.getId());
                s.executeUpdate();
            } 
        }
    }

    /**
     * Delete row from table books in DB.
     * @throws java.sql.SQLException - incorrect query
     */
    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM books WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title=" + title + ", authors=" + authors + '}';
    }

    
}
