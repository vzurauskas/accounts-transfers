package com.vzurauskas.accountstransfers.http;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vzurauskas.accountstransfers.AccountById;
import org.jooq.DSLContext;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqHref;

/*
 * Take for experimenting with Null Object pattern in HTTP CRUD scenarios.
 */
public final class GetAccountNo implements Take {

    private static final Logger log = LoggerFactory.getLogger(GetAccountNo.class);

    private final DSLContext db;

    public GetAccountNo(DSLContext db) {
        this.db = db;
    }

    @Override
    public Response act(Request req) throws IOException {
        String uri = new RqHref.Base(req).href().bare();
        log.info("GET /accounts/{}", id(uri));
        return new AccountById(id(uri), db).httpResponse();
    }

    private static UUID id(String uri) {
        return UUID.fromString(uri.substring(uri.lastIndexOf('/') + 1));
    }
}
