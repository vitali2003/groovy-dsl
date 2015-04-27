import ua.jug.dsl.groovy.DataManger
import ua.jug.dsl.groovy.DataManger.Data
import ua.jug.dsl.groovy.DataManger.Row

/**
 * @author yaroslav.yermilov
 */

new GroovyShell(binding()).evaluate(
'''
def data = 'data.csv'.loadData withHeader: true, ofTypes: [ 'int', 'string', 'date|yyyy-MM-dd', 'boolean' ]

(data | { it['date'] > now() && !it['weekday'] }).collectNameAndId().each { println it }
'''
)

static Binding binding() {

    String.metaClass.loadData = { params ->
        DataManger.loadData(delegate, params.withHeader, params.ofTypes)
    }

    Data.metaClass.or = { condition ->
        new Data(delegate.columnNames(), delegate.columnTypes(), delegate.rows().findAll(condition))
    }

    Row.metaClass.getAt = { String name ->
        delegate.column(name)
    }

    Data.metaClass.methodMissing = {String name, args ->
        if (name.startsWith('collect')) {
            def columns = name.substring(7).split('And')
            delegate.rows().collect({ row -> columns.collect { column -> row.column(column.toLowerCase()) } })
        }
    }

    def binding = new Binding()
    binding.now = { new Date() }

    return binding
}