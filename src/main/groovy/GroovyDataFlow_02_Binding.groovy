/**
 * @author yaroslav.yermilov
 */

new GroovyShell(binding()).evaluate(
'''
import ua.jug.dsl.groovy.DataManger

def data = DataManger.loadData 'data.csv', true, [ 'int', 'string', 'date|yyyy-MM-dd', 'boolean' ]

data.rows().findAll { it.column(2).after(DataManger.now()) && !(it.column(3)) }.collect { it.column(1) }.each { println it }
'''
)

static Binding binding() {
    new Binding()
}