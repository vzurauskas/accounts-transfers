package com.vzurauskas.accountstransfers;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class FakeAccounts implements Accounts {

    private final List<Account> accounts;
    private final FakeTransfers transfers;

    public FakeAccounts() {
        this.accounts = new LinkedList<>();
        this.transfers = new FakeTransfers();
    }

    @Override
    public Account add(String iban, String currency) {
        Account account = new FakeAccount(UUID.randomUUID(), iban, currency, transfers);
        accounts.add(account);
        return account;
    }

    @Override
    public Account find(UUID id) {
        return accounts.stream()
            .filter(account -> account.id().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No account with id=" + id));
    }
}
