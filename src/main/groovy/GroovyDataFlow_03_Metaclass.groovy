import ua.jug.dsl.groovy.DataManger

/**
 * @author yaroslav.yermilov
 */

new GroovyShell(binding()).evaluate(
'''
import ua.jug.dsl.groovy.DataManger

def data = 'data.csv'.loadData true, [ 'int', 'string', 'date|yyyy-MM-dd', 'boolean' ]

data.rows().findAll { it.column('date').after(DataManger.now()) && !(it.column('weekday')) }.collect { it.column('name') }.each { println it }
'''
)

static Binding binding() {

    String.metaClass.loadData = { header, columnTypes ->
        DataManger.loadData(delegate, header, columnTypes)
    }

    new Binding()
}