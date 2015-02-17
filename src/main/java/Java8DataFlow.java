import java.util.Date;
import java.util.List;

/**
 * @author yaroslav.yermilov
 */
public class Java8DataFlow {

    public static void main(String[] args) {
        List<Object[]> data = DataManger.loadData("data.csv", true, new String[]{"int", "string", "date|yyyy-MM-dd", "boolean"});

        List<Object[]> daysToWork = DataManger.filterData(data, row -> (((Date) row[2]).after(DataManger.now()) && !((Boolean) row[3])));

        List<Object[]> dayNamesToWork = DataManger.selectColumns(daysToWork, new int[]{1});

        dayNamesToWork.stream().map(dayName -> dayName[0]).forEach(System.out::println);
    }
}
