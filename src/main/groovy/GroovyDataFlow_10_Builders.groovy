import ua.jug.dsl.groovy.DataManger
import ua.jug.dsl.groovy.DataManger.Data
import ua.jug.dsl.groovy.DataManger.Row

/**
 * @author yaroslav.yermilov
 */

new GroovyShell(binding()).evaluate(
'''
def data = load data.csv(fileName: 'data.csv', header: true) {

    columns {
        type('int')
        type('string')
        type('date|yyyy-MM-dd')
        type('boolean')
    }
}

(data | { it['date'] > fromNow(1.day) && !it['weekday'] }).collectNameAndId().each { println it }
'''
)

static Binding binding() {

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

    Integer.metaClass.propertyMissing = {String name, args ->
        if (name.startsWith('day')) {
            delegate * 1000 * 60 * 60 * 24
        }
    }

    def binding = new Binding()
    binding.fromNow = { long delta ->
        new Date(System.currentTimeMillis() + delta)
    }

    binding.data = new DataLoadParametersBuilder()

    binding.load = { params ->
        DataManger.loadData(params.fileName, params.header, params.columnTypes)
    }

    return binding
}

class CsvFactory extends AbstractFactory {

    @Override
    boolean isLeaf() {
        false
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def parameters = new Expando()
        parameters.fileName = attributes.fileName
        parameters.header = attributes.header
        return parameters
    }
}

class ColumnsFactory extends AbstractFactory {

    private Object parent

    @Override
    boolean isLeaf() {
        false
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        new Expando()
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        this.parent = parent
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        this.parent.columnTypes = this.parent.columnTypes ? this.parent.columnTypes << child : [ child ]
    }
}

class TypeFactory extends AbstractFactory {

    @Override
    boolean isLeaf() {
        true
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        value
    }
}

class DataLoadParametersBuilder extends FactoryBuilderSupport {

    DataLoadParametersBuilder() {
        super(true)
    }

    def registerObjectFactories() {
        registerFactory('csv', new CsvFactory())
        registerFactory('columns', new ColumnsFactory())
        registerFactory('type', new TypeFactory())
    }
}