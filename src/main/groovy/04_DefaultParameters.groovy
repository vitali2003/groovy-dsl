import com.jeeconf.groovydsl.Monitoring

String.metaClass.sendStatus = { Map params ->
    long period = params?.every ?: 30000
    Monitoring.sendStatus(delegate, period)
}

new GroovyShell().evaluate(
'''
'380934902436'.sendStatus()
'''
)