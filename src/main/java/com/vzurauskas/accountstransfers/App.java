package com.vzurauskas.accountstransfers;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vzurauskas.accountstransfers.http.GetAccount;
import com.vzurauskas.accountstransfers.http.PostAccount;
import com.vzurauskas.accountstransfers.http.PostTransfer;
import com.vzurauskas.accountstransfers.jooq.Database;
import com.vzurauskas.accountstransfers.jooq.JooqAccounts;

import org.jooq.DSLContext;

import org.takes.Response;
import org.takes.facets.fallback.Fallback;
import org.takes.facets.fallback.FbChain;
import org.takes.facets.fallback.FbSlf4j;
import org.takes.facets.fallback.FbStatus;
import org.takes.facets.fallback.RqFallback;
import org.takes.facets.fallback.TkFallback;
import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;
import org.takes.misc.Opt;
import org.takes.rs.RsText;

public final class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(final String... args) throws SQLException, IOException {
        DSLContext db = new Database().connect();
        Accounts accounts = new JooqAccounts(db);
        new FtBasic(
            new TkFallback(
                new TkFork(
                    new FkRegex("/", "Accounts and Transfers"),
                    new FkRegex("/accounts", new TkFork(new FkMethods("POST", new PostAccount(accounts)))),
                    new FkRegex("/accounts/.+", new TkFork(new FkMethods("GET", new GetAccount(accounts)))),
                    new FkRegex("/transfers", new TkFork(new FkMethods("POST", new PostTransfer(accounts))))
                ),
                fallback()
            ),
            8080
        ).start(Exit.NEVER);
    }

    private static Fallback fallback() {
        return new FbChain(
            new FbSlf4j(),
            new FbStatus(404, new RsText("Resource not found.")),
            new FbStatus(405, new RsText("This method is not allowed here.")),
            new Fallback() {
                @Override
                public Opt<Response> route(final RqFallback req) {
                    return new Opt.Single<>(
                        new RsText("oops, something went terribly wrong!")
                    );
                }
            }
        );
    }
}
