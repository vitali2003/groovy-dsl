import ua.jug.dsl.groovy.DataManger

/**
 * @author yaroslav.yermilov
 */
def data

use(DataManger) {
    data = 'data.csv'.loadData(true, [ 'int', 'string', 'date|yyyy-MM-dd', 'boolean' ])
}

data.rows().findAll { it.column('date').after(DataManger.now()) && !(it.column('weekday')) }.collect { it.column('name') }.each { println it }



class DataLoader {

    static def loadData(String fileName, boolean header, List<String> columnTypes) {
        DataManger.loadData(fileName, header, columnTypes)
    }
}