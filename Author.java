package RDG;

/**
 *
 * @author sabinka
 * contains info about author of a book
 */
public class Author
{
    private int id;

    public Author(int id) 
    {
        this.id = id;
    } 

    public int getId() 
    {
        return id;
    }

    public void setId(int id) 
    {
        this.id = id;
    }


    @Override
    public String toString() 
    {
        return "Author{" + "id=" + id + '}';
    }
        
        
        
}