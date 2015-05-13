import com.jeeconf.groovydsl.Monitoring

String.metaClass.leftShift = { Map params ->
    long period = params?.every
    int times = params.notMoreThan ?: 3
    Monitoring.sendStatusPeriodically(delegate, period, times)
}


def binding = binding()
new GroovyShell(binding).evaluate(
'''
import java.util.concurrent.TimeUnit

period = TimeUnit.SECONDS.toMillis(10)
me << schedule(period, 1)
'''
)
println binding.period


static def binding() {
    def binding = new Binding()

    binding.me = '380934902436'

    binding.schedule = { long period, int times ->
        [ every : period, notMoreThan: times ]
    }

    return binding
}

