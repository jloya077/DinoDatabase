import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.lang.model.util.ElementScanner6;
/* 11 Queries Done So Far */
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
        System.out.println("3: Search by Species");
        System.out.println("4: Longest Dinosaur from each Habitat");
        System.out.println("5: Search Top Heaviest Dinosaurs");
        System.out.println("6: Search by Mininum Height");
        System.out.println("7: Number of Dinosaurs based on Habitat & Diet");
        System.out.println("8: Search number of Dinosaurs based on type");
        System.out.println("9: Longest Dinosaur in Database");
        System.out.println("10: Display Dinosaurs in between a selected range");
        in = input.nextInt();

        if(in == 0){return;}
        else if(in == 1){userQuery1(conn);}
        else if(in == 2){userQuery2(conn, input);}
        else if (in == 3){userQuery3(conn,input);}
        else if (in == 4){userQuery4(conn);}
        else if(in == 5){userQuery5(conn, input);}
        else if(in == 6){userQuery6(conn, input);}
        else if(in == 7){userQuery7(conn, input);}
        else if(in == 8){userQuery8(conn, input);}
        else if(in == 9){userQuery9(conn);}
        else if(in == 10){userQuery10(conn, input);}


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

    System.out.println("Would you like more information on " + dinoName +"?");
    System.out.println("1: Yes, 2: No");
    int answer = input.nextInt();
    if(answer == 1)
    {
      quickFix = input.nextLine();
      while(true)
      {
        String tableName = "";
        System.out.println("Which Information would you like?");
        System.out.println("Options: dinosaur, fossil, physical traits, pronunciation, taxonomy, time period ");
        System.out.println("Enter 'exit' to stop");
        tableName = input.nextLine();
        if(tableName.equals("exit"))
          return;
        else
          getTableInfo(conn, input, tableName, dinoName);
      }
    }
    else if(answer == 2)
    {
      return;
    }
    else 
    {
      System.out.println("Invalid Number.");
      return;
    }
 }

 public static void getTableInfo(Connection conn, Scanner input, String table, String dinosaur) throws SQLException
 {
    ResultSet result = null;
    Statement stmt = conn.createStatement();
    String res = "";

    if(table.equals("dinosaur"))
    {
      res = "select * " +
            "from Dinosaur " +
            "where d_name = \'"+dinosaur+"\'";
            
      result = stmt.executeQuery(res);
      System.out.println("--------------------------------------------------------------------------------------------");
      if(result.next())
      {
        System.out.println("Dino key: " + result.getInt(1));
        System.out.println("Name: " + result.getString(2));  
        System.out.println("Diet: " + result.getString(3)); 
        System.out.println("Time Period: " + result.getString(4));  
        System.out.println("Description: " + result.getString(5)); 
        System.out.println("Type: " + result.getString(6)); 
        System.out.println("Habitat Key: " + result.getInt(7));
        System.out.println("--------------------------------------------------------------------------------------------");
      }

    }
    else if(table.equals("fossil"))
    {
      res = "select f_fossilData, f_period from Dinosaur, fossil where d_name = \'"+dinosaur+"\'";
      result = stmt.executeQuery(res);

 
      System.out.println("--------------------------------------------------------------------------------------------");
      if(result.next())
      {
        System.out.println("Fossil Data: " + result.getString(1));
        System.out.println("Time Period: " + result.getString(2));
        System.out.println("--------------------------------------------------------------------------------------------");   
      }
    }
    else if(table.equals("physical traits"))
    {
      res = "select * from physicalTraits where pt_name = \'"+dinosaur+"\'";
      result = stmt.executeQuery(res);
      System.out.println("--------------------------------------------------------------------------------------------");
      if(result.next())
      {
        System.out.println("Name: " + result.getString(2));
        System.out.println("Leg Type: " + result.getString(3));
        System.out.println("Body Type: " + result.getString(4));
        System.out.println("Length: " + result.getString(5));
        System.out.println("Height: " + result.getString(6));
        System.out.println("Weight: " + result.getString(7));
        System.out.println("Mouth Type: " + result.getString(8));
        System.out.println("Defining Trait: " + result.getString(9));
        System.out.println("Class: " + result.getString(10));
        System.out.println("--------------------------------------------------------------------------------------------");   
      }
    }
    else if(table.equals("pronunciation"))
    {
      res = "select * from pronunciation where p_name = \'"+dinosaur+"\'";
      result = stmt.executeQuery(res);
      System.out.println("--------------------------------------------------------------------------------------------");
      if(result.next())
      {
        System.out.println("Name: " + result.getString(1));
        System.out.println("Enunciation: " + result.getString(2));
        System.out.println("Meaning: " + result.getString(3));
        System.out.println("--------------------------------------------------------------------------------------------");   
      }
    }
    else if(table.equals("taxonomy"))
    {
      res = "select * from taxonomy where t_genus = \'"+dinosaur+"\'";
      result = stmt.executeQuery(res);
      System.out.println("--------------------------------------------------------------------------------------------");
      if(result.next())
      {
        System.out.println("Genus: " + result.getString(1));
        System.out.println("Species: " + result.getString(2));
        System.out.println("Family: " + result.getString(3));
        System.out.println("Order: " + result.getString(4));
        System.out.println("--------------------------------------------------------------------------------------------");   
      }
    }
    else if(table.equals("time period"))
    {
      res = "select tp_comment, tp_name from Dinosaur, timeperiod where d_timeperiod = tp_name and d_name = \'"+dinosaur+"\'";
      result = stmt.executeQuery(res);
      System.out.println("--------------------------------------------------------------------------------------------");
      if(result.next())
      {
        System.out.println("Time Period: " + result.getString(2));
        System.out.println("Info: " + result.getString(1));
        System.out.println("--------------------------------------------------------------------------------------------");   
      }
    }
    else 
    {
      System.out.println("Invalid Table, please choose from available options.");
      System.out.println("--------------------------------------------------------------------------------------------");
      return;
    }

    stmt.close();
    result.close();
 }
 public static void userQuery3(Connection conn, Scanner input) throws SQLException
 {
   ResultSet result = null; //initialize results and prepared statement
   PreparedStatement pre = null;
   Statement stmt = conn.createStatement();

   String preStmt = "select d_name from Dinosaur, taxonomy where  t_species = ? and d_name = t_genus;";
   String res = "";
   String quickFix = input.nextLine();

   pre = conn.prepareStatement(preStmt);
   String dinoSpecies = ""; // initialize input

   System.out.println("Please Enter Species Name:");
   System.out.println("Example Format: A. horneri");
   
   dinoSpecies = input.nextLine();

   res = "select t_species from taxonomy where t_species = \'"+dinoSpecies+"\'";
   result = stmt.executeQuery(res);
   if(result.next())
   {
     dinoSpecies = result.getString("t_species");
   }
   else
   {
     System.out.println("Species not in database.");
     return;
   }

   pre.setString(1, dinoSpecies);
   result = pre.executeQuery();
   while(result.next())
   {
     System.out.println("Name: " + result.getString(1));
   }

   pre.close();
   stmt.close();
   result.close();
}

