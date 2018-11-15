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
        else if(in == 10)
        { 
          System.out.println("Disconnecting From Database...");
          disconnectDatabase(connection, input);
          
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
  public static void disconnectDatabase(Connection conn, Scanner input) throws SQLException
  {
    if(conn != null)
    {
      conn.close();
    }
  }
  public static String formatString(String in) //formats String so that the input is always right no matter how the user types it
  {
    if(in != null && in.length() > 0)
    {
      in = in.toLowerCase();
      in = in.substring(0,1).toUpperCase() + in.substring(1);
    }
    return in;

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
        System.out.println("2: Search by Specific Dinosaur");
        in = input.nextInt();
        if(in == 0)
        {
            return;
        }
        else if(in == 1)
        {
            userQuery1(conn);
        }
        else if(in == 2)
        {
          userQuery2(conn, input);
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
  public static void userQuery2(Connection conn, Scanner input) throws SQLException
  {

    ResultSet result = null; //initialize ResultSet
    PreparedStatement pre = null; //initialize prepared statement
    Statement stmt = conn.createStatement(); //initialize statement
    int dinoKey = 0; //initialize variables
    String dinoName = "";
    String res = "";

    String quickFix = input.nextLine(); //for some reason input skips over, doing this fixes it.

    String preStmt = 
    "select d_name, l_nation, h_name, tp_yearsAgo from Dinosaur, location, habitat, timeperiod where l_dinokey like ? and d_dinokey = ? and h_key = d_habkey and d_timeperiod = tp_name and d_name = ?";

    pre = conn.prepareStatement(preStmt);

    System.out.println("Please Enter Dinosaur Name:");
    dinoName = input.nextLine();
    dinoName = formatString(dinoName); //format into correct form

    res = "select d_name, d_dinokey from dinosaur where d_name = \'"+dinoName+"\'";
    result = stmt.executeQuery(res);

    if(result.next())
    {
      dinoName = result.getString("d_name"); //set variables
      dinoKey = result.getInt("d_dinokey");
    }
    else
    {
      System.out.println("Dinosaur not in database."); //check in case dinosaur not in database or something else happens
      System.out.println(dinoName);
      return;
    }
    
    pre.setString(1, "%" + dinoKey + "%"); //do this to fill in LIKE keyword
    pre.setInt(2, dinoKey);
    pre.setString(3, dinoName);

    result = pre.executeQuery(); //execute prepared statement
    //print out results
    if(result.next())
    {
      System.out.println("----------------------------------------------------------------------");
      System.out.println("Name: " + result.getString(1));
      System.out.println("Habitat: " + result.getString(3));
      System.out.println("Years Ago: " + result.getInt(4));
    }
    else
    {
      System.out.println("Error, Prepared Statement didn't execute"); //failsafe in case prepared statement failed
      return;
    }
    result = pre.executeQuery();
    System.out.print("Country(ies): ");
    //do while loop for countries since there's multiple countries
    while(result.next())
    {
      System.out.print(result.getString(2) + ", ");
    }
    System.out.println();
    System.out.println("----------------------------------------------------------------------");

    pre.close(); //close statements and resultset
    stmt.close();
    result.close();



  }

}