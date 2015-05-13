import com.jeeconf.groovydsl.Monitoring
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer
import org.codehaus.groovy.control.customizers.ImportCustomizer
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


new GroovyShell(compilerConfiguration()).evaluate(
'''
each 7 seconds - '380934902436'
each 5 seconds - '380934902436'
'''
)


static def compilerConfiguration() {
    CompilerConfiguration compilerConfiguration = new CompilerConfiguration()
    compilerConfiguration.addCompilationCustomizers(importCustomizer())
    compilerConfiguration.addCompilationCustomizers(astTransformationCustomizer())
    return compilerConfiguration
}

static def importCustomizer() {
    def importCustomizer = new ImportCustomizer()
    importCustomizer.addImports(Monitoring.name)
    return importCustomizer
}

static def astTransformationCustomizer() {
    new ASTTransformationCustomizer(new MonitoringASTTransformation())
}

@GroovyASTTransformation(phase=CompilePhase.CONVERSION)
public class MonitoringASTTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        scheduleRelatedStatements(sourceUnit).each { ExpressionStatement statement ->
            def params = extractScheduleParameters(statement)
            statement.expression = createRunMonitoringExpression(params)
        }
    }

    private def scheduleRelatedStatements(SourceUnit sourceUnit) {
        sourceUnit.getAST()?.getStatementBlock()?.statements
                    .findAll { it instanceof ExpressionStatement && it.expression instanceof MethodCallExpression }
                    .findAll { it.expression.objectExpression.method instanceof ConstantExpression && it.expression.objectExpression.method.value == 'each' }
    }

    private Map extractScheduleParameters(ExpressionStatement statement) {
        int periodValue = statement.expression.objectExpression.arguments.expressions[0].value
        String periodExp = statement.expression.method.value
        String phoneNumber = statement.expression.arguments.expressions[0].expression.value

        long period
        if (periodExp == 'seconds') {
            period = periodValue * 1000
        }

        [ phoneNumber: phoneNumber, period: period ]
    }

    private def createRunMonitoringExpression(Map params) {
        new AstBuilder().buildFromString("""
            new Thread({
                Monitoring.sendStatusPeriodically('$params.phoneNumber', $params.period)
            }).start()
        """).statements[0].expression[0]
    }
}