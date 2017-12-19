package ua.kiev.doctorvera.services;

import org.hibernate.jpa.internal.EntityManagerImpl;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ua.kiev.doctorvera.entities.FileRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.logging.Logger;


@Stateless
public class SQLService implements SQLServiceLocal {
    private final static Logger LOG = Logger.getLogger(SQLService.class.getName());

    @PersistenceContext(unitName = "DoctorVera")
    private EntityManager em;

    /**
     * This method should iterate through all tables in Data Base,
     * construct Insert statement with data from each table, and write it to returned file
     *
     * @return sql file with fresh data from Data Base
     * @throws IOException throws exception if file is not readable or writable
     */
    @Override
    public StreamedContent generateNewDatabaseDump() throws IOException {
        StringBuilder dump = new StringBuilder();
        //dump.append("START TRANSACTION;\nSET foreign_key_checks = 0;\n");
        for(String tableName: getTableNames()){
            dump.append(createTableDump(tableName));
        }
        //dump.append("SET foreign_key_checks = 1;\nCOMMIT;\n");
        String newDumpFileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "_DBDump";
        String extension = FileRepository.MimeType.SQL.name().toLowerCase();
        File tempFile = File.createTempFile(newDumpFileName, extension);

        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"));
        writer.write(dump.toString());
        writer.flush();
        writer.close();

        StreamedContent streamedContent = new DefaultStreamedContent(
                new FileInputStream(tempFile), FileRepository.MimeType.SQL.getMimeType(), newDumpFileName + "." + extension, "UTF-8"
        );
        return streamedContent;
    }

    /**
     * !!! ATTENTION !!!
     * <p>
     * DROPPING DATA BASE CAN BE HARMFUL FOR YOUR DATA :-)
     */
    @Override
    public void dropDatabase() {
        final List<Boolean> result = new ArrayList<>(1);
        result.add(false);
        //Actually it does not drop database it drops all its tables
        EntityManagerImpl entityManagerImpl = (EntityManagerImpl) em.getDelegate();
        List<String> tableNames = getTableNames();
        entityManagerImpl.getSession().doWork(
                connection -> {
                    String dropQuery = "DROP TABLE ";

                    // First we output the create table stuff
                    for (String tableName : tableNames) {
                        dropQuery += tableName;
                        if (tableNames.indexOf(tableName) != tableNames.size() - 1) {
                            dropQuery += ", ";
                        }else{
                            dropQuery += "; ";
                        }
                    }
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate("SET foreign_key_checks = 0;");
                    stmt.executeUpdate(dropQuery);
                    stmt.executeUpdate("SET foreign_key_checks = 1;");
                    LOG.info("Data Base tables dropped!");
                }
        );
    }

    /**
     * Think this method is Data Base specific (MySql),
     * currently it is dedicated for searching table names of current Schema
     *
     * @return All table names of current Schema
     */
    public List<String> getTableNames() {
        List<String> tableNames = new LinkedList<>();
        Query tableNamesQuery = em.createNativeQuery("select TABLE_NAME from information_schema.tables where TABLE_SCHEMA = 'public' AND table_type='BASE TABLE'");
        tableNames.addAll(tableNamesQuery.getResultList());
        LOG.finest(tableNames.toString());
        return tableNames;
    }

    /**
     * This file manually constructs table dump (it creates insert queries for the parsed table name)
     * Attention! It uses specific Hibernate session for retrieving jdbc connection,
     * and will not work with other jpa implementations
     *
     * @param tableName table name for which we want to create insert statements
     * @return string with insert query for parsed Data Base Table name
     */
    private String createTableDump(final String tableName) {
        final StringBuilder tableDump = new StringBuilder();
        EntityManagerImpl entityManagerImpl = (EntityManagerImpl) em.getDelegate();

        //Selecting all data from current table
        entityManagerImpl.getSession().doWork(
                connection -> {
                    // First we output the create table stuff
                    PreparedStatement stmt = connection.prepareStatement("SELECT * FROM " + tableName);
                    ResultSet rs = stmt.executeQuery();
                    if(!rs.isBeforeFirst()) return; //no results

                    // Creating helper table with ordered column names
                    List<String> columnNames = new ArrayList<>();
                    ResultSetMetaData rsmt = rs.getMetaData();
                    for (int i = 1; i <= rsmt.getColumnCount(); i++) {
                        if (!columnNames.contains(rsmt.getColumnName(i)))
                            columnNames.add(rsmt.getColumnName(i));
                    }

                    //Constructing first part of insert statement
                    tableDump.append("INSERT INTO " + tableName + "\n");

                    // Constructing second part of insert  statement (those with column names)
                    tableDump.append(" (");
                    Iterator<String> columnIterator = columnNames.iterator();
                    while (columnIterator.hasNext()) {
                        tableDump.append(columnIterator.next());
                        if (columnIterator.hasNext()) tableDump.append(", ");
                    }
                    tableDump.append(") VALUES \n");

                    //Iterating data and constructing third part of insert table (those with data :-))
                    while (rs.next()) {
                        tableDump.append("(");
                        Iterator columnNamesIterator = columnNames.iterator();
                        while (columnNamesIterator.hasNext()) {
                            String columnName = (String) columnNamesIterator.next();
                            tableDump.append(convertSqlObjectToString(rs, columnName, rsmt.getColumnType(columnNames.indexOf(columnName) + 1)));
                            if (columnNamesIterator.hasNext()) tableDump.append(", ");
                        }
                        if (rs.isLast()) {
                            tableDump.append("); \n");
                        } else {
                            tableDump.append("), \n");
                        }
                    }

                    rs.close();
                    stmt.close();
                }
        );

        LOG.finest(tableDump.toString());
        LOG.info("Data Base tables exported to String!");
        return tableDump.toString();
    }

    /**
     * This method creates string for sql query depending on actual DB data type
     * @param rs result set
     * @param columnName current column name
     * @param type current column data type
     * @return string that is ready to insert into insert query as value of some column
     * @throws SQLException
     */
    private String convertSqlObjectToString(ResultSet rs, String columnName, int type) throws SQLException {
        Object value = rs.getObject(columnName);
        if (value == null) {
            return "NULL";
        } else if (type == -2) { // BLOB
            return "decode('" + rs.getString(columnName).replaceFirst("\\\\x","") + "','hex')";
        } else if (type == -7) { // TINY INT
            return "" + rs.getBoolean(columnName);
        } else if (type == 12){ // VARCHAR
            return "'" + rs.getString(columnName).replaceAll("'", "''") + "'";
        }else {
            return "'" + value.toString().replaceAll("'", "''") + "'";
        }
    }

    /**
     * Checks if Db tables are populated,
     * checks only users table it has to hold at least one record with root user
     *
     * @return true - if tables are populated, false otherwise
     */
    public boolean isTablesPopulated(){
        EntityManagerImpl entityManagerImpl = (EntityManagerImpl) em.getDelegate();
        List<Integer> result = new ArrayList<>(1);
        entityManagerImpl.getSession().doWork(
                connection -> {
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
                    rs.next();
                    result.add(0, rs.getInt(1));
                }
        );
        return result.get(0) != 0;
    }
}
