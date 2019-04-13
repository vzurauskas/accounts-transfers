package com.vzurauskas.accountstransfers.jooq;

import java.util.AbstractMap;
import java.util.Collections;

import com.vzurauskas.accountstransfers.fakejooq.Statement;
import com.vzurauskas.accountstransfers.fakejooq.Table;
import org.jooq.tools.jdbc.MockResult;

public final class SelectAccountByIban implements Statement {

    private final Table accounts;

    public SelectAccountByIban(Table accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean matches(String sql) {
        return sql.toUpperCase().startsWith("SELECT * FROM ACCOUNT WHERE ACCOUNT.IBAN =");
    }

    @Override
    public MockResult[] execute(Object[] bindings) {
        return accounts.selectWhereDisjunct(
            Collections.singleton(new AbstractMap.SimpleEntry<>("IBAN", bindings[0]))
        )
            .map(MockResult::new)
            .toArray(MockResult[]::new);
    }
}
