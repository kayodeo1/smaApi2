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
		db.executeQuery(query, System.getenv("DATABASE_PASSWORD"));
	}
	public boolean checkInstance (String email) throws SQLException {
		refresh();
		String query = "select * from smaAuth where Trim(email) = '" + email+ "'";
		if (db.executeQuery(query, System.getenv("DATABASE_PASSWORD")).next()) {
			return true;
		}

		return false;

	}
	public Instance validate (String email ) throws SQLException {
		String query = "select * from smaAuth where Trim(email)='"+email+"'";
		System.out.println();
		ResultSet res = db.executeQuery(query, System.getenv("DATABASE_PASSWORD"));

		try {
			if (res.next()) {
				Instance out = new Instance(res.getString("email"),Integer.parseInt(res.getString("code").trim()),Long.parseLong(res.getString("time").trim()));
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
		 db.executeUpdate(query,System.getenv("DATABASE_PASSWORD"));
	 }
	 public void delete (String id) {
		 db.executeUpdate("delete from smaAuth where email = '"+id+"'", System.getenv("DATABASE_PASSWORD"));
	 }

}
