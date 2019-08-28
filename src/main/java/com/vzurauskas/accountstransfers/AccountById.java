package com.vzurauskas.accountstransfers;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

import com.vzurauskas.accountstransfers.http.HttpBadRequest;
import com.vzurauskas.accountstransfers.http.HttpOk;
import com.vzurauskas.accountstransfers.misc.UncheckedMapper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.takes.Response;

public final class AccountById {

    private static final UncheckedMapper mapper = new UncheckedMapper();

    private final UUID id;
    private final DSLContext db;

    public AccountById(UUID id, DSLContext db) {
        this.id = id;
        this.db = db;
    }

    public Response httpResponse() {
        return db
            .select()
            .from("ACCOUNT")
            .where(DSL.field("ACCOUNT.ID").eq(id))
            .fetch().stream()
            .findFirst()
            .map(this::ok)
            .orElse(new HttpBadRequest("No account with id=" + id));
    }

    private Response ok(Record record) {
        ObjectNode account = mapper.objectNode();
        account.put("id", record.get("ID", UUID.class).toString());
        account.put("iban", record.get("IBAN").toString());
        account.put("currency", record.get("CURRENCY").toString());
        return new HttpOk(account);
    }
}
