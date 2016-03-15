package ua.kiev.doctorvera.services;

import ua.kiev.doctorvera.entities.MessageLog;
import ua.kiev.doctorvera.entities.TransactionLog;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.MessageLogFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.TransactionLogFacadeLocal;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.utils.TemplateProcessor;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by volodymyr.bodnar on 1/9/2016.
 */
@Stateless
public class MailService implements MailServiceLocal {

    private final static Logger LOG = Logger.getLogger(MailService.class.getName());
    private final Properties properties = new Properties();
    private String from;
    private String login;
    private String password;

    private int totalEmailsToSend = 0;
    private int sentEmails = 0;

    @EJB
    TransactionLogFacadeLocal transactionLogFacade;

    @EJB
    MessageLogFacadeLocal messageLogFacade;

    @PostConstruct
    public void init(){
        //create properties
        properties.put("mail.smtp.auth", Config.getInstance().getString("SMTP_AUTH"));
        properties.put("mail.smtp.starttls.enable", Config.getInstance().getString("SMTP_STARTTLS"));
        properties.put("mail.smtp.host", Config.getInstance().getString("SMTP_SERVER"));
        properties.put("mail.smtp.port", Config.getInstance().getString("SMTP_PORT"));

        login = Config.getInstance().getString("SMTP_LOGIN");
        password = Config.getInstance().getString("SMTP_PASSWORD");
        from = Config.getInstance().getString("EMAIL_FROM");
    }
    @Override
    public void sendEmail(Users user, String text, String subject, TransactionLog transactionLog){
        List<Users> userList = new LinkedList<>();
        userList.add(user);
        sendEmail(userList, text, subject, transactionLog);
    }

    @Override
    public void sendEmail(List<Users> userList, String text, String subject, TransactionLog transactionLog){
        totalEmailsToSend = userList.size();
        sentEmails = 0;
        LOG.info("Starting to sent " + totalEmailsToSend + " emails");

            Session emailSession = Session.getInstance(properties,
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

                MessageLog messageLog = new MessageLog();
                messageLog.setUserCreated(transactionLog.getUserCreated());
                messageLog.setDateCreated(new Date());
                messageLog.setRecipient(user);
                messageLog.setTransaction(transactionLog);
                messageLog.setStatus(MessageLog.Status.NEW);
                messageLogFacade.create(messageLog);

                try{
                    MimeMessage message = new MimeMessage(emailSession);
                    message.setFrom(new InternetAddress(from));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                    message.setSubject(subject);
                    message.setText(processMessageText(text, user, transactionLog), "utf-8", "html");
                    Transport.send(message);

                    messageLog.setStatus(MessageLog.Status.SENT);
                    messageLogFacade.edit(messageLog);
                    sentEmails++;
                } catch (NoSuchProviderException e) {
                    messageLog.setStatus(MessageLog.Status.SEND_ERROR);
                    messageLog.setDetails(e.getMessage());
                    messageLogFacade.edit(messageLog);

                    LOG.severe(e.getMessage());
                } catch (MessagingException e) {
                    messageLog.setStatus(MessageLog.Status.SEND_ERROR);
                    messageLog.setDetails(e.getMessage());
                    messageLogFacade.edit(messageLog);

                    LOG.severe(e.getMessage());
                } catch (Exception e) {
                    messageLog.setStatus(MessageLog.Status.SEND_ERROR);
                    messageLog.setDetails(e.getMessage());
                    messageLogFacade.edit(messageLog);

                    LOG.severe(e.getMessage());
                }

                LOG.info("Email to user: " + user.getFirstName() + " " + user.getLastName() + " email: " + user.getEmail() + " has been sent");
            }

            transactionLog.setStatus(TransactionLog.Status.SENT);
            transactionLog.setRecipientsCount(totalEmailsToSend);
            transactionLog.setDetails(sentEmails + " of total " + totalEmailsToSend + " were successfully sent");
            transactionLogFacade.edit(transactionLog);

    }

    private String processMessageText(String text, Users user, TransactionLog transactionLog){
        TemplateProcessor processUtil = new TemplateProcessor();
        processUtil.setUser(user);
        text = processUtil.replaceUsingUsersData(text);
        text = text.replaceAll("$transactionId", transactionLog.getId() == null ? "" : "" + transactionLog.getId());
        return text;
    }

}