public static void userQuery4(Connection conn) throws SQLException
{
  System.out.println("Printing out longest dinosaurs of each habitat...");
  ResultSet result = null;
  Statement stmt = conn.createStatement();

  String res = "SELECT h_name ,d_name , MAX(pt_length) FROM Dinosaur, physicalTraits, habitat " +
  "WHERE d_dinokey = pt_dinokey AND h_key = d_habkey " + 
  "GROUP BY h_name " +
  "ORDER BY pt_length ASC;";

  result = stmt.executeQuery(res);

  while(result.next())
  {
    System.out.println("--------------------------------------------------------------------------------------------");
    System.out.println("Habitat: " + result.getString(1));
    System.out.println("Dinosaur: " + result.getString(2));
    System.out.println("Length: " + result.getFloat(3));
    System.out.println("--------------------------------------------------------------------------------------------");

  }

  stmt.close();
  result.close();

}

public static void userQuery5(Connection conn, Scanner input) throws SQLException
{
  ResultSet result = null; //initialize results and prepared statement
  PreparedStatement pre = null;
  int topNum = 0;
  int rank = 1;

  String preStmt = "SELECT h_name ,d_name , MAX(pt_weight) " +
                   "FROM Dinosaur, physicalTraits, habitat " +
                   "WHERE d_dinokey = pt_dinokey AND h_key = d_habkey and pt_weight != 'unknown' " +
                   "GROUP BY d_name " +
                   "ORDER BY pt_weight desc " +
                   "LIMIT ?;";
  pre = conn.prepareStatement(preStmt);

  System.out.println("Please Enter Number:");
  topNum = input.nextInt();

  pre.setInt(1, topNum);
  result = pre.executeQuery();

  System.out.println("--------------------------------------------------------------------------------------------");
  while(result.next())
  {
    System.out.println("Rank: " + rank);
    System.out.println("Habitat: " + result.getString(1));
    System.out.println("Dinosaur: " + result.getString(2));
    System.out.println("Weight: " + result.getFloat(3));
    System.out.println("--------------------------------------------------------------------------------------------");
    rank++;

  }

  pre.close();
  result.close();
}

