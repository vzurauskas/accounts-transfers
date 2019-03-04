package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

public final class SimpleAccount implements Account {

    private static final UncheckedMapper mapper = new UncheckedMapper();

    private final Account origin;
    private final String iban;
    private final String currency;

    public SimpleAccount(Account origin, String iban, String currency) {
        this.origin = origin;
        this.iban = iban;
        this.currency = currency;
    }

    @Override
    public UUID id() {
        return origin.id();
    }

    @Override
    public String iban() {
        return iban;
    }

    @Override
    public Transfer debit(Account creditor, BigDecimal amount, String currency) {
        return origin.debit(creditor, amount, currency);
    }

    @Override
    public Stream<Transaction> transactions() {
        return origin.transactions();
    }

    @Override
    public JsonNode json() {
        ObjectNode account = mapper.objectNode();
        account.put("id", id().toString());
        account.put("iban", iban());
        account.put("currency", currency);
        return account;
    }
}
