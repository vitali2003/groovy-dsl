import com.jeeconf.groovydsl.Monitoring

String.metaClass.leftShift = { Map params ->
    long period = params.every
    Monitoring.sendStatusPeriodically(delegate, period)
}

Integer.metaClass.propertyMissing = {String name ->
    if (name == 'seconds') {
        delegate * 1000
    }
}


static def binding() {
    def binding = new Binding()

    binding.me = '380934902436'

    binding.every = { long period ->
        [ every : period ]
    }

    return binding
}


String script = new File("../dsl/${this.class.name}.dsl").text
new GroovyShell(binding()).evaluate(script)