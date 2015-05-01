package com.jeeconf.groovydsl;

import org.apache.commons.io.IOUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yaroslav.yermilov
 */
public class Monitoring {

    public static void sendStatusPeriodically(String phoneNumber, long period) {
        new Monitoring().sendStatus(phoneNumber, period);
    }

    void sendStatus(String phoneNumber, long period) {
        while (true) {
            try {
                sendMessage(phoneNumber, "So Far, So Good... (at " + now() + ")");
                sleep(period);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    void sendMessage(String phoneNumber, String message) throws IOException {
        String apiKey = IOUtils.toString(NotADsl.class.getResourceAsStream("/api.key"));

        Map<String, String> parameters = new HashMap<>();
        parameters.put("apiId", apiKey);
        parameters.put("phoneNumber", phoneNumber);
        parameters.put("text", message);

        post("http://sms.ru/sms/send?api_id={apiId}&to={phoneNumber}&text={text}", parameters);
    }

    void sleep(long period) throws InterruptedException {
        Thread.sleep(period);
    }

    String now() {
        return LocalDateTime.now().toString();
    }

    void post(String url, Map<String, String> parameters) {
        new RestTemplate().postForLocation("http://sms.ru/sms/send?api_id={apiId}&to={phoneNumber}&text={text}", null, parameters);
    }
}
