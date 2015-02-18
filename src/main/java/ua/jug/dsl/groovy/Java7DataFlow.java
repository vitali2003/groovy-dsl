package ua.jug.dsl.groovy;

import java.util.*;
import java.util.function.Predicate;
import ua.jug.dsl.groovy.DataManger.Data;
import ua.jug.dsl.groovy.DataManger.Row;

/**
 * @author yaroslav.yermilov
 */
public class Java7DataFlow {

    public static void main(String[] args) {
        Data data = DataManger.loadData("data.csv", true, Arrays.asList("int", "string", "date|yyyy-MM-dd", "boolean"));

        Data daysToWork = DataManger.filterData(data, new Predicate<Row>() {

            @Override
            public boolean test(Row row) {
                return row.column(2, Date.class).after(DataManger.now()) && !(row.column(3, Boolean.class));
            }
        });

        Data dayNamesToWork = DataManger.selectColumns(daysToWork, Arrays.asList("name"));

        for (Row dayName : dayNamesToWork.rows()) {
            System.out.println(dayName.column(0));
        }
    }
}
