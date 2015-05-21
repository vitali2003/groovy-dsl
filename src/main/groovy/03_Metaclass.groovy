import com.jeeconf.groovydsl.Monitoring

String.metaClass.sendStatusPeriodically = { long period ->
    Monitoring.sendStatusPeriodically(delegate, period)
}


String script = new File("../dsl/${this.class.name}.dsl").text
new GroovyShell().evaluate(script)