import com.jeeconf.groovydsl.Monitoring

String.metaClass.leftShift = { Map params ->
    long period = params?.every
    int times = params.notMoreThan ?: 3
    Monitoring.sendStatusPeriodically(delegate, period, times)
}


static def binding() {
    def binding = new Binding()

    binding.me = '380934902436'

    binding.schedule = { long period, int times ->
        [ every : period, notMoreThan: times ]
    }

    return binding
}


def binding = binding()

String script = new File("../dsl/${this.class.name}.dsl").text
new GroovyShell(binding).evaluate(script)

println binding.period

