package com.vzurauskas.accountstransfers;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.jooq.exception.DataAccessException;

public final class FakeTransfers {

    private final List<FakeTransfer> transfers;

    public FakeTransfers() {
        this.transfers = new LinkedList<>();
    }

    public void add(FakeTransfer transfer) {
        if (transfers.contains(transfer)) {
            throw new DataAccessException("Idempotency violation");
        }
        transfers.add(transfer);
    }

    public Stream<Transaction> transactions() {
        return transfers.stream()
            .map(FakeTransfer::toTransaction);
    }
}
