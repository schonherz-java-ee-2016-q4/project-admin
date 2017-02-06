package hu.schonherz.project.admin.service.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailSender {

    private static Properties properties;

    private static void setGmailProperties() {
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
    }

    public static void sendFromGmail(final String to, final String password) {
        setGmailProperties();
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("schonherz.helpdesk@gmail.com", "redsnake327");
            }
        });
        // compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("schonherz.helpdesk@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Schonherz Helpdesk - Profile Changed");
            message.setText("Your new password is: \"" + password + "\" " + "You have to change it when you login to the site!");
            // send message
            Transport.send(message);
            log.info("Message sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
