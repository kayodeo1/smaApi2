package auth.kayodeo1.com;

import java.sql.SQLException;
import java.util.Scanner;

public class test {
    public static void main(String[] args) throws SQLException {
        // Read environment variables
    	auth authenticate = new auth();
        String appPassword = "sjsb hzcg qgeu sotg";
        appPassword = "Olecram2.";
        String email = "ojokayode566@outlook.com";

        mailSender msg = new mailSender(email,appPassword);
        System.out.println("Enter your email");
        Scanner in = new Scanner (System.in);
        String recipent = in.next();
        String subject= "Verification Mail from SMA Ministry of science and technology";
        int code = authenticate.genAuthInstance(recipent).getCode();
        String htmlContent = auth.generateHtmlContent(String.valueOf(code));
        msg.sendEmail(recipent, subject, htmlContent);
        System.out.println("code sent please enter");
        int codev = in.nextInt();
        authenticate.validateAuthInstance(recipent, codev);





    }

}
