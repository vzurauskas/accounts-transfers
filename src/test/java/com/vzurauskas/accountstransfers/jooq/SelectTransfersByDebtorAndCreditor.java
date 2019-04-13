package com.vzurauskas.accountstransfers.jooq;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.vzurauskas.accountstransfers.fakejooq.Statement;
import com.vzurauskas.accountstransfers.fakejooq.Table;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.jdbc.MockResult;

public final class SelectTransfersByDebtorAndCreditor implements Statement {

    private final DSLContext dsl;
    private final Table transfers;

    public SelectTransfersByDebtorAndCreditor(DSLContext dsl, Table transfers) {
        this.dsl = dsl;
        this.transfers = transfers;
    }

    @Override
    public boolean matches(String sql) {
        return sql.startsWith(
            "select * from TRANSFER where (TRANSFER.DEBTOR = cast(? as uuid) or TRANSFER.CREDITOR = cast(? as uuid))"
        );
    }

    @Override
    public MockResult[] execute(Object[] bindings) {
        final List<Record> rows = transfers.selectWhereDisjunct(
            Arrays.asList(
                new AbstractMap.SimpleEntry<>("DEBTOR", bindings[0]),
                new AbstractMap.SimpleEntry<>("CREDITOR", bindings[1])
            )
        ).collect(Collectors.toList());
        Result<Record8<UUID, Timestamp, UUID, UUID, BigDecimal, String, String, String>> result = dsl.newResult(
            DSL.field(DSL.name("TRANSFER", "ID"), SQLDataType.UUID),
            DSL.field(DSL.name("TRANSFER", "TIMESTAMP"), SQLDataType.TIMESTAMP),
            DSL.field(DSL.name("TRANSFER", "DEBTOR"), SQLDataType.UUID),
            DSL.field(DSL.name("TRANSFER", "CREDITOR"), SQLDataType.UUID),
            DSL.field(DSL.name("TRANSFER", "AMOUNT"), SQLDataType.DECIMAL),
            DSL.field(DSL.name("TRANSFER", "CURRENCY"), SQLDataType.VARCHAR),
            DSL.field(DSL.name("TRANSFER", "CLIENT_ID"), SQLDataType.VARCHAR),
            DSL.field(DSL.name("TRANSFER", "IDEMPOTENCY_KEY"), SQLDataType.VARCHAR)
        );
        transfers.selectWhereDisjunct(
            Arrays.asList(
                new AbstractMap.SimpleEntry<>("DEBTOR", bindings[0]),
                new AbstractMap.SimpleEntry<>("CREDITOR", bindings[1])
            )
        ).forEach(record -> result.add((Record8<UUID, Timestamp, UUID, UUID, BigDecimal, String, String, String>) record));
        return new MockResult[]{new MockResult(rows.size(), result)};
    }
}
