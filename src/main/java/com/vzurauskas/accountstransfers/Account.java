package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public interface Account {
    UUID id();
    void debit(Account creditor, Amount amount);
    JsonNode json();
}
