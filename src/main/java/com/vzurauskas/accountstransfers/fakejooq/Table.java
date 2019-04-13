package com.vzurauskas.accountstransfers.fakejooq;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import org.jooq.Record;

public interface Table {
    void insert(Record row) throws SQLException;
    Record select(Object id) throws SQLException;
    Stream<Record> selectWhereDisjunct(Collection<Map.Entry<String, Object>> conditions);
}
