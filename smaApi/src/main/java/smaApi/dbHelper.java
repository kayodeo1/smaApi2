package smaApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import auth.kayodeo1.com.KeyGenerator;
import auth.kayodeo1.com.NotificationModel;
import auth.kayodeo1.com.PasswordCrypto;


public class dbHelper {
	connectDB db = new connectDB();
	ArrayList<studentModel> studentsList;
public ArrayList<String> getKeys() {
	ArrayList<String> keys  = new ArrayList();
	keys.add(System.getenv("KEY1"));
	keys.add(System.getenv("KEY2"));
	keys.add(System.getenv("KEY3"));
	keys.add(System.getenv("KEY4"));

	return keys;

}

	String key = KeyGenerator.generateKey(getKeys());

	public boolean addStudent(studentModel student) throws SQLException {
	    String query = "INSERT INTO smaStudents (email, password) VALUES (?, ?)";
	    try (Connection conn = db.connectDB(System.getenv("DATABASE_PASSWORD"));
	         PreparedStatement pstmt = conn.prepareStatement(query)) {

	        if (!checkStudentExists(student.getEmail())) {
	            pstmt.setString(1, student.getEmail());
	            try {
					pstmt.setString(2, PasswordCrypto.encrypt(student.getPassword(), key));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            int rowsAffected = pstmt.executeUpdate();
	            return rowsAffected > 0;
	        }
	        return false;
	    }
	}

	public boolean deleteStudent(studentModel student) throws SQLException {
		// TODO update this query
		String query = "";
		if (checkStudentExists(student.getEmail())) {
			// TODO
			db.executeQuery(query, System.getenv("DATABASE_PASSWORD"));
			return true;

		}
		return false;
	}

	public boolean checkStudentExists(String email) throws SQLException {
	    String query = "SELECT 1 FROM smaStudents WHERE email = ?";
//	    System.out.println(System.getenv("DATABASE_PASSWORD"));
	    try (Connection conn = db.connectDB(System.getenv("DATABASE_PASSWORD"));
	         PreparedStatement pstmt = conn.prepareStatement(query)) {

	        pstmt.setString(1, email);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            return rs.next();
	        }
	    }
	}

	public ArrayList<studentModel> getAllStudent() throws Exception {
		studentsList = new ArrayList();
		ResultSet result = db.executeQuery("select * from smaStudents", System.getenv("DATABASE_PASSWORD"));
		while (result.next()) {
			studentModel res = new studentModel();
			res.setUserID(result.getInt("userID")); // userID is an integer
			res.setEmail(result.getString("email")); // email is a string
			res.setPassword(PasswordCrypto.encrypt(result.getString("password"), key)); // password is a string
			res.setFirstName(result.getString("firstName")); // firstName is a string
			res.setMiddleName(result.getString("middleName"));// middleName is a string
			res.setLastName(result.getString("lastName")); // lastName is a string
			res.setStartDate(result.getString("startDate")); // startDate is a date
			res.setEndDate(result.getString("endDate")); // endDate is a date
			res.setRole(result.getString("role")); // role is a string
			res.setInstitution(result.getString("institution")); // institution is a string
			res.setImgUrl(result.getString("imgUrl")); // imgUrl is a string
			res.setCourse(result.getString("course")); // course is a string
			if(res.getStartDate()!=null){// course is a string
				res.setProgress(
						res.getPercentComplete(res.getStartDate().replace("-", "/"), res.getEndDate().replace("-", "/"))); // progress
																															// is
																															// a
				}																										// is
																														// a
																														// decimal
			res.setStatus(result.getString("status")); // status is a string
			res.setDuration(result.getString("duration"));
			res.setGroup(result.getString("studentgroup"));
			studentsList.add(res);

		}
		return studentsList;

	}

	public studentModel getStudent(String email, String password) throws SQLException {

		if (checkStudentExists(email)) {
			return authenticate(email, password);
		}
		return null;
	}

