package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.jooq.Record;
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
        return record().get("ID", UUID.class);
    }

    @Override
    public String iban() {
        return record().get("IBAN").toString();
    }

    @Override
    public Transfer debit(Account creditor, BigDecimal amount, String currency) {
        return new JooqTransfer(db, this, creditor, amount, currency);
    }

    @Override
    public Stream<Transaction> transactions() {
        return db
            .select()
            .from("TRANSFER")
            .where(DSL.field("TRANSFER.DEBTOR").eq(id))
            .or(DSL.field("TRANSFER.CREDITOR").eq(id))
            .fetch().stream()
            .map(
                record -> new SimpleTransaction(
                    record.get("DEBTOR", UUID.class),
                    record.get("CREDITOR", UUID.class),
                    record.get("AMOUNT", BigDecimal.class),
                    record.get("CURRENCY", String.class)
                )
            );
    }

    @Override
    public JsonNode json() {
        ObjectNode account = mapper.objectNode();
        account.put("id", id.toString());
        account.put("iban", record().get("IBAN").toString());
        account.put("currency", record().get("CURRENCY").toString());
        return account;
    }

    private Record record() {
        return db
            .select()
            .from("ACCOUNT")
            .where(DSL.field("ACCOUNT.ID").eq(id))
            .fetch().stream()
            .findFirst().orElseThrow(() -> new IllegalArgumentException("No account with id=" + id));
    }
}
