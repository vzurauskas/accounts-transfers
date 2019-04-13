package com.vzurauskas.accountstransfers.jooq;

import java.sql.SQLException;
import java.util.UUID;

import com.vzurauskas.accountstransfers.fakejooq.Statement;
import com.vzurauskas.accountstransfers.fakejooq.Table;
import org.jooq.tools.jdbc.MockResult;

public final class SelectAccountById implements Statement {

    private final Table accounts;

    public SelectAccountById(Table accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean matches(String sql) {
        return sql.toUpperCase().startsWith("SELECT * FROM ACCOUNT WHERE ACCOUNT.ID =");
    }

    @Override
    public MockResult[] execute(Object[] bindings) throws SQLException {
        return new MockResult[]{
            new MockResult(accounts.select(UUID.fromString(bindings[0].toString())))
        };
    }
}
