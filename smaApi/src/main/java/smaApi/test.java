/*
 * @author Kayode Ojo
 */
package smaApi;

import java.sql.SQLException;

import auth.kayodeo1.com.mailSender;

public class test {

	public static void main(String[] args) throws SQLException{
		// TODO Auto-generated method stub
	mailSender s = new mailSender("siwes@lagosstate.gov.ng","TTest@#$1");

	try {
		s.sendEmail("ojokayode566@outlook.com", "lets see ", "testing");
	} catch (jakarta.mail.internet.AddressException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (jakarta.mail.MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


}
}
