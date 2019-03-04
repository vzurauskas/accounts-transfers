package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

public final class FakeAccount implements Account {

    private static final UncheckedMapper mapper = new UncheckedMapper();

    private final UUID id;
    private final String iban;
    private final String currency;
    private final FakeTransfers transfers;

    public FakeAccount(UUID id, String iban, String currency, FakeTransfers transfers) {
        this.id = id;
        this.iban = iban;
        this.currency = currency;
        this.transfers = transfers;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public Transfer debit(Account creditor, BigDecimal amount, String curr) {
        return new FakeTransfer(transfers, this, creditor, amount, curr);
    }

    @Override
    public Stream<Transaction> transactions() {
        return transfers.transactions();
    }

    @Override
    public JsonNode json() {
        ObjectNode account = mapper.objectNode();
        account.put("id", id.toString());
        account.put("iban", iban);
        account.put("currency", currency);
        return account;
    }
}
