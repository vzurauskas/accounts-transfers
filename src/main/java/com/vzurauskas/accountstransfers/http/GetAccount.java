package com.vzurauskas.accountstransfers.http;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vzurauskas.accountstransfers.Account;
import com.vzurauskas.accountstransfers.AccountWithBalance;
import com.vzurauskas.accountstransfers.Accounts;
import com.vzurauskas.accountstransfers.misc.UncheckedMapper;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqHref;

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
        Optional<Account> account = accounts.byId(id(uri));
        return account.isPresent()
            ? new HttpOk(new AccountWithBalance(account.get()).json())
            : new HttpBadRequest("No account with id=" + id(uri));
    }

    private static UUID id(String uri) {
        return UUID.fromString(uri.substring(uri.lastIndexOf('/') + 1));
    }
}
