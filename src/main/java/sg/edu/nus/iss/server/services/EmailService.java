package sg.edu.nus.iss.server.services;

import static sg.edu.nus.iss.server.constants.EmailConstant.*;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // Send email with new password
    public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
        System.out.println("Sending email to: " + email);
        Message message = createEmail(firstName, password, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        System.out.println("Connecting to SMTP server");
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        System.out.println("Sending message...");
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    // Create email content
    private Message createEmail(String firstName, String password, String email) throws MessagingException {
        System.out.println("Creating email...");
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        // Send email TO someone
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Hello " + firstName + ", \n\n Your new account password is: " + password + "\n\n The Support Team");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }
    
    // Create email session
    private Session getEmailSession() {
        System.out.println("Creating email session...");
        // Setting up properties
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, null);
    }   

}
