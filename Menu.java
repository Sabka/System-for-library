import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Alexander Šimko, sabinka
 */
public abstract class Menu {

    private boolean exit;

    public void run() throws IOException 
    {
        exit = false;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (!exit)
        {
            System.out.println();
            print();
            System.out.println();

            String line = br.readLine();
            if (line == null) 
            {
                return;
            }

            System.out.println();

            handle(line);
            
            if(!exit)
            {
                System.out.println("Press enter to continue");
                br.readLine();
            }
        }
    }

    public void exit() 
    {
        exit = true;
    }

    public abstract void print();

    public abstract void handle(String option);	
}