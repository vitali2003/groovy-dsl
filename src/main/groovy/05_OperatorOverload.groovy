import com.jeeconf.groovydsl.Monitoring

String.metaClass.leftShift = { Map params ->
    long period = params.every
    Monitoring.sendStatus(delegate, period)
}

new GroovyShell().evaluate(
'''
'380934902436' << [ every: 30000 ]
'''
)