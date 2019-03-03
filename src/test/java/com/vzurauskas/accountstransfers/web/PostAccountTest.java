package com.vzurauskas.accountstransfers.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.vzurauskas.accountstransfers.Accounts;
import com.vzurauskas.accountstransfers.FakeAccounts;
import com.vzurauskas.accountstransfers.UncheckedMapper;
import org.takes.rq.RqFake;

final class PostAccountTest {

    private final UncheckedMapper mapper = new UncheckedMapper();

    private final Accounts accounts = new FakeAccounts();

    @Test
    void postsAccount() throws IOException {
        assertEquals(
            "DE89370400440532013000",
            mapper.json(
                new PostAccount(accounts).act(
                    new RqFake(
                        "POST", "/accounts",
                        mapper.text(
                            mapper.objectNode().put("iban", "DE89370400440532013000")
                        )
                    )
                ).body()
            ).get("iban").textValue()
        );
    }
}