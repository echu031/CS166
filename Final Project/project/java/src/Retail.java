/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Retail {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   // Stores the id of the logged in user
   public String userId;

   /**
    * Creates a new instance of Retail shop
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Retail(String dbname, String dbport, String user, String passwd) throws SQLException {
      this.userId = "-1";

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Retail

   // Method to calculate euclidean distance between two latitude, longitude pairs. 
   public double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Retail.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Retail esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Retail object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Retail (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (esql.userId != "-1") {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Stores within 30 miles");
                System.out.println("2. View Product List");
                System.out.println("3. Place a Order");
                System.out.println("4. View 5 recent orders");

                //the following functionalities basically used by managers
                System.out.println("5. Update Product");
                System.out.println("6. View 5 recent Product Updates Info");
                System.out.println("7. View 5 Popular Items");
                System.out.println("8. View 5 Popular Customers");
                System.out.println("9. Place Product Supply Request to Warehouse");
		System.out.println("10. View Store Order Info");
		System.out.println("11. Update Users (Admin Only)");

                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewStores(esql); break;
                   case 2: viewProducts(esql); break;
                   case 3: placeOrder(esql); break;
                   case 4: viewRecentOrders(esql); break;
                   case 5: updateProduct(esql); break;
                   case 6: viewRecentUpdates(esql); break;
                   case 7: viewPopularProducts(esql); break;
                   case 8: viewPopularCustomers(esql); break;
                   case 9: placeProductSupplyRequests(esql); break;
		   case 10: viewStoreOrderInfo(esql); break;
		   case 11: updateUsers(esql); break;

                   case 20: usermenu = false; esql.userId = "-1"; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /* 
    * Checks if string can be converted to an integer
    * @boolean
    **/
   public static boolean isInteger(String s) {
	try {
		Integer.parseInt(s);
		return true;
	} catch (Exception e) {
		return false;
	}
   }

   /*
    * Checks if string can be converted to a float
    * @boolean
    **/
   public static boolean isFloat(String s) {
        try {   
                Float.parseFloat(s);
                return true;
        } catch (Exception e) {
                return false;
        }
   }

   /*
    * Checks that user is of type manager or admin
    * @boolean
    **/ 
   public static boolean isManager(Retail esql) {
	try {
		String query = String.format("SELECT type FROM USERS WHERE userID = '%s'", esql.userId);
		List<List<String>> result = esql.executeQueryAndReturnResult(query);
		String type = result.get(0).get(0);
		if (type.contains("customer")) return false;
	} catch (Exception e) {
		System.out.println(e.getMessage());
	}
	return true;
   }

   /*
    * Checks that user is of type admin
    * @boolean
    **/
   public static boolean isAdmin(Retail esql) {
	try {
		String query = String.format("SELECT type FROM USERS WHERE userID = '%s'", esql.userId);
                List<List<String>> result = esql.executeQueryAndReturnResult(query);
                String type = result.get(0).get(0);
                if (type.contains("admin")) return true;
	} catch (Exception e) {
                System.out.println(e.getMessage());
        }
        return false;
   }

   /*
    * Get store ID from user
    * @String; @null if user input is invalid
    **/
   public static String getStoreId(Retail esql) {
	try {
		System.out.print("\tEnter Store ID: ");
        	String storeId = in.readLine();

		// Check that input is in correct format & that the store exists
                if (isInteger(storeId)) {
			String storeQuery = String.format("SELECT storeID FROM STORE WHERE storeID = '%s'", storeId);
                        int resultNum = esql.executeQuery(storeQuery);
                        if (resultNum == 0) System.out.println("Sorry, no store exists with this ID.");
			else return storeId;
		} else {
			System.out.println("Invalid Store ID; Must be an integer.");
		}
	} catch (Exception e) {
		System.err.println(e.getMessage());
	}
	return null;
   }

   /*
    * Creates a new user
    **/
   public static void CreateUser(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();

	 // Validate name
	 while (name.isEmpty()) {
		System.out.println("Invalid Name; Must be at least one character long; Please Try Again...");
		System.out.print("\tEnter name: ");
		name = in.readLine();
	 }

         System.out.print("\tEnter password: ");
         String password = in.readLine();
	
	 // Validate password
	 while (password.isEmpty()) {
		System.out.println("Invalid Password; Must be at least one character long; Please Try Again...");
		System.out.print("\tEnter password: ");
		password = in.readLine();
	 }

         System.out.print("\tEnter latitude: ");   
         String latitude = in.readLine();       //enter lat value between [0.0, 100.0]

	 // Validate latitude
	 float latFloat = Float.parseFloat(latitude);
	 while (latFloat < 0 || latFloat > 100) {
		System.out.println("Invalid Latitude; Must be between [0.0, 100.0]");
		System.out.println("Please Try Again...");
		System.out.print("\tEnter latitude: ");
		latitude = in.readLine();
		latFloat = Float.parseFloat(latitude);
	 }

         System.out.print("\tEnter longitude: ");  //enter long value between [0.0, 100.0]
         String longitude = in.readLine();

	 // Validate longitude
	 float longFloat = Float.parseFloat(longitude);
	 while (longFloat < 0 || longFloat > 100) {
		System.out.println("Invalid Longitude; Must be between [0.0, 100.0]");
		System.out.println("Please Try Again...");
		System.out.print("\tEnter longitude: ");
		longitude = in.readLine();
		longFloat = Float.parseFloat(longitude);
	 }

	 // Return error if user already exists
	 String checkUser = String.format("SELECT * FROM USERS WHERE name = '%s' AND password = '%s'", name, password);
	 if (esql.executeQuery(checkUser) > 0) {
		System.out.println("User already exists!");
		return;
	 }
         
         String type="customer";
	 String query = String.format("INSERT INTO USERS (name, password, latitude, longitude, type) VALUES ('%s','%s', %s, %s,'%s')", name, password, latitude, longitude, type);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser


   /*
    * Verify log in credentials for an existing user. Sets Retail variable to user's ID, when valid.
    * @return User name if valid; Null if the user does not exist
    **/
   public static String LogIn(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USERS WHERE name = '%s' AND password = '%s'", name, password);

	 List<List<String>> res = esql.executeQueryAndReturnResult(query);
         int userNum = esql.executeQuery(query);
	 if (userNum > 0) {
		// Store userId of the logged in user
		esql.userId = res.get(0).get(0);
		return name;
	 }
	 
	 // Login failed
	 System.out.println("Name or Password Incorrect!");
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definition go in here

   /*
    * Prints all stores located within 30 miles of the user.
    **/  
   public static void viewStores(Retail esql) {
	try {
		// Use sql function/query to find all stores within 30 miles of the user
		// Processing is done on db
		String select = "SELECT s.storeID, s.name, s.latitude, s.longitude, s.managerID, s.dateEstablished AS Established FROM STORE s, USERS u WHERE ";
		String condition1 = String.format("u.userID = '%s' AND ", esql.userId);
		String condition2 = "calculate_distance(u.latitude, u.longitude, s.latitude, s.longitude) <= 30";
		String query = select + condition1 + condition2;
		esql.executeQueryAndPrintResult(query);

	} catch (Exception e) {
		System.err.println(e.getMessage());
	}
   }

   /*
    * Prints product information of all products at a given store.
    **/
   public static void viewProducts(Retail esql) {
	try {
		// Get store ID from user
		String storeId = getStoreId(esql);
		
		if (storeId != null) {
			String select = "SELECT productName AS Name, numberOfUnits AS Units, pricePerUnit AS Price ";
			String from = "FROM PRODUCT ";
			String where = String.format("WHERE storeID = '%s'", storeId);
			String query = select + from + where;
			esql.executeQueryAndPrintResult(query);
		}
	} catch (Exception e) {
		System.err.println(e.getMessage());
	}
   }

   public static void placeOrder(Retail esql) {
	try {
		// Get store ID from user
                String storeId = getStoreId(esql);

		// Check that store is within 30 miles of user
		if (storeId != null) {
			String distCondition1 = String.format("SELECT * FROM STORE s, USERS u WHERE u.userID = '%s' AND s.storeID = '%s' AND ", esql.userId, storeId);
			String distCondition2 = "calculate_distance(u.latitude, u.longitude, s.latitude, s.longitude) <= 30";
			String distQuery = distCondition1 + distCondition2;
			int storeResNum = esql.executeQuery(distQuery);
			if (storeResNum == 0) {
				System.out.println("Sorry, store must be within 30 miles of your location.");
				return;
			}
		}

		// Get product name 
		System.out.print("\tEnter Product: ");
		String product = in.readLine();

		// Get number of units
		System.out.print("\tEnter # of units: ");
		String units = in.readLine();
		if (!isInteger(units)) {
			System.out.println("Invalid units; Must enter an integer.");
			return;
		}

		// Check that the store has enough units of the product
		String productSelect = "SELECT pricePerUnit FROM PRODUCT ";
		String productCondition = String.format("WHERE storeID = '%s' AND productName = '%s' AND numberOfUnits >= '%s'", storeId, product, units);
		String productQuery = productSelect + productCondition;
		int productResNum = esql.executeQuery(productQuery);
		if (productResNum == 0) {
			System.out.println("Sorry, store does not have that many units of the desired product.");
			System.out.println("Use Option 2 to view the products and their available quantities from this store.");
			return;
		}

		// Insert order into Orders table
		String insert = "INSERT INTO ORDERS (customerID, storeID, productName, unitsOrdered, orderTime) ";
		String values = String.format("VALUES ('%s', '%s', '%s', '%s', CURRENT_TIMESTAMP)", esql.userId, storeId, product, units);
		String ordersInsert = insert + values;
		esql.executeUpdate(ordersInsert);
		System.out.println("Order successfully placed!");

		// Update numberOfUnits in Product table
		// TODO: May be able to implement this as a trigger (fires after order is inserted into Orders table^)
		String update = String.format("UPDATE PRODUCT SET numberOfUnits = numberOfUnits - '%s' WHERE storeID = '%s' AND productName = '%s'", units, storeId, product);
		esql.executeUpdate(update);
	} catch (Exception e) {
		System.err.println(e.getMessage());
	}
   }

   /*
    * Print Customer's 5 most recent orders
    **/
   public static void viewRecentOrders(Retail esql) {
	try {
		String select = "SELECT o.storeID, s.name, o.productName, o.unitsOrdered, o.orderTime FROM ORDERS o, STORE s ";
		String where = String.format("WHERE o.customerID = '%s' AND o.storeID = s.storeID ORDER BY o.orderTime DESC LIMIT 5", esql.userId);
		String query = select + where;
		esql.executeQueryAndPrintResult(query);
	} catch (Exception e) {
                System.err.println(e.getMessage());
        }
   }
   
   /*
    * Update product information, including: numberOfUnits and pricePerUnit.
    **/ 
   public static void updateProduct(Retail esql) {
	try {
		// Verify that user is a manager
		if (!isManager(esql)) {
			System.out.println("Sorry, you must be a manager to access this option.");
			return;
		}

		// Get store ID
		String storeId = getStoreId(esql);
		if (storeId == null) return;

		// Check that user is a manager of this store
		String userQuery = String.format("SELECT type FROM USERS WHERE userID = '%s'", esql.userId);
		List<List<String>> userResult = esql.executeQueryAndReturnResult(userQuery);
		if (userResult.get(0).get(0).contains("manager")) {
			String storeQuery = String.format("SELECT managerID FROM STORE WHERE storeID = '%s'", storeId);
			List<List<String>> storeResult = esql.executeQueryAndReturnResult(storeQuery);
			if (!storeResult.get(0).get(0).equals(esql.userId)) {
				System.out.println("Sorry, you can only update products in your own store.");
				return;
			}
		}

		// Get product name
		System.out.print("\tEnter Product: ");
		String product = in.readLine();

		// Check that the store has this product
		String productQuery = String.format("SELECT numberOfUnits FROM PRODUCT WHERE storeID = '%s' AND productName = '%s'", storeId, product);
		int productResult = esql.executeQuery(productQuery);
		if (productResult == 0) {
			System.out.println("Sorry, this store does not have that product.");
			return;
		}

		// Get number of units
		System.out.print("\tEnter # of units: ");
                String units = in.readLine();
                if (!isInteger(units)) {
                        System.out.println("Invalid units; Must enter an integer.");
                        return;
                }

		// Get price per unit
		System.out.print("\tEnter price per unit: ");
		String price = in.readLine();
		if (!isFloat(price)) {
			System.out.println("Invalid price; Must enter a floating point number.");
			return;
		}

		// Update numberOfUnits and pricePerUnit in Product table
		String update = String.format("UPDATE PRODUCT SET numberOfUnits = '%s', pricePerUnit = '%s' WHERE storeID = '%s' AND productName = '%s'", units, price, storeId, product);
		esql.executeUpdate(update);

		// Insert new tuple into ProductUpdates table
		String insert = String.format("INSERT INTO PRODUCTUPDATES (managerID, storeID, productName, updatedOn) VALUES ('%s', '%s', '%s', CURRENT_TIMESTAMP)", esql.userId, storeId, product);
		esql.executeUpdate(insert);
	} catch (Exception e) {
                System.err.println(e.getMessage());
        }
   }

   /*
    * Print Manager's/Admin's last 5 product updates.
    **/
   public static void viewRecentUpdates(Retail esql) {
	try {
		// Get manager's store ID
		String storeQuery = String.format("SELECT storeID FROM STORE WHERE managerID = '%s'", esql.userId);
		List<List<String>> storeResult = esql.executeQueryAndReturnResult(storeQuery);
		if (storeResult.size() == 0) {
			System.out.println("Sorry, you currently do not manage any stores.");
			return;
		}
		String storeId = storeResult.get(0).get(0);

		// Print 5 most recent updates
		String query = String.format("SELECT * FROM PRODUCTUPDATES WHERE storeID = '%s' ORDER BY updatedOn DESC LIMIT 5", storeId);
		esql.executeQueryAndPrintResult(query);
	} catch (Exception e) {
                System.err.println(e.getMessage());
        }
   }
   
   /*
    * Permits Admins to update user information.
    **/
   public static void updateUsers(Retail esql) {
	try {
		// Verify user is admin
		if (!isAdmin(esql)) {
                        System.out.println("Sorry, you must be an admin to access this option.");
                        return;
                }

		// Get User ID of user to edit
		System.out.print("\tEnter User ID: ");
		String user = in.readLine();

		// Check that user exists
		String userQuery = String.format("SELECT * FROM USERS WHERE userID = '%s'", user);
		int userResult = esql.executeQuery(userQuery);
		if (userResult == 0) {
			System.out.println("Sorry, a user does not exist with that ID.");
			return;
		}

		// Get Name
		System.out.print("\tEnter Name: ");
		String name = in.readLine();
		while (name.isEmpty()) {
                	System.out.println("Invalid Name; Must be at least one character long; Please Try Again...");
                	System.out.print("\tEnter name: ");
                	name = in.readLine();
         	}

		// Get Latitude
		System.out.print("\tEnter latitude: ");   
         	String latitude = in.readLine();
		float latFloat = Float.parseFloat(latitude);
	 	while (latFloat < 0 || latFloat > 100) {
			System.out.println("Invalid Latitude; Must be between [0.0, 100.0]");
			System.out.println("Please Try Again...");
			System.out.print("\tEnter latitude: ");
			latitude = in.readLine();
			latFloat = Float.parseFloat(latitude);
	 	}

		// Get Longitude
		System.out.print("\tEnter longitude: ");  //enter long value between [0.0, 100.0]
         	String longitude = in.readLine();
		float longFloat = Float.parseFloat(longitude);
	 	while (longFloat < 0 || longFloat > 100) {
			System.out.println("Invalid Longitude; Must be between [0.0, 100.0]");
			System.out.println("Please Try Again...");
			System.out.print("\tEnter longitude: ");
			longitude = in.readLine();
			longFloat = Float.parseFloat(longitude);
	 	}

		// Get Type
		System.out.print("\tEnter User Type: ");
		String type = in.readLine();
		while (!(type.equals("customer") || type.equals("manager") || type.equals("admin"))) {
			System.out.println("Invalid Type; Must be 'customer', 'manager', or 'admin'");
			System.out.println("Please Try Again...");
			System.out.print("\tEnter User Type: ");
			type = in.readLine();
		}

		// Update user
		String update = String.format("UPDATE USERS SET name = '%s', latitude = '%s', longitude = '%s', type = '%s'", name, latitude, longitude, type);
		esql.executeUpdate(update);
	} catch (Exception e) {
                System.err.println(e.getMessage());
        }
   }

   /*
    * Allow managers to see order information for their store.
    **/
   public static void viewStoreOrderInfo(Retail esql) {
	try {
		// Check that user is a manager
		if (!isManager(esql)) {
			System.out.println("Sorry, must be a manager to use this function.");
			return;
		}

		// Get store ID
		String storeQuery = String.format("SELECT storeID FROM STORE WHERE managerID = '%s'", esql.userId);
		List<List<String>> storeResult = esql.executeQueryAndReturnResult(storeQuery);
		if (storeResult.size() == 0) {
			System.out.println("Sorry, you currently do not manage any stores.");
			return;
		}
		String storeId = storeResult.get(0).get(0);

		// Print store order info
		String select = "SELECT o.orderNumber AS OrderNumber, u.name AS CustomerName, o.storeID AS StoreID, o.productName AS ProductName, o.orderTime AS Date ";
		String from = "FROM ORDERS o, USERS u ";
		String where = String.format("WHERE o.storeID = '%s' AND o.customerID = u.userID", storeId);
		String query = select + from + where;
		esql.executeQueryAndPrintResult(query);
	} catch (Exception e) {
                System.err.println(e.getMessage());
        }
   }

   /*
    * Allow managers to view the 5 most popular products ordered from their store.
    **/ 
   public static void viewPopularProducts(Retail esql) {
	try {
		// Check that user is a manager
		if (!isManager(esql)) {
			System.out.println("Sorry, must be a manager to use this function.");
			return;
		}

		// Get Store ID
		String storeQuery = String.format("SELECT storeID FROM STORE WHERE managerID = '%s'", esql.userId);
                List<List<String>> storeResult = esql.executeQueryAndReturnResult(storeQuery);
                if (storeResult.size() == 0) {
                        System.out.println("Sorry, you currently do not manage any stores.");
                        return;
                }
                String storeId = storeResult.get(0).get(0);

		// Get popular products from store
		String select = "SELECT productName, COUNT(*) AS NumOfOrders FROM ORDERS ";
		String condition = String.format("WHERE storeID = '%s' ", storeId);
		String group = "GROUP BY productName ORDER BY COUNT(*) DESC LIMIT 5";
		String query = select + condition + group;
		esql.executeQueryAndPrintResult(query);
	} catch (Exception e) {
                System.err.println(e.getMessage());
        }
   }

   /*
    * Allow managers to view their 5 most popular customers.
    **/
   public static void viewPopularCustomers(Retail esql) {
	try {
		// Check that user is a manager
		if (!isManager(esql)) {
                        System.out.println("Sorry, must be a manager to use this function.");
                        return;
                }

		// Get Store ID
		String storeQuery = String.format("SELECT storeID FROM STORE WHERE managerID = '%s'", esql.userId);
                List<List<String>> storeResult = esql.executeQueryAndReturnResult(storeQuery);
                if (storeResult.size() == 0) {
                        System.out.println("Sorry, you currently do not manage any stores.");
                        return;
                }
                String storeId = storeResult.get(0).get(0);

		// Get popular customers from manager's store
		String select = "SELECT o.customerID, u.name, COUNT(*) AS NumOfOrders FROM ORDERS o, USERS u ";
		String condition = String.format("WHERE storeID = '%s' AND o.customerID = u.userID ", storeId);
		String group = "GROUP BY o.customerID, u.name ORDER BY COUNT(*) DESC LIMIT 5";
		String query = select + condition + group;
		esql.executeQueryAndPrintResult(query);
	} catch (Exception e) {
                System.err.println(e.getMessage());
        }
   }

   
   public static void placeProductSupplyRequests(Retail esql) {
	try {
		// Check if is Manager
		if (!isManager(esql)) {
			System.out.println("Sorry, you must be a manager to access this option.");
			return;
		}
	
		// Get store ID from user
		String storeId = getStoreId(esql);
		if (storeId == null) return;

		// Verify that this store is managed by this user
		String storeQuery = String.format("SELECT storeID FROM STORE WHERE managerID = '%s'", esql.userId);
                List<List<String>> storeResult = esql.executeQueryAndReturnResult(storeQuery);
		if (!storeResult.get(0).get(0).contains(storeId)) {
			System.out.println("Sorry, you do not manage this store.");
			return;
		}

		// Get product name 
		System.out.print("\tEnter Product: ");
		String product = in.readLine();

		// Check that the store sells this product
		String productQuery = String.format("SELECT storeID FROM PRODUCT WHERE productName = '%s' AND storeID = '%s'", product, storeId);
		int productResult = esql.executeQuery(productQuery);
		if (productResult == 0) {
			System.out.println("Sorry, your store does not sell this product.");
			return;
		}

		// Get number of units
		System.out.print("\tEnter # of units: ");
		String units = in.readLine();
		if (!isInteger(units)) {
			System.out.println("Invalid units; Must enter an integer.");
			return;
		}

		// Get Warehouse ID from user
		System.out.print("\tEnter WarehouseID: ");
		String warehouseID = in.readLine();
		if (!isInteger(warehouseID)) {
			System.out.println("Invalid WarehouseID; Must enter an integer.");
			return;
		}
	
		// Checking if warehouse exists
		String warehouseQuery = String.format("SELECT warehouseID FROM WAREHOUSE WHERE WarehouseID = '%s'", warehouseID);
		List<List<String>> warehouseResult = esql.executeQueryAndReturnResult(warehouseQuery);
		int warehouseExists = esql.executeQuery(warehouseQuery);
		if (warehouseExists == 0) {
			System.out.println("Warehouse does not exist; Exitting");
                	return;
		}
	
		// insert new product supply request into table productsupplyrequests
		String insert = String.format("INSERT INTO ProductSupplyRequests(managerID, warehouseID, storeID, productName, unitsRequested) VALUES ('%s', '%s', '%s','%s', %s)", esql.userId, warehouseID, storeId, product, units);
		esql.executeUpdate(insert);

		// update the amount of the product
		String update = String.format("UPDATE PRODUCT SET numberOfUnits = numberOfUnits + '%s' WHERE storeID = '%s' AND productName = '%s'", units, storeId, product);
		esql.executeUpdate(update);
	}catch (Exception e) {
		System.err.println(e.getMessage());
	}
    }
	


}//end Retail

