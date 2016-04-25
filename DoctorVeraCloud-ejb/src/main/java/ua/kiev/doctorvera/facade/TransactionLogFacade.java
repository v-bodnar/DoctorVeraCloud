package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.TransactionLogFacadeLocal;
import ua.kiev.doctorvera.services.SMSService;
import ua.kiev.doctorvera.services.SMSServiceLocal;

import javax.ejb.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Class for implementing main operations with MessageScheduler entity
 *
 * @author Volodymyr Bodnar
 * @date 23.11.2015
 */
@Stateless
public class TransactionLogFacade extends AbstractFacade<TransactionLog> implements TransactionLogFacadeLocal {

    @EJB
    private SMSServiceLocal smsService;

    public TransactionLogFacade() {
        super(TransactionLog.class);
    }
    @Override
    public List<TransactionLog> findUncompleted(){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TransactionLog> cq = cb.createQuery(TransactionLog.class);

        Root root = cq.from(TransactionLog.class);
        cq.distinct(true);

        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate progressStatusPredicate = cb.equal(root.<TransactionLog.Status>get("status"), TransactionLog.Status.PROGRESS);
        Predicate sentStatusPredicate = cb.equal(root.<TransactionLog.Status>get("status"), TransactionLog.Status.SENT);
        Predicate emailTypePredicate = cb.equal(root.<MessageTemplate.Type>get("messageTemplate").get("type"), MessageTemplate.Type.EMAIL);
        Predicate smsTypePredicate = cb.equal(root.<MessageTemplate.Type>get("messageTemplate").get("type"), MessageTemplate.Type.SMS);
        Predicate emailPredicate = cb.and(emailTypePredicate, progressStatusPredicate);
        Predicate smsPredicate = cb.and(smsTypePredicate, cb.or(progressStatusPredicate, sentStatusPredicate));

        cq.select(root).where(deletedPredicate, cb.or(emailPredicate, smsPredicate));

        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public void checkTransactionStatus(){
        checkTransactionStatus(findUncompleted(), true);
    }

    @Override
    public void checkTransactionStatus(List<TransactionLog> transactionLogs, Boolean checkOnSmsGateway){
        if(transactionLogs == null) return;
        for(TransactionLog transactionLog : transactionLogs){
            checkTransactionStatus(transactionLog, checkOnSmsGateway);
        }
    }

    @Override
    public void checkTransactionStatus(TransactionLog transactionLog, Boolean checkOnSmsGateway){
        boolean transactionStatusChanged = false;
        //-------- SMS ---------
        if(transactionLog.getMessageTemplate().getType() == MessageTemplate.Type.SMS) {

            //Case when transaction is finished
            if (transactionLog.getStatus() == TransactionLog.Status.DELIVERED
                    || transactionLog.getStatus() == TransactionLog.Status.SEND_ERROR
                    || transactionLog.getStatus() == TransactionLog.Status.DELIVERY_ERROR) return;

            //Refreshing status
            if(checkOnSmsGateway){
                smsService.checkSmsStatus(transactionLog);
            }

//            int delivered = 0;
//            for (MessageLog messageLog : transactionLog.getMessageLogs()) {
//                switch (messageLog.getStatus()) {
//                    case SEND_ERROR:
//                        transactionLog.setStatus(TransactionLog.Status.SEND_ERROR);
//                        transactionStatusChanged = true;
//                        break;
//                    case DELIVERY_ERROR:
//                        transactionLog.setStatus(TransactionLog.Status.DELIVERY_ERROR);
//                        transactionStatusChanged = true;
//                        break;
//                    case DELIVERED:
//                        delivered++;
//                        break;
//                }
//            }
//            if (transactionLog.getRecipientsCount() != null && transactionLog.getRecipientsCount() == delivered) {
//                transactionLog.setStatus(TransactionLog.Status.DELIVERED);
//                transactionStatusChanged = true;
//            }

        }//-------- EMAILS ---------
        else if(transactionLog.getMessageTemplate().getType() == MessageTemplate.Type.EMAIL) {

            //Case when transaction is finished
            if (transactionLog.getStatus() == TransactionLog.Status.SENT
                    || transactionLog.getStatus() == TransactionLog.Status.SEND_ERROR) return;

            int sent = 0;
            for (MessageLog messageLog : transactionLog.getMessageLogs()) {
                switch (messageLog.getStatus()) {
                    case SEND_ERROR:
                        transactionLog.setStatus(TransactionLog.Status.SEND_ERROR);
                        transactionStatusChanged = true;
                        break;
                    case SENT:
                        sent++;
                        break;
                }
            }
            if (transactionLog.getRecipientsCount() == sent) {
                transactionLog.setStatus(TransactionLog.Status.SENT);
                transactionStatusChanged = true;
            }
        }
        if(transactionStatusChanged) edit(transactionLog);
    }
}
