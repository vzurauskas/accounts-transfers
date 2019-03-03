package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
}
