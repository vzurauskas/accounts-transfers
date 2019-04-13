package com.vzurauskas.accountstransfers.fakejooq;

import java.util.Collection;

import org.jooq.Record;

public interface Constraint {

    boolean satisfies(Collection<Record> table, Record inserted);
}
