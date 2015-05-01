import com.jeeconf.groovydsl.Monitoring

String.metaClass.sendStatus = { long period ->
    Monitoring.sendStatus(delegate, period)
}

new GroovyShell().evaluate(
'''
'380934902436'.sendStatus 30000
'''
)