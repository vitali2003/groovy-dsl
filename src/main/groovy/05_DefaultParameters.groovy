import com.jeeconf.groovydsl.Monitoring

String.metaClass.sendStatusPeriodically = { Map params ->
    long period = params?.every
    int times = params.notMoreThan ?: 3
    Monitoring.sendStatusPeriodically(delegate, period, times)
}

new GroovyShell().evaluate(
'''
'380934902436'.sendStatusPeriodically every: 1000
'''
)