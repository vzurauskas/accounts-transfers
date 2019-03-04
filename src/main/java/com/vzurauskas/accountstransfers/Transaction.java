package com.vzurauskas.accountstransfers;

public interface Transaction {
    Amount amountFor(Account account);
}
