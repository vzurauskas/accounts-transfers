package com.vzurauskas.accountstransfers.web;

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
    void getsAccount() throws IOException {
        Account account = accounts.add("DE89370400440532666000");
        assertEquals(
            mapper.objectNode()
                .put("id", account.id().toString())
                .put("iban", "DE89370400440532666000"),
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