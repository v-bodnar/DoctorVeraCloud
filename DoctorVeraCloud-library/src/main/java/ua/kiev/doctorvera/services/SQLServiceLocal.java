package ua.kiev.doctorvera.services;

import org.primefaces.model.StreamedContent;

import javax.ejb.Local;
import java.io.IOException;

/**
 * Service is dedicated for low level database manipulating
 *
 * Created by volodymyr.bodnar on 4/8/2016.
 */
@Local
public interface SQLServiceLocal {

    /**
     * This method should iterate through all tables in Data Base,
     * construct Insert statement with data from each table, and write it to returned file
     * @return sql file with fresh data from Data Base
     * @throws IOException throws exception if file is not readable or writable
     */
    StreamedContent generateNewDatabaseDump() throws IOException;

    /**
     * !!! ATTENTION !!!
     *
     * DROPPING DATA BASE CAN BE HARMFUL FOR YOUR DATA :-)
     */
    void dropDatabase();

    /**
     * Checks if tables are populated, checks only users table, it has to contain at least root user
     * @return true if tables are populated, false - otherwise
     */
    boolean isTablesPopulated();
}
