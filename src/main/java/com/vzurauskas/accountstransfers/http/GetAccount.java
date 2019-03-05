package com.vzurauskas.accountstransfers.http;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vzurauskas.accountstransfers.AccountWithBalance;
import com.vzurauskas.accountstransfers.Accounts;
import com.vzurauskas.accountstransfers.misc.UncheckedMapper;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqHref;
import org.takes.rs.RsJson;
import org.takes.rs.RsWithBody;

public final class GetAccount implements Take {

    private static final Logger log = LoggerFactory.getLogger(GetAccount.class);
    private final UncheckedMapper mapper = new UncheckedMapper();

    private final Accounts accounts;

    public GetAccount(Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public Response act(Request req) throws IOException {
        String uri = new RqHref.Base(req).href().bare();
        log.info("GET /accounts/{}", id(uri));
        return new RsJson(
            new RsWithBody(
                mapper.bytes(
                    new AccountWithBalance(accounts.byId(id(uri))).json()
                )
            )
        );
    }

    private static UUID id(String uri) {
        return UUID.fromString(uri.substring(uri.lastIndexOf('/') + 1));
    }
}
