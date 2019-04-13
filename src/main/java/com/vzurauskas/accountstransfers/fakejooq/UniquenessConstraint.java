package com.vzurauskas.accountstransfers.fakejooq;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jooq.Record;

public final class UniquenessConstraint implements Constraint {
    private final Collection<String> columns;

    public UniquenessConstraint(Collection<String> columns) {
        this.columns = new ArrayList<>(columns);
    }

    @Override
    public boolean satisfies(Collection<Record> table, Record inserted) {
        List<Map.Entry<String, Object>> values = new ArrayList<>(4);
        columns.forEach(column -> values.add(new AbstractMap.SimpleEntry<>(column, inserted.get(column))));
        return noRowWithCells(table.stream(), values);
    }

    private static boolean noRowWithCells(Stream<Record> rows, Collection<Map.Entry<String, Object>> cells) {
        return rows.noneMatch(
            row -> cells.stream().allMatch(
                cell -> rowHasValue(row, cell)
            )
        );
    }

    private static boolean rowHasValue(Record row, Map.Entry<String, Object> cell) {
        return row.get(cell.getKey()).equals(cell.getValue());
    }
}
