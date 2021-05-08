package STATS;

/**
 *
 * @author sabinka
 */
public class Stat1Row 
{
    private int bookId;
    private double numDays;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public double getNumDays() {
        return numDays;
    }

    public void setNumDays(double numDays) {
        this.numDays = numDays;
    }

    @Override
    public String toString() {
        return "book: " + bookId + " avgAvaiDays: " + numDays;
    }
    
}
