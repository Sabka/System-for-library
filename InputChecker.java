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
    /**
    * check whether entered id belongs to any book in DB
     * @param bId - book id
     * @return t/f 
     * @throws java.sql.SQLException
    */
    public static boolean checkBook(int bId) throws SQLException
    {
        Book b = BookFinder.getINSTANCE().findById(bId);
        
        return b != null;
    }
    
    /**
    * check whether entered id belongs to any reader in DB
     * @param rId - reader id
     * @return t/f 
     * @throws java.sql.SQLException
    */
    public static boolean checkReader(int rId) throws SQLException
    {
        Reader r = ReaderFinder.getINSTANCE().findById(rId);
        return r != null; 
    }
    
    /**
    * check whether the reader with entered has valid account
     * @param rId - reader id
     * @return t/f 
     * @throws java.sql.SQLException
    */
    public static boolean checkReaderValidity(int rId) throws SQLException
    {
        Reader r = ReaderFinder.getINSTANCE().findById(rId);
        return r.getValidTil().after(new Timestamp(System.currentTimeMillis()));
    }
    
    /**
    * create new timestamp which is entered days later than entered timestamp
     * @param t - old timestamp
     * @param days - number of days
     * @return created timestamp
    */
    public static Timestamp getLaterTimestamp(Timestamp t, int days)
    {
        Timestamp tmp = new Timestamp(t.getTime());
        for(int i=0; i<days; i++)
        {
            tmp = new Timestamp(tmp.getTime() + 24*60*60*1000);
        }
        return tmp;
    }
    
    /**
    * check whether entered percentage is from valid range
     * @param perc - number of percents
     * @return t/f
    */
    public static boolean checkPercentage(double perc)
    {
        if (perc < 0) 
        {
            return false;
        } 
        return perc <= 100; 
    }
    
    /**
    * check whether entered id belongs to any stock in DB
     * @param sId - stock id
     * @return t/f 
     * @throws java.sql.SQLException
    */
    public static boolean checkStock(int sId) throws SQLException
    {
        Stock s = StockFinder.getINSTANCE().findById(sId);
        
        return s != null;
    }
    
    /**
    * check whether entered id belongs to any copy in DB
     * @param bId - copy id
     * @return t/f 
     * @throws java.sql.SQLException
    */
    public static boolean checkCopy(int bId) throws SQLException
    {
        Copy b = CopyFinder.getINSTANCE().findById(bId);
        
        return b != null;
    }
    
    /**
    * check whether entered id belongs to any category in DB
     * @param cId - category id
     * @return t/f 
     * @throws java.sql.SQLException
    */
    public static boolean checkCat(int cId) throws SQLException
    {
        Category b = CategoryFinder.getINSTANCE().findById(cId);
        
        return b != null;
    }
    
    /**
    * check whether entered id belongs to any reservation in DB
     * @param rId - reservation id
     * @return t/f 
     * @throws java.sql.SQLException
    */
    public static boolean checkReservation(int rId) throws SQLException
    {
        Reservation b = ReservationFinder.getINSTANCE().findById(rId);
        
        return b != null;
    }
    
    /**
    * check whether entered id belongs to any rental in DB
     * @param rId - rental id
     * @return t/f 
     * @throws java.sql.SQLException
    */
    public static boolean checkRental(int rId) throws SQLException
    {
        Rental b = RentalFinder.getINSTANCE().findById(rId);
        
        return b != null;
    }
}
