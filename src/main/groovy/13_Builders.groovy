import com.jeeconf.groovydsl.Monitoring

Integer.metaClass.propertyMissing = {String name ->
    if (name == 'seconds') {
        return delegate * 1000
    }
    if (name == 'times') {
        return delegate
    }
}


static def binding() {
    def binding = new Binding()

    binding.me = '380934902436'

    binding.status = new SendStatusWithScheduleParametersBuilder()

    binding.send = { params ->
        params.schedule.each {
            Monitoring.sendStatusPeriodically(params.to, it.period, it.exactly)
        }
    }

    return binding
}

class WithScheduleFactory extends AbstractFactory {

    def current

    @Override
    boolean isLeaf() {
        false
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def parameters = new Expando()
        parameters.to = attributes.to
        current = parameters
        return parameters
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        current.schedule = (current.schedule?:[]) + child
    }
}

class ScheduleFactory extends AbstractFactory {

    @Override
    boolean isLeaf() {
        true
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        attributes
    }
}

class SendStatusWithScheduleParametersBuilder extends FactoryBuilderSupport {

    SendStatusWithScheduleParametersBuilder() {
        super(true)
    }

    def registerObjectFactories() {
        registerFactory('withSchedule', new WithScheduleFactory())
        registerFactory('schedule', new ScheduleFactory())
    }
}


String script = new File("../dsl/${this.class.name}.dsl").text
new GroovyShell(binding()).evaluate(script)