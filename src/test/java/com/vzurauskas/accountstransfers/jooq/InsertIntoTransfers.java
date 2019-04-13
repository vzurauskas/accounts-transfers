package com.vzurauskas.accountstransfers.jooq;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import com.vzurauskas.accountstransfers.fakejooq.Statement;
import com.vzurauskas.accountstransfers.fakejooq.Table;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.jdbc.MockResult;

public final class InsertIntoTransfers implements Statement {

    private final DSLContext dsl;
    private final Table transfers;

    public InsertIntoTransfers(DSLContext dsl, Table transfers) {
        this.dsl = dsl;
        this.transfers = transfers;
    }

    @Override
    public boolean matches(String sql) {
        return sql.toUpperCase().startsWith("INSERT INTO TRANSFER ");
    }

    @Override
    public MockResult[] execute(Object[] bindings) throws SQLException {
        transfers.insert(
            dsl.newRecord(
                DSL.field(DSL.name("TRANSFER", "ID"), SQLDataType.UUID),
                DSL.field(DSL.name("TRANSFER", "TIMESTAMP"), SQLDataType.TIMESTAMP),
                DSL.field(DSL.name("TRANSFER", "DEBTOR"), SQLDataType.UUID),
                DSL.field(DSL.name("TRANSFER", "CREDITOR"), SQLDataType.UUID),
                DSL.field(DSL.name("TRANSFER", "AMOUNT"), SQLDataType.DECIMAL),
                DSL.field(DSL.name("TRANSFER", "CURRENCY"), SQLDataType.VARCHAR),
                DSL.field(DSL.name("TRANSFER", "CLIENT_ID"), SQLDataType.VARCHAR),
                DSL.field(DSL.name("TRANSFER", "IDEMPOTENCY_KEY"), SQLDataType.VARCHAR)
            ).values(
                UUID.fromString(bindings[0].toString()),
                Timestamp.valueOf(bindings[1].toString().split("\\+")[0]),
                UUID.fromString(bindings[2].toString()),
                UUID.fromString(bindings[3].toString()),
                new BigDecimal(bindings[4].toString()),
                bindings[5].toString(),
                bindings[6].toString(),
                bindings[7].toString()
            )
        );
        return new MockResult[0];
    }
}
