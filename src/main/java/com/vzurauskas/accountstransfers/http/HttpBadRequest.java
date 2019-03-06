package com.vzurauskas.accountstransfers.http;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.io.InputStream;

import com.vzurauskas.accountstransfers.misc.UncheckedMapper;
import org.takes.Response;
import org.takes.rs.RsWithBody;
import org.takes.rs.RsWithStatus;
import org.takes.rs.RsWithType;

public final class HttpBadRequest implements Response {

    private final UncheckedMapper mapper = new UncheckedMapper();

    private final Response origin;

    public HttpBadRequest(String message) {
        this.origin = new RsWithType(
            new RsWithStatus(
                new RsWithBody(
                    mapper.bytes(mapper.objectNode().put("message", message))
                ),
                400
            ),
            "application/json"
        );
    }

    @Override
    public InputStream body() throws IOException {
        return origin.body();
    }

    @Override
    public Iterable<String> head() throws IOException {
        return origin.head();
    }
}
