package MAIN;

import java.sql.Connection;

/**
 *
 * @author  Alexander Šimko, sabinka
 */
public class DBContext {
    private static Connection connection;
    
    public static void setConnection(Connection c)
    {
        if (c == null) 
        {
            throw new NullPointerException("connection cannot be null");
        }
        connection = c;
    }
    
    public static Connection getConnection()
    {
        if (connection == null) 
        {
            throw new IllegalStateException("connection must be set before calling this method");
        }
        return connection;
    }

    public static void clear() 
    {
        connection = null;
    }
    
}
