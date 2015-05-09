import com.jeeconf.groovydsl.Monitoring
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

Integer.metaClass.propertyMissing = {String name ->
    if (name == 'seconds') {
        return delegate * 1000
    }
    if (name == 'times') {
        return delegate
    }
}

CompilerConfiguration compilerConfiguration = new CompilerConfiguration()
compilerConfiguration.addCompilationCustomizers(new ASTTransformationCustomizer(new MonitoringASTTransformation()))

new GroovyShell(compilerConfiguration).evaluate(
'''
each 7 seconds - '380934902436'
each 5 seconds - '380934902436'
'''
)

@GroovyASTTransformation(phase=CompilePhase.CONVERSION)
public class MonitoringASTTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        def statusStatements = sourceUnit.getAST()?.getStatementBlock()?.statements
            .findAll { it instanceof ExpressionStatement && it.expression instanceof MethodCallExpression }
            .findAll { it.expression.objectExpression.method instanceof ConstantExpression && it.expression.objectExpression.method.value == 'each' }

        statusStatements.each { ExpressionStatement statement ->
            println statement

            def params = extractParameters(statement)
            statement.expression = createExpression(params)
        }
    }

    private Map extractParameters(ExpressionStatement statement) {
        assert null : 'not implemented yet'
    }

    private Map createExpression(Map params) {
        assert null : 'not implemented yet'
    }
}