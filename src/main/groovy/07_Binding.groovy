import com.jeeconf.groovydsl.Monitoring

String.metaClass.leftShift = { Map params ->
    long period = params?.every
    int times = params.notMoreThan ?: 3
    Monitoring.sendStatusPeriodically(delegate, period, times)
}


static def binding() {
    def binding = new Binding()

    binding.me = '380934902436'

    binding.every = { long period ->
        [ every : period ]
    }

    return binding
}


String script = new File("../dsl/${this.class.name}.dsl").text
new GroovyShell(binding()).evaluate(script)