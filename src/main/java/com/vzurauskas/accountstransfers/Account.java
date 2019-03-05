package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public interface Account {
    UUID id();
    String iban();
    Transfer debit(Account creditor, BigDecimal amount, String currency, Map<String, String> headers);
    Stream<Transaction> transactions();
    JsonNode json();
}
