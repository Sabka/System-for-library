package UI;

import TS.ResRenManager;
import STATS.*;
import TS.*;
import RDG.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Alexander Šimko, sabinka
 */
public class MainMenu extends Menu {


    @Override
    public void print() {
        System.out.println("***************************************");
        System.out.println("                CRUD                   ");
        System.out.println("***************************************");
        System.out.println("  1. list all the readers             ");
        System.out.println("  2. show a reader                    ");
        System.out.println("  3. add a reader                     ");
        System.out.println("  4. edit a reader                    ");
        System.out.println("  5. delete a reader                  ");
        System.out.println("  7. find book by title               ");
        System.out.println("  8. list of books by author          ");
        System.out.println("  9. add a book                       ");
        System.out.println("  10. edit a book                     ");
        System.out.println("  11. delete a book                   ");
        System.out.println("  12. list of copies of a book        ");
        System.out.println("  13. add a copy                      ");
        System.out.println("  14. edit a copy                     ");
        System.out.println("  15. delete a copy                   ");
        System.out.println("  16. add/change a category of a copy ");
        System.out.println("  17. remove category of a copy       ");
        System.out.println("  18. list of stocks        ");
        System.out.println("  19. add a stock                  ");
        System.out.println("  20. edit a stock                     ");
        System.out.println("  21. delete a stock                   ");
        System.out.println("\n***************************************");
        System.out.println("                ZDO                    ");
        System.out.println("***************************************");
        System.out.println(" 30. create a reservation            ");
        System.out.println(" 31. get reserved book            ");        
        System.out.println(" 32. return a book            ");
        System.out.println(" 33. check not returned books            ");        
        System.out.println(" 34. pay fees            ");
        System.out.println("\n***************************************");
        System.out.println("                STATS                  ");
        System.out.println("***************************************");
        System.out.println(" 60. book availability stats         ");
        System.out.println(" 61. delay stats         ");
        System.out.println("\n***************************************");
        System.out.println(" 100. exit                           ");
        System.out.println("***************************************");
        
    }
    
    
    
    /**
     *
     * @param option
     */
    @Override
    public void handle(String option)
    {
        try {
            switch (option) {
                case "1":   listAllReaders(); break;
                case "2":   showAReader(); break;
                case "3":   addAReader(); break;
                case "4":   editAReader(); break;
                case "5":   deleteAReader(); break;
                case "7":   findBookByTitle(); break;
                case "8":   findBookByAuthor(); break;
                case "9":   addABook(); break;
                case "10":   editABook(); break;
                case "11":   deleteABook(); break;
                case "12":   findAllCopiesOfBook(); break;
                case "13":   addACopy(); break;
                case "14":   editACopy(); break;
                case "15":   deleteACopy(); break;
                case "16":   addACategory(); break;
                case "17":   removeACategory(); break;
                case "18":   findAllStocks(); break;
                case "19":   addAStock(); break;
                case "20":   editAStock(); break;
                case "21":   deleteAStock(); break;
                case "30":   createAReservation(); break;
                case "31":   getReservedBook(); break;                
                case "32":   returnBook(); break;
                case "33":   makeFeesForNotReturned(); break;
                case "34":   payFees(); break;
                case "60":   getBookAvailStats(); break;
                case "61":   delayStats(); break;
                case "100":   exit(); break;
                default:    System.out.println("Unknown option"); break;
            }
        }
        catch(SQLException | IOException  e) {
            throw new RuntimeException(e);
        } catch (ParseException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Prints all readers.
     */
    private void listAllReaders() throws SQLException
    {
        ReaderFinder rf = ReaderFinder.getINSTANCE();
        List<Reader> lr = rf.findAll();   
        lr.forEach(tmp -> {
            System.out.println(tmp);
        });
        if(lr.isEmpty()) System.out.println("No readers found");
    }


    /**
     * Read id and prints a reader with this id.
     */
    private void showAReader() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a reader id:");
        int rId = Integer.parseInt(br.readLine());

        Reader r = ReaderFinder.getINSTANCE().findById(rId);
        
        if (!InputChecker.checkReader(rId)) 
        {
            System.out.println("No such reader exists");
        } 
        else 
        {
            System.out.println(r);
        }

    }

    /**
     * Read readers attributes and add reader to DB.
     */
    private void addAReader() throws IOException, SQLException, ParseException 
    {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        Reader r = new Reader();
        
        System.out.println("Enter first name:");
        r.setFirstName(br.readLine());
        
        System.out.println("Enter last name:");
        r.setLastName(br.readLine());
        
        Timestamp t = new Timestamp(System.currentTimeMillis());
        t = InputChecker.getLaterTimestamp(t, 365);
        r.setValidTil(t);
        
        r.insert();
        
        System.out.println("The reader has been sucessfully added - his acount is valid for 365 days");
        System.out.print("The reader's id is: ");
        System.out.println(r.getId());
    }

    /**
     * Read readers attributes and update reader in DB.
     */
    private void editAReader() throws IOException, SQLException, ParseException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a reader's id:");
        int rId = Integer.parseInt(br.readLine());

        Reader r = ReaderFinder.getINSTANCE().findById(rId);
        
        if (!InputChecker.checkReader(rId)) 
        {
            System.out.println("No such reader exists");
            return;
        } 
        
        System.out.println(r);

        System.out.println("Enter first name:");
        r.setFirstName(br.readLine());
        
        System.out.println("Enter last name:");
        r.setLastName(br.readLine());
        
        System.out.println("Enter date - end of validation :");
        String ts = br.readLine();
        
        Timestamp t = Timestamp.valueOf(ts);
        r.setValidTil(t);

        r.update();

        System.out.println("The reader has been successfully updated");
        
    }

