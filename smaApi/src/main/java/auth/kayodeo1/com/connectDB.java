package auth.kayodeo1.com;

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
	System.out.println(pwd);

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
public ResultSet executeQuery(String query, String password) {
    ResultSet result = null;
    Connection con = null;
    try {
        con = connectDB(password);
        Statement st = con.createStatement();
        result = st.executeQuery(query);
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("SQL Error: " + e.getMessage());
    }
    return result;
}

// Add a new method to close the connection
public void closeConnection(Connection con) {
    if (con != null) {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
}


