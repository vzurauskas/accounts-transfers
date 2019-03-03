package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public final class JooqAccount implements Account {

    private static final UncheckedMapper mapper = new UncheckedMapper();

    private final UUID id;
    private final DSLContext db;

    public JooqAccount(UUID id, DSLContext db) {
        this.id = id;
        this.db = db;
    }

    @Override
    public UUID id() {
        return db
            .select()
            .from("ACCOUNT")
            .where(DSL.field("ACCOUNT.ID").eq(id))
            .fetch().stream()
            .findFirst().orElseThrow(() -> new IllegalArgumentException("No account with id=" + id))
            .get("ID", UUID.class);
    }

    @Override
    public void debit(Account creditor, Amount amount) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public JsonNode json() {
        ObjectNode account = mapper.objectNode();
        account.put("iban", iban());
        return account;
    }

    private String iban() {
        return db
            .select()
            .from("ACCOUNT")
            .where(DSL.field("ACCOUNT.ID").eq(id))
            .fetch().stream()
            .findFirst().orElseThrow(() -> new IllegalArgumentException("No account with id=" + id))
            .get("IBAN").toString();
    }
}
