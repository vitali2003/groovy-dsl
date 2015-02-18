package ua.jug.dsl.groovy;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author yaroslav.yermilov
 */
public class DataManger {

    public static Data loadData(String fileName, boolean header, List<String> columnTypes) {
        List<Row> rows = new ArrayList<>();

        List<String> lines = null;
        try {
            lines = IOUtils.readLines(Java7DataFlow.class.getResourceAsStream("/" + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> columnNames = new ArrayList<>();
        if (header) {
            columnNames = Arrays.asList(lines.get(0).split(","));
        }

        for (int i = header ? 1 : 0; i < lines.size(); i++) {
            String[] columns = lines.get(i).split(",");
            Object[] values = new Object[columns.length];
            for (int j = 0; j < columns.length; j++) {
                if (columnTypes.get(j).equals("int")) {
                    values[j] = Integer.parseInt(columns[j]);
                }
                if (columnTypes.get(j).equals("string")) {
                    values[j] = columns[j];
                }
                if (columnTypes.get(j).startsWith("date|")) {
                    try {
                        values[j] = new SimpleDateFormat(columnTypes.get(j).substring(5)).parse(columns[j]);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (columnTypes.get(j).startsWith("boolean")) {
                    values[j] = Boolean.parseBoolean(columns[j]);
                }
            }

            rows.add(new Row(values));
        }

        return new Data(columnNames, columnTypes, rows);
    }

    public static Data filterData(Data data, Predicate<Row> predicate) {
        List<Row> result = new ArrayList<>();
        for (Row row : data.rows()) {
            if (predicate.test(row)) {
                result.add(row);
            }
        }
        return new Data(data.columnNames(), data.columnTypes(), result);
    }

    public static Data selectColumns(Data data, List<String> columns) {
        List<Row> result = new ArrayList<>();
        for (Row row : data.rows()) {
            Object[] selectedColumns = new Object[columns.size()];
            for (int i = 0; i < columns.size(); i++) {
                if (data.columnNames().isEmpty()) {
                    selectedColumns[i] = row.column(Integer.parseInt(columns.get(i)));
                } else {
                    selectedColumns[i] = row.column(data.columnNames().indexOf(columns.get(i)));
                }
            }
            result.add(new Row(selectedColumns));
        }

        List<String> columnNames = new ArrayList<>();
        List<String> columnTypes = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            columnNames.add(columns.get(i));
            columnTypes.add(data.columnTypes().get(data.columnNames().indexOf(columns.get(i))));
        }

        return new Data(columnNames, columnTypes, result);
    }

    public static Date now() {
        return new Date();
    }

    public static class Data {

        private final List<String> columnNames;
        private final List<String> columnTypes;
        private final List<Row> rows;

        public Data(List<String> columnNames, List<String> columnTypes, List<Row> rows) {
            this.columnNames = new ArrayList<>(columnNames);
            this.columnTypes = new ArrayList<>(columnTypes);
            this.rows = new ArrayList<>(rows);

            for (Row row : this.rows) {
                row.setColumnNames(this.columnNames);
            }
        }

        public List<Row> rows() {
            return Collections.unmodifiableList(rows);
        }

        public List<String> columnNames() {
            return Collections.unmodifiableList(columnNames);
        }

        public List<String> columnTypes() {
            return Collections.unmodifiableList(columnTypes);
        }
    }

    public static class Row {

        private List<String> columnNames;
        Object[] values;

        public Row(Object[] values) {
            this.values = Arrays.copyOf(values, values.length);
        }

        public Object column(String name) {
            return column(columnNames.indexOf(name));
        }

        public Object column(int index) {
            return values[index];
        }

        public <T> T column(int index, Class<T> clazz) {
            return (T) column(index);
        }

        public Object value() {
            return column(0);
        }

        public void setColumnNames(List<String> columnNames) {
            this.columnNames = columnNames;
        }
    }
}