	private studentModel authenticate(String email, String password) throws SQLException {
	    // TODO: Implement proper password hashing (do not use encryption for passwords)
	    System.out.println("Trying to login: " + email);
	    
	    try (ResultSet result = db.executeQuery("SELECT * FROM smaStudents WHERE email ='"+email.trim()+"'",System.getenv("DATABASE_PASSWORD"))) {
	        if (result.next()) {
	            System.out.println("Got email " + result.getString("email"));
	            
	            String storedPassword = result.getString("password");
	            if (PasswordCrypto.verify(password, storedPassword,key)) {
	                System.out.println("Password verified");
	                studentModel res = new studentModel();
	                res.setUserID(result.getInt("userID"));
	                res.setEmail(result.getString("email"));
	                res.setPassword(storedPassword); // Store the hashed password
	                res.setFirstName(result.getString("firstName"));
	                res.setMiddleName(result.getString("middleName"));
	                res.setLastName(result.getString("lastName"));
	                res.setStartDate(result.getString("startDate"));
	                res.setEndDate(result.getString("endDate"));
	                res.setRole(result.getString("role"));
	                res.setInstitution(result.getString("institution"));
	                
	                String imgUrl = result.getString("imgUrl");
	                res.setImgUrl(imgUrl.equals("NOT_SET") ? "" : imgUrl);
	                
	                res.setCourse(result.getString("course"));
	                res.setPhoneNumber(result.getString("phonenumber"));
	                
	                String startDate = res.getStartDate();
	                String endDate = res.getEndDate();
	                if (startDate != null && endDate != null) {
	                    res.setProgress(res.getPercentComplete(startDate.replace("-", "/"), endDate.replace("-", "/")));
	                } else {
	                    res.setProgress(0);
	                }
	                
	                res.setStatus(result.getString("status"));
	                res.setDuration(result.getString("duration"));
	                res.setGroup(result.getString("studentgroup"));
	                
	                return res;
	            } else {
	                System.out.println("Wrong password");
	                return null;
	            }
	        } else {
	            System.out.println("No user found with email: " + email);
	            return null;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new SQLException("Authentication failed", e);
	    }
	}

	public boolean updateStudent(studentModel s) throws SQLException {
		StringBuilder queryBuilder = new StringBuilder("UPDATE smaStudents SET ");
		List<String> updateFields = new ArrayList<>();
		List<Object> parameters = new ArrayList<>();

		if (s.getFirstName() != null) {
			updateFields.add("firstName = ?");
			parameters.add(s.getFirstName());
		}
		if (s.getMiddleName() != null) {
			updateFields.add("middleName = ?");
			parameters.add(s.getMiddleName());
		}
		if (s.getLastName() != null) {
			updateFields.add("lastName = ?");
			parameters.add(s.getLastName());
		}
		if (s.getStartDate() != null) {
			updateFields.add("startDate = ?");
			parameters.add(s.getStartDate());
		}
		if (s.getEndDate() != null) {
			updateFields.add("endDate = ?");
			parameters.add(s.getEndDate());
		}

		if (s.getInstitution() != null) {
			updateFields.add("institution = ?");
			parameters.add(s.getInstitution());
		}

		if (s.getImgUrl() != null) {
			updateFields.add("imgUrl = ?");
			parameters.add(s.getImgUrl());
		}
		if (s.getCourse() != null) {
			updateFields.add("course = ?");
			parameters.add(s.getCourse());
		}

		if (s.getDuration() != null) {
			updateFields.add("duration = ?");
			parameters.add(s.getDuration());
		}
		if (s.getPhoneNumber() != null) {
			updateFields.add("phoneNumber = ?");
			parameters.add(s.getPhoneNumber());
		}

		if (updateFields.isEmpty()) {
			return false; // No fields to update
		}

		queryBuilder.append(String.join(", ", updateFields));
		queryBuilder.append(" WHERE email = ?");

		try (PreparedStatement stmt = db.connectDB(System.getenv("DATABASE_PASSWORD")).prepareStatement(queryBuilder.toString())) {
			for (int i = 0; i < parameters.size(); i++) {
				stmt.setObject(i + 1, parameters.get(i));
			}
			stmt.setString(parameters.size() + 1, s.getEmail());

			int rowsAffected = stmt.executeUpdate();

			// Update studentgroup separately if it's provided
			if (s.getGroup() != null) {
				try (PreparedStatement groupStmt = db.connectDB(System.getenv("DATABASE_PASSWORD"))
						.prepareStatement("UPDATE smaStudents SET studentgroup = ? WHERE email = ?")) {
					groupStmt.setString(1, s.getGroup());
					groupStmt.setString(2, s.getEmail());
					groupStmt.executeUpdate();
				}
			}

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updatePassword(String email, String oldPassword, String newPassword) throws SQLException {
	    String selectQuery = "SELECT password FROM smaStudents WHERE email = ?";
	    String updateQuery = "UPDATE smaStudents SET password = ? WHERE email = ?";

	    try (Connection conn = db.connectDB(System.getenv("DATABASE_PASSWORD"));
	         PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
	         PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

	        // Verify old password
	        selectStmt.setString(1, email);
	        try (ResultSet rs = selectStmt.executeQuery()) {
	            if (rs.next()) {
	                String storedPassword = rs.getString("password");
	                try {
	                    storedPassword = PasswordCrypto.decrypt(storedPassword, key);
	                    if (!verifyPassword(oldPassword, storedPassword)) {
	                        return false; // Old password doesn't match
	                    }
	                } catch (Exception e) {
	                    throw new SQLException("Error decrypting password", e);
	                }
	            } else {
	                return false; // User not found
	            }
	        }
	        // Update with new password
	        String hashedNewPassword = hashPassword(newPassword);
	        updateStmt.setString(1, hashedNewPassword);
	        updateStmt.setString(2, email);

	        int rowsAffected = updateStmt.executeUpdate();
	        return rowsAffected > 0;
	    }
	}

	private boolean verifyPassword(String inputPassword, String storedPassword) {
	    return inputPassword.equals(storedPassword);
	}

	private String hashPassword(String password) {
	    try {
	        return PasswordCrypto.encrypt(password, key);
	    } catch (Exception e) {
	        throw new RuntimeException("Error hashing password", e);
	    }
	}

	public boolean updateProfilePicture(String email, String filePath) throws SQLException {
	    String sql = "UPDATE smaStudents SET imgUrl = ? WHERE email = ?";
	    try (Connection conn = db.connectDB(System.getenv("DATABASE_PASSWORD"));
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, filePath);
	        pstmt.setString(2, email);
	        int affectedRows = pstmt.executeUpdate();
	        return affectedRows > 0;
	    }

	}

	public boolean resetPassword(String email, String newPassword) {
		 String selectQuery = "SELECT password FROM smaStudents WHERE email = ?";
		    String updateQuery = "UPDATE smaStudents SET password = ? WHERE email = ?";

		    try (Connection conn = db.connectDB(System.getenv("DATABASE_PASSWORD"));
		         PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
		         PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

		        // Verify old password
		        selectStmt.setString(1, email);
		        try (ResultSet rs = selectStmt.executeQuery()) {
		            if (rs.next()) {
		            	String hashedNewPassword = hashPassword(newPassword);
				        updateStmt.setString(1, hashedNewPassword);
				        updateStmt.setString(2, email);

		        }
		        // Update with new password


		        int rowsAffected = updateStmt.executeUpdate();
		        return rowsAffected > 0;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return false;
	}

	public boolean changeRole(String email, String role) {
		String selectQuery = "SELECT password FROM smaStudents WHERE email = ?";
	    String updateQuery = "UPDATE smaStudents SET role = ? WHERE email = ?";

	    try (Connection conn = db.connectDB(System.getenv("DATABASE_PASSWORD"));
	         PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
	         PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

	        // Verify old password
	        selectStmt.setString(1, email);
	        try (ResultSet rs = selectStmt.executeQuery()) {
	            if (rs.next()) {
			        updateStmt.setString(1, role);
			        updateStmt.setString(2, email);

	        }
	        // Update with new role


	        int rowsAffected = updateStmt.executeUpdate();
	        return rowsAffected > 0;
	    }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return false;
	}

	public void addNotification(NotificationModel notification) {
	    String query = "INSERT INTO smaNotifications (title, content, time, author, status, timestamp, category) " +
	                   "VALUES (?, ?, ?, ?, ?, ?, ?)";

	    try (Connection conn = db.connectDB(System.getenv("DATABASE_PASSWORD"));
	         PreparedStatement pstmt = conn.prepareStatement(query)) {

	        pstmt.setString(1, notification.getTitle());
	        pstmt.setString(2, notification.getContent());
	        pstmt.setString(3, notification.getTime());
	        pstmt.setString(4, notification.getAuthor());
	        pstmt.setString(5, "unread");  // Default status
	        pstmt.setString(6, String.valueOf(notification.getTimeStamp()));
	        pstmt.setString(7, notification.getCategory().toLowerCase());

	        int affectedRows = pstmt.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Creating notification failed, no rows affected.");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public ArrayList<NotificationModel> getNotifications(String category) {
		ArrayList<NotificationModel> notifications = new ArrayList();

		String query = "select * from smaNotifications where category = '" +category.toLowerCase()+"'";

		try {
			ResultSet res = db.executeQuery(query, System.getenv("DATABASE_PASSWORD"));
			while (res.next()) {
				NotificationModel currentNotification = new NotificationModel();
				currentNotification.setAuthor(res.getString("author"));
				currentNotification.setContent(res.getString("content"));
				currentNotification.setTitle(res.getString("title"));
				currentNotification.setTimeStamp(Long.parseLong(res.getString("timestamp")));
				currentNotification.setTime(res.getString("time"));
				currentNotification.setStatus(res.getString("status"));
				notifications.add(currentNotification);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return notifications;
	}

	public void deleteNotification(studentModel student) {
		// TODO Auto-generated method stub

	}

	public boolean changeGroup(String email, String group) {
		String selectQuery = "SELECT password FROM smaStudents WHERE email = ?";
	    String updateQuery = "UPDATE smaStudents SET studentgroup = ? WHERE email = ?";

	    try (Connection conn = db.connectDB(System.getenv("DATABASE_PASSWORD"));
	         PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
	         PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

	        // Verify old password
	        selectStmt.setString(1, email);
	        try (ResultSet rs = selectStmt.executeQuery()) {
	            if (rs.next()) {
			        updateStmt.setString(1, group);
			        updateStmt.setString(2, email);

	        }
	        // Update with new role


	        int rowsAffected = updateStmt.executeUpdate();
	        return rowsAffected > 0;
	    }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return false;
	}
}