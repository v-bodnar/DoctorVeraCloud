package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.MessageLog;
import ua.kiev.doctorvera.facadeLocal.MessageLogFacadeLocal;

import javax.ejb.Stateless;

/**
 * Class for implementing main operations with MessageLog entity
 *
 * @author Volodymyr Bodnar
 * @date 23.11.2015
 */
@Stateless
public class MessageLogFacade  extends AbstractFacade<MessageLog> implements MessageLogFacadeLocal {

    public MessageLogFacade() {
        super(MessageLog.class);
    }
}
