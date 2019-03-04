package com.vzurauskas.accountstransfers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public final class JooqTransfer implements Transfer {

    private static final UncheckedMapper mapper = new UncheckedMapper();

    private final DSLContext db;
    private final Account debtor;
    private final Account creditor;
    private final BigDecimal amount;
    private final String currency;

    public JooqTransfer(DSLContext db, Account debtor, Account creditor, BigDecimal amount, String currency) {
        this.db = db;
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
            .values(UUID.randomUUID(), OffsetDateTime.now(), debtor.id(), creditor.id(), amount, currency)
            .execute();
    }
}
