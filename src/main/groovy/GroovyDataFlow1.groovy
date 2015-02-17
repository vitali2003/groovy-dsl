/**
 * @author yaroslav.yermilov
 */
def data = DataManger.loadData "data.csv", true, [ "int", "string", "date|yyyy-MM-dd", "boolean" ] as String[]

data.findAll { it[2].after(DataManger.now()) && !it[3] }.collect { it[1] }.each { println it }