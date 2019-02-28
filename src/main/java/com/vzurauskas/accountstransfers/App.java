package com.vzurauskas.accountstransfers;

import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;

public final class App {
    public static void main(final String... args) throws java.io.IOException {
        new FtBasic(
            new TkFork(
                new FkRegex("/", "hello, world!")
            ),
            8080
        ).start(Exit.NEVER);
    }
}
