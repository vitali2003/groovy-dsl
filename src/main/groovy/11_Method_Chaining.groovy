import com.jeeconf.groovydsl.Monitoring

Integer.metaClass.propertyMissing = {String name ->
    if (name == 'seconds') {
        delegate * 1000
    }
}


new GroovyShell(binding()).evaluate(
'''
please send status to:me every 30.seconds
'''
)


static def binding() {
    def binding = new Binding()

    binding.please = { Closure action ->
        [ status: { Map params ->
            String phoneNumber = params.to
            [ every: { long period ->
                action.call(phoneNumber, period)
            } ]
        } ]
    }

    binding.send = { String phoneNumber, long period ->
        Monitoring.sendStatusPeriodically(phoneNumber, period)
    }

    binding.me = '380934902436'

    return binding
}