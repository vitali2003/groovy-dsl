import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author yaroslav.yermilov
 */
public class Java7DataFlow {

    public static void main(String[] args) {
        List<Object[]> data = DataManger.loadData("data.csv", true, new String[]{"int", "string", "date|yyyy-MM-dd", "boolean"});

        List<Object[]> daysToWork = DataManger.filterData(data, new Predicate<Object[]>() {

            @Override
            public boolean test(Object[] row) {
                return (((Date) row[2]).after(DataManger.now()) && !((Boolean) row[3]));
            }
        });

        List<Object[]> dayNamesToWork = DataManger.selectColumns(daysToWork, new int[]{1});

        for (Object[] dayName : dayNamesToWork) {
            System.out.println(dayName[0]);
        }
    }
}
