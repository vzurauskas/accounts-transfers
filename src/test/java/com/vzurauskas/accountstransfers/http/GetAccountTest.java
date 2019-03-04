package com.vzurauskas.accountstransfers.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.vzurauskas.accountstransfers.Account;
import com.vzurauskas.accountstransfers.Accounts;
import com.vzurauskas.accountstransfers.FakeAccounts;
import com.vzurauskas.accountstransfers.UncheckedMapper;
import org.takes.rq.RqFake;

final class GetAccountTest {

    private final UncheckedMapper mapper = new UncheckedMapper();

    private final Accounts accounts = new FakeAccounts();

    @Test
    void getsAccountWithNoTransactions() throws IOException {
        Account account = accounts.add("DE89370400440532666000", "EUR");
        assertEquals(
            mapper.objectNode()
                .put("id", account.id().toString())
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
                        "GET", "/accounts/" + account.id()
                    )
                ).body()
            )
        );
    }
}