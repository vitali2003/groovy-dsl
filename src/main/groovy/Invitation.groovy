import java.time.LocalDateTime
import java.time.LocalTime

Integer.metaClass.propertyMissing = {String name ->
    if (name == 'am') {
        new LocalTime(delegate, 0, 0, 0).toString()
    }
}


static def binding() {
    def binding = new Binding()

    binding.myTalk = 'Building DSL-s with Groovy'

    binding.tomorrow = LocalDateTime.now().toLocalDate().plusDays(1).toString()

    binding.jeeconf = 'jeeconf'

    binding.please = { Closure action ->
        [ invitation: { Map params ->
            String pattern = '''come {date} at {time} to listen '{talk}' on #{conference} to see how it's implemented'''
            String talk = params.for
            String conference = params.on
            [ at: { String time, String date ->
                action.call(pattern, [ date: date, time: time, talk: talk, conference: conference ])
            } ]
        } ]
    }

    binding.generate = { String pattern, Map params  ->
        String message = pattern
        params.each { key, value ->
            message = message.replace("{$key}", value)
        }
        println message
    }

    return binding
}


new GroovyShell(binding()).evaluate('''
please generate invitation for: myTalk, on: jeeconf at 10.am, tomorrow
''')


























