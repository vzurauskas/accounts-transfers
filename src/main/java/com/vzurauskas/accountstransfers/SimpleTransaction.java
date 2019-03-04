package com.vzurauskas.accountstransfers;

import java.math.BigDecimal;
import java.util.UUID;

public final class SimpleTransaction implements Transaction {

    private final UUID debtor;
    private final UUID creditor;
    private final BigDecimal amount;
    private final String currency;

    public SimpleTransaction(UUID debtor, UUID creditor, BigDecimal amount, String currency) {
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    public Amount amountFor(Account account) {
        BigDecimal result;
        if (debtor.equals(account.id())) {
            result = amount.negate();
        } else if (creditor.equals(account.id())) {
            result = amount;
        } else {
            throw new IllegalArgumentException("This transfer does not relate to account " + account.id());
        }
        return new Amount(result, currency);
    }
}
