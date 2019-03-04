package com.vzurauskas.accountstransfers;

import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public final class JooqAccounts implements Accounts {

    private final DSLContext db;

    public JooqAccounts(DSLContext db) {
        this.db = db;
    }

    @Override
    public Account add(String iban, String currency) {
        UUID id = UUID.randomUUID();
        db
            .insertInto(DSL.table("ACCOUNT"), DSL.field("ID"), DSL.field("IBAN"), DSL.field("CURRENCY"))
            .values(id, iban, currency)
            .execute();
        return new JooqAccount(id, db);
    }

    @Override
    public Account find(UUID id) {
        return new JooqAccount(id, db);
    }
}
