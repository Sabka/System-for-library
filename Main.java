package MAIN;

import UI.MainMenu;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.postgresql.ds.PGSimpleDataSource;


/**
 *
 * @author Alexander Šimko, sabinka
 */
public class Main {
    
    public static void main(String[]args) throws SQLException, IOException 
    {
        
    /**
     * @param args the command line arguments
     */
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerName("db.dai.fmph.uniba.sk");
        ds.setDatabaseName("playground");
        ds.setUser("samporova1@uniba.sk");
        ds.setPassword("sabinka1");
        System.out.println(ds);
        

        try(Connection c = ds.getConnection())
        {
            DBContext.setConnection(c);
            

            MainMenu mainMenu = new MainMenu();
            mainMenu.run();
  
        }
        finally
        {
            DBContext.clear();

        }
    }

}