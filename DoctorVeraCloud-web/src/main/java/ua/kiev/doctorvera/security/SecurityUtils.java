package ua.kiev.doctorvera.security;

import ua.kiev.doctorvera.entities.Policy;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.PolicyFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UserGroupsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.resources.Mapping;
import ua.kiev.doctorvera.views.SessionParams;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by volodymyr.bodnar on 05.10.2015.
 */
@Named
@SessionScoped
public class SecurityUtils implements Serializable{

    @Inject
    private SessionParams sessionParams;

    @EJB
    private UsersFacadeLocal usersFacade;

    @EJB
    private PolicyFacadeLocal policyFacade;

    @EJB
    private UserGroupsFacadeLocal userTypesFacade;

    private static final Map<String, SecurityPolicy> mappedPagesToPolicies  = new HashMap<>();

    private List<Policy> currentUserPermissions;

    private final static Logger LOG = Logger.getLogger(SecurityUtils.class.getName());
    private static final String PERMISSION_DENIED_PAGE = Mapping.getInstance().getString("PERMISSION_DENIED_PAGE");
    private static final String MAIN_PAGE_URL = Mapping.getInstance().getString("MAIN_PAGE");
    private static final String USER_PROFILE_PAGE_URL = Mapping.getInstance().getString("USER_PROFILE_PAGE");
    private static final String USERS_PAGE_URL = Mapping.getInstance().getString("USERS_PAGE");
    private static final String USER_ADD_PAGE_URL = Mapping.getInstance().getString("USER_ADD_PAGE");
    private static final String USER_GROUPS_PAGE_URL = Mapping.getInstance().getString("USER_TYPES_PAGE");
    private static final String SEND_SMS_PAGE_URL = Mapping.getInstance().getString("SEND_SMS_PAGE");
    private static final String ROOMS_PAGE_URL = Mapping.getInstance().getString("ROOMS_PAGE");
    private static final String PLAN_PAGE_URL = Mapping.getInstance().getString("PLAN_PAGE");
    private static final String METHODS_PAGE_URL = Mapping.getInstance().getString("METHODS_PAGE");
    private static final String PAYMENTS_PAGE_URL = Mapping.getInstance().getString("PAYMENTS_PAGE");
    private static final String SCHEDULE_PAGE_URL = Mapping.getInstance().getString("SCHEDULE_PAGE");
    private static final String SCHEDULE_GENERAL_PAGE_URL = Mapping.getInstance().getString("SCHEDULE_GENERAL_PAGE");
    private static final String PLAN_GENERAL_PAGE_URL = Mapping.getInstance().getString("PLAN_GENERAL_PAGE");
    private static final String DELIVERY_GROUPS_PAGE_URL = Mapping.getInstance().getString("DELIVERY_GROUPS_PAGE");
    private static Map<SecurityPolicy,Policy> allMappedPolicies = new HashMap<>();

    private static int SUPER_ADMIN_ID;
    private static int SUPER_ADMIN_USER_GROUP_ID;

    private Boolean alreadySynchronized = false;


    public SecurityUtils(){
    }

    @PostConstruct
    public void init(){

        SUPER_ADMIN_ID = Integer.parseInt(Config.getInstance().getString("SUPER_ADMIN_ID"));
        SUPER_ADMIN_USER_GROUP_ID = Integer.parseInt(Config.getInstance().getString("SUPER_ADMIN_USER_GROUP_ID"));
        if(!alreadySynchronized){
            synchronize();
            checkAdminPermissions();
            mapPagesToPolicies();
        }
    }

    private static void mapPagesToPolicies(){
        mappedPagesToPolicies.put(MAIN_PAGE_URL, SecurityPolicy.MENU_ITEM_MAIN);
        mappedPagesToPolicies.put(USER_PROFILE_PAGE_URL, SecurityPolicy.USERS_USERS_PROFILE);
        mappedPagesToPolicies.put(USERS_PAGE_URL, SecurityPolicy.MENU_ITEM_USERS);
        mappedPagesToPolicies.put(USER_ADD_PAGE_URL, SecurityPolicy.USERS_ADD_USER);
        mappedPagesToPolicies.put(USER_GROUPS_PAGE_URL, SecurityPolicy.MENU_ITEM_USER_GROUPS);
        mappedPagesToPolicies.put(SEND_SMS_PAGE_URL, SecurityPolicy.MENU_ITEM_SEND_SMS);
        mappedPagesToPolicies.put(ROOMS_PAGE_URL, SecurityPolicy.MENU_ITEM_ROOMS);
        mappedPagesToPolicies.put(PLAN_PAGE_URL, SecurityPolicy.MENU_ITEM_MAIN);
        mappedPagesToPolicies.put(METHODS_PAGE_URL, SecurityPolicy.MENU_ITEM_METHODS);
        mappedPagesToPolicies.put(PAYMENTS_PAGE_URL, SecurityPolicy.MENU_ITEM_PAYMENTS);
        mappedPagesToPolicies.put(SCHEDULE_PAGE_URL, SecurityPolicy.MENU_ITEM_SCHEDULE);
        mappedPagesToPolicies.put(SCHEDULE_GENERAL_PAGE_URL, SecurityPolicy.MENU_ITEM_SCHEDULE_GENERAL);
        mappedPagesToPolicies.put(PLAN_GENERAL_PAGE_URL, SecurityPolicy.MENU_ITEM_PLAN_GENERAL);
        mappedPagesToPolicies.put(DELIVERY_GROUPS_PAGE_URL, SecurityPolicy.MENU_ITEM_DELIVERY_GROUPS);
    }

