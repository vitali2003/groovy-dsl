package com.jeeconf.groovydsl

import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class MonitoringSpec extends Specification {

    Monitoring monitoring = Spy(Monitoring)

    def 'void sendStatus(String phoneNumber, long period)'() {
        given:

        when:
            3 * monitoring.forever() >>> [ true, true, false ]

            2 * monitoring.sleep(1234) >> { /* do nothing */ }

            2 * monitoring.now() >>> [ 'now-1', 'now-2' ]

            monitoring.sendStatus('phoneNumber', 1234)

        then:
            1 * monitoring.sendMessage('phoneNumber', 'So Far, So Good... (at now-1)')
            1 * monitoring.sendMessage('phoneNumber', 'So Far, So Good... (at now-2)')

    }
}
