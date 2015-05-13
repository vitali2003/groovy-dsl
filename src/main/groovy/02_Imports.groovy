import com.jeeconf.groovydsl.Monitoring
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

new GroovyShell(compilerConfiguration()).evaluate(
'''
Monitoring.sendStatusPeriodically('380934902436', 30000)
'''
)


static def compilerConfiguration() {
    def compilerConfiguration = new CompilerConfiguration()
    compilerConfiguration.addCompilationCustomizers(importCustomizer())
    return compilerConfiguration
}

static def importCustomizer() {
    def importCustomizer = new ImportCustomizer()
    importCustomizer.addImports(Monitoring.name)
    return importCustomizer
}