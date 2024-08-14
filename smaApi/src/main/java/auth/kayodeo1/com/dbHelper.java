package auth.kayodeo1.com;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

import smaApi.connectDB;

public class dbHelper {
	connectDB db = new connectDB();
	public void writeDb(Instance instance) throws SQLException {
		refresh();
		if (checkInstance(instance.getEmail())) {
			delete(instance.getEmail());
		return;
		}
		String query = "insert into  smaAuth (email , code , time ) values ( '"+ instance.getEmail() + " ' , ' " + instance.getCode() + "','" + instance.getTime()+"')";
		db.executeQuery(query, "2377");
	}
	public boolean checkInstance (String email) throws SQLException {
		refresh();
		String query = "select * from smaAuth where Trim(email) = '" + email+ "'";
		if (db.executeQuery(query, "2377").next()) {
			return true;
		}

		return false;

	}
	public Instance validate (String email ) throws SQLException {
		String query = "select * from smaAuth where Trim(email)='"+email+"'";
		System.out.println();
		ResultSet res = db.executeQuery(query, "2377");

		try {
			if (res.next()) {
				Instance out = new Instance(res.getString(2),Integer.parseInt(res.getString(3).trim()),Long.parseLong(res.getString(4).trim()));
				return out;
			}System.out.println("error");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	 public void refresh () {
		 long currentTime = Instant.now().getEpochSecond();
		 String query = "Delete from smaAuth where (time::INTEGER) < "+currentTime;
		 db.executeUpdate(query,"2377");
	 }
	 public void delete (String id) {
		 db.executeUpdate("delete from smaAuth where email = '"+id+"'", "2377");
	 }

}
