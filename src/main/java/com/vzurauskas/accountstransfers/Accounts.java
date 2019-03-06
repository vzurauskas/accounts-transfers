package com.vzurauskas.accountstransfers;

import java.util.Optional;
import java.util.UUID;

public interface Accounts {
    UUID add(String iban, String currency);
    Optional<Account> byId(UUID id);
    Optional<Account> byIban(String iban);
}
