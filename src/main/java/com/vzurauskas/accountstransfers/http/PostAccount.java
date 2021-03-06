package com.vzurauskas.accountstransfers.http;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vzurauskas.accountstransfers.AccountWithBalance;
import com.vzurauskas.accountstransfers.Accounts;
import com.vzurauskas.accountstransfers.misc.UncheckedMapper;
import org.takes.Body;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqGreedy;

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
        Body body = new RqGreedy(req);
        return accounts.byId(
            accounts.add(text(body, "iban"), text(body, "currency"))
        )
            .map(AccountWithBalance::new)
            .map(account -> new HttpCreated(account.json()))
            .orElseThrow(() -> new IllegalStateException("Somehow can't find account which was just created."));
    }

    private String text(Body req, String name) throws IOException {
        return Optional.ofNullable(mapper.json(req.body()).get(name))
            .filter(JsonNode::isTextual)
            .map(JsonNode::textValue)
            .orElseThrow(() -> new IllegalArgumentException("Bad request - no '" + name + "'."));
    }
}
