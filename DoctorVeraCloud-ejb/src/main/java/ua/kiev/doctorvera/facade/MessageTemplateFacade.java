package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.MessageTemplate;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.MessageTemplateFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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

    /**
     @return List<MessageTemplate> List of all MessageTemplate existing entities that are not marked as deleted
     and have corresponding type value
     @param type search for templates with this type
     */
    public List<MessageTemplate> findByType(MessageTemplate.Type type){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<MessageTemplate> cq = cb.createQuery(MessageTemplate.class);

        Root root = cq.from(MessageTemplate.class);
        cq.distinct(true);

        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate typePredicate = cb.and(cb.equal(root.<MessageTemplate.Type>get("type"), type));
        cq.select(root).where(deletedPredicate, typePredicate);

        return getEntityManager().createQuery(cq).getResultList();
    }
}
