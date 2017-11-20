package ua.kiev.doctorvera.sql;

import org.hibernate.dialect.PostgreSQL9Dialect;

import java.sql.Types;

/**
 * Created by Vova on 11/20/2017.
 */
public class PostgreSQLDialect extends PostgreSQL9Dialect{
    public PostgreSQLDialect() {
        super();
        registerColumnType(Types.BLOB, "bytea");
    }
}
