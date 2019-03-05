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
    public UUID add(String iban, String currency) {
        UUID id = UUID.randomUUID();
        db
            .insertInto(DSL.table("ACCOUNT"), DSL.field("ID"), DSL.field("IBAN"), DSL.field("CURRENCY"))
            .values(id, iban, currency)
            .execute();
        return id;
    }

    @Override
    public Account byId(UUID id) {
        return db
            .select()
            .from("ACCOUNT")
            .where(DSL.field("ACCOUNT.ID").eq(id))
            .fetch().stream()
            .findFirst()
            .map(record -> new JooqAccount(record, db))
            .orElseThrow(() -> new IllegalArgumentException("No account with id=" + id));
    }

    @Override
    public Account byIban(String iban) {
        return db
            .select()
            .from("ACCOUNT")
            .where(DSL.field("ACCOUNT.IBAN").eq(iban))
            .fetch().stream()
            .findFirst()
            .map(record -> new JooqAccount(record, db))
            .orElseThrow(() -> new IllegalArgumentException("No account with iban=" + iban));
    }
}
