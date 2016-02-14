package ua.kiev.doctorvera.services;

import ua.kiev.doctorvera.entities.TransactionLog;
import ua.kiev.doctorvera.entities.Users;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by volodymyr.bodnar on 1/10/2016.
 */
@Local
public interface SMSServiceLocal {
    void sendSMS(Users user, String message, TransactionLog transactionLog);
    void sendSMS(List<Users> userList, String message, TransactionLog transactionLog);
}
