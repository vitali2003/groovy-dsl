import com.jeeconf.groovydsl.Monitoring

String.metaClass.sendStatusPeriodically = { Map params ->
    long period = params?.every
    int times = params.notMoreThan ?: 3
    Monitoring.sendStatusPeriodically(delegate, period, times)
}


String script = new File("../dsl/${this.class.name}.dsl").text
new GroovyShell().evaluate(script)