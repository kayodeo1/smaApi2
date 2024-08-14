package auth.kayodeo1.com;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class mailSender {

    private String username;
    private String password;
    private String smtpHost = "smtp.office365.com";
    private int smtpPort = 587;

    // Constructor
    public mailSender(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Method to send an email
    public void sendEmail(String recipientEmail, String subject, String messageContent) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", String.valueOf(smtpPort));
        props.put("mail.smtp.ssl.trust", smtpHost);
        props.put("mail.debug", "true");
        props.put("mail.smtp.provider.class", "org.eclipse.angus.mail.smtp.SMTPProvider");

        Thread.currentThread().setContextClassLoader(Session.class.getClassLoader());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setContent(messageContent, "text/html");

            Transport transport = session.getTransport("smtp");
            transport.connect(smtpHost, smtpPort, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Email sent successfully!");

        } catch (AuthenticationFailedException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            e.printStackTrace();
        } catch (MessagingException e) {
            System.out.println("Messaging exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}