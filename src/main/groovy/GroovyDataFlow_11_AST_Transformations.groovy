import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.ListExpression
import org.codehaus.groovy.ast.expr.MapEntryExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.NamedArgumentListExpression
import org.codehaus.groovy.ast.expr.TupleExpression
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import ua.jug.dsl.groovy.DataManger
import ua.jug.dsl.groovy.DataManger.Data
import ua.jug.dsl.groovy.DataManger.Row

/**
 * @author yaroslav.yermilov
 */

new GroovyShell(binding(), compilerConfiguration()).evaluate(
'''
def data = 'data.csv'.loadData withHeader: true, ofTypes: 'int' | 'string' | 'date|yyyy-MM-dd' | 'boolean'

(data | { it['date'] > fromNow(1.day) && !it['weekday'] }).collectNameAndId().each { println it }
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

    Integer.metaClass.propertyMissing = {String name, args ->
        if (name.startsWith('day')) {
            delegate * 1000 * 60 * 60 * 24
        }
    }

    def binding = new Binding()
    binding.fromNow = { long delta ->
        new Date(System.currentTimeMillis() + delta)
    }

    return binding
}

static CompilerConfiguration compilerConfiguration() {
    CompilerConfiguration compilerConfiguration = new CompilerConfiguration()
    compilerConfiguration.addCompilationCustomizers(new ASTTransformationCustomizer(new ColumnTypeASTTransformation()))

    return compilerConfiguration
}

@GroovyASTTransformation(phase=CompilePhase.CONVERSION)
public class ColumnTypeASTTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        sourceUnit.getAST()?.getStatementBlock()?.statements
            .findAll { Statement statement ->
                (statement instanceof ExpressionStatement) &&
                (statement.expression instanceof DeclarationExpression) &&
                ((statement.expression as DeclarationExpression).rightExpression as MethodCallExpression).methodAsString == 'loadData'
            }.forEach { ExpressionStatement statement ->
                MapEntryExpression columnTypesExpression = ((((statement.expression as DeclarationExpression).rightExpression as MethodCallExpression).arguments as TupleExpression).getExpression(0) as NamedArgumentListExpression).getMapEntryExpressions().get(1)
                List<ConstantExpression> columnTypes  = extract(columnTypesExpression.valueExpression)
                ListExpression columnTypesList = new ListExpression(columnTypes)
                columnTypesExpression.valueExpression = columnTypesList
            }
    }

    private List<ConstantExpression> extract(Expression expression) {
        if (expression instanceof BinaryExpression) {
            return extract(expression.leftExpression) + extract(expression.rightExpression)
        }

        if (expression instanceof ConstantExpression) {
            return [ expression ]
        }
    }
}