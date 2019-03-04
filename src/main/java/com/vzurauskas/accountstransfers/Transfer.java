package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.JsonNode;

public interface Transfer {
    void execute();
    JsonNode json();
}
