import ua.jug.dsl.groovy.DataManger

/**
 * @author yaroslav.yermilov
 */
def data = DataManger.loadData('data.csv', true, [ 'int', 'string', 'date|yyyy-MM-dd', 'boolean' ])

def daysToWork = DataManger.filterData(data, { row -> row.column(2).after(DataManger.now()) && !(row.column(3)) })

def dayNamesToWork = DataManger.selectColumns(daysToWork, [ 'name' ])

dayNamesToWork.rows().each { println it.value() }