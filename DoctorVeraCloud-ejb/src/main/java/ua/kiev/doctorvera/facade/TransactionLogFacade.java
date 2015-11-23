package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.TransactionLog;
import ua.kiev.doctorvera.facadeLocal.TransactionLogFacadeLocal;

import javax.ejb.Stateless;

/**
 * Class for implementing main operations with MessageScheduler entity
 *
 * @author Volodymyr Bodnar
 * @date 23.11.2015
 */
@Stateless
public class TransactionLogFacade extends AbstractFacade<TransactionLog> implements TransactionLogFacadeLocal {

    public TransactionLogFacade() {
        super(TransactionLog.class);
    }
}
