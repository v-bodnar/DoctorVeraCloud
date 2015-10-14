package ua.kiev.doctorvera.security;

import ua.kiev.doctorvera.entities.Policy;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PolicyFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UserGroupsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.views.SessionParams;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by volodymyr.bodnar on 05.10.2015.
 */
@Named
@ApplicationScoped
public class SecurityUtils implements Serializable{

    @Inject
    private SessionParams sessionParams;

    @EJB
    private UsersFacadeLocal usersFacade;

    @EJB
    private PolicyFacadeLocal policyFacade;

    @EJB
    private UserGroupsFacadeLocal userTypesFacade;

    private final static Logger LOG = Logger.getLogger(SecurityUtils.class.getName());

    private static Map<SecurityPolicy,Policy> allMappedPolicies = new HashMap<>();

    private static int SUPER_ADMIN_ID;
    private static int SUPER_ADMIN_USER_GROUP_ID;

    private Boolean alreadySynchronized = false;


    public SecurityUtils(){
    }

    @PostConstruct
    public void init(){

        SUPER_ADMIN_ID = Integer.parseInt(Config.getInstance().getProperty("SUPER_ADMIN_ID"));
        SUPER_ADMIN_USER_GROUP_ID = Integer.parseInt(Config.getProperty("SUPER_ADMIN_USER_GROUP_ID"));
        if(!alreadySynchronized){
            synchronize();
            checkAdminPermissions();
        }
    }

    /**
     * Synchronizes all policies from Enum with their representation in DB
     */
    private void synchronize() {
        LOG.info("Start synchronizing security Policies");
        List<SecurityPolicy> securityPolicies = Arrays.asList(SecurityPolicy.values());

        int policiesCreated = 0;
        int policiesUpdated = 0;

        for (SecurityPolicy securityPolicy : securityPolicies) {
            Policy policy = null;

            policy = policyFacade.findByStringId(securityPolicy.getStringId());

            if (policy == null) {
                //policy has not been found and has to be created
                Policy newPolicy = new Policy();
                newPolicy.setName(securityPolicy.getName());
                newPolicy.setStringId(securityPolicy.getStringId());
                newPolicy.setPolicyGroup(securityPolicy.getPolicyGroup());
                newPolicy.setDateCreated(new Date());
                newPolicy.setUserCreated(sessionParams.getAuthorizedUser());
                policyFacade.create(newPolicy);
                policiesCreated++;
            } else if (!isEqual(policy, securityPolicy)) {
                //policy has been found and needs synchronization
                policy.setName(securityPolicy.getName());
                policy.setStringId(securityPolicy.getStringId());
                policy.setPolicyGroup(securityPolicy.getPolicyGroup());
                policy.setDateCreated(new Date());
                policy.setUserCreated(sessionParams.getAuthorizedUser());
                policyFacade.edit(policy);
                policiesUpdated++;
            }
            policy = policyFacade.findByStringId(securityPolicy.getStringId());
            allMappedPolicies.put(SecurityPolicy.valueOf(policy.getStringId()), policy);
        }
        alreadySynchronized = true;
        LOG.info("Security Policy synchronisation completed successfully.");
        LOG.info("Created new policies: " + policiesCreated);
        LOG.info("Updated existing policies: " + policiesUpdated);
        LOG.info("Total number of Security Policies: " + allMappedPolicies.size());
    }

    /**
     * Assigns all rights to admin user
     */
    private void checkAdminPermissions(){
        Users superAdmin = usersFacade.find(SUPER_ADMIN_ID);
        UserGroups superAdminGroup = userTypesFacade.find(SUPER_ADMIN_USER_GROUP_ID);

        List<UserGroups> groups = userTypesFacade.findByUser(superAdmin);

        if(!groups.contains(superAdminGroup))
            userTypesFacade.addUser(superAdmin, superAdminGroup, sessionParams.getAuthorizedUser());

        for(Policy policy : convertPoliciesToEntities(Arrays.asList(SecurityPolicy.values()))){
            policyFacade.addUserGroups(superAdminGroup, policy, sessionParams.getAuthorizedUser());
        }
    }

    /**
     *  Method converts Policy entity to its representation SecurityPolicy enum
     * @param policyEntityList list of entities that has to be converted
     * @return list of SecurityPolicy enums
     */
    public static List<SecurityPolicy> convertEntitiesToPolicies(List<Policy> policyEntityList){
        List<SecurityPolicy> result = new ArrayList<>();
        for(Policy policyEntity: policyEntityList){
            result.add(SecurityPolicy.valueOf(policyEntity.getStringId()));
        }
        return result;
    }

    /**
     *  Method converts SecurityPolicy enum to its representation Policy entity
     * @param securityPolicyList list of enums that has to be converted
     * @return list of SecurityPolicy enums
     */
    public static List<Policy> convertPoliciesToEntities(List<SecurityPolicy> securityPolicyList){
        List<Policy> result = new ArrayList<>();
        for(SecurityPolicy policyEnum : securityPolicyList){
            result.add(allMappedPolicies.get(policyEnum));
        }
        return result;
    }

    /**
     * Checks all Policy entity fields for equality with its enum representation
     * Used to check for policy modifications
     * @param policyFromDB
     * @param policyEnum
     * @return
     */
    private static boolean isEqual(Policy policyFromDB, SecurityPolicy  policyEnum) {
        boolean equals = false;
        if (policyEnum.getName().equals(policyFromDB.getName()) &&
                policyEnum.getStringId().equals(policyFromDB.getStringId()) &&
                policyEnum.getPolicyGroup().equals(policyFromDB.getPolicyGroup())
                ) {
            equals = true;
        }
        return equals;
    }

    /**
     * Checks users permissions to currently executed method
     * @param policy executed method is annotated with this policy
     * @return boolean true - if usersType contains currently parsed policy, false - otherwise
     */
    public boolean checkPermissions(SecurityPolicy policy) {
        List<SecurityPolicy> policiesListFromDb = convertEntitiesToPolicies(userTypesFacade.findPoliciesByUser(sessionParams.getAuthorizedUser()));
        return policiesListFromDb.contains(policy);
    }
    public boolean checkPermissions(String policy) {
        return checkPermissions(SecurityPolicy.valueOf(policy));
    }

    public Boolean isAlreadySynchronized() {
        return alreadySynchronized;
    }

    public void setAlreadySynchronized(Boolean alreadySynchronized) {
        this.alreadySynchronized = alreadySynchronized;
    }
}
