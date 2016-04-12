package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.TransactionLog;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Interface for declaring main operations with TransactionLog entity
 * @author Volodymyr Bodnar
 * @date 22.11.2015.
 */
@Local
public interface TransactionLogFacadeLocal  extends CRUDFacade<TransactionLog>{
    List<TransactionLog> findUncompleted();
    void checkTransactionStatus();
    void checkTransactionStatus(TransactionLog transactionLog);
    void checkTransactionStatus(List<TransactionLog> transactionLogs);
}
