package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public final class JooqTransfer implements Transfer {

    private static final UncheckedMapper mapper = new UncheckedMapper();

    private final DSLContext db;
    private final UUID id;
    private final Account debtor;
    private final Account creditor;
    private final BigDecimal amount;
    private final String currency;

    public JooqTransfer(DSLContext db, Account debtor, Account creditor, BigDecimal amount, String currency) {
        this.db = db;
        this.id = UUID.randomUUID();
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    public void execute() {
        db
            .insertInto(
                DSL.table("TRANSFER"),
                DSL.field("ID"),
                DSL.field("TIMESTAMP"),
                DSL.field("DEBTOR"),
                DSL.field("CREDITOR"),
                DSL.field("AMOUNT"),
                DSL.field("CURRENCY"))
            .values(id, OffsetDateTime.now(), debtor.id(), creditor.id(), amount, currency)
            .execute();
    }

    @Override
    public JsonNode json() {
        ObjectNode transfer = mapper.objectNode();
        transfer.put("id", id.toString());
        transfer.put("debtor", debtor.iban());
        transfer.put("creditor", creditor.iban());
        transfer.set("instructedAmount", new Amount(amount, currency).json());
        return transfer;
    }
}
