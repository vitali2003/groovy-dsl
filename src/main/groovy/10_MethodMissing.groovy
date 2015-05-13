import com.jeeconf.groovydsl.Monitoring

String.metaClass.methodMissing = { String name, args ->
    if (name.startsWith('notifyEvery')) {
        Monitoring.sendStatusPeriodically(delegate, extractPeriodFromMethodName(name))
    }
}


new GroovyShell(binding()).evaluate(
'''
me.notifyEvery30Seconds()
'''
)


static def binding() {
    def binding = new Binding()

    binding.me = '380934902436'

    return binding
}

static long extractPeriodFromMethodName(String name) {
    String period = name.substring('notifyEvery'.length(), name.length())

    int numberIndex = 0
    while (Character.isDigit(period.charAt(numberIndex))) { numberIndex++ }
    long numPeriod = Integer.parseInt(period.substring(0, numberIndex))

    String strPeriod = period.substring(numberIndex, period.length())
    if (strPeriod.toLowerCase() == 'seconds') {
        return numPeriod * 1000
    }

    return numPeriod
}