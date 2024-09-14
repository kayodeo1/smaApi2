/*
 * @author Kayode Ojo
 */
package smaApi;

import java.sql.SQLException;

import auth.kayodeo1.com.apacheMail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

public class test2 {

	public static void main(String[] args) throws SQLException, AddressException, MessagingException {
		// TODO Auto-generated method stub
		String appPassword = System.getenv("APP_EMAIL1");
		String email = System.getenv("APP_PASSWORD1");
 apacheMail  m = new apacheMail(email,appPassword);
 m.sendEmail("ojokayode566@gmail.com", "testing", email);




	}

}
