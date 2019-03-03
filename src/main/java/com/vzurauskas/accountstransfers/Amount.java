package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;

public interface Amount {
    Amount plus(Amount other);
    Amount minus(Amount other);
    JsonNode json();
}
