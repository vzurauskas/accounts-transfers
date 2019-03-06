package com.vzurauskas.accountstransfers.jooq;

import java.util.Optional;
import java.util.UUID;

import com.vzurauskas.accountstransfers.Account;
import com.vzurauskas.accountstransfers.Accounts;

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
    public Optional<Account> byId(UUID id) {
        return db
            .select()
            .from("ACCOUNT")
            .where(DSL.field("ACCOUNT.ID").eq(id))
            .fetch().stream()
            .findFirst()
            .map(record -> new JooqAccount(record, db));

    }

    @Override
    public Optional<Account> byIban(String iban) {
        return db
            .select()
            .from("ACCOUNT")
            .where(DSL.field("ACCOUNT.IBAN").eq(iban))
            .fetch().stream()
            .findFirst()
            .map(record -> new JooqAccount(record, db));
    }
}
