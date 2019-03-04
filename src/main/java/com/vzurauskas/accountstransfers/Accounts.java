package com.vzurauskas.accountstransfers;

import java.util.UUID;

public interface Accounts {
    Account add(String iban, String currency);
    Account byId(UUID id);
    Account byIban(String iban);
}
