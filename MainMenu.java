/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * @author sabinka
 */
public class MainMenu extends Menu {


    @Override
    public void print() {
        System.out.println("*********************************");
        System.out.println("* 1. list all the readers       *");
        System.out.println("* 2. show a reader              *");
        System.out.println("* 3. add a reader               *");
        System.out.println("* 4. edit a reader              *");
        System.out.println("* 5. delete a reader            *");
        System.out.println("* 7. find book by title         *");
        System.out.println("* 8. list of books by author    *");
        System.out.println("* 9. add a book                 *");
        System.out.println("* 10. edit a book               *");
        System.out.println("* 11. delete a book             *");
        System.out.println("* 12. list of copies of a book  *");
        System.out.println("* 13. add a copy                *");
        System.out.println("* 14. edit a copy               *");
        System.out.println("* 15. delete a copy             *");
        System.out.println("* 16. create a reservation      *");
        System.out.println("* 100. exit                     *");
        System.out.println("*********************************");
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
                case "16":   createAReservation(); break;
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
    
    private void listAllReaders() throws SQLException 
    {
        ReaderFinder rf = ReaderFinder.getINSTANCE();
        List<Reader> lr = rf.findAll();   
        for(Reader tmp:lr)
        {
            System.out.println(tmp);
        }
    }
        
        
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

    private void addAReader() throws IOException, SQLException, ParseException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        Reader r = new Reader();
        
        System.out.println("Enter first name:");
        r.setFirstName(br.readLine());
        System.out.println("Enter last name:");
        r.setLastName(br.readLine());
        //System.out.println("Enter date - end of validation :");
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        //Date parsedDate = (Date) dateFormat.parse(br.readLine());
        //Timestamp t = new java.sql.Timestamp(parsedDate.getTime()); 
        r.setValidTil(new Timestamp(System.currentTimeMillis() + (365*24*60*60*1000)));
        
        r.insert();
        
        System.out.println("The reader has been sucessfully added - his acount is valid for 365 days");
        System.out.print("The reader's id is: ");
        System.out.println(r.getId());
    }

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
    
    
    private void findBookByTitle() throws SQLException, IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BookFinder bf = BookFinder.getINSTANCE();
        System.out.println("Enter book title:");
        String title = br.readLine();
        Book b = bf.findByTitle(title);   
        if (b == null) 
        {
            System.out.println("No such book exists");
        }
      
    }
    
    private void findBookByAuthor() throws SQLException, IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BookFinder bf = BookFinder.getINSTANCE();
        System.out.println("Enter a author's last name:");
        String name = br.readLine();
        List<Book> lb = bf.findByAuthor(name);   
        if(lb.isEmpty()) System.out.println("No book found");
    }

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
        
    }

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
    
    private boolean findAllAvailableCopies(int bId) throws SQLException
    {

        List <Copy> c = CopyFinder.getINSTANCE().findCopiesOfBook(bId);
        for(Copy tmp: c)
        {
            System.out.println((tmp.isAvailable()?"avail ":"not avail ") + tmp);
        }
        return c.size() != 0;
        
    }
    
    private void createAReservation() throws IOException, SQLException
    {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // check reader
        System.out.println("Enter reader's id:");
        int readerId = Integer.parseInt(br.readLine());
        Reader reader = ReaderFinder.getINSTANCE().findById(readerId);
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
        
    }
    
    
}
