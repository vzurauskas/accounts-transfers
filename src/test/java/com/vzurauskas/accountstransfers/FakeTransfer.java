package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.util.UUID;

import com.vzurauskas.accountstransfers.misc.UncheckedMapper;

public final class FakeTransfer implements Transfer {

    private final UncheckedMapper mapper = new UncheckedMapper();

    private final FakeTransfers transfers;
    private final Account debtor;
    private final Account creditor;
    private final BigDecimal amount;
    private final String currency;

    public FakeTransfer(FakeTransfers transfers, Account debtor, Account creditor, BigDecimal amount, String currency) {
        this.transfers = transfers;
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    public void execute() {
        transfers.add(this);
    }

    @Override
    public JsonNode json() {
        ObjectNode transfer = mapper.objectNode();
        transfer.put("id", UUID.randomUUID().toString());
        transfer.put("debtor", debtor.iban());
        transfer.put("creditor", creditor.iban());
        transfer.set("instructedAmount", new Amount(amount, currency).json());
        return transfer;
    }

    public Transaction toTransaction() {
        return new SimpleTransaction(debtor.id(), creditor.id(), amount, currency);
    }
}
