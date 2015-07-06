package facade;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.kiev.doctorvera.entities.UserTypes;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.entities.UsersHasUserTypes;
import ua.kiev.doctorvera.facadeLocal.UserTypesFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersHasUserTypesFacadeLocal;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by volodymyr.bodnar on 02.05.2015.
 */
public class UsersHasUserTypesFacadeTest {
    private static EJBContainer container;
    private static UsersHasUserTypesFacadeLocal usersHasUserTypesFacade;
    private static UserTypesFacadeLocal userTypesFacade;
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
            userTypesFacade = (UserTypesFacadeLocal) container.getContext().lookup("java:global/classes/UserTypesFacade");
            usersFacade = (UsersFacadeLocal) container.getContext().lookup("java:global/classes/UsersFacade");
            usersHasUserTypesFacade = (UsersHasUserTypesFacadeLocal) container.getContext().lookup("java:global/classes/UsersHasUserTypesFacade");
        } catch (NamingException ex) {
            Logger.getLogger(UsersHasUserTypesFacadeTest.class.getName()).log(Level.SEVERE, null, ex);
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
        System.out.println("Testing method findUsersByType");
        UserTypes adminUserType = userTypesFacade.find(ADMIN_GROUP_ID);
        assertNotNull(adminUserType);

        List<UsersHasUserTypes> relations = usersHasUserTypesFacade.findUsersByType(adminUserType);
        assertNotNull(relations);
        assertTrue(relations.size()>0);
    }

    @Test
    public void testFindUsersByType() throws Exception {
        System.out.println("Testing method findUsersByType");
        assertNotNull(adminUser);

        List<UsersHasUserTypes> relations = usersHasUserTypesFacade.findTypesByUser(adminUser);
        assertNotNull(relations);
        assertTrue(relations.size()>0);
    }
}