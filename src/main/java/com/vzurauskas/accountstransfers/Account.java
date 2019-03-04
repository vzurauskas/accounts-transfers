package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

public interface Account {
    UUID id();
    Transfer debit(Account creditor, BigDecimal amount, String currency);
    Stream<Transaction> transactions();
    JsonNode json();
}
