/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        System.out.println("\n***************************************");
        System.out.println("                STATS                  ");
        System.out.println("***************************************");
        System.out.println(" 60. book availability stats         ");
        System.out.println("\n***************************************");
        System.out.println(" 100. exit                           ");
        System.out.println("***************************************");
        
    }
    
    
    
    /**
     *
     * @param option
     * @throws Exception
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
                case "60":   getBookAvailStats(); break;
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
        for(Reader tmp:lr)
        {
            System.out.println(tmp);
        }
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
        
        if (r == null) {
            System.out.println("No such reader exists");
        } else {
            System.out.println(r);
        }

    }

    /**
     * Read readers attributes and add reader to DB.
     */
    private void addAReader() throws IOException, SQLException, ParseException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        Reader r = new Reader();
        
        System.out.println("Enter first name:");
        r.setFirstName(br.readLine());
        System.out.println("Enter last name:");
        r.setLastName(br.readLine());
        Timestamp t = new Timestamp(System.currentTimeMillis());
        for(int i=0; i<365; i++) 
        {
            t = new Timestamp(t.getTime() + 24*60*60*1000);
        }
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
        
        if (r == null) {
            System.out.println("No such reader exists");
        } else {
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
    }

    /**
     * Read readers id and delete reader from DB.
     */
    private void deleteAReader() throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a reader's id:");
        int rId = Integer.parseInt(br.readLine());

        Reader r = ReaderFinder.getINSTANCE().findById(rId);
        
        if (r == null) {
            System.out.println("No such reader exists");
        } else {
            r.delete();
            System.out.println("The reader has been successfully deleted");
        }
    }

    /**
     * Read books title and prints this book atributes.
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
                System.out.println((bf.checkAvailability(tmp.getId())?"":"not ") + "available " + tmp);
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
                System.out.println((bf.checkAvailability(tmp.getId())?"":"not ") + "available " + tmp);
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
        
        if (b == null) {
            System.out.println("No such book exists");
        } else {
            System.out.println(b);

            System.out.println("Enter title:");
            b.setTitle(br.readLine());
           
            b.update();

            System.out.println("The book has been successfully updated");
        }
        
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
        
        if (b == null) {
            System.out.println("No such book exists");
        } else {
            b.delete();
            System.out.println("The book has been successfully deleted");
        }
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
        if(c.size() == 0) System.out.println("No copy.");
        
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
        System.out.println("Enter state:");
        b.setState(Double.parseDouble(br.readLine()));
        System.out.println("Enter true/false if copy is available distantly:");
        b.setAvailableDistantly(Boolean.parseBoolean(br.readLine()));
        System.out.println("Enter id of default stock:");
        b.setStockId(Integer.parseInt(br.readLine()));
    
        b.insert();
        
        System.out.println("The copy has been sucessfully added");
        System.out.print("The copy's id is: ");
        System.out.println(b.getId());
        
    }

    /**
     * Read copy atributes and update it in DB.
     */
    private void editACopy() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a copy's id:");
        int bId = Integer.parseInt(br.readLine());

        Copy b = CopyFinder.getINSTANCE().findById(bId);
        
        if (b == null) {
            System.out.println("No such copy exists");
        } else {
            System.out.println(b);

            System.out.println("Enter book_id:");
            b.setBookId(Integer.parseInt(br.readLine()));
            System.out.println("Enter state:");
            b.setState(Double.parseDouble(br.readLine()));
            System.out.println("Enter true/false if copy is available distantly:");
            b.setAvailableDistantly(Boolean.parseBoolean(br.readLine()));
            System.out.println("Enter id of default stock:");
            b.setStockId(Integer.parseInt(br.readLine()));

            b.update();

            System.out.println("The copy has been successfully updated");
        }
        
    }


    /**
     * Read copy id and delete it from DB.
     */
    private void deleteACopy() throws IOException, SQLException
    {
       BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a copy's id:");
        int bId = Integer.parseInt(br.readLine());

        Copy b = CopyFinder.getINSTANCE().findById(bId);
        
        if (b == null) {
            System.out.println("No such copy exists");
        } else {
            b.delete();
            System.out.println("The copy has been successfully deleted");
        }
    }

    /**
     * Read book id and prints all copies of book with their availability
     */
    private boolean findAllAvailableCopies(int bId) throws SQLException
    {
        List <Copy> c = CopyFinder.getINSTANCE().findCopiesOfBook(bId);
        for(Copy tmp: c)
        {
            System.out.println((tmp.isAvailable()?"avail ":"not avail ") + tmp);
        }
        return c.size() != 0;
        
    }

    /**
     * Read readers, book and copy arguments and creates a reservation.
     */
    private void createAReservation() throws IOException, SQLException
    {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // check reader
        System.out.println("Enter reader's id:");
        int readerId = Integer.parseInt(br.readLine());
        Reader reader = ReaderFinder.getINSTANCE().findById(readerId);
        if(reader == null)
        {
            System.out.println("Incorrect reader's id.");
            return;
        }
        if(reader.getValidTil().before(new Timestamp(System.currentTimeMillis())))
        {
            System.out.println("Your account is not valid yet.");
            return;
        }
        if(ReaderFinder.getINSTANCE().hasOpenedFees(readerId))
        {
            System.out.println("You have unpayed fees.");
            return;
        }
        
        // check book and copy
        System.out.println("Enter book id:");
        int bId = Integer.parseInt(br.readLine());
        
        if(!findAllAvailableCopies(bId))
        {
           System.out.println("Sorry, there is no available copy of this book.");
           return;
        }
        
        System.out.println("Enter copy's id:");
        int cId = Integer.parseInt(br.readLine());
     
        Copy c = CopyFinder.getINSTANCE().findById(cId);
        if(!c.isAvailable())
           {
            System.out.println("Sorry, this copy is not available.");
            return;
        }
        
        if(!c.isAvailableDistantly())
        {
            System.out.println("Sorry, this copy cant be reserved (not available distantly).");
            return;
        }
        
        // everything ok, create reservation
        Reservation res = new Reservation();
        res.setCopyId(cId);
        res.setDateFrom(new Timestamp(System.currentTimeMillis()));
        res.autosetDateTo();
        res.setReaderId(readerId);
        
        res.insert();
        
        System.out.println("Book has been succesfully reserved.");
        
        List<Announcement> a = DeliveryManager.manageReservations(); // magicky presun knih
        for(Announcement tmp:a)
        {
            System.out.println(a);
        }
        
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
        
        Copy b = CopyFinder.getINSTANCE().findById(bId);
        if(b == null)
        {
            System.out.println("Copy with this id does not exists");
            return;
        }
        
        System.out.println("CATEGORIES:");
        for(Category cat:CategoryFinder.getINSTANCE().findAll())
        {
            System.out.println(cat);
        }
       
        
        System.out.println("\nEnter category id:");
        int cId = Integer.parseInt(br.readLine());
        
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
        
        Copy b = CopyFinder.getINSTANCE().findById(bId);
        if(b == null)
        {
            System.out.println("Copy with this id does not exists");
            return;
        }
      
        b.setCategory(null);
        b.update();
        
        System.out.println("Copy category has been succesfully removed.");
        
    }


    /**
     * Prints all stocks in DB
     */
    private void findAllStocks() throws SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StockFinder bf = StockFinder.getINSTANCE();
     
        List<Stock> lb = bf.findAll();   
        if(lb.isEmpty()) System.out.println("No stock found");
        for(Stock s:lb)
        {
            System.out.println(s);
        }
    }


    /**
     * Read stock atributes and add it to DB.
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
     * Read stock atributes and update it in DB.
     */
    private void editAStock() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a stocks's id:");
        int bId = Integer.parseInt(br.readLine());

        Stock b = StockFinder.getINSTANCE().findById(bId);
        
        if (b == null) {
            System.out.println("No such stock exists");
        } else {
            System.out.println(b);

            System.out.println("Enter adress:");
            b.setAdress(br.readLine());
           
            b.update();

            System.out.println("The stock has been successfully updated");
        }
        
    }

    /**
     * Read stock id and delete it from DB.
     */
    private void deleteAStock() throws IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter a stock's id:");
        int bId = Integer.parseInt(br.readLine());

        Stock b = StockFinder.getINSTANCE().findById(bId);
        
        if (b == null) {
            System.out.println("No such stock exists");
        } else {
            b.delete();
            System.out.println("The stock has been successfully deleted");
        }
        
    }

    private void getReservedBook() throws IOException, SQLException 
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // check reader
        System.out.println("Enter reader's id:");
        int readerId = Integer.parseInt(br.readLine());
        Reader reader = ReaderFinder.getINSTANCE().findById(readerId);
        if(reader == null)
        {
            System.out.println("Incorrect reader's id.");
            return;
        }
        if(reader.getValidTil().before(new Timestamp(System.currentTimeMillis())))
        {
            System.out.println("Your account is not valid yet.");
            return;
        }
        if(ReaderFinder.getINSTANCE().hasOpenedFees(readerId))
        {
            System.out.println("You have unpayed fees.");
            return;
        }
        
        // reader ok, print his active reservations
        
        List<Reservation> activeRess = ReservationFinder.getINSTANCE().findReadersActiveReservations(readerId);
        for(Reservation tmp: activeRess)
        {
            System.out.println(tmp);
        }
        if(activeRess.isEmpty()){ System.out.println("Reader has no active reservations."); return;}
        
        
        // check book and copy
        
        System.out.println("\nEnter reservation id:");
        int rId = Integer.parseInt(br.readLine());
     
        Reservation r = ReservationFinder.getINSTANCE().findById(rId);
        
        if(r == null) 
        {
            System.out.println("Incorrect reservation id.");
            return;
        }
        
        
        Copy c = CopyFinder.getINSTANCE().findById(r.getCopyId());
        
        if(!c.isInLibrary()) 
        {
            System.out.println("Sorry, this copy is not in library yet.");
            return;   
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
        
        
        System.out.println("Book has been succesfully rented till " + res.getDateTo() + " . Enjoy reading.");
        
    }

    private void returnBook() throws IOException, SQLException 
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // reader
        System.out.println("Enter reader's id:");
        int readerId = Integer.parseInt(br.readLine());
        Reader reader = ReaderFinder.getINSTANCE().findById(readerId);
       
        if(reader == null)
        {
            System.out.println("Reader with entered id does not exist");
            return;
        }
        
        // reader ok, print his active rentals
        List<Rental> activeRens = RentalFinder.getINSTANCE().findReadersActiveRentals(readerId);
        for(Rental tmp: activeRens)
        {
            System.out.println(tmp);
        }
        if(activeRens.isEmpty()) { System.out.println("Reader has no active rentals."); return;}
        
        System.out.println("\nEnter rental id:");
        int rId = Integer.parseInt(br.readLine());
     
        Rental r = RentalFinder.getINSTANCE().findById(rId);
        
        if(r == null) 
        {
            System.out.println("Incorrect rental id.");
            return;
        }
         
        System.out.println("Enter copy state (percents):");
        Double state = Double.parseDouble(br.readLine());
        
        
        Copy c = CopyFinder.getINSTANCE().findById(r.getCopyId());
        
        if((c.getState() - state) > 0.3)
        {
            Double amount = (c.getState() - state) * Fee.EUROSPERDECRESEDPERCENT;
            System.out.println("The state of book decreased, reader has new fee to pay.");
            
            Fee f = new Fee();
            f.setReaderId(readerId);
            f.setAmount(amount);
            f.setClosed(false);
            f.insert();
            
            System.out.println(f);
        }
        
      
        // everything ok, update rental
        r.setReturned(new Timestamp(System.currentTimeMillis()));
        r.update();
        
        
        // book will not be in library
        c.setInLibrary(true);
        c.setState(state);
        c.update();
        
        DeliveryManager.manageReturned();
        
    }

    public void makeFeesForNotReturned() throws IOException, SQLException 
    {
       
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            // reader
        System.out.println("Enter date:");
        String ts = br.readLine();
        Timestamp t = Timestamp.valueOf(ts);

        List<FeeAnnouncement> annList = FeeMaker.feesForNotReturnedCopies(t);

        annList.forEach(ann -> {
            System.out.println(ann);
        });
        
        System.out.println("All rental dates had been successfully checked.");

    }
    
}
