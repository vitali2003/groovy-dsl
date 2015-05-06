import com.jeeconf.groovydsl.Monitoring
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

def importCustomizer = new ImportCustomizer()
importCustomizer.addImports(Monitoring.name)

def compilerConfiguration = new CompilerConfiguration()
compilerConfiguration.addCompilationCustomizers(importCustomizer)

new GroovyShell(compilerConfiguration).evaluate(
'''
Monitoring.sendStatusPeriodically('380934902436', 30000)
'''
)