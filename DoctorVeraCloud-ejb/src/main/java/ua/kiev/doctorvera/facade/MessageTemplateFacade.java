package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.MessageTemplate;
import ua.kiev.doctorvera.facadeLocal.MessageTemplateFacadeLocal;

import javax.ejb.Stateless;

/**
 * Class for implementing main operations with MessageScheduler entity
 *
 * @author Volodymyr Bodnar
 * @date 23.11.2015
 */
@Stateless
public class MessageTemplateFacade  extends AbstractFacade<MessageTemplate> implements MessageTemplateFacadeLocal {

    public MessageTemplateFacade() {
        super(MessageTemplate.class);
    }
}
