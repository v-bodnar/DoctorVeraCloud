/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import ua.kiev.doctorvera.entities.MethodTypes;
import ua.kiev.doctorvera.facadeLocal.MethodTypesFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Class for implementing main operations with User entity
 * @author Volodymyr Bodnar
 */
@Stateless
public class MethodTypesFacade extends AbstractFacade<MethodTypes> implements MethodTypesFacadeLocal {
		
    public MethodTypesFacade() {
        super(MethodTypes.class);
    }

    /**
    Searches for all MethodTypes by the given name
    @return List<MethodTypes> List of existing MethodTypes entities that are not marked as deleted
    @param shortName - short name to search for
    */
    @Override
    public List<MethodTypes> findByShortName(String shortName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<MethodTypes> cq = cb.createQuery(MethodTypes.class);
        Root<MethodTypes> root = cq.from(MethodTypes.class);

        cq.select(root).where(cb.equal(root.<String>get("shortName"), shortName),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<MethodTypes> resultList = getEntityManager().createQuery(cq).getResultList();

        return resultList;
    }
}
