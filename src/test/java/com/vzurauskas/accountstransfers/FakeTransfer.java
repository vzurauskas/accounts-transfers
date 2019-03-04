package com.vzurauskas.accountstransfers;

import java.math.BigDecimal;

public final class FakeTransfer implements Transfer {

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

    public Transaction toTransaction() {
        return new SimpleTransaction(debtor.id(), creditor.id(), amount, currency);
    }
}
