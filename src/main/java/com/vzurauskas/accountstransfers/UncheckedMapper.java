package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public final class UncheckedMapper {

    private final ObjectMapper mapper;

    public UncheckedMapper() {
        this(new ObjectMapper());
    }

    public UncheckedMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ObjectNode objectNode() {
        return mapper.createObjectNode();
    }

    public byte[] bytes(JsonNode node) {
        try {
            return mapper.writeValueAsBytes(node);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public CharSequence text(JsonNode node) {
        try {
            return mapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public JsonNode json(InputStream input) {
        try {
            return mapper.readValue(input, JsonNode.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public JsonNode json(String value) {
        try {
            return mapper.readTree(value);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
