/**
 * @author yaroslav.yermilov
 */

new GroovyShell(binding()).evaluate(
'''
def data = "data.csv".loadData(true, [ "int", "string", "date|yyyy-MM-dd", "boolean" ])
data.findAll { it[2].after(DataManger.now()) && !it[3] }.collect { it[1] }.each { println it }
'''
)

static Binding binding() {

    String.metaClass.loadData = { header, columnTypes ->
        DataManger.loadData(delegate, header, columnTypes as String[])
    }

    new Binding()
}