    /**
     * Synchronizes all policies from Enum with their representation in DB
     */
    private void synchronize() {
        LOG.info("Start synchronizing security Policies");
        List<SecurityPolicy> securityPolicies = Arrays.asList(SecurityPolicy.values());

        int policiesCreated = 0;
        int policiesUpdated = 0;

        Map<String, Policy> allPoliciesInDb = new HashMap();
        if(allPoliciesInDb.isEmpty()) {
            for (Policy policy : policyFacade.initializeLazyEntity(policyFacade.findAll())) {
                allPoliciesInDb.put(policy.getStringId(), policy);
            }
        }

        for (SecurityPolicy securityPolicy : securityPolicies) {
            Policy policy = allPoliciesInDb.get(securityPolicy.name());

            if (policy == null) {
                //policy has not been found and has to be created
                Policy newPolicy = new Policy();
                newPolicy.setStringId(securityPolicy.name());
                newPolicy.setPolicyGroup(securityPolicy.getPolicyGroup().name());
                newPolicy.setDateCreated(new Date());
                newPolicy.setUserCreated(sessionParams.getAuthorizedUser());
                policyFacade.create(newPolicy);
                policiesCreated++;
                policy = newPolicy;
            } else if (!isEqual(policy, securityPolicy)) {
                //policy has been found and needs synchronization
                policy.setStringId(securityPolicy.name());
                policy.setPolicyGroup(securityPolicy.getPolicyGroup().name());
                policy.setDateCreated(new Date());
                policy.setUserCreated(sessionParams.getAuthorizedUser());
                policyFacade.edit(policy);
                policiesUpdated++;
            }
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
        Users superAdmin = usersFacade.initializeLazyEntity(usersFacade.find(SUPER_ADMIN_ID));
        UserGroups superAdminGroup = userTypesFacade.find(SUPER_ADMIN_USER_GROUP_ID);

        List<UserGroups> groups = userTypesFacade.findByUser(superAdmin);

        if(!groups.contains(superAdminGroup)) {
            superAdmin.getUserGroups().add(superAdminGroup);
            usersFacade.edit(superAdmin);
        }

        List<Policy> convertedPolicies = convertPoliciesToEntities(Arrays.asList(SecurityPolicy.values()));
        boolean updatePolicies = false;
        for(Policy policy : convertedPolicies){
            if (!policy.getUserGroups().contains(superAdminGroup)) {
                policy.getUserGroups().add(superAdminGroup);
                updatePolicies = true;
            }
        }
        if(updatePolicies)
            policyFacade.edit(convertedPolicies);
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
    public List<Policy> convertPoliciesToEntities(List<SecurityPolicy> securityPolicyList){
        List<Policy> result = new ArrayList<>();
        for(SecurityPolicy policyEnum : securityPolicyList){
            result.add(allMappedPolicies.get(policyEnum));
        }
        return policyFacade.initializeLazyEntity(result);
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
        if (policyEnum.name().equals(policyFromDB.getStringId()) &&
                policyEnum.getPolicyGroup().name().equals(policyFromDB.getPolicyGroup())
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
        if (currentUserPermissions == null) {
            currentUserPermissions = policyFacade.findByUser(sessionParams.getAuthorizedUser());
        }
        List<SecurityPolicy> policiesListFromDb = convertEntitiesToPolicies(currentUserPermissions);
        return policiesListFromDb.contains(policy);
    }
    public boolean checkPermissions(String policy) {
        return checkPermissions(SecurityPolicy.valueOf(policy));
    }

    public void checkPermissionAndRedirect(SecurityPolicy policy) throws IOException {
        if(!checkPermissions(policy))
            FacesContext.getCurrentInstance().getExternalContext().redirect(PERMISSION_DENIED_PAGE);
    }

    public static Map<String, SecurityPolicy> getMappedPagesToPolicies() {
        return mappedPagesToPolicies;
    }

    public Boolean isAlreadySynchronized() {
        return alreadySynchronized;
    }

    public void setAlreadySynchronized(Boolean alreadySynchronized) {
        this.alreadySynchronized = alreadySynchronized;
    }

}
