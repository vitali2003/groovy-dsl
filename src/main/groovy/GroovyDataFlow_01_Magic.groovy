import ua.jug.dsl.groovy.DataManger

/**
 * @author yaroslav.yermilov
 */
def data = DataManger.loadData('data.csv', true, [ 'int', 'string', 'date|yyyy-MM-dd', 'boolean' ])

data.rows().findAll { it.column('date').after(DataManger.now()) && !(it.column('weekday')) }.collect { it.column('name') }.each { println it }