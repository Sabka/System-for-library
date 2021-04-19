package UI;

import RDG.Book;
import RDG.BookFinder;
import RDG.Category;
import RDG.CategoryFinder;
import RDG.Copy;
import RDG.CopyFinder;
import RDG.Reader;
import RDG.ReaderFinder;
import RDG.Rental;
import RDG.RentalFinder;
import RDG.Reservation;
import RDG.ReservationFinder;
import RDG.Stock;
import RDG.StockFinder;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author sabinka
 */
public class InputChecker 
{
    public static boolean checkBook(int bId) throws SQLException
    {
        Book b = BookFinder.getINSTANCE().findById(bId);
        
        return b != null;
    }
    
    public static boolean checkReader(int rId) throws SQLException
    {
        Reader r = ReaderFinder.getINSTANCE().findById(rId);
        return r != null; 
    }
    
    public static boolean checkReaderValidity(int rId) throws SQLException
    {
        Reader r = ReaderFinder.getINSTANCE().findById(rId);
        return r.getValidTil().after(new Timestamp(System.currentTimeMillis()));
    }
    
    
    public static Timestamp getLaterTimestamp(Timestamp t, int days)
    {
        Timestamp tmp = new Timestamp(t.getTime());
        for(int i=0; i<days; i++)
        {
            tmp = new Timestamp(tmp.getTime() + 24*60*60*1000);
        }
        return tmp;
    }
    
    public static boolean checkPercentage(double perc)
    {
        if (perc < 0) 
        {
            return false;
        } 
        return perc <= 100; 
    }
    
    public static boolean checkStock(int sId) throws SQLException
    {
        Stock s = StockFinder.getINSTANCE().findById(sId);
        
        return s != null;
    }
    
    public static boolean checkCopy(int bId) throws SQLException
    {
        Copy b = CopyFinder.getINSTANCE().findById(bId);
        
        return b != null;
    }
    
    public static boolean checkCat(int cId) throws SQLException
    {
        Category b = CategoryFinder.getINSTANCE().findById(cId);
        
        return b != null;
    }
    
    public static boolean checkReservation(int rId) throws SQLException
    {
        Reservation b = ReservationFinder.getINSTANCE().findById(rId);
        
        return b != null;
    }
    
    
    public static boolean checkRental(int rId) throws SQLException
    {
        Rental b = RentalFinder.getINSTANCE().findById(rId);
        
        return b != null;
    }
}
