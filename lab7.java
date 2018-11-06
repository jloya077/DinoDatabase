import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class lab7
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
        System.out.println("-------Context Menu--------");
        System.out.println("0: Quit Program");
        System.out.println("1: Connect to Database");
        System.out.println("2: Create new table 'Warehouse'");
        System.out.println("3: Create new entry for table 'Warehouse'");
        System.out.println("4: Call Query 1");
        System.out.println("5: Call Query 2");
        System.out.println("6: Call Query 3");
        System.out.println("7: Call Query 4");
        System.out.println("8: Call Query 5");
        System.out.println("9: Call Query 6");
        System.out.println("10: Disconnect from Database");
        System.out.println("Please enter desired option:");
        in = input.nextInt();
        if(in == 0)
        {
          System.out.println("Quitting Program...Goodbye");
          break;
        }
        if(in == 1)
        {
          System.out.println("Connecting to Database...");
          connection = connectDatabase(connection);
          System.out.println("Connected to Database.");

        }
        else if(in == 2)
        {
          System.out.println("Creating table 'Warehouse'");
          // create a database connection
          Statement stat = connection.createStatement();
          String tbl = createTable(); 
          stat.executeUpdate(tbl);
          stat.close();
        }
        else if(in == 3)
        {
          System.out.println("Create new Entry");
          newEntry(connection,input);
        }
        else if(in == 4)
        {
          System.out.println("Executing Query 1...");
          queryOne(connection);
        }
        else if(in == 5)
        {
          System.out.println("Executing Query 2...");
          queryTwo(connection);
        }
        else if(in == 6)
        {
          System.out.println("Executing Query 3...");
          queryThree(connection,input);

        }
        else if(in == 7)
        {
          System.out.println("Executing Query 4...");
          queryFour(connection, input);

        }
        else if(in == 8)
        {
          System.out.println("Executing Query 5...");
          queryFive(connection, input);
        }
        else if(in == 9)
        {
          System.out.println("Executing Query 6...");
          querySix(connection,input);
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
    conn = DriverManager.getConnection("jdbc:sqlite:TPCH.db");
    return conn;
  }
  public static void disconnectDatabase(Connection conn, Scanner input) throws SQLException
  {
    if(conn != null)
    {
      conn.close();
    }
  }
  public static String createTable()
  {
    String sql = "create table if not exists " + " warehouse" + "(w_warehousekey decimal(3,0) not null, "
    + " w_name char(25) not null, " + " w_supplierkey decimal(2,0) not null, " + " w_capacity decimal(6,2) not null, "
    + " w_address varchar(40) not null, " + " w_nationkey decimal(2,0) not null)";

    return sql;
  }

  public static void newEntry(Connection conn, Scanner input) throws SQLException
  {
    ResultSet result = null;
    
    String newE = "insert into warehouse " +  " values (?, ?, ?, ?, ?, ?)";
    String rs = " ";
    
    int suppKey = 0;
    int natKey = 0;
    int wKey = 0;

    PreparedStatement pre = conn.prepareStatement(newE);
    Statement stmt = conn.createStatement();

    
      result = stmt.executeQuery("select max(w_warehousekey) as maxKey from warehouse");
      if(result.next())
      {
        wKey = result.getInt("maxKey");
        wKey++;
      }
      else
      {
        wKey++; 
      }
     
    
   
    String why = input.nextLine();
    System.out.println("Please enter warehouse name: ");
    String wName = input.nextLine();

    System.out.println("Please enter supplier name: ");
    String sName = input.nextLine();
    
    rs = "select s_suppkey from supplier " + " where s_name = \'"+sName+"\'";
    result = stmt.executeQuery(rs);
    if(result.next())
    {
      suppKey = result.getInt("s_suppkey"); 
    }
    else
    {
      System.out.println("entry not found");
      return;
    }

    System.out.println("Please enter capacity number: ");
    int cap = input.nextInt();
    why = input.nextLine();

    System.out.println("Please enter address: ");
    String address = input.nextLine();

    System.out.println("Please enter nation: ");
    String nation = input.nextLine().toUpperCase();

    rs = "select n_nationkey from nation where n_name = \'"+nation+"\'";
    result = stmt.executeQuery(rs);
    if(result.next())
    {
      natKey = result.getInt("n_nationkey"); 
    }
    else
    {
      System.out.println("entry not found");
      return;
    }

    pre.setInt(1, wKey);
    pre.setString(2, wName);
    pre.setInt(3, suppKey);
    pre.setInt(4, cap);
    pre.setString(5, address);
    pre.setInt(6, natKey);

    pre.executeUpdate();
    System.out.println("Update Executed.");

    pre.close();
    stmt.close();
  }

  public static void queryOne(Connection conn) throws SQLException
  {
    Statement stmt = conn.createStatement();
    String rs = "select s_name from supplier, (select min(SQ1.wCount) as minCount, s_name as minName from supplier, warehouse, (select count(w_warehousekey) as wCount from warehouse)as SQ1 where s_suppkey = w_supplierkey)as SQ2 where s_name = SQ2.minName;";
    ResultSet result = stmt.executeQuery(rs);
    String name = result.getString("s_name");
    System.out.println(name);
    
    stmt.close();
    result.close();
  }

  public static void queryTwo(Connection conn) throws SQLException
  {
    Statement stmt = conn.createStatement();
    String rs = "select s_name, max(w_capacity) as maxCount from warehouse, supplier where w_supplierkey = s_suppkey group by s_name;";
    String name = "";
    int max = 0;
    ResultSet result = stmt.executeQuery(rs);
    while(result.next())
    {
     name = result.getString("s_name");
     max = result.getInt("maxCount");

     System.out.println("Supplier Name: " + name);
     System.out.println("Maximum Capacity: " + max);
    }
    stmt.close();
    result.close();

  }

  public static void queryThree(Connection conn, Scanner input) throws SQLException
  {
    Statement stmt = conn.createStatement();
    
    System.out.println("Enter a value: ");
    int value = input.nextInt();
    String name = "";

    String rs = "select w_name from warehouse, nation, region where w_nationkey = n_nationkey and n_regionkey and r_regionkey and r_name = 'EUROPE' and w_capacity < '"+value+"' group by w_name;";

    ResultSet result = stmt.executeQuery(rs);
    while(result.next())
    {
      name = result.getString("w_name");
      System.out.println("Warehouse Name: " + name);
    }
    stmt.close();
    result.close();
  }

  public static void queryFour(Connection conn, Scanner input) throws SQLException
  {
    Statement stmt = conn.createStatement();
    String why = input.nextLine(); //have to do it for input to work properly
    System.out.println("Enter a supplier name: ");
    String supp = input.nextLine();

    String rs = "select distinct s_name from supplier, warehouse, (select sum(w_capacity) as wCap, s_name as supp from warehouse, supplier where w_supplierkey = s_suppkey group by s_name) as SQ1,(select sum(ps_availqty) as sumQty, s_name as supp2 from supplier, partsupp where ps_suppkey = s_suppkey group by s_name) as SQ2 where SQ1.wCap >= SQ2.sumQty and SQ2.supp2 = SQ1.supp and SQ1.supp = s_name and s_name = \'"+supp+"\' ;";

    ResultSet result = stmt.executeQuery(rs);
    if(result.next())
    {
      System.out.println("The supplier " + supp + " is capable of fitting all of its products in its warehouses."); 
    }
    else
    {
      System.out.println("entry not found or supplier " + supp + " is not capable of fitting all of its products in its warehouses.");
    }

    stmt.close();
    result.close();

  }

  public static void queryFive(Connection conn, Scanner input) throws SQLException
  {
    Statement stmt = conn.createStatement();
    String why = input.nextLine(); //for some reason program skips over, this helps remedy that

    System.out.println("Enter a nation name: ");
    String nation = input.nextLine().toUpperCase();

    String wName = ""; //initializing variables
    String nName = "";
    int wCap = 0;

    String rs ="select n_name, w_name, w_capacity from warehouse, nation where w_nationkey = n_nationkey and n_name = \'"+nation+"\' group by n_name, w_name order by w_capacity desc;";
    ResultSet result = stmt.executeQuery(rs);
    if(!result.next())
      System.out.println("Nation not found.");
    
    result = stmt.executeQuery(rs);
    while(result.next())
    {
      wName = result.getString("w_name");
      wCap = result.getInt("w_capacity");
      nName = result.getString("n_name");

      System.out.println(nName + " " + wName + " " + wCap);
    }
   
    
    stmt.close();
    result.close();
  }
  public static void querySix(Connection conn, Scanner input) throws SQLException, InterruptedException
  {
    Statement stmt = conn.createStatement();
    ResultSet result = null;
    String rs = "";
    String why = input.nextLine(); //for some reason program skips over, this helps remedy that
    int aKey, bKey = 0;

    System.out.println("Enter acquring supplier: ");
    String aName = input.nextLine();
    rs = "select s_suppkey from supplier " + " where s_name = \'"+aName+"\'";
    result = stmt.executeQuery(rs);
    if(result.next())
    {
      aKey = result.getInt("s_suppkey"); 
    }
    else
    {
      System.out.println("entry not found");
      System.out.println("Cancelling Update...");
      return;
    }

    System.out.println("Enter acquired supplier: ");
    String bName = input.nextLine();
    rs = "select s_suppkey from supplier " + " where s_name = \'"+bName+"\'";
    result = stmt.executeQuery(rs);
    if(result.next())
    {
      bKey = result.getInt("s_suppkey"); 
    }
    else
    {
      System.out.println("entry not found");
      System.out.println("Cancelling Update...");
      return;
    }

    rs = "update warehouse set w_supplierkey = '"+aKey+"'  where w_supplierkey = '"+bKey+"'";
    stmt.executeUpdate(rs);
    System.out.println("Executing update...");
    TimeUnit.SECONDS.sleep(1);
    System.out.println("Update Executed.");

    stmt.close(); //closing necessary things
    result.close();
  }
  

}