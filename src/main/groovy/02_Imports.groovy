import com.jeeconf.groovydsl.Monitoring
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

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


String script = new File("../dsl/${this.class.name}.dsl").text
new GroovyShell(compilerConfiguration()).evaluate(script)