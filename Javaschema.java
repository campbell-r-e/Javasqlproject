
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Javaschema {
    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) throws SQLException {
        
        
        String DB_URL = "jdbc:mysql:";
		String USER = "";
		String PASS = "";
        String databaseName= "Falcon_airlines";
        String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        
		Connection conn= conns(DB_URL,USER,PASS);
        Statement statement = conn.createStatement();
        statement.executeUpdate(sql);
        String useDatabase = "USE " + databaseName;
        statement.executeUpdate(useDatabase);
        createTable(statement,conn);
        System.out.println("""
                           Welcome to the Falcon airline booking system.
                           To book a flight press 1 \n to view reservation press 2 \n To Cancel reservation press 3""");
        int option =Integer.parseInt(input.nextLine());
        if (option == 1 ) {
            insert_booking(statement,conn);
        }
        if (option ==2 ) {
            view_booking(statement);
        }
        if(option == 3){
            delete_booking(statement,conn);

        }else{
            while(option>3||option<1){
                System.out.println("Invalid number must be less than 3 or greater than or equl to 1");
                System.out.println("""
                           Welcome to the Falcon airline booking system.
                           To book a flight press 1\n to view reservation press 2 \n To Cancel reservation press 3""");
                option =Integer.parseInt(input.nextLine());
               
            }
        }
       




        statement.close();
        conn.close();




    
    }

    public static Connection conns(String DB_URL,String USER,String PASS) throws SQLException{
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);return conn;
	

    }
    public static void createTable(Statement statements,Connection conn) throws SQLException{
        String Flight = "CREATE TABLE IF NOT EXISTS Flight "+
               "(Flight_id int, " +
               " Depart DATETIME(0), " + 
			   "Arrival DATETIME(0), "+
			    "Depart_from VARCHAR(100),"+
				"Destination_airport VARCHAR(100),"+
                "PRIMARY KEY(Flight_id))";


        


        
            statements.executeUpdate(Flight);
        

         String Booking = "CREATE TABLE IF NOT EXISTS Booking "+"(ID int,"+"Flight_id int,"+"Paasanger_name VARCHAR(100),"+"FOREIGN KEY(Flight_id)References Flight(Flight_id))";
        statements.executeUpdate(Booking);


          String Flight_data_values= "INSERT IGNORE Flight (Flight_id, Depart, Arrival, Depart_from, Destination_airport) " +
           "VALUES (?, ?, ?, ?, ?)";

           PreparedStatement statement = conn.prepareStatement(Flight_data_values);


            String[][] flightdata = {
            {"1", "2024-04-05 15:30:00", "2024-04-05 17:30:00", "Indianapolis", "Orlando MCO"},
            {"2", "2024-04-06 15:30:00", "2024-04-05 17:30:00", "Indianapolis", "New York City LGA"},
            {"3", "2024-04-07 12:30:00", "2024-04-05 14:30:00", "Indianapolis", "Orlando MCO"},
            {"4", "2024-04-08 15:30:00", "2024-04-05 17:30:00", "Indianapolis", "Orlando MCO"},
            {"5", "2024-04-09 15:30:00", "2024-04-05 17:30:00", "Indianapolis", "Orlando MCO"},
            {"6", "2024-04-10 15:30:00", "2024-04-05 17:30:00", "Indianapolis", "Aruba AUA"},
            {"7", "2024-04-11 15:30:00", "2024-04-05 17:30:00", "Indianapolis", "Houston HOU"},
            {"8", "2024-04-11 15:30:00", "2024-04-05 17:30:00", "Indianapolis", "Houston HOU"},
            {"9", "2024-04-11 15:30:00", "2024-04-05 17:30:00", "Indianapolis", "Houston HOU"},
            {"10", "2024-04-11 15:30:00", "2024-04-05 17:30:00", "Indianapolis", "New York City LGA"},
            {"11", "2024-04-12 15:30:00", "2024-04-05 17:30:00", "Indianapolis", "Kansas City MCI"},
            {"12", "2024-04-13 15:30:00", "2024-04-05 17:30:00", "Indianapolis", "Kansas City MCI"}
            };

            for (String[] flight : flightdata) {
            statement.setInt(1, Integer.parseInt(flight[0]));
            statement.setString(2, flight[1]);
            statement.setString(3, flight[2]);
            statement.setString(4, flight[3]);
            statement.setString(5, flight[4]);
            statement.addBatch();
            }

            statement.executeBatch();

      


    }
    
    public static void insert_booking(Statement statment,Connection conn) throws SQLException{
            
        String query = "SELECT * FROM Flight"; 
        ResultSet rs = statment.executeQuery(query);
        while (rs.next()) {
            System.out.print("Flight_id: " + rs.getInt("Flight_id"));
            System.out.print(",Depart " + rs.getTimestamp("Depart"));
            System.out.print(", Sex: " + rs.getTimestamp("Arrival"));
            System.out.println(", Department: " + rs.getString("Depart_from"));
            System.out.println(", Department: " + rs.getString("Destination_airport"));
            }

            System.out.println("Enter an ID for later so you can manage your bookings");
            int id = Integer.parseInt(input.nextLine());
            System.out.println("Enter the ID for the flight you would like to book.");
            int flight = Integer.parseInt(input.nextLine());
            System.out.println("Enter your name.");
            String name = input.nextLine();

            String query2="INSERT INTO Booking(ID,Flight_id,Paasanger_name)VALUES(?,?,?)";
            PreparedStatement statements = conn.prepareStatement(query2);
		
		    statements.setInt(1,id);
            statements.setInt(2,flight);
            statements.setString(3,name);
            statements.executeUpdate();
            statements.close();



        




    }
    public static void view_booking(Statement statment) throws SQLException{
        System.out.println("Enter your ID from earlier to view your bookings");
        int id = Integer.parseInt(input.nextLine());
        
        
        



        String query2 = "SELECT * FROM Booking INNER JOIN Flight ON Booking.Flight_id = Flight.Flight_id WHERE Booking.ID ="+id;
        ResultSet rs = statment.executeQuery(query2);
        while (rs.next()) {
           
            System.out.print("Flight_id:" + rs.getInt("Flight_id"));
            System.out.print(",Depart_Time:" + rs.getTimestamp("Depart"));
            System.out.print("Arrival_Time:" + rs.getTimestamp("Arrival"));
            System.out.println("Depart_from:" + rs.getString("Depart_from"));
            System.out.println("Destination Airport:" + rs.getString("Destination_airport"));
            }

        

    }
    public static void delete_booking(Statement statment,Connection conn) throws SQLException{
        System.out.println("What is your Booking ID?");
        int id = input.nextInt();

        String query2 = "SELECT * FROM Booking INNER JOIN Flight ON Booking.Flight_id = Flight.Flight_id WHERE Booking.ID ="+id;
        ResultSet rs = statment.executeQuery(query2);
        while (rs.next()) {
           
            System.out.print("Flight_id:" + rs.getInt("Flight_id"));
            System.out.print(",Depart_Time:" + rs.getTimestamp("Depart"));
            System.out.print("Arrival_Time:" + rs.getTimestamp("Arrival"));
            System.out.println("Depart_from:" + rs.getString("Depart_from"));
            System.out.println("Destination Airport:" + rs.getString("Destination_airport"));
            }



        //
       


        

        System.out.println("Below is your reservations:");


        System.out.println("Enter the Flight id of the reservation that you want to delete.");
        int flight_id = input.nextInt();

        String delete_query = "DELETE FROM Booking WHERE Flight_id = " + flight_id + " AND id = " + id;
            
        PreparedStatement statements = conn.prepareStatement(delete_query);

        statements.executeUpdate();
        statements.close();

		

        

    }

    
}
