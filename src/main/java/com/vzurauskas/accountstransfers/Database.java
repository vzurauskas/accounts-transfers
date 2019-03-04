package com.vzurauskas.accountstransfers;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCPDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public final class Database {

    private final DataSource source;

    public Database() {
        this.source = dataSource();
    }

    public DSLContext connect() throws SQLException {
        DSLContext context = DSL.using(source.getConnection(), SQLDialect.H2);
        initTables(context);
        return context;
    }

    private static DataSource dataSource() {
        final BoneCPDataSource src = new BoneCPDataSource();
        src.setDriverClass("org.h2.Driver");
        //src.setJdbcUrl("jdbc:h2:~/test;DB_CLOSE_DELAY=-1");
        src.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        src.setUser("");
        src.setPassword("");
        return src;
    }

    private static void initTables(DSLContext context) {
        context.createTableIfNotExists("ACCOUNT")
            .column("ID", SQLDataType.UUID)
            .column("IBAN", SQLDataType.VARCHAR.length(34).nullable(false))
            .column("CURRENCY", SQLDataType.VARCHAR.length(8).nullable(false))
            .constraints(
                DSL.constraint("PK_ACCOUNT").primaryKey("ID")
            )
            .execute();
        context.createTableIfNotExists("TRANSFER")
            .column("ID", SQLDataType.UUID)
            .column("TIMESTAMP", SQLDataType.TIMESTAMP)
            .column("DEBTOR", SQLDataType.UUID)
            .column("CREDITOR", SQLDataType.UUID)
            .column("AMOUNT", SQLDataType.DECIMAL)
            .column("CURRENCY", SQLDataType.VARCHAR(8))
            .constraints(
                DSL.constraint("PK_TRANSFER").primaryKey("ID")
            )
            .execute();
    }
}
