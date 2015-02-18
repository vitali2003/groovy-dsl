import ua.jug.dsl.groovy.DataManger
import ua.jug.dsl.groovy.DataManger.Data
import ua.jug.dsl.groovy.DataManger.Row

/**
 * @author yaroslav.yermilov
 */

new GroovyShell(binding()).evaluate(
'''
import ua.jug.dsl.groovy.DataManger

def data = 'data.csv'.loadData withHeader: true, ofTypes: [ 'int', 'string', 'date|yyyy-MM-dd', 'boolean' ]

(data | { it['date'] > DataManger.now() && !it['weekday'] }).collect { it['name'] }.each { println it }
'''
)

static Binding binding() {

    String.metaClass.loadData = { params ->
        DataManger.loadData(delegate, params.withHeader, params.ofTypes)
    }

    Data.metaClass.or = { condition ->
        delegate.rows().findAll(condition)
    }

    Row.metaClass.getAt = { String name ->
        delegate.column(name)
    }

    new Binding()
}