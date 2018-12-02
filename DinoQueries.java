import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;

public class DinoQueries
{
    public static String formatString(String in) //formats String so that the input is always right no matter how the user types it
    { 
        if(in != null && in.length() > 0)
        {
            in = in.toLowerCase();
            in = in.substring(0,1).toUpperCase() + in.substring(1);
        }
        return in;

    }
    
    public static void insertData(Connection conn, Scanner input) throws SQLException
    {  
        ResultSet result = null;
        PreparedStatement pre = null;
        Statement stmt = conn.createStatement();

        String quickFix = input.nextLine();
        String res, preStmt, colName, dinoName, info = "";
        String entries[]  = new String[5];
        String tables[] = new String[5];
        
        String dinoKey = "";
        int maxKey, counter = 0;


        tables[0] = "select * from Dinosaur";
        tables[1] = "select * from fossil";
        tables[2] = "select * from physicalTraits";
        tables[3] = "select * from pronunciation";
        tables[4] = "select * from taxonomy";

        entries[0] = "insert into Dinosaur values(?, ?, ?, ?, ?, ?, ?)";
        entries[1] = "insert into fossil values(?, ?, ?, ?);";
        entries[2] = "insert into physicalTraits values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        entries[3] = "insert into pronunciation values(?, ?, ?);";
        entries[4] = "insert into taxonomy values(?, ?, ?, ?, ?);";

        System.out.print("Enter Dinosaur You'd Like to insert: ");
        dinoName = input.nextLine();
        dinoName = formatString(dinoName);
        System.out.println();


        res = "select d_name, d_dinokey from dinosaur where d_name = \'"+dinoName+"\'";
        result = stmt.executeQuery(res);
        if(result.next())
        {
            System.out.println("Dinosaur already in database.");
            return;
        }

        res = "select max(d_dinokey) from Dinosaur"; //4
        result = stmt.executeQuery(res);
        if(result.next())
        {
            maxKey = result.getInt(1);
            maxKey++;
            dinoKey = Integer.toString(maxKey);
        }



        while(counter < tables.length)
        {
            result = stmt.executeQuery(tables[counter]);
            ResultSetMetaData resultMeta = result.getMetaData();
            int colNumber = resultMeta.getColumnCount();
            preStmt = entries[counter];
            pre = conn.prepareStatement(preStmt);

            for(int i = 1; i <= colNumber; i++)
            {
                colName = resultMeta.getColumnName(i);
                System.out.print("Enter " + colName + ": ");
                info = input.nextLine();
                if(colName.equals("d_name") || colName.equals("pt_name") || colName.equals("p_name") || colName.equals("t_genus"))
                    info = dinoName;
                else if(colName.equals("d_dinokey") ||colName.equals("f_dinokey") || colName.equals("pt_dinokey") || colName.equals("t_dinokey"))
                    info = dinoKey;
                else
                    info = info.toLowerCase();
            
                pre.setString(i, info);
            }
            pre.executeUpdate();
            counter++;
        
        }

        System.out.println("New Data Inserted Successfully");

    }
}