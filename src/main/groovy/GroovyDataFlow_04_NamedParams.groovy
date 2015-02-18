import ua.jug.dsl.groovy.DataManger

/**
 * @author yaroslav.yermilov
 */

new GroovyShell(binding()).evaluate(
'''
import ua.jug.dsl.groovy.DataManger

def data = 'data.csv'.loadData withHeader: true, ofTypes: [ 'int', 'string', 'date|yyyy-MM-dd', 'boolean' ]

data.rows().findAll { it.column(2).after(DataManger.now()) && !(it.column(3)) }.collect { it.column(1) }.each { println it }
'''
)

static Binding binding() {

    String.metaClass.loadData = { params ->
        DataManger.loadData(delegate, params.withHeader, params.ofTypes)
    }

    new Binding()
}