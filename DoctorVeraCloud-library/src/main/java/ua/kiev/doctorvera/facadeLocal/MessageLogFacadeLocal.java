package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.MessageLog;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Interface for declaring main operations with MessageLog entity
 * @author Volodymyr Bodnar
 * @date 22.11.2015.
 */
public interface MessageLogFacadeLocal extends CRUDFacade<MessageLog> {

}
