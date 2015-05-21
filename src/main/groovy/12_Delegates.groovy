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

    binding.status = { Closure closure ->
        def statusConfiguration = new StatusConfiguration()
        def configure = closure.rehydrate(statusConfiguration, binding, this)
        configure.resolveStrategy = Closure.DELEGATE_FIRST
        configure()

        Monitoring.sendStatusPeriodically(statusConfiguration.phoneNumber, statusConfiguration.period, statusConfiguration.count)
    }

    return binding
}

class StatusConfiguration {

    String phoneNumber
    long period
    int count

    void to(String phoneNumber) {
        this.phoneNumber = phoneNumber
    }

    void period(long period) {
        this.period = period
    }

    void notMoreThan(int count) {
        this.count = count
    }
}


String script = new File("../dsl/${this.class.name}.dsl").text
new GroovyShell(binding()).evaluate(script)