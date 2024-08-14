/*
 * @author Kayode Ojo
 */
package smaApi;

public class studentModel {
	private int userID;
	private String password;
	private String email;
	private String role="Student";
	private String institution;
	private String jwt;
	public studentModel() {


	}
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	@Override
	public String toString() {
		return "studentModel [userID=" + userID + ", email=" + email + "], password="+ password ;
	}

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String username) {
		this.email = username;
	}
	public String getRole() {
		// TODO Auto-generated method stub
		return role;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public void setRole(String role) {
		this.role=role;
	}

}
