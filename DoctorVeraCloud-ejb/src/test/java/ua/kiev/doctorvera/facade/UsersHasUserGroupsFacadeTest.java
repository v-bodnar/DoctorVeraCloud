package ua.kiev.doctorvera.facade;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ua.kiev.doctorvera.entities.UserGroups;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.entities.UsersHasUserGroups;
import ua.kiev.doctorvera.facadeLocal.UserGroupsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersHasUserGroupsFacadeLocal;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by volodymyr.bodnar on 02.05.2015.
 */
@Ignore
public class UsersHasUserGroupsFacadeTest {
    private static EJBContainer container;
    private static UsersHasUserGroupsFacadeLocal usersHasUserTypesFacade;
    private static UserGroupsFacadeLocal userTypesFacade;
    private static UsersFacadeLocal usersFacade;
    private static Users adminUser;
    public static final String ADMIN_LOGIN = "root";
    public static final Integer ADMIN_GROUP_ID = 3;

    @BeforeClass
    public static void setUp() {
        //Creating EJB container and searching for facade instances
        System.out.println("Create EJB container");
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        try {
            userTypesFacade = (UserGroupsFacadeLocal) container.getContext().lookup("java:global/classes/UserGroupsFacade");
            usersFacade = (UsersFacadeLocal) container.getContext().lookup("java:global/classes/UsersFacade");
            usersHasUserTypesFacade = (UsersHasUserGroupsFacadeLocal) container.getContext().lookup("java:global/classes/UsersHasUserGroupsFacade");
        } catch (NamingException ex) {
            Logger.getLogger(UsersHasUserGroupsFacadeTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Searching for admin user
        System.out.println("Find admin...");
        adminUser = usersFacade.findByUsername(ADMIN_LOGIN);
    }

    @AfterClass
    public static  void tearDown() {
        //Destroying EJB container
        System.out.println("Destroy EJB container");
        container.close();
    }

    @Test
    public void testFindTypesByUser() throws Exception {
        System.out.println("Testing method findUsersByGroup");
        UserGroups adminUserType = userTypesFacade.find(ADMIN_GROUP_ID);
        assertNotNull(adminUserType);

        List<UsersHasUserGroups> relations = usersHasUserTypesFacade.findUsersByGroup(adminUserType);
        assertNotNull(relations);
        assertTrue(relations.size()>0);
    }

    @Test
    public void testFindUsersByType() throws Exception {
        System.out.println("Testing method findUsersByGroup");
        assertNotNull(adminUser);

        List<UsersHasUserGroups> relations = usersHasUserTypesFacade.findGroupsByUser(adminUser);
        assertNotNull(relations);
        assertTrue(relations.size()>0);
    }
}