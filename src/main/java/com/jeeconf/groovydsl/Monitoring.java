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

    public static void sendStatus(String phoneNumber, long period) {
        while (true) {
            try {
                sendMessage(phoneNumber, "So Far, So Good... (at " + LocalDateTime.now() + ")");
                Thread.sleep(period);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void sendMessage(String phoneNumber, String message) throws IOException {
        String apiKey = IOUtils.toString(NotADsl.class.getResourceAsStream("/api.key"));

        Map<String, String> parameters = new HashMap<>();
        parameters.put("apiId", apiKey);
        parameters.put("phoneNumber", phoneNumber);
        parameters.put("text", message);

        new RestTemplate().postForLocation("http://sms.ru/sms/send?api_id={apiId}&to={phoneNumber}&text={text}", null, parameters);
    }
}
