package com.jeeconf.groovydsl

import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class MonitoringSpec extends Specification {

    Monitoring monitoring = Spy(Monitoring)

    def 'void sendStatus(String phoneNumber, long period)'() {
        given:
            String phoneNumber = '322-223-322'
            long period = 1234

        when:
            2 * monitoring.sleep(period) >> { /* do nothing */ }

            2 * monitoring.now() >>> [ 'now-1', 'now-2' ]

            monitoring.sendStatus(phoneNumber, period, 2)

        then:
            1 * monitoring.sendMessage(phoneNumber, 'So Far, So Good... (at now-1)')
            1 * monitoring.sendMessage(phoneNumber, 'So Far, So Good... (at now-2)')

    }

    def 'void sendMessage(String phoneNumber, String message) throws IOException'() {
        when:
            1 * monitoring.apiKey() >> 'api-key'

            monitoring.sendMessage(phoneNumber, message)

        then:
            1 * monitoring.post('http://sms.ru/sms/send?api_id={apiId}&to={phoneNumber}&text={text}', params)

        where:
            phoneNumber   | message  | params
            '322-223-322' | 'hello!' | [ apiId: 'api-key', phoneNumber: '322-223-322', text: 'hello!' ]
            '911'         | 'help!'  | [ apiId: 'api-key', phoneNumber: '911',         text: 'help!'  ]
    }
}
