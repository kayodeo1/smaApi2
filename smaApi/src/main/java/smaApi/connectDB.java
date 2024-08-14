package smaApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class connectDB {

public Connection connectDB(String pwd) throws SQLException {

		try {
		    Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
		    // Handle the exception appropriately
		    e.printStackTrace();
		}


		String password=pwd;
		String username="postgres";
		String url="jdbc:postgresql://localhost:5432/postgres";
		Connection con = DriverManager.getConnection(url, username, password);
		return con;


	}
	public ResultSet executeQuery (String query, String password) throws SQLException{

		ResultSet result ;
		try {
			Connection con = connectDB(password);
			Statement st = con.createStatement();
			result = st.executeQuery(query);
			con.close();

			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	public void executeUpdate(String query,String password) {
		try {
			Connection con = connectDB(password);
			Statement st = con.createStatement();
			st.executeUpdate(query);
			con.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;

	}

	}



