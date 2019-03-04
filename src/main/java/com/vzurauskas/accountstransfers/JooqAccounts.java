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
    public Account byId(UUID id) {
        return new JooqAccount(id, db);
    }

    @Override
    public Account byIban(String iban) {
        return db
            .select()
            .from("ACCOUNT")
            .where(DSL.field("ACCOUNT.IBAN").eq(iban))
            .fetch().stream()
            .findFirst()
            .map(
                record -> new SimpleAccount(
                    new JooqAccount(record.get("ID", UUID.class), db),
                    record.get("IBAN").toString(),
                    record.get("CURRENCY").toString()
                )
            ).orElseThrow(() -> new IllegalArgumentException("No account with iban=" + iban));
    }
}
