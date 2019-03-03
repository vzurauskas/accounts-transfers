package com.vzurauskas.accountstransfers;

import java.io.IOException;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;

public final class App {

    public static void main(final String... args) throws SQLException, IOException {
        DSLContext db = new Database().connect();

        Accounts accounts = new JooqAccounts(db);
        Account one = accounts.add("123456");
        Account two = accounts.add("555");
        System.out.println(accounts.find(two.id()).json());


        new FtBasic(
            new TkFork(
                new FkRegex("/", "hello, world!")
            ),
            8080
        ).start(Exit.NEVER);
    }
}
