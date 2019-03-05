package com.vzurauskas.accountstransfers.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.vzurauskas.accountstransfers.AccountWithBalance;
import com.vzurauskas.accountstransfers.Accounts;
import com.vzurauskas.accountstransfers.Amount;
import com.vzurauskas.accountstransfers.FakeAccounts;
import com.vzurauskas.accountstransfers.misc.UncheckedMapper;
import org.takes.rq.RqFake;
import org.takes.rq.RqWithHeaders;

final class PostTransferTest {

    private final UncheckedMapper mapper = new UncheckedMapper();

    private final Accounts accounts = new FakeAccounts();

    @Test
    void displaysTransferInResponse() throws IOException {
        accounts.add("debtorIban", "EUR");
        accounts.add("creditorIban", "EUR");
        JsonNode request = mapper.objectNode()
            .put("creditor", "creditorIban")
            .put("debtor", "debtorIban")
            .set(
                "instructedAmount",
                mapper.objectNode()
                    .put("amount", "10.00")
                    .put("currency", "EUR")
            );
        assertEquals(
            request,
            mapper.json(
                new PostTransfer(accounts).act(
                    new RqFake("POST", "/transfers", mapper.string(request))
                ).body()
            )
        );
    }

    @Test
    void makesThreeTransfers() throws IOException {
        accounts.add("debtorIban", "EUR");
        accounts.add("creditorIban", "EUR");
        JsonNode request = mapper.objectNode()
            .put("creditor", "creditorIban")
            .put("debtor", "debtorIban")
            .set(
                "instructedAmount",
                mapper.objectNode()
                    .put("amount", "10.00")
                    .put("currency", "EUR")
            );

        new PostTransfer(accounts).act(
            new RqWithHeaders(
                new RqFake("POST", "/transfers", mapper.string(request)),
                "x-client-id: client",
                "x-idempotency-key: abc1"
            )
        );
        new PostTransfer(accounts).act(
            new RqWithHeaders(
                new RqFake("POST", "/transfers", mapper.string(request)),
                "x-client-id: client",
                "x-idempotency-key: abc2"
            )
        );
        new PostTransfer(accounts).act(
            new RqWithHeaders(
                new RqFake("POST", "/transfers", mapper.string(request)),
                "x-client-id: client",
                "x-idempotency-key: abc3"
            )
        );

        assertEquals(
            new Amount(new BigDecimal("-30.00"), "EUR").json(),
            new AccountWithBalance(accounts.byIban("debtorIban")).json().get("balance")
        );
        assertEquals(
            new Amount(new BigDecimal("30.00"), "EUR").json(),
            new AccountWithBalance(accounts.byIban("creditorIban")).json().get("balance")
        );
    }

    @Test
    void postTransferIsIdempotent() throws IOException {
        accounts.add("debtorIban", "EUR");
        accounts.add("creditorIban", "EUR");
        JsonNode request = mapper.objectNode()
            .put("creditor", "creditorIban")
            .put("debtor", "debtorIban")
            .set(
                "instructedAmount",
                mapper.objectNode()
                    .put("amount", "10.00")
                    .put("currency", "EUR")
            );

        new PostTransfer(accounts).act(
            new RqWithHeaders(
                new RqFake("POST", "/transfers", mapper.string(request)),
                "x-client-id: client",
                "x-idempotency-key: same"
            )
        );
        new PostTransfer(accounts).act(
            new RqWithHeaders(
                new RqFake("POST", "/transfers", mapper.string(request)),
                "x-client-id: client",
                "x-idempotency-key: same"
            )
        );

        assertEquals(1L, accounts.byIban("debtorIban").transactions().count());
        assertEquals(1L, accounts.byIban("creditorIban").transactions().count());
        assertEquals(
            new Amount(new BigDecimal("-10.00"), "EUR").json(),
            new AccountWithBalance(accounts.byIban("debtorIban")).json().get("balance")
        );
        assertEquals(
            new Amount(new BigDecimal("10.00"), "EUR").json(),
            new AccountWithBalance(accounts.byIban("creditorIban")).json().get("balance")
        );
    }
}