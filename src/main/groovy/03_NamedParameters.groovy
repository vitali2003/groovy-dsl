import com.jeeconf.groovydsl.Monitoring

String.metaClass.sendStatus = { Map params ->
    long period = params.every
    Monitoring.sendStatus(delegate, period)
}

new GroovyShell().evaluate(
'''
'380934902436'.sendStatus every: 30000
'''
)