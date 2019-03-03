package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

public final class SimpleAccount implements Account {

    private static final UncheckedMapper mapper = new UncheckedMapper();

    private final UUID id;
    private final String iban;

    public SimpleAccount(UUID id, String iban) {
        this.id = id;
        this.iban = iban;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public void debit(Account creditor, Amount amount) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public JsonNode json() {
        ObjectNode account = mapper.objectNode();
        account.put("id", id.toString());
        account.put("iban", iban);
        return account;
    }
}
