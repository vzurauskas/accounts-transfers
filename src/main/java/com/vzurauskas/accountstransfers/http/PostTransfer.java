package com.vzurauskas.accountstransfers.http;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vzurauskas.accountstransfers.Account;
import com.vzurauskas.accountstransfers.AccountWithBalance;
import com.vzurauskas.accountstransfers.Accounts;
import com.vzurauskas.accountstransfers.Transfer;
import com.vzurauskas.accountstransfers.misc.UncheckedMapper;
import org.takes.Body;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqGreedy;
import org.takes.rq.RqHeaders;
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
        Request request = new RqGreedy(req);
        Optional<Account> debtor = accounts.byIban(text(request, "/debtor"));
        Optional<Account> creditor = accounts.byIban(text(request, "/creditor"));
        if (!debtor.isPresent()) {
            return new HttpBadRequest("No account with IBAN=" + text(request, "/debtor"));
        }
        if (!creditor.isPresent()) {
            return new HttpBadRequest("No account with IBAN=" + text(request, "/creditor"));
        }
        Transfer transfer = debtor.get().debit(
            creditor.get(),
            new BigDecimal(text(request, "/instructedAmount/amount")),
            text(request, "/instructedAmount/currency"),
            headers(request)
        );
        transfer.execute();
        return new HttpOk(transfer.json());
    }

    private static Map<String, String> headers(Request request) throws IOException {
        Map<String, String> map = new HashMap<>(16);
        RqHeaders.Smart headers = new RqHeaders.Smart(request);
        for (String name : headers.names()) {
            map.put(name, headers.single(name));
        }
        return map;
    }

    private String text(Body req, String path) throws IOException {
        return Optional.ofNullable(mapper.json(req.body()).at(path))
            .filter(JsonNode::isTextual)
            .map(JsonNode::textValue)
            .orElseThrow(() -> new IllegalArgumentException("Bad request - no '" + path + "' value."));
    }
}
