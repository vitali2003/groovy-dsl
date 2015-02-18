import ua.jug.dsl.groovy.DataManger

/**
 * @author yaroslav.yermilov
 */
def data = DataManger.loadData('data.csv', true, [ 'int', 'string', 'date|yyyy-MM-dd', 'boolean' ])

data.rows().findAll { it.column(2).after(DataManger.now()) && !(it.column(3)) }.collect { it.column(1) }.each { println it }