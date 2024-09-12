package auth.kayodeo1.com;

import java.util.Properties;

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
    private String smtpHost = "smtp.office365.com";
    private int smtpPort = 587;
    private String github ="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";

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
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.debug", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setContent(messageContent, "text/html");
        Transport.send(message);
        System.out.println("Email sent successfully!");
    }

}


