package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.vzurauskas.accountstransfers.misc.UncheckedMapper;

public final class Amount {

    private static final UncheckedMapper mapper = new UncheckedMapper();
    private static final DecimalFormat format = new DecimalFormat("#,##0.00");

    private final BigDecimal amount;
    private final String currency;

    public Amount(JsonNode json) {
        this(new BigDecimal(json.get("amount").textValue()), json.get("currency").textValue());
    }

    public Amount(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Amount plus(Amount other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies don't match.");
        }
        return new Amount(amount.add(other.amount), currency);
    }

    public JsonNode json() {
        ObjectNode account = mapper.objectNode();
        account.put("amount", format.format(amount));
        account.put("currency", currency);
        return account;
    }
}
