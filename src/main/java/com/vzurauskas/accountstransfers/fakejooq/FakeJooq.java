package com.vzurauskas.accountstransfers.fakejooq;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;

public final class FakeJooq implements MockDataProvider {

    private final List<Statement> statements;

    public FakeJooq(List<Statement> statements) {
        this.statements = new ArrayList<>(statements);
    }

    @Override
    public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
        Optional<Statement> statement = statements.stream()
            .filter(stmt -> stmt.matches(ctx.sql()))
            .findFirst();
        return statement
            .orElseThrow(() -> new UnsupportedOperationException("Can't understand SQL " + ctx.sql()))
            .execute(ctx.bindings());
    }
}
