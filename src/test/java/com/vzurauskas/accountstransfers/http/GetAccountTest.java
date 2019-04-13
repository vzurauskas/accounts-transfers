package com.vzurauskas.accountstransfers.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.vzurauskas.accountstransfers.Accounts;
import com.vzurauskas.accountstransfers.jooq.FakeDatabase;
import com.vzurauskas.accountstransfers.jooq.JooqAccounts;
import com.vzurauskas.accountstransfers.misc.UncheckedMapper;
import org.takes.rq.RqFake;

final class GetAccountTest {

    private final UncheckedMapper mapper = new UncheckedMapper();

    private final Accounts accounts = new JooqAccounts(
        new FakeDatabase().connect()
    );

    @Test
    void getsAccountWithNoTransactions() throws IOException {
        UUID account = accounts.add("DE89370400440532666000", "EUR");
        assertEquals(
            mapper.objectNode()
                .put("id", account.toString())
                .put("iban", "DE89370400440532666000")
                .put("currency", "EUR")
                .set(
                    "balance",
                    mapper.objectNode()
                        .put("amount", "0.00")
                        .put("currency", "EUR")
                ),
            mapper.json(
                new GetAccount(accounts).act(
                    new RqFake(
                        "GET", "/accounts/" + account
                    )
                ).body()
            )
        );
    }
}