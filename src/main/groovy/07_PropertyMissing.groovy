import com.jeeconf.groovydsl.Monitoring

String.metaClass.leftShift = { Map params ->
    long period = params.every
    Monitoring.sendStatus(delegate, period)
}

Integer.metaClass.propertyMissing = {String name ->
    if (name == 'seconds') {
        delegate * 1000
    }
}

def binding = new Binding()

binding.me = '380934902436'

binding.every = { long period ->
    [ every : period ]
}

new GroovyShell(binding).evaluate(
'''
me << every(30.seconds)
'''
)