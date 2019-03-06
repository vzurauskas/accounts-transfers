package com.vzurauskas.accountstransfers.jooq;

import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vzurauskas.accountstransfers.Transfer;

import org.jooq.exception.DataAccessException;

public final class IdempotentTransfer implements Transfer {

    private static final Logger log = LoggerFactory.getLogger(IdempotentTransfer.class);

    private final Transfer origin;

    public IdempotentTransfer(Transfer origin) {
        this.origin = origin;
    }

    @Override
    public void execute() {
        try {
            origin.execute();
        } catch (DataAccessException e) {
            log.debug("Idempotency violation.", e);
        }

    }

    @Override
    public JsonNode json() {
        return origin.json();
    }
}
