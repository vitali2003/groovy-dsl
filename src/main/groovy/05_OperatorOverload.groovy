import com.jeeconf.groovydsl.Monitoring

String.metaClass.leftShift = { Map params ->
    long period = params?.every
    int times = params.notMoreThan ?: 3
    Monitoring.sendStatusPeriodically(delegate, period, times)
}

new GroovyShell().evaluate(
'''
'380934902436' << [ every: 30000 ]
'''
)