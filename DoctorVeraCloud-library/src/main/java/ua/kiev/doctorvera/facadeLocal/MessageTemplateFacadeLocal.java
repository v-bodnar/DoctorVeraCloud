package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.MessageTemplate;

import java.util.List;

/**
 * Interface for declaring main operations with MessageTemplate entity
 * @author Volodymyr Bodnar
 * @date 22.11.2015.
 */
public interface MessageTemplateFacadeLocal  extends CRUDFacade<MessageTemplate>{

    List<MessageTemplate> findByType(MessageTemplate.Type type);
    List<MessageTemplate> findByType(MessageTemplate.Type type, Boolean system);
    List<MessageTemplate> findByName(String Name);
}
