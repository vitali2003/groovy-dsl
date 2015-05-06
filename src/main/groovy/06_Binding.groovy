import com.jeeconf.groovydsl.Monitoring

String.metaClass.leftShift = { Map params ->
    long period = params?.every
    int times = params.notMoreThan ?: 3
    Monitoring.sendStatusPeriodically(delegate, period, times)
}

def binding = new Binding()

binding.me = '380934902436'

binding.every = { long period ->
    [ every : period ]
}

new GroovyShell(binding).evaluate(
'''
me << every(30000)
'''
)