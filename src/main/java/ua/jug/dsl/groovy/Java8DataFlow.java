package ua.jug.dsl.groovy;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import ua.jug.dsl.groovy.DataManger.Data;
import ua.jug.dsl.groovy.DataManger.Row;

/**
 * @author yaroslav.yermilov
 */
public class Java8DataFlow {

    public static void main(String[] args) {
        Data data = DataManger.loadData("data.csv", true, Arrays.asList("int", "string", "date|yyyy-MM-dd", "boolean"));

        Data daysToWork = DataManger.filterData(data, row -> row.column(2, Date.class).after(DataManger.now()) && !(row.column(3, Boolean.class)));

        Data dayNamesToWork = DataManger.selectColumns(daysToWork, Arrays.asList("name"));

        dayNamesToWork.rows().stream().map(row -> row.value()).forEach(System.out::println);
    }
}
