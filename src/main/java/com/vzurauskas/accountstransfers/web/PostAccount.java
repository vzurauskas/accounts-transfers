package com.vzurauskas.accountstransfers.web;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vzurauskas.accountstransfers.Account;
import com.vzurauskas.accountstransfers.Accounts;
import com.vzurauskas.accountstransfers.UncheckedMapper;
import org.takes.Body;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqGreedy;
import org.takes.rs.RsWithBody;
import org.takes.rs.RsWithStatus;
import org.takes.rs.RsWithType;

public final class PostAccount implements Take {

    private static final Logger log = LoggerFactory.getLogger(PostAccount.class);
    private final UncheckedMapper mapper = new UncheckedMapper();

    private final Accounts accounts;

    public PostAccount(Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public Response act(Request req) throws IOException {
        log.info("POST /accounts");
        return response(accounts.add(
            iban(new RqGreedy(req)))
        );
    }

    private Response response(Account account) {
        return new RsWithType(
            new RsWithStatus(
                new RsWithBody(
                    mapper.bytes(account.json())
                ),
                201
            ),
            "application/json"
        );
    }

    private String iban(Body req) throws IOException {
        return Optional.ofNullable(
            mapper.json(
                req.body()
            ).get("iban").textValue()
        ).orElseThrow(() -> new IllegalArgumentException("Bad request - no 'iban'."));
    }
}
