import com.jeeconf.groovydsl.Monitoring

String.metaClass.leftShift = { Map params ->
    long period = params.every
    Monitoring.sendStatus(delegate, period)
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