public static void userQuery6(Connection conn, Scanner input) throws SQLException
{
  ResultSet result = null; //initialize results and prepared statement
  PreparedStatement pre = null;
  int topNum = 0;

  String preStmt = "SELECT d_name , pt_height " +
                   "FROM Dinosaur, physicalTraits " +
                   "WHERE d_dinokey = pt_dinokey and pt_height != 'unknown' " +
                   "GROUP BY d_name " +
                   "HAVING pt_height >= ?;";

  pre = conn.prepareStatement(preStmt);

  System.out.println("Please Enter Number:");
  topNum = input.nextInt();

  pre.setInt(1, topNum);
  result = pre.executeQuery();

  System.out.println("--------------------------------------------------------------------------------------------");
  while(result.next())
  {
    System.out.println("Dinosaur: " + result.getString(1));
    System.out.println("Height: " + result.getFloat(2));
    System.out.println("--------------------------------------------------------------------------------------------");

  }

  pre.close();
  result.close();
}

public static void userQuery7(Connection conn, Scanner input) throws SQLException
{
  ResultSet result = null; //initialize results and prepared statement
  PreparedStatement pre = null;
  Statement stmt = conn.createStatement();

  String quickFix = input.nextLine();

  String res = "";
  String habName = "";
  String dietName = "";
  String preStmt = "SELECT COUNT(d_name) " +
                   "FROM Dinosaur " +
                   "WHERE d_name IN (SELECT d_name FROM Dinosaur, habitat " +
                                    "WHERE d_habkey = h_key " +
                                    "AND h_name = ? AND d_diet = ?);";

  pre = conn.prepareStatement(preStmt);
  
  System.out.println("Please Select Habitat.");
  System.out.println("Options: forest, aquatic, desert, plains, arid grassland, mountain,");
  System.out.println("canyon, river, woodland, swamp, floodplain, unknown");

  habName = input.nextLine();
  habName = habName.toLowerCase();
  res = "select h_name from habitat where h_name = \'"+habName+"\'";
  result = stmt.executeQuery(res);
  if(result.next())
    habName = result.getString("h_name");
  else
  {
    System.out.println("Habitat not found");
    return;
  }

  System.out.println("Please select Diet type.");
  System.out.println("Options: herbivore, carnivore");
  
  dietName = input.nextLine();
  dietName = dietName.toLowerCase();
  res = "select d_diet from dinosaur where d_diet = \'"+dietName+"\'";
  result = stmt.executeQuery(res);
  if(result.next())
    dietName = result.getString("d_diet");
  else
  {
    System.out.println("Diet type not found.");
    return;
  }

  pre.setString(1, habName);
  pre.setString(2, dietName);

  result = pre.executeQuery();
  if(result.next())
  {
    System.out.println("--------------------------------------------------------------------------------------------");
    System.out.println("Number of Dinosaurs with diet " + dietName + " and habitat " + habName + ": " + result.getInt(1));
    System.out.println("--------------------------------------------------------------------------------------------");
  }
  else
  {
    System.out.println("Statement did not execute.");
    return;
  }
  stmt.close();
  pre.close();
  result.close();

}

