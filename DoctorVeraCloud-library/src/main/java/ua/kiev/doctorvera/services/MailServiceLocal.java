package ua.kiev.doctorvera.services;

import ua.kiev.doctorvera.entities.Users;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * Created by volodymyr.bodnar on 1/9/2016.
 */
@Local
public interface MailServiceLocal {
    void sendEmail(Users user, String message,  String subject);
    void sendEmail(List<Users> userList, String message, String subject);
}
