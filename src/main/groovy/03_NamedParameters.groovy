import com.jeeconf.groovydsl.Monitoring

String.metaClass.sendStatusPeriodically = { Map params ->
    long period = params.every
    Monitoring.sendStatusPeriodically(delegate, period)
}

new GroovyShell().evaluate(
'''
'380934902436'.sendStatusPeriodically every: 30000
'''
)