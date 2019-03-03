package com.vzurauskas.accountstransfers;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class FakeAccounts implements Accounts {

    private final List<Account> accounts;

    public FakeAccounts() {
        this.accounts = new LinkedList<>();
    }

    @Override
    public Account add(String iban) {
        Account account = new SimpleAccount(UUID.randomUUID(), iban);
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
