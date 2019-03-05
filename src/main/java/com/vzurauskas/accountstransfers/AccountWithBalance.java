package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public final class AccountWithBalance implements Account {

    private static final String DEFAULT_CURRENCY = "EUR";

    private final Account origin;

    public AccountWithBalance(Account origin) {
        this.origin = origin;
    }

    @Override
    public UUID id() {
        return origin.id();
    }

    @Override
    public String iban() {
        return origin.iban();
    }

    @Override
    public Transfer debit(Account creditor, BigDecimal amount, String currency, Map<String, String> headers) {
        return origin.debit(creditor, amount, currency, headers);
    }

    @Override
    public Stream<Transaction> transactions() {
        return origin.transactions();
    }

    @Override
    public JsonNode json() {
        return ((ObjectNode) origin.json().deepCopy())
            .set("balance", balance().json());
    }

    private Amount balance() {
        return origin.transactions()
            .map(transaction -> transaction.amountFor(this))
            .reduce(Amount::plus)
            .orElse(new Amount(BigDecimal.ZERO, DEFAULT_CURRENCY));
    }
}
