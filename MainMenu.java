package UI;

import TS.ResRenManager;
import STATS.*;
import TS.*;
import RDG.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.min;
import java.sql.SQLException;
import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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
        System.out.println(" 35. book delivery            ");
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
                case "35":   bookDeliv(); break;
                case "60":   getBookAvailStats(); break;
                case "61":   delayStats(); break;
                case "100":   exit(); break;
                default:    System.out.println("Unknown option"); break;
            }
        
    }

    /**
     * Prints all readers.
     */
    private void listAllReaders()
    {
        try
        {
            ReaderFinder rf = ReaderFinder.getINSTANCE();
            List<Reader> lr = rf.findAll(); 

            for(int i=0; i< min(lr.size(), 50); i++)
            {
               System.out.println(lr.get(i));
            }

            if(lr.isEmpty()) System.out.println("No readers found");
            else
            {
                System.out.println( rf.countAll() + " readers found");
            }
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        
    }


    /**
     * Read id and prints a reader with this id.
     */
    private void showAReader()
    {
        try
        {
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
        
    }

    /**
     * Read readers attributes and add reader to DB.
     */
    private void addAReader() 
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
        
    }

    /**
     * Read readers attributes and update reader in DB.
     */
    private void editAReader()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
        
    }

    /**
     * Read readers id and delete reader from DB.
     */
    private void deleteAReader() 
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
        
        
    }

    /**
     * Read books title and prints this book attributes.
     */
    private void findBookByTitle()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
        
      
    }


    /**
     * Read books author and prints this book attributes.
     */
    private void findBookByAuthor()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
        
    }


    /**
     *  Read books attributes and add it to DB.
     */
    private void addABook()
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            Book b = new Book();

            System.out.println("Enter title:");
            b.setTitle(br.readLine());

            System.out.println("Enter authors ids (coma separated):");
            List<String> ids = Arrays.asList( br.readLine().trim().split(","));
            List<Author> as = new ArrayList();
            ids.forEach(x -> as.add(new Author(Integer.parseInt(x))));
            b.setAuthors(as);

            b.insert();

            System.out.println("The book has been sucessfully added");
            System.out.print("The book's id is: ");
            System.out.println(b.getId());
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
        
    }


    /**
     * Read books attributes and update it in DB.
     */
    private void editABook()
    {
        try
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

            System.out.println("Enter authors ids (coma separated):");
            List<String> ids = Arrays.asList( br.readLine().trim().split(","));
            List<Author> as = new ArrayList();
            ids.forEach(x -> as.add(new Author(Integer.parseInt(x))));
            b.setAuthors(as);

            b.update();

            System.out.println("The book has been successfully updated");
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
        
        
    }

    /**
     * Read books id and delete it from DB.
     */
    private void deleteABook()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }

    /**
     * Read book id and find all copies of book in DB
     */
    private void findAllCopiesOfBook()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }


    /**
     * Read copy attributes and add it to DB.
     */
    private void addACopy()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }

    /**
     * Read copy attributes and update it in DB.
     */
    private void editACopy()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }


    /**
     * Read copy id and delete it from DB.
     */
    private void deleteACopy()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }

    

    /**
     * Read readers, book and copy arguments and creates a reservation.
     */
    private void createAReservation()
    {
        try
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

            if(CopyFinder.getINSTANCE().findById(id).isInLibrary())
            {
                System.out.println(new Announcement(readerId, id));
            }

            // prines knihy do kniznice a vypis oznamenia
            try
            {
                //System.out.println("delivery start");
                deliverBooks(); 
                //System.out.println("delivery end");
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }
    
    /**
     * Deliver books and print announcements for readers.
     */
    private void deliverBooks()
    {
        try
        {
            List<Announcement> a = DeliveryManager.manageReservations(); // magicky presun knih
            a.forEach(item -> 
            {
                System.out.println(item);
            });
            
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        
        
    }

    /**
     * Read year and prints book availability stats in this year.
     */
    private void getBookAvailStats()
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter year:");
            int year = Integer.parseInt(br.readLine());
            for(Stat1Row stat:Stats.bookAvailability(year))
            {
                System.out.println(stat);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }

    /**
     * Read copy id and category id, then adds this category to this copy.
     */
    private void addACategory()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }

    /**
     * Read copy id and set its category to null.
     */
    private void removeACategory()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
        
    }


    /**
     * Prints all stocks in DB
     */
    private void findAllStocks()
    {
        try
        {
            StockFinder bf = StockFinder.getINSTANCE();

            List<Stock> lb = bf.findAll();   
            if(lb.isEmpty()) System.out.println("No stock found");
            else
            {

                for(int i=0; i<min(50, lb.size()); i++)
                {
                    System.out.println(lb.get(i));
                }
                System.out.println(lb.size() + " stocks found");
            }
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        
    }


    /**
     * Read stock attributes and add it to DB.
     */
    private void addAStock()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }

    /**
     * Read stock attributes and update it in DB.
     */
    private void editAStock()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
       
        
    }

    /**
     * Read stock id and delete it from DB.
     */
    private void deleteAStock()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
        
    }

    /**
     * Reader can get copies he reserved.
     */
    private void getReservedBook()
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }

    /**
     * Reader can return a copy he rented.
     */
    private void returnBook()
    {
        try
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
                if(f != null) 
                {
                    System.out.println("The state of book decreased, reader has new fee to pay.");   
                    System.out.println(f);   
                }

            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                return;
            }

            System.out.println("Book has been succesfully returned.");
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }

    /**
     * Make fees for copies that werent returned in time.
     */
    public void makeFeesForNotReturned()
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter date:");
            String ts = br.readLine();
            Timestamp t = Timestamp.valueOf(ts);

            try
            {
               List<FeeAnnouncement> annList = FeeManager.feesForNotReturnedCopies(t);
               annList.forEach(ann -> 
                {
                    System.out.println(ann);
                });

            }
            catch(Exception e)
            {
                System.out.println("Something went wrong, please try again.");
                return;
            }


            System.out.println("All rental dates had been successfully checked.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }

    /**
     * Reader pay for his fees.
     */
    private void payFees()
    {
        try
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
                    try
                    {
                        FeeManager.payFees(valid, readerId);
                    }
                    catch(Exception e)
                    {
                        System.out.println(e.getMessage());
                        return;
                    }
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
        
    }

    /**
     * Prints delay states based on entered N.
     */
    private void delayStats() 
    {
        try
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
        catch(SQLException e)
        {
            System.out.println("Something went wrong, please try again.");
        }
        catch(IOException e)
        {
            System.out.println("Wrong input !");
        }
    }

    /**
     * Deliver reserved books to library and return books to stocks.
     */
    private void bookDeliv() 
    {
        try
        {
            DeliveryManager.manageReservations();
            DeliveryManager.manageReturned();
            System.out.println("Delivery succesful.");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
}
