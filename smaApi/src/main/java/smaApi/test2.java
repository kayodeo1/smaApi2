/*
 * @author Kayode Ojo
 */
package smaApi;

import java.sql.SQLException;

import auth.kayodeo1.com.mailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

public class test2 {

	public static void main(String[] args) throws SQLException, AddressException, MessagingException {
		// TODO Auto-generated method stub
		String appPassword = "@Tana1234";
		String email = "mhedheyghold12@gmail.com";
 mailSender  m = new mailSender(email,appPassword);
 m.sendEmail(email, appPassword, email);




	}

}
