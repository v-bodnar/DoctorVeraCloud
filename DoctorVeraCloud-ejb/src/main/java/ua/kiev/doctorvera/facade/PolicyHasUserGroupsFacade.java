/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kiev.doctorvera.facade;

import ua.kiev.doctorvera.entities.Policy;
import ua.kiev.doctorvera.entities.PolicyHasUserGroups;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.facadeLocal.PolicyHasUserGroupsFacadeLocal;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bodun
 */
@Stateless
public class PolicyHasUserGroupsFacade extends AbstractFacade<PolicyHasUserGroups> implements PolicyHasUserGroupsFacadeLocal {

    public PolicyHasUserGroupsFacade() {
        super(PolicyHasUserGroups.class);
    }

    /**
    Searches for given policy in the Reference table PolicyHasUserGroupsFacade and returns all found records
    @param policy - policy to search for
    @return List<PolicyHasUserGroupsFacade> List of PolicyHasUserGroupsFacade records that match search parameter
     */
	@Override
    public List<PolicyHasUserGroups> findPolicyByPolicy(Policy policy) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PolicyHasUserGroups> cq = cb.createQuery(PolicyHasUserGroups.class);
        Root<PolicyHasUserGroups> root = cq.from(PolicyHasUserGroups.class);

        cq.select(root).where(cb.equal(root.<Policy>get("policy"), policy),cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<PolicyHasUserGroups> result = getEntityManager().createQuery(cq).getResultList();
        if(result == null) return new ArrayList<>();
        else return result;
	}

    /**
    Searches for given User Group in the Reference table PolicyHasUserGroupsFacade and returns all found records
    @param group - User Group to search for
    @return List<PolicyHasUserGroupsFacade> List of PolicyHasUserGroupsFacade records that match search parameter
     */
	@Override
	public List<PolicyHasUserGroups> findPolicyByGroup(UserGroups group) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PolicyHasUserGroups> cq = cb.createQuery(PolicyHasUserGroups.class);
        Root<PolicyHasUserGroups> root = cq.from(PolicyHasUserGroups.class);

        cq.select(root).where(cb.equal(root.<UserGroups>get("userGroup"), group), cb.isFalse(root.<Boolean>get("deleted")));
        cq.distinct(true);
        List<PolicyHasUserGroups> result = getEntityManager().createQuery(cq).getResultList();
        if(result == null) return new ArrayList<>();
        else return result;
	}

    /**
     Searches for the Reference record in PolicyHasUserGroupsFacadeLocal and returns all found records
     @param group - User Group to search for
     @param policy - Policy to search for
     @return List<PolicyHasUserGroupsFacadeLocal> List of PolicyHasUserGroupsFacadeLocal records that match search parameter
     */
    @Override
    public List<PolicyHasUserGroups> findPolicyByGroupAndPolicy(UserGroups group, Policy policy){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PolicyHasUserGroups> cq = cb.createQuery(PolicyHasUserGroups.class);
        Root<PolicyHasUserGroups> root = cq.from(PolicyHasUserGroups.class);
        Predicate groupPredicate = cb.and(cb.equal(root.<UserGroups>get("userGroup"), group));
        Predicate policyPredicate = cb.and(cb.equal(root.<Policy>get("policy"), policy));
        Predicate deletedPredicate = cb.and(cb.isFalse(root.<Boolean>get("deleted")));
        cq.select(root).where(policyPredicate, groupPredicate, deletedPredicate);
        cq.distinct(true);
        List<PolicyHasUserGroups> result = getEntityManager().createQuery(cq).getResultList();
        if(result == null) return new ArrayList<>();
        else return result;
    }


}
