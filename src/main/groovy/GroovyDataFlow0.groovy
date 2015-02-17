/**
 * @author yaroslav.yermilov
 */
def data = DataManger.loadData("data.csv", true, [ "int", "string", "date|yyyy-MM-dd", "boolean" ] as String[])

def daysToWork = DataManger.filterData(data, { row -> row[2].after(DataManger.now()) && !row[3] })

def dayNamesToWork = DataManger.selectColumns(daysToWork, [ 1 ] as int[])

dayNamesToWork.each { println it }