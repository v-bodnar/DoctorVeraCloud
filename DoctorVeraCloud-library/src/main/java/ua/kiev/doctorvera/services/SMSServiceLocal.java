package ua.kiev.doctorvera.services;

import ua.kiev.doctorvera.entities.TransactionLog;
import ua.kiev.doctorvera.entities.Users;

import javax.ejb.Local;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by volodymyr.bodnar on 1/10/2016.
 */
@Local
public interface SMSServiceLocal {
    /**
     * Send one SMS to Gateway, type - single
     * It checks whether user is agreed to receive sms and if he has phoneNumber
     */
    boolean sendSMS(Users user, String message, TransactionLog transactionLog);

    /**
     * Send multiple SMS with the same content to Gateway, type - bulk
     */
    boolean sendSMS(List<Users> userList, String message, TransactionLog transactionLog);

    /**
     * Send multiple SMS with different content for each number to Gateway, type - individual
     */
    boolean sendSMS(LinkedHashMap<Users, String> messages, TransactionLog transactionLog);

    /**
     * Send one SMS to Gateway, type - single
     * It checks whether user is agreed to receive sms and if he has phoneNumber
     * If there is no user with the given phone number, current user will be written as a recipient
     */
    boolean sendSMS(String phone, String message, TransactionLog transactionLog);

    /**
     * Sends request to SMS Gateway and retrieves message/transactions status
     * @param transactionLog transaction log for which status has to be refreshed
     */
    void checkSmsStatus(TransactionLog transactionLog);
}
