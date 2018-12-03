import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;

/* 20(potentially) Queries Done So Far 
   Implement Another One once Daniel Updates Database
   have request table include user name so User can search]
   request table by User name 
   System.out.println("--------------------------------------------------------------------------------------------"); 
   System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "============================================================================================" + ANSI_RESET);
   original line separator just in case  */
public class Dino extends DinoQueries
{
   protected static Boolean histLogin = false;
   protected static Boolean adminLogin = false;
   protected static String login;

  public static void main(String[] args) throws SQLException, InterruptedException
  {
    Connection connection = null;
    connection = connectDatabase(connection);
    Scanner input = new Scanner(System.in);
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + dinoBanner2 + ANSI_RESET);
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + dinoBanner + ANSI_RESET);
    TimeUnit.SECONDS.sleep(1);
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + jurassicDino2 + ANSI_RESET);
    try
    {
      int in = -1;
      while(true)
      {
        TimeUnit.SECONDS.sleep(2);
        System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "-------DinoDatabase Main Menu--------" + ANSI_RESET);
        System.out.println("0: Quit Program");
        System.out.println("1: Log in");
        System.out.println("2: Create Account");
        System.out.println("3: Display Search Options");
        System.out.println("4: Log Out");
        System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "-------------------------------------" + ANSI_RESET);
        System.out.print("Please enter desired option: ");

        try
        {
          in = input.nextInt();
        }
        catch(InputMismatchException e)
        {
          System.out.println("Invalid Input, Please Try Again.");
          input = new Scanner(System.in);
        }

        System.out.println();
        if(in == 0)
        {
          System.out.println("Quitting Program...Goodbye");
          disconnectDatabase(connection, input);
          break;
        }
        else if(in == 1)
        {
            userLogin(connection, input, 1);
        }
        else if(in == 2)
        {
            userLogin(connection, input, 2);
        }
        else if(in == 3)
        {
            System.out.println("Displaying Search Options");
            userMenu(connection, input);
        }
        else if(in == 4)
        {
            histLogin = false;
            adminLogin = false;
            System.out.println("Logout Successful.");
            System.out.println("Goodbye " + login);
            login = "";
        }
        else if(in > 4)
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
  public static void userLogin(Connection conn, Scanner input, int log) throws SQLException
  {
      ResultSet result = null;
      PreparedStatement pre = null;

      String password, type = "";
      String quickFix = input.nextLine();


      System.out.print("Please Enter User Name: ");
      login = input.nextLine();
      System.out.println("");

      System.out.print("Please Enter User Password: ");
      password = input.nextLine();
      System.out.println("");
      
      if(log == 1)
      {
        pre = conn.prepareStatement(logCheck);
        pre.setString(1, login);
        pre.setString(2, password);
        result = pre.executeQuery();

        if(result.next())
        {
          type = result.getString("u_type");
          if(type.equals("HIST"))
            histLogin = true;
          else if(type.equals("ADMIN"))
            adminLogin = true;

          System.out.println("Login Successful.");
          System.out.println("Welcome " + login);
        }
       else
        System.out.println("User Name or Password is Incorrect.");
      }
      else if(log == 2)
      {
        pre = conn.prepareStatement(logCheck2);
        pre.setString(1, login);
        result = pre.executeQuery();
        if(result.next())
          System.out.println("Username already taken.");
        else
        {
          pre = conn.prepareStatement(logCreate);
          pre.setString(1, login);
          pre.setString(2, password);
          pre.setString(3, "USER");
          pre.executeUpdate();
          System.out.println("Account Created.");
          System.out.println("Welcome " + login);
        }
      }
      else 
          System.out.println("Error executing login/ account creation");


  }
  public static void userTableInfo(Connection conn, Scanner input, String dinoName) throws SQLException
  {
    String quickFix = input.nextLine();
    while(true)
    {
      String tableName = "";
      System.out.println("Which Information would you like?");
      System.out.println("Options: dinosaur, fossil, physical traits, pronunciation, taxonomy, time period ");
      System.out.println("Enter 'exit' to stop");
      
      System.out.println("Enter Table: ");
      tableName = input.nextLine();
      System.out.println();

      if(tableName.equals("exit"))
        return;
      else
        getTableInfo(conn, input, tableName, dinoName);
    }

  }
 
  public static void getTableInfo(Connection conn, Scanner input, String table, String dinosaur) throws SQLException
  {
     ResultSet result = null;
     Statement stmt = conn.createStatement();
     PreparedStatement pre = null;
     String res = "";
 
     table = table.toLowerCase();
     
     if(table.equals("dinosaur"))
     {
       if(dinosaur.equals("all"))
        res = selDino;
      else
       res =  "select * " +              
              "from Dinosaur " +
              "where d_name = \'"+dinosaur+"\'";
             
       result = stmt.executeQuery(res);
       System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       while(result.next())
       {
         System.out.println("Dino key: " + result.getInt(1));
         System.out.println("Name: " + result.getString(2));  
         System.out.println("Diet: " + result.getString(3)); 
         System.out.println("Time Period: " + result.getString(4));  
         System.out.println("Description: " + result.getString(5)); 
         System.out.println("Type: " + result.getString(6)); 
         System.out.println("Habitat Key: " + result.getInt(7));
         System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       }
 
     }
     else if(table.equals("fossil"))
     {
       if(dinosaur.equals("all"))
        res =  selFossil;
       else
        res = "select d_name, f_fossilData, f_fossilEvidence, f_period from Dinosaur, fossil where d_dinokey = f_dinokey and d_name = \'"+dinosaur+"\'";
       
        result = stmt.executeQuery(res);
 
  
        System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       while(result.next())
       {
         System.out.println("Name: " + result.getString(1)); 
         System.out.println("Fossil Data: " + result.getString(2));
         System.out.println("Fossil Evidence: " + result.getString(3));
         System.out.println("Time Period: " + result.getString(4));
         System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       }
     }
     else if(table.equals("physical traits"))
     {
       if(dinosaur.equals("all"))
        res = selPhysTrait;
       else
        res = "select * from physicalTraits where pt_name = \'"+dinosaur+"\'"; 

       result = stmt.executeQuery(res);
       System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       while(result.next())
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
         System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       }
     }
     else if(table.equals("pronunciation"))
     {
       if(dinosaur.equals("all"))
        res = selPronounce;
       else
        res = "select * from pronunciation where p_name = \'"+dinosaur+"\'";
       result = stmt.executeQuery(res);
       System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       while(result.next())
       {
         System.out.println("Name: " + result.getString(1));
         System.out.println("Enunciation: " + result.getString(2));
         System.out.println("Meaning: " + result.getString(3));
         System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       }
     }
     else if(table.equals("taxonomy"))
     {
       if(dinosaur.equals("all"))
        res = selTax;
       else
        res = "select * from taxonomy where t_genus = \'"+dinosaur+"\'";
       
       result = stmt.executeQuery(res);
       System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       while(result.next())
       {
         System.out.println("Genus: " + result.getString(1));
         System.out.println("Species: " + result.getString(2));
         System.out.println("Family: " + result.getString(3));
         System.out.println("Order: " + result.getString(4));
         System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       }
     }
     else if(table.equals("time period"))
     {
       if(dinosaur.equals("all"))
        res = selTime;
       else
        res = "select tp_comment, tp_name from Dinosaur, timeperiod where d_timeperiod = tp_name and d_name = \'"+dinosaur+"\'";
       
       result = stmt.executeQuery(res);
       System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       while(result.next())
       {
         System.out.println("Time Period: " + result.getString(2));
         System.out.println("Info: " + result.getString(1));
         System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
       }
     }
     else if(table.equals("requests") && (histLogin || adminLogin))
     {
       if(dinosaur.equals("all"))
       {
        res = selRequest;
        result = stmt.executeQuery(res);
       }
       else if(dinosaur.equals(login))
       {
         pre = conn.prepareStatement(reqFind);
         pre.setString(1, login);
         result = pre.executeQuery();
       }
       else
       {
        pre = conn.prepareStatement(selRequestSpec);
        pre.setString(1, dinosaur);
        result = pre.executeQuery();
       }
        System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
        while(result.next())
        {
          System.out.println("Name: " + result.getString(1));
          System.out.println("Table Name: " + result.getString(2));
          System.out.println("Comment 1: " + result.getString(3));
          System.out.println("Comment 2: " + result.getString(4));
          System.out.println("Update Status: " + result.getString(5));
          System.out.println("User Name: " + result.getString(6));
          System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
        }
     }
     else 
     {
      
       System.out.println("Invalid Table, please choose from available options.");
       return;
     }
 
     stmt.close();
     result.close();
  }
  public static void userMenu(Connection conn, Scanner input) throws SQLException, InterruptedException
  {
    int in = -1;
    while(true)
    {
        TimeUnit.SECONDS.sleep(2);
        System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "-------User Menu--------" + ANSI_RESET);
        System.out.println("0: Exit User Menu");
        System.out.println("1: I'm Feeling Lucky");
        System.out.println("2: Display Complete Dinosaur Data");
        System.out.println("3: Search by Specific Dinosaur");
        System.out.println("4: Search by Species");
        System.out.println("5: Longest Dinosaur from each Habitat");
        System.out.println("6: Search Top Heaviest Dinosaurs");
        System.out.println("7: Search by Mininum Height");
        System.out.println("8: Number of Dinosaurs based on Habitat & Diet");
        System.out.println("9: Search number of Dinosaurs based on type");
        System.out.println("10: Longest Dinosaur in Database");
        System.out.println("11: Display Dinosaurs in between a selected range");
        System.out.println("12: Display Dinosaur Species based on habitat");
        System.out.println("13: Display Dinosaurs based on Body Type");
        System.out.println("14: Display Dinosaurs based on Mouth Type, Diet Type, and Movement");
        if(histLogin || adminLogin)
        {
            System.out.println("15: Submit a Request");
            System.out.println("16: Look up Complete Request List");
            System.out.println("17: Look up Request Update Status by Dinosaur");
            System.out.println("18: Look up Own Request Status");        
        }
        if(adminLogin)
        {
          System.out.println("19: Insert Dinosaur Into Database.");
          System.out.println("20: Update Dinosaur Info from Database.");
          System.out.println("21: Delete Dinosaur From Database.");
          System.out.println("22: Change User Status.");
        }
        System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "------------------------" + ANSI_RESET);
        System.out.print("Please enter desired option: ");

        try
        {
          in = input.nextInt();
        }
        catch(InputMismatchException e)
        {
          System.out.println("Invalid Input, Please Try Again.");
          input = new Scanner(System.in);
        }
        System.out.println();

        if(in == 0){return;}
        else if(in == 1){userQuery1(conn, input);}
        else if(in == 2){userTableInfo(conn, input, "all");}
        else if(in == 3){userQuery3(conn, input);}
        else if (in == 4){userQuery4(conn,input);}
        else if (in == 5){userQuery5(conn);}
        else if(in == 6){userQuery6(conn, input);}
        else if(in == 7){userQuery7(conn, input);}
        else if(in == 8){userQuery8(conn, input);}
        else if(in == 9){userQuery9(conn, input);}
        else if(in == 10){userQuery10(conn);}
        else if(in == 11){userQuery11(conn, input);}
        else if(in == 12){userQuery12(conn, input);}
        else if(in == 13){userQuery13(conn, input);}
        else if(in == 14){userQuery14(conn, input);}
        if(histLogin || adminLogin)
        {
          if(in == 15){histQuery1(conn, input);}
          else if(in == 16){getTableInfo(conn, input, "requests", "all");}
          else if(in == 17){histQuery2(conn, input);}
          else if(in == 18){getTableInfo(conn, input, "requests", login);}
        }
        if(adminLogin)
        {
          if(in == 19){insertData(conn, input);}
          else if(in == 20){updateData(conn, input);}
          else if(in == 21){deleteData(conn, input);}
          else if(in == 22){updateUser(conn, input);}
        }
        if(histLogin)
          if(in > 18){System.out.println("Invalid Option, Please Try Again.");}
        if(adminLogin)
          if(in > 22){System.out.println("Invalid Option, Please Try Again.");}
        if(!histLogin && !adminLogin)
          if(in > 14){System.out.println("Invalid Option, Please Try Again.");}


    }
  }
  public static void userQuery1(Connection conn, Scanner input) throws SQLException
  {
    ResultSet result = null;
    Statement stmt = conn.createStatement();
    int maxKey = 0;
    int random = 0;
    String res = "";
    String dinoName = "";
    String in = "";

    res = maxDinoKey; //4
    result = stmt.executeQuery(res);
    if(result.next())
    {
      maxKey = result.getInt(1);
    }

    random = ThreadLocalRandom.current().nextInt(1, maxKey + 1);

    res = "select d_name from Dinosaur where d_dinokey = \'"+random+"\'";
    result = stmt.executeQuery(res);
    if(result.next())
    {
      dinoName = result.getString(1);
      System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
      System.out.println("Dinosaur: " + result.getString(1));
      System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
    }

    stmt.close();
    result.close();

    System.out.println("Would you like more information on " + dinoName +"?");
    System.out.println("1: Yes, 2: No");
    int answer = input.nextInt();
    if(answer == 1)
    {
      userTableInfo(conn, input, dinoName);
    }
    else if(answer == 2)
    {
      return;
    }
    else 
    {
      System.out.println("Invalid Number.");
      //return;
    }


    
  }
  public static void userQuery3(Connection conn, Scanner input) throws SQLException
  {

    ResultSet result = null; //initialize ResultSet
    PreparedStatement pre = null; //initialize prepared statement
    Statement stmt = conn.createStatement(); //initialize statement
    int dinoKey = 0; //initialize variables
    String dinoName = "";
    String res = "";

    String quickFix = input.nextLine(); //for some reason input skips over, doing this fixes it.

    System.out.println("Please Enter Dinosaur Name:");
    dinoName = input.nextLine();
    dinoName = formatString(dinoName); //format into correct form

    pre = conn.prepareStatement(dinoCheck);
    pre.setString(1, dinoName);
    result = pre.executeQuery();
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
    
   
    pre = conn.prepareStatement(findDino);
    pre.setString(1, "%" + dinoKey + "%"); //do this to fill in LIKE keyword
    pre.setInt(2, dinoKey);
    pre.setString(3, dinoName);

    result = pre.executeQuery(); //execute prepared statement
    //print out results
    if(result.next())
    {
      System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
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
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);

    pre.close(); //close statements and resultset
    stmt.close();
    result.close();

    System.out.println("Would you like more information on " + dinoName +"?");
    System.out.println("1: Yes, 2: No");
    int answer = input.nextInt();
    if(answer == 1)
    {
      userTableInfo(conn, input, dinoName);
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

 public static void userQuery4(Connection conn, Scanner input) throws SQLException
 {
   ResultSet result = null; //initialize results and prepared statement
   PreparedStatement pre = null;
   Statement stmt = conn.createStatement();

   String preStmt = findSpecies;
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
   System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
   while(result.next())
   {
    
     System.out.println("Name: " + result.getString(1));
     System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
   }

   pre.close();
   stmt.close();
   result.close();
}

public static void userQuery5(Connection conn) throws SQLException
{
  System.out.println("Printing out longest dinosaurs of each habitat...");
  ResultSet result = null;
  Statement stmt = conn.createStatement();

  String res = longDinos;

  result = stmt.executeQuery(res);

  System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
  while(result.next())
  {
    System.out.println("Habitat: " + result.getString(1));
    System.out.println("Dinosaur: " + result.getString(2));
    System.out.println("Length: " + result.getFloat(3));
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
  }

  stmt.close();
  result.close();

}

public static void userQuery6(Connection conn, Scanner input) throws SQLException
{
  ResultSet result = null; //initialize results and prepared statement
  PreparedStatement pre = null;
  int topNum = 0;
  int rank = 1;

  String preStmt = topHeavy;
  pre = conn.prepareStatement(preStmt);

  System.out.println("Please Enter Number:");
  topNum = input.nextInt();

  pre.setInt(1, topNum);
  result = pre.executeQuery();

  System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
  while(result.next())
  {
    System.out.println("Rank: " + rank);
    System.out.println("Habitat: " + result.getString(1));
    System.out.println("Dinosaur: " + result.getString(2));
    System.out.println("Weight: " + result.getFloat(3));
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
    rank++;

  }

  pre.close();
  result.close();
}

public static void userQuery7(Connection conn, Scanner input) throws SQLException
{
  ResultSet result = null; //initialize results and prepared statement
  PreparedStatement pre = null;
  float topNum = 0;

  String preStmt = minHeight;

  pre = conn.prepareStatement(preStmt);

  System.out.println("Please Enter Number:");
  topNum = input.nextFloat();

  pre.setFloat(1, topNum);
  result = pre.executeQuery();

  System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
  while(result.next())
  {
    System.out.println("Dinosaur: " + result.getString(1));
    System.out.println("Height: " + result.getFloat(2));
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);

  }

  pre.close();
  result.close();
}

public static void userQuery8(Connection conn, Scanner input) throws SQLException
{
  ResultSet result = null; //initialize results and prepared statement
  PreparedStatement pre = null;
  Statement stmt = conn.createStatement();

  String quickFix = input.nextLine();

  String res = "";
  String habName = "";
  String dietName = "";
  String preStmt = numDinosHabDiet;

  pre = conn.prepareStatement(preStmt);
  System.out.println("Options: forest, aquatic, desert, plains, arid grassland, mountain,");
  System.out.println("canyon, river, woodland, swamp, floodplain, unknown");
  System.out.print("Please Select Habitat: ");

  habName = input.nextLine();
  habName = habName.toLowerCase();
  System.out.println();

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
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
    System.out.println("Number of Dinosaurs with " + dietName + " diet and "+ habName + " habitat: " + result.getInt(1));
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
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

public static void userQuery9(Connection conn, Scanner input) throws SQLException
{
  ResultSet result = null;
  PreparedStatement pre = null;

  String dinoType = "";
  String quickFix = input.nextLine();
  String preStmt = numDinosType;
  pre = conn.prepareStatement(preStmt);

  System.out.println("Please enter Dinosaur Type:");
  System.out.println("Options: Land, Air, Sea");

  dinoType = input.nextLine();
  dinoType = dinoType.toLowerCase();

  pre.setString(1, "%" + dinoType + "%"); //do this to fill in LIKE keyword

  result = pre.executeQuery();
  if(result.next())
  {
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
    System.out.println("Number of dinosaurs of " + dinoType + " type: " + result.getInt(1));
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
  }
  else
  {
    System.out.println("Error in executing Query");
    return;
  }

  pre.close();
  result.close();
}

public static void userQuery10(Connection conn) throws SQLException
{
  ResultSet result = null;
  Statement stmt = conn.createStatement();

  String res = longestDino;

  result = stmt.executeQuery(res);
  
  System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
  while(result.next())
  {
    System.out.println("Name: " + result.getString(1));
    System.out.println("Enunciation: " + result.getString(2));
    System.out.println("Length: " + result.getInt(3));
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
  }
  stmt.close();
  result.close();
}

public static void userQuery11(Connection conn, Scanner input) throws SQLException
{
  ResultSet result = null;
  PreparedStatement pre = null;

  float min, max = 0;
  String quickFix = input.nextLine();
  String trait, preStmt = "";

  System.out.println("Please select  measurement type: ");
  System.out.println("Options: Length, Height, Weight");
  trait = input.nextLine();
  trait = formatString(trait);

  if(trait.equals("Length"))
    preStmt = dinoLength;
  else if(trait.equals("Height"))
    preStmt = dinoHeight;
  else if(trait.equals("Weight"))
    preStmt = dinoWeight;
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

  System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
  while(result.next())
  {
    System.out.println("Name: " + result.getString(1));
    System.out.println(trait + ": " + result.getFloat(2));
    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);


  }

  pre.close();
  result.close();
}

  public static void userQuery12(Connection conn, Scanner input) throws SQLException
  {
    ResultSet result = null;
    PreparedStatement pre = null;
    Statement stmt = conn.createStatement();
    
    String quickFix = input.nextLine();
    String res = "";
    String habName = "";
    
    String preStmt = speciesHab;
    pre = conn.prepareStatement(preStmt);
    
    System.out.println("Options: forest, aquatic, desert, plains, arid grassland, mountain,");
    System.out.println("canyon, river, woodland, swamp, floodplain, unknown");
    System.out.print("Please Select Habitat: ");
  
    habName = input.nextLine();
    habName = habName.toLowerCase();
    System.out.println();

    res = "select h_name from habitat where h_name = \'"+habName+"\'";
    result = stmt.executeQuery(res);
    if(result.next())
      habName = result.getString("h_name");
    else
    {
      System.out.println("Habitat not found");
      return;
    }
    
    pre.setString(1, habName);

    result = pre.executeQuery();

    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
    while(result.next())
    {
        System.out.println("Name: " + result.getString(1));
        System.out.println("Species: " + result.getString(2));
        System.out.println("Habitat: " + habName);
        System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
    }

    stmt.close();
    result.close();
    pre.close();
    
  
  }

  public static void userQuery13(Connection conn, Scanner input) throws SQLException
  {
    ResultSet result = null;
    PreparedStatement pre = null;

    String preStmt = dinoBodyType;

    String quickFix = input.nextLine();
    String bodyType = "";

    System.out.print("Please Enter Desired Body Type: ");
    bodyType = input.nextLine();
    System.out.println();

    pre = conn.prepareStatement(preStmt);
    pre.setString(1, "%" + bodyType + "%");

    result = pre.executeQuery();

    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
    while(result.next())
    {
      System.out.println("Name: " + result.getString(1));
      System.out.println("Body: " + result.getString(2));
      System.out.println("Length: " + result.getFloat(3));
      System.out.println("Time Period: " + result.getString(4));
      System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
    }
    
  }

  public static void userQuery14(Connection conn, Scanner input) throws SQLException
  {
    ResultSet result = null;
    PreparedStatement pre = null;

    String quickFix = input.nextLine();
    String mvmtType, mouthType, dietType = "";

    String preStmt = dinoMvmtMouthDiet;
    pre = conn.prepareStatement(preStmt);

    System.out.println("Options: Land, Air, Water");
    System.out.print("Please Enter Movement Type: ");
    mvmtType = input.nextLine();
    mvmtType = mvmtType.toLowerCase();

    System.out.println();
    System.out.print("Please Enter Mouth Type: ");
    mouthType = input.nextLine();
    mouthType = mouthType.toLowerCase();
    System.out.println();

    System.out.print("Please Enter Diet Type: ");
    dietType = input.nextLine();
    dietType = dietType.toLowerCase();
    System.out.println();

    pre.setString(1, mvmtType);
    pre.setString(2, "%" + mouthType + "%");
    pre.setString(3, dietType);

    result = pre.executeQuery();

    System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
    while(result.next())
    {
      System.out.println("Name: " + result.getString(1));
      System.out.println("Movement Type: " + result.getString(2));
      System.out.println("Diet: " + result.getString(3));
      System.out.println("Mouth: " + result.getString(4));
      System.out.println("Time Period: " + result.getString(5));
      System.out.println(ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET);
    }

    result.close();
    pre.close();

  }

  public static void histQuery1(Connection conn, Scanner input) throws SQLException
  {

    PreparedStatement pre = null;
    String quickFix = input.nextLine();
    pre = conn.prepareStatement(reqInsert);

    String res, dinoName, tableName, comment1, comment2 = "";
    
    System.out.println("Please Enter Dinosaur Name: ");
    System.out.println("Enter N/A if not applicable");
    dinoName = input.nextLine();
    dinoName = formatString(dinoName);
    
    System.out.println("Options: Dinosaur, Fossil, Habitat, Location, Physical Traits, Pronunciation, Taxonomy, Time Period");
    System.out.print("Please Enter Table Name: ");
    tableName = input.nextLine();
    System.out.println();

    System.out.print("Please Enter 1st Comment: ");
    comment1 = input.nextLine();
    System.out.println();

    System.out.print("Please Enter 2nd Comment: ");
    comment2 = input.nextLine();
    System.out.println();

    pre.setString(1, dinoName);
    pre.setString(2, tableName);
    pre.setString(3, comment1);
    pre.setString(4, comment2);
    pre.setString(5, "f");
    pre.setString(6, login);

    pre.executeUpdate();

    System.out.println("Request Submitted.");

    pre.close();
}

  public static void histQuery2(Connection conn, Scanner input) throws SQLException
  { 
    String res, dinoName = "";
    String quickFix = input.nextLine();

    System.out.print("Please Enter Dinosaur Name: ");
    dinoName = input.nextLine();
    dinoName = formatString(dinoName);

    getTableInfo(conn, input, "requests", dinoName);
  }

}