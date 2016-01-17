package ua.kiev.doctorvera.services;

import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.resources.Config;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by volodymyr.bodnar on 1/9/2016.
 */
@Stateless
public class MailService implements MailServiceLocal {

    private final static Logger LOG = Logger.getLogger(SchedulerService.class.getName());
    private final Properties properties = new Properties();
    private String from;
    private String login;
    private String password;

    private int totalEmailsToSend = 0;
    private int sentEmails = 0;

    @PostConstruct
    public void init(){
        //create properties
        properties.put("mail.smtp.auth", Config.getInstance().getProperty("SMTP_AUTH"));
        properties.put("mail.smtp.starttls.enable", Config.getInstance().getProperty("SMTP_STARTTLS"));
        properties.put("mail.smtp.host", Config.getInstance().getProperty("SMTP_SERVER"));
        properties.put("mail.smtp.port", Config.getInstance().getProperty("SMTP_PORT"));

        login = Config.getInstance().getProperty("SMTP_LOGIN");
        password = Config.getInstance().getProperty("SMTP_PASSWORD");
        from = Config.getInstance().getProperty("EMAIL_FROM");
    }

    public void sendEmail(Users user, String text, String subject){
        List<Users> userList = new LinkedList<>();
        userList.add(user);
        sendEmail(userList, text, subject);
    }

    public void sendEmail(List<Users> userList, String text, String subject){
        totalEmailsToSend = userList.size();
        sentEmails = 0;
        LOG.info("Starting to sent " + totalEmailsToSend + " emails");

        try {
            Session emailSession = Session.getDefaultInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(login, password);
                        }
                    });

            for(Users user : userList) {
                if(user.getEmail() == null || user.getEmail().isEmpty() || !user.isMessagingAccepted()){
                    LOG.info("User: " + user.getFirstName() + " " + user.getLastName() + " has no email");
                    return;
                }
                MimeMessage message = new MimeMessage(emailSession);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                message.setSubject(subject);
                message.setText(processMessageText(text, user), "utf-8", "html");
                Transport.send(message);
                sentEmails++;
                LOG.info("Email to user: " + user.getFirstName() + " " + user.getLastName() + " email: " + user.getEmail() + " has been sent");
            }
        } catch (NoSuchProviderException e) {
            LOG.severe(e.getMessage());
        } catch (MessagingException e) {
            LOG.severe(e.getMessage());
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }
    }

    private String processMessageText(String text, Users user){

        if(user.getFirstName() != null)
            text.replaceAll("$usersFirstName",user.getFirstName());
        if(user.getLastName() != null)
            text.replaceAll("$usersLastName",user.getLastName());
        if(user.getMiddleName() != null)
            text.replaceAll("$usersMiddleName",user.getMiddleName());
        if(user.getEmail() != null)
            text.replaceAll("$usersEmail",user.getEmail());
        return text;
    }

}
