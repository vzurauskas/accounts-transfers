package com.vzurauskas.accountstransfers;

import java.util.UUID;

public interface Accounts {
    Account add(String iban);
    Account find(UUID id);
}
