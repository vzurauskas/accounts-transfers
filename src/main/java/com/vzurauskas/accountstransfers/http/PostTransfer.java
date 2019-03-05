package com.vzurauskas.accountstransfers.http;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vzurauskas.accountstransfers.Accounts;
import com.vzurauskas.accountstransfers.Transfer;
import com.vzurauskas.accountstransfers.misc.UncheckedMapper;
import org.takes.Body;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqGreedy;
import org.takes.rs.RsWithBody;
import org.takes.rs.RsWithStatus;
import org.takes.rs.RsWithType;

public final class PostTransfer implements Take {

    private static final Logger log = LoggerFactory.getLogger(PostTransfer.class);
    private final UncheckedMapper mapper = new UncheckedMapper();

    private final Accounts accounts;

    public PostTransfer(Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public Response act(Request req) throws IOException {
        log.info("POST /transfers");
        Body body = new RqGreedy(req);
        Transfer transfer = accounts.byIban(text(body, "/debtor")).debit(
            accounts.byIban(text(body, "/creditor")),
            new BigDecimal(text(body, "/instructedAmount/amount")),
            text(body, "/instructedAmount/currency")
        );
        transfer.execute();
        return response(transfer);
    }

    private Response response(Transfer transfer) {
        return new RsWithType(
            new RsWithStatus(
                new RsWithBody(
                    mapper.bytes(transfer.json())
                ),
                201
            ),
            "application/json"
        );
    }

    private String text(Body req, String path) throws IOException {
        return Optional.ofNullable(mapper.json(req.body()).at(path))
            .filter(JsonNode::isTextual)
            .map(JsonNode::textValue)
            .orElseThrow(() -> new IllegalArgumentException("Bad request - no '" + path + "' value."));
    }
}
