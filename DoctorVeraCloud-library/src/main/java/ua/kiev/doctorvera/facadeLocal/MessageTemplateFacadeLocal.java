package ua.kiev.doctorvera.facadeLocal;

import org.primefaces.model.SortOrder;
import ua.kiev.doctorvera.entities.MessageTemplate;
import ua.kiev.doctorvera.entities.Users;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

/**
 * Interface for declaring main operations with MessageTemplate entity
 * @author Volodymyr Bodnar
 * @date 22.11.2015.
 */
public interface MessageTemplateFacadeLocal  extends CRUDFacade<MessageTemplate>{

}
