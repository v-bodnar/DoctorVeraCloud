package ua.kiev.doctorvera.core;

import org.apache.commons.io.IOUtils;
import ua.kiev.doctorvera.services.SQLServiceLocal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * This bean will be started in the first order.
 * All checks and application preparations should take place here
 *
 * Created by volodymyr.bodnar on 4/6/2016.
 */
@Singleton
@Startup
public class StartUpBean {

    private final static Logger LOG = Logger.getLogger(StartUpBean.class.getName());

    @PersistenceContext(unitName = "DoctorVera")
    private EntityManager em;

    @EJB
    SQLServiceLocal sqlService;

    @PostConstruct
    public void init(){
        populateTables();
    }

    /**
     * This methods populates DB tables with data held in dump.sql
     */
    private void populateTables(){
        if(!sqlService.isTablesPopulated()){
            Query tablePopulationQuery;
            try {
                tablePopulationQuery = em.createNativeQuery(getSqlQueryForPopulatingTables());
            } catch (IOException e) {
                LOG.severe(e.getMessage());
                return;
            }
            tablePopulationQuery.executeUpdate();
            LOG.info("Data Base tables restored from dump!");
        }
    }

    /**
     * Method loads dump.sql file that is held in resources folder and converts it to string
     *
     * @return string with sql
     * @throws IOException
     */
    private String getSqlQueryForPopulatingTables() throws IOException {
        ClassLoader loader = StartUpBean.class.getClassLoader();
        InputStream inputStream = loader.getResourceAsStream("dump.sql");
        String dump = "SET foreign_key_checks = 0;  " + IOUtils.toString(inputStream, "UTF-8") + " SET foreign_key_checks = 1;";
        inputStream.close();
        return dump;
    }
}
