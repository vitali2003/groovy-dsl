import com.jeeconf.groovydsl.Monitoring

String.metaClass.sendStatusPeriodically = { long period ->
    Monitoring.sendStatusPeriodically(delegate, period)
}


new GroovyShell().evaluate(
'''
'380934902436'.sendStatusPeriodically 30000
'''
)