package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.MessageScheduler;
import ua.kiev.doctorvera.facadeLocal.MessageSchedulerFacadeLocal;

import javax.ejb.Stateless;

/**
 * Class for implementing main operations with MessageScheduler entity
 *
 * @author Volodymyr Bodnar
 * @date 23.11.2015
 */
@Stateless
public class MessageSchedulerFacade  extends AbstractFacade<MessageScheduler> implements MessageSchedulerFacadeLocal {

    public MessageSchedulerFacade() {
        super(MessageScheduler.class);
    }
}
