package ua.kiev.doctorvera.facade;

import org.junit.Test;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Vova on 12/23/2017.
 */
public class MailSendTest {

    private final Properties properties = new Properties();
    private String from;
    private String login;
    private String password;

    @Test
    public void sendMail(){
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        login = "v.bodnar85@gmail.com";
        password = "nenimdada6215891";
        from = "v.bodnar85@gmail.com";

        Session emailSession = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, password);
                    }
                });
        try{
            MimeMessage message = new MimeMessage(emailSession);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("v.bodnar85@gmail.com"));
            message.setSubject("ввввчмывмв");
            message.setText("<div id=\"header\" style=\"\n" +
                    "background: -moz-linear-gradient(-45deg,  #006400 0%, #61c419 98%);\n" +
                    "background: -webkit-linear-gradient(-45deg,  #006400 0%,#61c419 98%);\n" +
                    "background: linear-gradient(135deg,  #006400 0%,#61c419 98%);\n" +
                    "filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#006400', endColorstr='#61c419',GradientType=1 );\n" +
                    "width:100%; height:125px;\n" +
                    "-webkit-border-top-left-radius: 5px;\n" +
                    "-webkit-border-top-right-radius: 5px;\n" +
                    "-moz-border-radius-topleft: 5px;\n" +
                    "-moz-border-radius-topright: 5px;\n" +
                    "border-top-left-radius: 5px;\n" +
                    "border-top-right-radius: 5px;\n" +
                    "\"><div style=\"float:left\"><h1 style=\"color:gold; margin-left:50px;\">DoctorVera</h1></div>\n" +
                    "<div style=\"float:right;text-align:center\">\n" +
                    "\t<span style=\"color:#ffcc33;\">www.doctorvera.kiev.ua<br>\n" +
                    "\tinfo@doctorvera.kiev.ua</span><br>\n" +
                    "\t<span style=\"color:darkgreen;\">Адрес:03037, Украина,<br>\n" +
                    "\t&nbsp;г. Киев, ул. И. Клименка, 10/17<br>\n" +
                    "\tТел.: (044)332-86-17</span>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "<div style=\"color:#312E25;\">\n" +
                    "<p><span style=\"font-weight: bold;\">Добрый день, Director Director!</span></p>\n" +
                    "<p>Вы были записаны на прием к доктору: Doctor Doctor</p>\n" +
                    "<p>Прием назначен на 2017-12-20 на 12:50</p>\n" +
                    "<p>Исследование: УЗИ ОБП</p>\n" +
                    "<p>Стандартная стоимость: 100.0 грн.<br></p>\n" +
                    "</div><br>\n" +
                    "<div style=\"color:darkgreen; font-size:8pt\">\n" +
                    "Для того чтобы больше не получать эту рассылку <a style=\"color:gold;\" href=\"http://cloud.doctorvera.kiev.ua/unsubscribe.xhtml?email=bodik@list.ru?transactionId=$transactionId\">пройдите по ссылке\n" +
                    "</a></div>\n", "utf-8", "html");
            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
