import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.postgresql.ds.PGSimpleDataSource;


/**
 *
 * @author sabinka
 */
public class Main {
    
    public static void main(String[]args) throws SQLException, IOException 
    {
        /**
     * @param args the command line arguments
     */
        //System.out.println("123");
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerName("db.dai.fmph.uniba.sk");
        ds.setDatabaseName("playground");
        ds.setUser("samporova1@uniba.sk");
        ds.setPassword("sabinka1");
        System.out.println(ds);
        

        try(Connection c = ds.getConnection())
        {
            DBContext.setConnection(c);
            
            //Reader r = new Reader();
            //r.setFirstName("robo");
            //r.setLastName("hood");
            //r.insert();
            
            //ReaderFinder rf = ReaderFinder.getINSTANCE();
            
            /*Reader copy = rf.findById(10);
            System.out.println(copy);
            
            List<Reader> lr = rf.findAll();
            for(Reader tmp:lr)
            {
                System.out.println(tmp);
            }*/
                
           
            MainMenu mainMenu = new MainMenu();
            mainMenu.run();
  
        }
        finally
        {
            DBContext.clear();

        }
    }

}