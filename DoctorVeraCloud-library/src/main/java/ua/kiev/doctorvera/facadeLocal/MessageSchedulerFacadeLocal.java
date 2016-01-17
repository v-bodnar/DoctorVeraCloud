package ua.kiev.doctorvera.facadeLocal;

import org.primefaces.model.SortOrder;
import ua.kiev.doctorvera.entities.MessageScheduler;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

/**
 * Interface for declaring main operations with MessageScheduler entity
 * @author Volodymyr Bodnar
 * @date 22.11.2015.
 */
public interface MessageSchedulerFacadeLocal extends CRUDFacade<MessageScheduler> {

}
