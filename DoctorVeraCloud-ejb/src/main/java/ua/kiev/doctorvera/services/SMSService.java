package ua.kiev.doctorvera.services;

import ua.kiev.doctorvera.entities.MessageLog;
import ua.kiev.doctorvera.entities.TransactionLog;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.MessageLogFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.TransactionLogFacadeLocal;
import ua.kiev.doctorvera.utils.SMSGateway;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by volodymyr.bodnar on 1/10/2016.
 */
@Stateless
public class SMSService implements SMSServiceLocal{

    @EJB
    TransactionLogFacadeLocal transactionLogFacade;

    @EJB
    MessageLogFacadeLocal messageLogFacade;


    @Override
    public void sendSMS(Users user, String message, TransactionLog transactionLog) {
        Map<SMSGateway.SMSResultConstants, String> result = SMSGateway.send(user.getPhoneNumberMobile(), message);

        if(result == null) return;
        MessageLog messageLog = new MessageLog();
        messageLog.setUserCreated(transactionLog.getUserCreated());
        messageLog.setDateCreated(new Date());
        messageLog.setRecipient(user);
        messageLog.setTransaction(transactionLog);
        MessageLog.Status status;
        switch (result.get(SMSGateway.SMSResultConstants.STATUS)){
            case("waiting"):
                status = MessageLog.Status.WAITING;
                break;
            case("sending"):
                status = MessageLog.Status.SENDING;
                break;
            case("sent"):
                status = MessageLog.Status.SENT;
                break;
            case("paused"):
                status = MessageLog.Status.PAUSED;
                break;
            case("canceled"):
                status = MessageLog.Status.CANCELED;
                break;
            default:
                status = MessageLog.Status.NEW;
                break;
        }
        messageLog.setStatus(status);
        messageLog.setuId(result.get(SMSGateway.SMSResultConstants.UUID));
        messageLogFacade.create(messageLog);
    }

    @Override
    public void sendSMS(List<Users> userList, String message, TransactionLog transactionLog) {
        //todo
    }
}
