package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import com.vzurauskas.accountstransfers.misc.UncheckedMapper;

public final class FakeTransfer implements Transfer {

    private final UncheckedMapper mapper = new UncheckedMapper();

    private final FakeTransfers transfers;
    private final Account debtor;
    private final Account creditor;
    private final BigDecimal amount;
    private final String currency;
    private final Map<String, String> headers;

    public FakeTransfer(
        FakeTransfers transfers,
        Account debtor,
        Account creditor,
        BigDecimal amount,
        String currency,
        Map<String, String> headers
    ) {
        this.transfers = transfers;
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
        this.currency = currency;
        this.headers = headers;
    }

    @Override
    public void execute() {
        transfers.add(this);
    }

    @Override
    public JsonNode json() {
        ObjectNode transfer = mapper.objectNode();
        transfer.put("debtor", debtor.iban());
        transfer.put("creditor", creditor.iban());
        transfer.set("instructedAmount", new Amount(amount, currency).json());
        return transfer;
    }

    public Transaction toTransaction() {
        return new SimpleTransaction(debtor.id(), creditor.id(), amount, currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FakeTransfer)) return false;
        final FakeTransfer other = (FakeTransfer) o;
        return Objects.equals(headers.get("x-client-id"), other.headers.get("x-client-id"))
            && Objects.equals(headers.get("x-idempotency-key"), other.headers.get("x-idempotency-key"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers.get("x-client-id"), headers.get("x-idempotency-key"));
    }
}
