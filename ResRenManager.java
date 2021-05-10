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
import org.postgresql.util.PSQLException;

/**
 *
 * @author sabinka
 */
public class ResRenManager 
{
    final static double MAXOLDNEWSTATEDIF = 0.3;
    
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
        for(int i=0; i<10; i++)
        {
            try
            {
                DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                // pri Repeatable Read sa moze zarezervovat rovnaka kopia pre roznych userov v rovnakom case
                // = serializacna anomalia
                // potrebujeme SERIALIZABLE
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
                //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                //br.readLine();

                Reservation res = new Reservation();
                res.setCopyId(tmp.getId());
                res.setDateFrom(new Timestamp(System.currentTimeMillis()));
                res.autosetDateTo();
                res.setReaderId(readerId);

                res.insert();
                return tmp.getId();
            }
            catch(PSQLException e)
            {
                // serializacna chyba, opakujem
                System.err.print("opakujem\n");
            }
            finally
            {
                DBContext.getConnection().commit();
                DBContext.getConnection().setAutoCommit(true);
            }
            
        }
        throw new Error("Something went wong, please try again");
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
        for(int i=0; i<10; i++)
        {
            try
            {
                DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                // nestaci RC, lebo ak by si v rovnakom case reader poziciaval rovnaku reservation, vzniknu 2 rentals tej istehj kopie
                // RR staci, lebo serializacna anomalia nenastava
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
                    throw new Exception("Reservation with entered id does not exists.");
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

                // dummy nacitavanie pre ucely testovania serializacie
                //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                //br.readLine();

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
            catch(PSQLException e)
            {
                // serializacia mala problem, opakujme
                System.err.print("opakujem\n");
            }
            finally
            {
                DBContext.getConnection().commit();
                DBContext.getConnection().setAutoCommit(true);
            }
            
        }
        throw new Error("Something went wong, please try again");
          
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
        for(int i=0; i<10; i++)
        {
            try
            {
                DBContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                // opat ak uvazujeme, ze by 1 reader vracal rovnaku knihu na 2 miestach naraz, 
                // nestaci RC, ale treba RR, aby mu nemohli vzniknut 2 poplatky
                // RR odchyti, ze sa nieco updatlo co som uz citala a snazi sa opakovat, 
                //ale throwne, ze uz bola vratena
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
                if(!r.getReaderId().equals(readerId)) throw new Exception("This is not your rental.");
                if(r.getReturned() != null) throw new Exception("Book has been already returned.");
                    
                Copy c = CopyFinder.getINSTANCE().findById(r.getCopyId());
                Fee res = null;

                if((c.getState() - state) > MAXOLDNEWSTATEDIF)
                {
                    Double amount = (c.getState() - state) * Fee.EUROSPERDECRESEDPERCENT;
                    

                    Fee f = new Fee();
                    f.setReaderId(readerId);
                    f.setAmount(amount);
                    f.setClosed(false);
                    f.insert();

                    res = f;
                }

                // dummy nacitavanie pre ucely testovania serializacie
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                br.readLine();

                // everything ok, update rental
                r.setReturned(new Timestamp(System.currentTimeMillis()));
                r.update();


                // book will not be in library
                c.setInLibrary(true);
                c.setState(state);
                c.update();
                return res;

            }
            catch(PSQLException e)
            {
                // serializacny problem, opakujeme
                System.err.print("opakujem\n");
            }
            finally
            {
                DBContext.getConnection().commit();
                DBContext.getConnection().setAutoCommit(true);
                //DeliveryManager.manageReturned();
            }
        }
        throw new Error("Something went wong, please try again");
    }
     
}
