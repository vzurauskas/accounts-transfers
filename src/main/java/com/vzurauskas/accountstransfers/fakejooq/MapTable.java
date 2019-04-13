package com.vzurauskas.accountstransfers.fakejooq;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.jooq.Record;

public final class MapTable implements Table {

    private final String name;
    private final Map<Object, Record> rows;
    private final Collection<Constraint> constraints;

    public MapTable(String name) {
        this(name, Collections.emptyList());
    }

    public MapTable(String name, Collection<Constraint> constraints) {
        this.name = name;
        this.rows = new HashMap<>(8);
        this.constraints = new ArrayList<>(constraints);
    }

    @Override
    public void insert(Record row) throws SQLException {
        if (rows.containsKey(row.get(0))) {
            throw new SQLException("Table " + name + " already contains record with ID=" + row.get(0));
        }
        if (!constraints.stream().allMatch(constraint -> constraint.satisfies(rows.values(), row))) {
            throw new SQLException("Table " + name + " constraint violation.");
        }
        rows.put(row.get(0), row);
    }

    @Override
    public Record select(Object id) throws SQLException {
        if (!rows.containsKey(id)) {
            throw new SQLException("No record with ID=" + id + " in table " + name);
        }
        return rows.get(id);
    }

    @Override
    public Stream<Record> selectWhereDisjunct(Collection<Map.Entry<String, Object>> conditions) {
        return rows.values().stream().filter(
            row -> conditions.stream().anyMatch(
                condition -> condition.getValue().equals(row.get(condition.getKey()))
            )
        );
    }
}
