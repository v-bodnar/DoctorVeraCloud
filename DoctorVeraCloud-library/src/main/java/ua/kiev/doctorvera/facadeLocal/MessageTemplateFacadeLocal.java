package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.MessageTemplate;

import javax.ejb.Local;
import java.util.List;

/**
 * Interface for declaring main operations with MessageTemplate entity
 * @author Volodymyr Bodnar
 * @date 22.11.2015.
 */
@Local
public interface MessageTemplateFacadeLocal  extends CRUDFacade<MessageTemplate>{

    List<MessageTemplate> findByType(MessageTemplate.Type type);
    List<MessageTemplate> findByType(MessageTemplate.Type type, Boolean system);
    List<MessageTemplate> findByName(String Name);
}
