/**
 * @author yaroslav.yermilov
 */

new GroovyShell(binding()).evaluate(
'''
def data = "data.csv".loadData withHeader: true, ofTypes: [ "int", "string", "date|yyyy-MM-dd", "boolean" ]
data.findAll { it[2].after(DataManger.now()) && !it[3] }.collect { it[1] }.each { println it }
'''
)

static Binding binding() {

    String.metaClass.loadData = { params ->
        DataManger.loadData(delegate, params.withHeader, params.ofTypes as String[])
    }

    new Binding()
}