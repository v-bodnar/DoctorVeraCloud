package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.TransactionLog;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Interface for declaring main operations with TransactionLog entity
 * @author Volodymyr Bodnar
 * @date 22.11.2015.
 */
public interface TransactionLogFacadeLocal  extends CRUDFacade<TransactionLog>{

}
