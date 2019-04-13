package com.vzurauskas.accountstransfers.jooq;

import java.sql.SQLException;
import java.util.UUID;

import com.vzurauskas.accountstransfers.fakejooq.Statement;
import com.vzurauskas.accountstransfers.fakejooq.Table;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.jdbc.MockResult;

public final class InsertIntoAccounts implements Statement {

    private final DSLContext dsl;
    private final Table accounts;

    public InsertIntoAccounts(DSLContext dsl, Table accounts) {
        this.dsl = dsl;
        this.accounts = accounts;
    }

    @Override
    public boolean matches(String sql) {
        return sql.toUpperCase().startsWith("INSERT INTO ACCOUNT ");
    }

    @Override
    public MockResult[] execute(Object[] bindings) throws SQLException {
        accounts.insert(
            dsl.newRecord(
                DSL.field(DSL.name("ACCOUNT", "ID"), SQLDataType.UUID),
                DSL.field(DSL.name("ACCOUNT", "IBAN"), SQLDataType.VARCHAR),
                DSL.field(DSL.name("ACCOUNT", "CURRENCY"), SQLDataType.VARCHAR)
            ).values(
                UUID.fromString(bindings[0].toString()),
                bindings[1].toString(),
                bindings[2].toString()
            )
        );
        return new MockResult[0];
    }
}
