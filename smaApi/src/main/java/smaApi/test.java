/*
 * @author Kayode Ojo
 */
package smaApi;

import java.sql.SQLException;

import auth.kayodeo1.com.mailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

public class test {

	public static void main(String[] args) throws SQLException, AddressException, MessagingException {
		// TODO Auto-generated method stub
	mailSender s = new mailSender("adnanopeyemi148@gmail.com","Akinpelu2003");

	s.sendEmail("ojokayode566@outlook.com", "fuck ", "testing");


}
}
