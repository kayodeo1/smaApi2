/*
 * @author Kayode Ojo
 */
package smaApi;

import java.sql.SQLException;

public class test2 {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

	dbHelper db = new dbHelper();
	studentModel student = db.getStudent("Hassanat", "manyide");
	System.out.println(student.toString());




	}

}
