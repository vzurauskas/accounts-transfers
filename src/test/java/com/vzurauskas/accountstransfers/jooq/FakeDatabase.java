package com.vzurauskas.accountstransfers.jooq;

import java.util.Arrays;
import java.util.Collections;

import com.vzurauskas.accountstransfers.fakejooq.FakeJooq;
import com.vzurauskas.accountstransfers.fakejooq.MapTable;
import com.vzurauskas.accountstransfers.fakejooq.Table;
import com.vzurauskas.accountstransfers.fakejooq.UniquenessConstraint;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;

public final class FakeDatabase {

    private final FakeJooq jooq;

    public FakeDatabase() {
        Table accounts = new MapTable("ACCOUNT");
        Table transfers = new MapTable(
            "TRANSFER",
            Collections.singleton(new UniquenessConstraint(Arrays.asList("CLIENT_ID", "IDEMPOTENCY_KEY")))
        );
        DSLContext dsl = DSL.using(SQLDialect.H2);
        this.jooq = new FakeJooq(
            Arrays.asList(
                new InsertIntoAccounts(dsl, accounts),
                new SelectAccountById(accounts),
                new SelectAccountByIban(accounts),
                new InsertIntoTransfers(dsl, transfers),
                new SelectTransfersByDebtorAndCreditor(dsl, transfers)
            )
        );
    }

    public DSLContext connect() {
        return DSL.using(
            new MockConnection(jooq), SQLDialect.H2
        );
    }
}