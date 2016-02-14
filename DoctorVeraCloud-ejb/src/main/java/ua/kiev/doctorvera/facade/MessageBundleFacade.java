package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.Locale;
import ua.kiev.doctorvera.entities.MessageBundle;
import ua.kiev.doctorvera.facadeLocal.MessageBundleFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for implementing main operations with Locale entity
 *
 * @author Volodymyr Bodnar
 * @date 23.01.2016
 */
@Stateless
public class MessageBundleFacade extends AbstractFacade<MessageBundle> implements MessageBundleFacadeLocal {
    public MessageBundleFacade() {
        super(MessageBundle.class);
    }

    /**
     * This method retrieves map with string messages for given locale from persistent storage
     * @param locale given locale
     * @return map with string messages for given locale
     */
    @Override
    public Map<String, String> findAllMessages(Locale locale) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<MessageBundle> cq = cb.createQuery(MessageBundle.class);

        Root root = cq.from(MessageBundle.class);
        cq.distinct(true);
        cq.orderBy(cb.asc(root.get("messageKey")));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate localePredicate = cb.and(cb.equal(root.get("locale"), locale));
        Predicate typePredicate = cb.and(cb.equal(root.get("type"), MessageBundle.Type.MESSAGE));
        cq.select(root).where(deletedPredicate, localePredicate,typePredicate);

        List<MessageBundle> bundleList = getEntityManager().createQuery(cq).getResultList();

        Map<String, String> messages = new LinkedHashMap<>();
        for(MessageBundle bundle : bundleList){
            messages.put(bundle.getMessageKey(), bundle.getValue());
        }

        return messages;
    }

    /**
     * This method returns map with string constants from persistent storage
     * @return map with string constants
     */
    @Override
    public Map<String, String> findAllConfigEntries() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<MessageBundle> cq = cb.createQuery(MessageBundle.class);

        Root root = cq.from(MessageBundle.class);
        cq.distinct(true);
        cq.orderBy(cb.asc(root.get("messageKey")));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate typePredicate = cb.and(cb.equal(root.get("type"), MessageBundle.Type.CONFIG));
        cq.select(root).where(deletedPredicate, typePredicate);

        List<MessageBundle> bundleList = getEntityManager().createQuery(cq).getResultList();

        Map<String, String> messages = new LinkedHashMap<>();
        for(MessageBundle bundle : bundleList){
            messages.put(bundle.getMessageKey(), bundle.getValue());
        }

        return messages;
    }

    /**
     * This method returns map with string constants from persistent storage
     * @return map with string constants
     */
    @Override
    public Map<String, String> findAllMappingEntries() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<MessageBundle> cq = cb.createQuery(MessageBundle.class);

        Root root = cq.from(MessageBundle.class);
        cq.distinct(true);
        cq.orderBy(cb.asc(root.get("messageKey")));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate typePredicate = cb.and(cb.equal(root.get("type"), MessageBundle.Type.MAPPING));
        cq.select(root).where(deletedPredicate, typePredicate);

        List<MessageBundle> bundleList = getEntityManager().createQuery(cq).getResultList();

        Map<String, String> messages = new LinkedHashMap<>();
        for(MessageBundle bundle : bundleList){
            messages.put(bundle.getMessageKey(), bundle.getValue());
        }

        return messages;
    }

    /**
     * This method retrieves entity of MessageBundle given locale and key
     * that contains string value from persistent storage
     * @param key key to search for
     * @param locale with the given locale
     * @return
     */
    @Override
    public MessageBundle findMessageByLocaleAndKey(String key, Locale locale) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<MessageBundle> cq = cb.createQuery(MessageBundle.class);

        Root root = cq.from(MessageBundle.class);
        cq.distinct(true);
        cq.orderBy(cb.asc(root.get("messageKey")));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate localePredicate = cb.and(cb.equal(root.get("locale"), locale));
        Predicate messageKeyPredicate = cb.and(cb.equal(root.get("messageKey"), key));
        Predicate typePredicate = cb.and(cb.equal(root.get("type"), MessageBundle.Type.MESSAGE));
        cq.select(root).where(deletedPredicate, localePredicate, typePredicate, messageKeyPredicate);

        try {
            MessageBundle message = getEntityManager().createQuery(cq).getSingleResult();
            return message;
        }catch (NoResultException e) {
            return null;
        }
    }

    /**
     * This method retrieves list of MessageBundle entities by given locale
     * that contains keys and corresponding string values
     * @param locale with the given locale
     * @return
     */
    @Override
    public List<MessageBundle> findAllMessagesByLocale(Locale locale) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<MessageBundle> cq = cb.createQuery(MessageBundle.class);

        Root root = cq.from(MessageBundle.class);
        cq.distinct(true);
        cq.orderBy(cb.asc(root.get("messageKey")));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        Predicate localePredicate = cb.and(cb.equal(root.get("locale"), locale));
        Predicate typePredicate = cb.and(cb.equal(root.get("type"), MessageBundle.Type.MESSAGE));
        cq.select(root).where(deletedPredicate, localePredicate, typePredicate);

        List<MessageBundle> messages = getEntityManager().createQuery(cq).getResultList();

        return messages;
    }
}