    /**
     * Read readers id and delete reader from DB.
     */
    private void deleteAReader() throws SQLException, IOException 
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a reader's id:");
        int rId = Integer.parseInt(br.readLine());

        Reader r = ReaderFinder.getINSTANCE().findById(rId);
        
        if (!InputChecker.checkReader(rId)) 
        {
            System.out.println("No such reader exists");
            return;
        } 
        
        r.delete();
        System.out.println("The reader has been successfully deleted");
        
    }

    /**
     * Read books title and prints this book attributes.
     */
    private void findBookByTitle() throws SQLException, IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BookFinder bf = BookFinder.getINSTANCE();
        System.out.println("Enter book title:");
        String title = br.readLine();
        List<Book> lb = bf.findByTitle(title);   
        if(lb.isEmpty()) System.out.println("No book found");
        else
        {
            for(Book tmp:lb)
            {
                System.out.println((BookFinder.checkAvailability(tmp.getId())?"":"not ") + "available " + tmp);
            }
        }
      
    }


    /**
     * Read books author and prints this book attributes.
     */
    private void findBookByAuthor() throws SQLException, IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        BookFinder bf = BookFinder.getINSTANCE();
        System.out.println("Enter a author's last name:");
        String name = br.readLine();
        List<Book> lb = bf.findByAuthor(name);   
        if(lb.isEmpty()) System.out.println("No book found");
        else
        {
            for(Book tmp:lb)
            {
                System.out.println((BookFinder.checkAvailability(tmp.getId())?"":"not ") + "available " + tmp);
            }
        }
    }


    /**
     *  Read books attributes and add it to DB.
     */
    private void addABook() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        Book b = new Book();
        
        System.out.println("Enter title:");
        b.setTitle(br.readLine());
       
        b.insert();
        
        System.out.println("The book has been sucessfully added");
        System.out.print("The book's id is: ");
        System.out.println(b.getId());
        
    }


    /**
     * Read books attributes and update it in DB.
     */
    private void editABook() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a book's id:");
        int bId = Integer.parseInt(br.readLine());

        Book b = BookFinder.getINSTANCE().findById(bId);
        
        if (!InputChecker.checkBook(bId))
        {
            System.out.println("No such book exists");
            return;
        } 
        
        System.out.println(b);

        System.out.println("Enter title:");
        b.setTitle(br.readLine());

        b.update();

        System.out.println("The book has been successfully updated");
        
    }

    /**
     * Read books id and delete it from DB.
     */
    private void deleteABook() throws SQLException, IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a book's id:");
        int bId = Integer.parseInt(br.readLine());

        Book b = BookFinder.getINSTANCE().findById(bId);
        
        if (!InputChecker.checkBook(bId)) 
        {
            System.out.println("No such book exists");
            return;
        }
        
        b.delete();
        System.out.println("The book has been successfully deleted");
        
    }

    /**
     * Read book id and find all copies of book in DB
     */
    private void findAllCopiesOfBook() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        CopyFinder cf = CopyFinder.getINSTANCE();
        System.out.println("Enter book id:");
        int bId = Integer.parseInt(br.readLine());
        List <Copy> c = cf.findCopiesOfBook(bId);
        for(Copy tmp: c)
        {
            System.out.println((tmp.isAvailable()?"avail ":"not avail ") + tmp);
        }
        if(c.isEmpty()) System.out.println("No copy.");
        
    }


    /**
     * Read copy attributes and add it to DB.
     */
    private void addACopy() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        Copy b = new Copy();
        
        System.out.println("Enter book_id:");
        b.setBookId(Integer.parseInt(br.readLine()));
        
        if (!InputChecker.checkBook(b.getBookId())) 
        {
            System.out.println("No such book exists");
            return;
        }
        
        System.out.println("Enter state:");
        b.setState(Double.parseDouble(br.readLine()));
        
        if(!InputChecker.checkPercentage(b.getState()))
        {
            System.out.println("State from invalid range");
            return;
        }
        
        System.out.println("Enter true/false if copy is available distantly:");
        b.setAvailableDistantly(Boolean.parseBoolean(br.readLine()));
        
        System.out.println("Enter id of default stock:");
        b.setStockId(Integer.parseInt(br.readLine()));
        
        if(!InputChecker.checkStock(b.getStockId()))
        {
            System.out.println("No such stock exists");
            return;
        }
        
        b.insert();
        
        System.out.println("The copy has been sucessfully added");
        System.out.print("The copy's id is: ");
        System.out.println(b.getId());
        
    }

    /**
     * Read copy attributes and update it in DB.
     */
    private void editACopy() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a copy's id:");
        int bId = Integer.parseInt(br.readLine());

        Copy b = CopyFinder.getINSTANCE().findById(bId);
        
        if (!InputChecker.checkCopy(bId)) 
        {
            System.out.println("No such copy exists");
            return;
        }
        
        System.out.println(b);

        System.out.println("Enter book_id:");
        b.setBookId(Integer.parseInt(br.readLine()));
        if (!InputChecker.checkBook(b.getBookId())) 
        {
            System.out.println("No such book exists");
            return;
        }
        
        System.out.println("Enter state:");
        b.setState(Double.parseDouble(br.readLine()));
        if(!InputChecker.checkPercentage(b.getState()))
        {
        System.out.println("State from invalid range");
        return;
        }
        
        System.out.println("Enter true/false if copy is available distantly:");
        b.setAvailableDistantly(Boolean.parseBoolean(br.readLine()));
        
        System.out.println("Enter id of default stock:");
        b.setStockId(Integer.parseInt(br.readLine()));
        
        if(!InputChecker.checkStock(b.getStockId()))
        {
            System.out.println("No such stock exists");
            return;
        }
        
        b.update();

        System.out.println("The copy has been successfully updated");

    }


    /**
     * Read copy id and delete it from DB.
     */
    private void deleteACopy() throws IOException, SQLException
    {
       BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a copy's id:");
        int bId = Integer.parseInt(br.readLine());

        
        if (!InputChecker.checkCopy(bId)) 
        {
            System.out.println("No such copy exists");
            return;
        } 
        
        Copy b = CopyFinder.getINSTANCE().findById(bId);
        b.delete();
        System.out.println("The copy has been successfully deleted");
        
    }

    

    /**
     * Read readers, book and copy arguments and creates a reservation.
     */
    private void createAReservation() throws IOException, SQLException
    {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
       
        System.out.println("Enter reader's id:");
        int readerId = Integer.parseInt(br.readLine());
     
        
        System.out.println("Enter book id:");
        int bId = Integer.parseInt(br.readLine());
        
        
        //create reservation
        int id;
        try
        {

            id = ResRenManager.createReservation(readerId, bId);
        }

        catch(Exception e)
        {

            System.out.println(e.getMessage());
            return;
        }
        
        
        System.out.println("Copy "+ id +" has been succesfully reserved.");
        
        // prines knihy do kniznice a vypis oznamenia
        try
        {
            System.out.println("delivery start");
            deliverBooks(); 
            System.out.println("delivery end");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return;
        }
     
    }
    
    /**
     * Deliver books and print announcements for readers.
     */
    private void deliverBooks() throws SQLException, Exception
    {
        List<Announcement> a = DeliveryManager.manageReservations(); // magicky presun knih
        a.forEach(item -> 
        {
            System.out.println(item);
        });
        
    }

    /**
     * Read year and prints book availability stats in this year.
     */
    private void getBookAvailStats() throws SQLException, IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
     
        System.out.println("Enter year:");
        int year = Integer.parseInt(br.readLine());
        for(Stat1Row stat:Stats.bookAvailability(year))
        {
            System.out.println(stat);
        }
    }

    /**
     * Read copy id and category id, then adds this category to this copy.
     */
    private void addACategory() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter copy id:");
        int bId = Integer.parseInt(br.readLine());
        
        if(!InputChecker.checkCopy(bId))
        {
            System.out.println("Copy with this id does not exists");
            return;
        }
        
        System.out.println("CATEGORIES:");
        for(Category cat:CategoryFinder.findAll())
        {
            System.out.println(cat);
        }
       
        System.out.println("\nEnter category id:");
        int cId = Integer.parseInt(br.readLine());
        if(!InputChecker.checkCat(cId))
        {
            System.out.println("Invalid category id.");
            return;
            
        }
        
        Copy b = CopyFinder.getINSTANCE().findById(bId);
        b.setCategory(cId);
        b.update();
        
        System.out.println("Copy category has been succesfully changed.");
        
    }

    /**
     * Read copy id and set its category to null.
     */
    private void removeACategory() throws SQLException, IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter copy id:");
        int bId = Integer.parseInt(br.readLine());
        
        
        if(!InputChecker.checkCopy(bId))
        {
            System.out.println("Copy with this id does not exists");
            return;
        }
        
        Copy b = CopyFinder.getINSTANCE().findById(bId);
        b.setCategory(null);
        b.update();
        
        System.out.println("Copy category has been succesfully removed.");
        
    }


    /**
     * Prints all stocks in DB
     */
    private void findAllStocks() throws SQLException
    {
        StockFinder bf = StockFinder.getINSTANCE();
     
        List<Stock> lb = bf.findAll();   
        if(lb.isEmpty()) System.out.println("No stock found");
        lb.forEach(o -> 
        {
            System.out.println(o);
        });
    }


    /**
     * Read stock attributes and add it to DB.
     */
    private void addAStock() throws SQLException, IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        Stock b = new Stock();
        
        System.out.println("Enter adress:");
        b.setAdress(br.readLine());
       
        b.insert();
        
        System.out.println("The stock has been sucessfully added");
        System.out.print("The stock's id is: ");
        System.out.println(b.getId());
    }

    /**
     * Read stock attributes and update it in DB.
     */
    private void editAStock() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a stocks's id:");
        int bId = Integer.parseInt(br.readLine());
 
        if (!InputChecker.checkStock(bId)) 
        {
            System.out.println("No such stock exists");
            return;
        }
        
        Stock b = StockFinder.getINSTANCE().findById(bId);
        System.out.println(b);

        System.out.println("Enter adress:");
        b.setAdress(br.readLine());

        b.update();

        System.out.println("The stock has been successfully updated");
       
        
    }

    /**
     * Read stock id and delete it from DB.
     */
    private void deleteAStock() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a stock's id:");
        int bId = Integer.parseInt(br.readLine());
        
        if (!InputChecker.checkStock(bId)) 
        {
            System.out.println("No such stock exists");
            return;
        }
        
        Stock b = StockFinder.getINSTANCE().findById(bId);

        b.delete();
        System.out.println("The stock has been successfully deleted");
        
    }

    /**
     * Reader can get copies he reserved.
     */
    private void getReservedBook() throws IOException, SQLException 
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
       
        System.out.println("Enter reader's id:");
        int readerId = Integer.parseInt(br.readLine());
        
        // print active reservations
        List<Reservation> activeRess = ReservationFinder.getINSTANCE().findReadersActiveReservations(readerId);
        activeRess.forEach(tmp -> 
        {
            System.out.println(tmp);
        });
        if(activeRess.isEmpty())
        {
            System.out.println("Reader has no active reservations."); 
            return;
        }
        
        System.out.println("\nEnter reservation id:");
        int rId = Integer.parseInt(br.readLine());
        
        
        Timestamp tmp;
        
        try
        {
            tmp = ResRenManager.getReservedBooks(readerId, rId);
        }

        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return;
        }
        
        
        System.out.println("Book has been succesfully rented till " + tmp + " . Enjoy reading.");
        
    }

    /**
     * Reader can return a copy he rented.
     */
    private void returnBook() throws IOException, SQLException 
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // reader
        System.out.println("Enter reader's id:");
        int readerId = Integer.parseInt(br.readLine());
      
        // print his active rentals
        List<Rental> activeRens = RentalFinder.getINSTANCE().findReadersActiveRentals(readerId);
        activeRens.forEach(tmp -> {
            System.out.println(tmp);
        });
        if(activeRens.isEmpty()) 
        { 
            System.out.println("Reader has no active rentals."); 
            return;
        }
        
        System.out.println("\nEnter rental id:");
        int rId = Integer.parseInt(br.readLine());
     
         
        System.out.println("Enter copy state (%):");
        Double state = Double.parseDouble(br.readLine());
       
        Fee f;
        try
        {
            f = ResRenManager.returnBook(readerId, rId, state);
            if(f != null) System.out.println(f);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return;
        }
        
        System.out.println("Book has been succesfully returned.");
        
    }

    /**
     * Make fees for copies that werent returned in time.
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public void makeFeesForNotReturned() throws IOException, SQLException 
    {
       
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
        System.out.println("Enter date:");
        String ts = br.readLine();
        Timestamp t = Timestamp.valueOf(ts);

        List<FeeAnnouncement> annList = FeeManager.feesForNotReturnedCopies(t);

        annList.forEach(ann -> 
        {
            System.out.println(ann);
        });
        
        System.out.println("All rental dates had been successfully checked.");

    }

    /**
     * Reader pay for his fees.
     */
    private void payFees() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // reader
        System.out.println("Enter reader's id:");
        int readerId = Integer.parseInt(br.readLine());
      
        // his fees
        List<Fee> readersFees = FeeFinder.getINSTANCE().findUnpayedByReaderID(readerId);
        if(readersFees.isEmpty())
        {
            System.out.println("Reader with entered id has no unpayed fees");
            return;
            
        }
        
        readersFees.forEach(f -> {
            System.out.println(f);
        });
        
        // paying
        System.out.println("Enter comma separated fee ids");
        List<Integer> fIds = Arrays.asList(br.readLine().split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
        
        List<Fee> valid = FeeManager.validFees(readersFees, fIds);
        double sum;
        
        try
        {
            sum = FeeManager.countSum(valid);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return;
        }
        
        System.out.println("The complete sum to pay:" + sum + "euros");
        
        // confirmation
        System.out.println("Please confirm, the sum was payed. T -> payed,F -> unpayed");
        String confirmation = br.readLine();
        switch (confirmation.trim()) 
        {
            case "T":
                FeeManager.payFees(valid);
                System.out.println("Fees were succesfully payed.");
                break;
            case "F":
                System.out.println("Fees were not payed.");
                break;
            default:
                System.out.println("Wrong confirmation.");
                break;
        }
        
        
        
    }

    /**
     * Prints delay states based on entered N.
     */
    private void delayStats() throws IOException, SQLException 
    {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter N.");
        int n = Integer.parseInt(br.readLine());
        List<Stats2Row> lines = Stats.getDelayStats(n);
        lines.forEach(line -> 
        {
            System.out.println(line);
        });
    }
    
}
