package TS;

import MAIN.DBContext;
import RDG.Copy;
import RDG.CopyFinder;
import RDG.Fee;
import RDG.ReaderFinder;
import RDG.Rental;
import RDG.RentalFinder;
import RDG.Reservation;
import RDG.ReservationFinder;
import UI.InputChecker;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author sabinka
 */
public class ResRenManager 
{
    
    /**
    * create new reservation based on input
     * @param readerId
     * @param bId - book id
     * @return reserved copy id
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
    */
    public static int createReservation(int readerId, int bId) throws SQLException, Exception 
    {
        try
        {
            DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            DBContext.getConnection().setAutoCommit(false);
             // check reader
            if(!InputChecker.checkReader(readerId))
            {
                throw new Exception("Incorrect reader's id.");
            }

            if(!InputChecker.checkReaderValidity(readerId))
            {
                throw new Exception("Your account is not valid yet.");
            }

            if(ReaderFinder.getINSTANCE().hasOpenedFees(readerId))
            {
                throw new Exception("You have unpayed fees.");
            }


            Copy tmp = null;
            for(Copy c: CopyFinder.getINSTANCE().findCopiesOfBook(bId))
            {
                if(c.isAvailable())
                {
                    tmp = c;
                    break;
                }

            }
            if(tmp == null)
            {
               throw new Exception("Sorry, there is no available copy of this book.");
            }
            
            // dummy nacitavanie pre ucely testovania serializacie
            
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            br.readLine();

            Reservation res = new Reservation();
            res.setCopyId(tmp.getId());
            res.setDateFrom(new Timestamp(System.currentTimeMillis()));
            res.autosetDateTo();
            res.setReaderId(readerId);

            res.insert();
            return tmp.getId();
        }
        finally
        {
            DBContext.getConnection().commit();
            DBContext.getConnection().setAutoCommit(true);
        }
    }
    
    /**
    * create rental from reservation
     * @param readerId
     * @param rId - reservation id
     * @return timestamp - date_to of reservation
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
    */  
    public static Timestamp getReservedBooks(int readerId, int rId) throws SQLException, Exception
    {
        try
        {
            DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            DBContext.getConnection().setAutoCommit(false);
             // check reader
            if(!InputChecker.checkReader(readerId))
            {
                throw new Exception("Incorrect reader's id.");
            }

            if(!InputChecker.checkReaderValidity(readerId))
            {
                throw new Exception("Your account is not valid yet.");
            }
            if(ReaderFinder.getINSTANCE().hasOpenedFees(readerId))
            {
                throw new Exception("You have unpayed fees.");
            }

             // check book and copy
            Reservation r = ReservationFinder.getINSTANCE().findById(rId);

            if(!InputChecker.checkReservation(rId)) 
            {
                throw new Exception("Incorrect reservation id.");
            }
            
            if(!r.getReaderId().equals(readerId))
            {
                throw new Exception("This is not your reservation.");
            }  
            
            if(r.isRented())
            {
                throw new Exception("This reservation.is not active anymore.");
            }  

            if(!InputChecker.checkCopy(r.getCopyId()))
            {
                throw new Exception("Sorry, copy was deleted. Please create new reservation.");
            }

            Copy c = CopyFinder.getINSTANCE().findById(r.getCopyId()); 
            if(!c.isInLibrary()) 
            {
                throw new Exception("Sorry, this copy is not in library yet.");
            }

            // everything ok, create rental
            Rental res = new Rental();
            res.setCopyId(r.getCopyId());
            res.setDateFrom(new Timestamp(System.currentTimeMillis()));
            res.autosetDateTo();
            res.setReaderId(readerId);

            res.insert();

            // update reservation
            r.setRented(true);
            r.update();

            // book will not be in library
            c.setInLibrary(false);
            c.update();
            return r.getDateTo();
        }
        finally
        {
            DBContext.getConnection().commit();
            DBContext.getConnection().setAutoCommit(true);
        }
          
    }
    
    /**
    * manage returning rented copy
     * @param readerId
     * @param rId - id of a rental
     * @param state - state of a copy (%)
     * @return instance of fee - if copy was damaged, else null
     * @throws java.sql.SQLException
    */
    public static Fee returnBook(int readerId, int rId, double state) throws SQLException, Exception
    {
        try
        {
            DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            DBContext.getConnection().setAutoCommit(false);
            
            if(!InputChecker.checkReader(readerId))
            {
                throw new Exception("Reader with entered id does not exist"); 
            }
            
            if(!InputChecker.checkRental(rId)) 
            {
                throw new Exception("Incorrect rental id.");
            }
            
            Rental r = RentalFinder.getINSTANCE().findById(rId);
            Copy c = CopyFinder.getINSTANCE().findById(r.getCopyId());
            Fee res = null;

            if((c.getState() - state) > 0.3)
            {
                Double amount = (c.getState() - state) * Fee.EUROSPERDECRESEDPERCENT;
                System.out.println("The state of book decreased, reader has new fee to pay.");

                Fee f = new Fee();
                f.setReaderId(readerId);
                f.setAmount(amount);
                f.setClosed(false);
                f.insert();
                
                res = f;
            }


            // everything ok, update rental
            r.setReturned(new Timestamp(System.currentTimeMillis()));
            r.update();


            // book will not be in library
            c.setInLibrary(true);
            c.setState(state);
            c.update();
            return res;
            
        }
        finally
        {
            DBContext.getConnection().commit();
            DBContext.getConnection().setAutoCommit(true);
        }
        
    }
    
    
}
