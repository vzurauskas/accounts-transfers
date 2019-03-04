package com.vzurauskas.accountstransfers;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public final class FakeTransfers {

    private final List<Transaction> transactions;

    public FakeTransfers() {
        this.transactions = new LinkedList<>();
    }

    public void add(FakeTransfer transfer) {
        transactions.add(transfer.toTransaction());
    }

    public Stream<Transaction> transactions() {
        return transactions.stream();
    }
}
