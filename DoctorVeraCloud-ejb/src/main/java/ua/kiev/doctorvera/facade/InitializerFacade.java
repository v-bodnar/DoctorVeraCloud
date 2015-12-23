package ua.kiev.doctorvera.facade;

import org.hibernate.jpa.criteria.path.RootImpl;
import org.hibernate.jpa.internal.metamodel.PluralAttributeImpl;
import ua.kiev.doctorvera.entities.Identified;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.InitializerFacadeLocal;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.transaction.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by volodymyr.bodnar on 28.11.2015.
 */
@Stateless
public class InitializerFacade implements InitializerFacadeLocal {

    @PersistenceContext(unitName = "DoctorVera")
    private EntityManager em;

    @Override
    public Identified<Integer> initializeLazyEntity(Identified<Integer> entity) {

        try {
            entity = em.find(entity.getClass(),entity.getId());
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.getType().equals(Collection.class) || field.getType().equals(Set.class) || field.getType().equals(List.class)) {
                    field.setAccessible(true);
                    Collection collection = (Collection) field.get(entity);

                    collection.size();
                    field.set(entity, collection);
                    field.setAccessible(false);
                }
            }
            return entity;
        }  catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<? extends Identified> initializeLazyEntity(List<? extends Identified> entityList) {
        if (entityList.isEmpty()) return null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Class clazz = entityList.get(0).getClass();
        CriteriaQuery cq = cb.createQuery(clazz);
        Root root = cq.from(clazz);
//        Todo find resolution for org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
//        List<String> manyToManyFields = new LinkedList<>();
//        for (Object attribute : ((RootImpl)root).getEntityType().getPluralAttributes()){
//            if(attribute instanceof PluralAttribute && (((PluralAttribute) attribute).getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_MANY)){
//                //manyToManyFields.add(((PluralAttribute) attribute).getName());
//                root.fetch(((PluralAttribute) attribute).getName(),JoinType.INNER);
//            }
//        }

        cq.select(root).where(root.in(entityList));
        cq.distinct(true);
        List<Identified> result = new ArrayList<Identified>();
        for(Identified entity : (List<Identified>)em.createQuery(cq).getResultList()){
            result.add(initializeLazyEntity(entity));
        }
        return result;
    }

}
