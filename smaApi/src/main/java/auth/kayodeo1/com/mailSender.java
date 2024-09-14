package auth.kayodeo1.com;

import java.util.Properties;

import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class mailSender {
    private String username;
    private String password;
    private String smtpHost = "mail.lagosstate.gov.ng";
    private  String smtpPort = "465";

    // Constructor
    public mailSender(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Method to send an email
    public synchronized void sendEmail(String recipientEmail, String subject, String messageContent) throws AddressException, MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.proxy.host", "proxy.lagosstate.gov.ng");
        props.put("mail.smtp.proxy.port", "8080");
        props.put("mail.smtp.auth.mechanisms", "LOGIN");
        props.put("mail.debug", "true");
        props.put("mail.debug.auth", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setContent(messageContent, "text/html");

        try {
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (AuthenticationFailedException e) {
            System.err.println("Authentication failed: " + e.getMessage());
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}