public static void userQuery8(Connection conn, Scanner input) throws SQLException
{
  ResultSet result = null;
  PreparedStatement pre = null;

  String dinoType = "";
  String quickFix = input.nextLine();
  String preStmt = "SELECT COUNT(*) " +
                   "FROM (SELECT d_name as dName " +
                   "FROM Dinosaur, physicalTraits " +
                   "WHERE d_type like ? AND d_dinokey = pt_dinokey " +
                   "GROUP BY d_name) as SQ1";
  pre = conn.prepareStatement(preStmt);

  System.out.println("Please enter Dinosaur Type:");
  System.out.println("Options: Land, Air, Sea");

  dinoType = input.nextLine();
  dinoType = dinoType.toLowerCase();

  pre.setString(1, "%" + dinoType + "%"); //do this to fill in LIKE keyword

  result = pre.executeQuery();
  if(result.next())
  {
    System.out.println("--------------------------------------------------------------------------------------------");
    System.out.println("Number of dinosaurs of " + dinoType + " type: " + result.getInt(1));
    System.out.println("--------------------------------------------------------------------------------------------");
  }
  else
  {
    System.out.println("Error in executing Query");
    return;
  }

  pre.close();
  result.close();
}

public static void userQuery9(Connection conn) throws SQLException
{
  ResultSet result = null;
  Statement stmt = conn.createStatement();

  String res = "SELECT p_name ,p_enunciation, SQ1.maxL " +
               "FROM pronunciation , Dinosaur, (SELECT d_dinokey as maxDino, max(pt_length) as maxL " +
                                               "FROM Dinosaur, physicalTraits " +
                                               "WHERE pt_dinokey = d_dinokey) as SQ1 " +
                                              "WHERE p_name = d_name AND d_dinokey = SQ1.maxDino;";

  result = stmt.executeQuery(res);
  
  System.out.println("--------------------------------------------------------------------------------------------");
  while(result.next())
  {
    System.out.println("Name: " + result.getString(1));
    System.out.println("Enunciation: " + result.getString(2));
    System.out.println("Length: " + result.getInt(3));
    System.out.println("--------------------------------------------------------------------------------------------");
  }
  stmt.close();
  result.close();
}

public static void userQuery10(Connection conn, Scanner input) throws SQLException
{
  ResultSet result = null;
  PreparedStatement pre = null;

  float min = 0;
  float max = 0;

  String quickFix = input.nextLine();
  String trait = "";
  String attribute = "";
  String preStmt = "";
  
  System.out.println("Please select  measurement type: ");
  System.out.println("Options: Length, Height, Weight");
  trait = input.nextLine();
  trait = formatString(trait);

  if(trait.equals("Length"))
  {
    attribute = "pt_length";
    preStmt = "SELECT d_name, pt_length " +
                   "FROM Dinosaur, physicalTraits " +
                   "WHERE d_dinokey = pt_dinokey AND pt_length BETWEEN ? AND ?;";
  }
  else if(trait.equals("Height"))
  {
    attribute = "pt_height";
    preStmt = "SELECT d_name, pt_height " +
    "FROM Dinosaur, physicalTraits " +
    "WHERE d_dinokey = pt_dinokey AND pt_height BETWEEN ? AND ?;";
  }
  else if(trait.equals("Weight"))
  {
    attribute = "pt_weight";
    preStmt = "SELECT d_name, pt_weight " +
    "FROM Dinosaur, physicalTraits " +
    "WHERE d_dinokey = pt_dinokey AND pt_weight BETWEEN ? AND ?;";
   
  }
  else
  {
    System.out.println("Option not found.");
    return;
  }

  pre = conn.prepareStatement(preStmt);
  System.out.print("Please enter mininum: ");
  min = input.nextFloat();
  System.out.println();

  System.out.print("Please enter maximum: ");
  max = input.nextFloat();
  System.out.println();

  pre.setFloat(1, min);
  pre.setFloat(2, max);

  result = pre.executeQuery();

  System.out.println("--------------------------------------------------------------------------------------------");
  while(result.next())
  {
    System.out.println("Name: " + result.getString(1));
    System.out.println(trait + ": " + result.getFloat(2));
    System.out.println("--------------------------------------------------------------------------------------------");


  }

  pre.close();
  result.close();


}

}