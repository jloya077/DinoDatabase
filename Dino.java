import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Dino
{
  public static void main(String[] args) throws SQLException, InterruptedException
  {
    Connection connection = null;
    Scanner input = new Scanner(System.in);
    try
    {
      int in;
      while(true)
      {
        TimeUnit.SECONDS.sleep(2);
        System.out.println("-------DinoDatabase Main Menu--------");
        System.out.println("0: Quit Program");
        System.out.println("1: Connect to DinoDatabase");
        System.out.println("2: Display Search Options");
        System.out.println("10: Disconnect from Database");
        System.out.println("Please enter desired option:");
        in = input.nextInt();
        if(in == 0)
        {
          System.out.println("Quitting Program...Goodbye");
          break;
        }
        else if(in == 1)
        {
          System.out.println("Connecting to DinoDatabase...");
          connection = connectDatabase(connection);
          System.out.println("Connected to DinoDatabase.");

        }
        else if(in == 2)
        {
            System.out.println("Displaying Search Options");
            userMenu(connection, input);
        }
        else if(in > 10)
        {
          System.out.println("Invalid value entered, please try again");
        }
      }
      

    }
    catch(SQLException e)
    {
      // if the error message is "out of memory",
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
        {
          //stat.close();
          input.close();
          connection.close();
        }
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e.getMessage());
      }
    }
    input.close();
  }
  public static Connection connectDatabase(Connection conn) throws SQLException
  {
    conn = DriverManager.getConnection("jdbc:sqlite:Dino.db");
    return conn;
  }
  public static void userMenu(Connection conn, Scanner input) throws SQLException, InterruptedException
  {
    int in = 0;
    while(true)
    {
        TimeUnit.SECONDS.sleep(2);
        System.out.println("-------User Menu--------");
        System.out.println("0: Exit User Menu");
        System.out.println("1: Display Complete Dinosaur Data");
        in = input.nextInt();
        if(in == 0)
        {
            return;
        }
        else if(in == 1)
        {
            userQuery1(conn);
        }


    }
  }

  public static void userQuery1(Connection conn) throws SQLException
  {    
    String query = "select * from Dinosaur";
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    
    while(rs.next())
    {
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Dino key: " + rs.getInt(1));
        System.out.println("Name: " + rs.getString(2));  
        System.out.println("Diet: " + rs.getString(3)); 
        System.out.println("Time Period: " + rs.getString(4));  
        System.out.println("Description: " + rs.getString(5)); 
        System.out.println("Type: " + rs.getString(6)); 
        System.out.println("Habitat Key: " + rs.getInt(7));
        System.out.println("--------------------------------------------------------------------------------------------");
    }
    stmt.close();
    rs.close();
  }
}