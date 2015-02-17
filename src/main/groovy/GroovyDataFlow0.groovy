import org.apache.commons.io.IOUtils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.function.Predicate

/**
 * @author yaroslav.yermilov
 */
def data = loadData("data.csv", true, [ "int", "string", "date|yyyy-MM-dd", "boolean" ] as String[])

def daysToWork = filterData(data, { row -> row[2].after(new Date()) && !row[3] })

def dayNamesToWork = selectColumns(daysToWork, [ 1 ] as int[])

dayNamesToWork.each { println it[0] }






public static List<Object[]> loadData(String fileName, boolean header, String[] columnTypes) {
    List<Object[]> rows = new ArrayList<Object[]>();

    List<String> lines = null;
    try {
        lines = IOUtils.readLines(Java7DataFlow.class.getResourceAsStream("/" + fileName));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    for (int i = header ? 1 : 0; i < lines.size(); i++) {
        String[] columns = lines.get(i).split(",");
        Object[] values = new Object[columns.length];
        for (int j = 0; j < columns.length; j++) {
            if (columnTypes[j].equals("int")) {
                values[j] = Integer.parseInt(columns[j]);
            }
            if (columnTypes[j].equals("string")) {
                values[j] = columns[j];
            }
            if (columnTypes[j].startsWith("date|")) {
                try {
                    values[j] = new SimpleDateFormat(columnTypes[j].substring(5)).parse(columns[j]);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            if (columnTypes[j].startsWith("boolean")) {
                values[j] = Boolean.parseBoolean(columns[j]);
            }
        }

        rows.add(values);
    }

    return rows;
}

public static List<Object[]> filterData(List<Object[]> data, Predicate<Object[]> predicate) {
    List<Object[]> result = new ArrayList<Object[]>();
    for (Object[] row : data) {
        if (predicate.test(row)) {
            result.add(row);
        }
    }
    return result;
}

public static List<Object[]> selectColumns(List<Object[]> data, int[] columns) {
    List<Object[]> result = new ArrayList<Object[]>();
    for (Object[] row : data) {
        Object[] selectedColumns = new Object[columns.length];
        for (int i = 0; i < columns.length; i++) {
            selectedColumns[i] = row[columns[i]];
        }
        result.add(selectedColumns);
    }
    return result;
}