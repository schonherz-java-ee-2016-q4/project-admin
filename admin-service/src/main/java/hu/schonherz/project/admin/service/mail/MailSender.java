package hu.schonherz.project.admin.service.mail;

import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Stateless
public class MailSender {

    @Resource(name = "java:/mail/admin")
    private  Session session;

    public  void sendFromGmail(final String to, final String password) {
        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Schonherz Helpdesk - Profile Changed");
            message.setText("Your new password is: \"" + password + "\" " + "You have to change it when you login to the site!");
            Transport.send(message);
            log.info("Message sent successfully!");
        } catch (MessagingException e) {
            log.warn("Message was not send!");
            throw new RuntimeException(e);
        }

    }
}
