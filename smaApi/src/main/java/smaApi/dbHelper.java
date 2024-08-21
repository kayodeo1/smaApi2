package smaApi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class dbHelper {
	connectDB db = new connectDB();
	ArrayList<studentModel> studentsList ;
	public boolean addStudent(studentModel student ) throws SQLException {
		String query = "insert into smaStudents (email,password) values ('"+student.getEmail()+"','"+student.getPassword()+"')";
		if (!checkStudentExists(student.getEmail())) {
			db.executeQuery(query, "2377");
			return true;

		}
		return false;

	}
	public boolean deleteStudent(studentModel student) throws SQLException {
		//TODO update this query
		String query = "";
		if (checkStudentExists(student.getEmail())) {
			//TODO
			db.executeQuery(query, "2377");
			return true;

		}
		return false;
	}
	public boolean checkStudentExists(String email) throws SQLException {
		ResultSet rs = db.executeQuery("select * from smaStudents where email = '"+ email+"'","2377");
		if (rs.next()) {
			return true;

		}else {
			return false;
		}
		// TODO Auto-generated method stub

	}
	public ArrayList<studentModel> getAllStudent () throws SQLException{
		studentsList = new ArrayList();
		ResultSet result = db.executeQuery("select * from smaStudents","2377");
		while (result.next()) {
			studentModel student = new studentModel();
			 student.setUserID(result.getInt("userID"));
	         student.setEmail(result.getString("email"));
	         student.setPassword("*******");
			student.setInstitution("Ministry of Science and Tech");
	         studentsList.add(student);


		}
	       return studentsList;

	}
	public studentModel getStudent(String email , String password) throws SQLException {

		if (checkStudentExists(email) ){
			return authenticate (email , password);
	}
		return null;
}
	private studentModel authenticate(String email, String password) throws SQLException {
		// TODO Auto-generated method stub
		ResultSet result = db.executeQuery( "select * from smaStudents where email = '"+ email+"'","2377");
		result.next();
		if (result.getString(3).equals(password)) {
			System.out.println("yes");
			studentModel res = new studentModel ();
			res.setUserID(result.getInt(1));
			res.setEmail(result.getString(2));
			res.setPassword(result.getString(3));
			res.setInstitution("Ministry of Science and Tech");
			return res;
		}
		System.out.println("wrong password");
		return null;
	}
	public boolean updateStudent (studentModel s) {
		//TODO
		String query ="update smatudents where";
		return false;
		

	}
}
