package Connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	static String adresSerwera = "192.168.90.123";
	Connection conn=null;
	public static Connection dbConnector()
	{
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection conn=DriverManager.getConnection("jdbc:mariadb://"+adresSerwera+"/fatdb","listy","listy1234"); // ORYGINAL DATABASE

			return conn;
		}catch (Exception e)
		{
			System.out.println("connection to database is not set");
			return null;
		}
	}
}
