package STATS;

/**
 *
 * @author sabinka
 * contains 1 row of result of avail stats
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
