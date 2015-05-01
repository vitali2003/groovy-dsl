package com.jeeconf.groovydsl;

import org.apache.commons.io.IOUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yaroslav.yermilov
 */
public class NotADsl {

    public static void main(String[] args) {
        try {
            String apiKey = IOUtils.toString(NotADsl.class.getResourceAsStream("/api.key"));

            while (true) {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("apiId", apiKey);
                parameters.put("phoneNumber", "380934902436");
                parameters.put("text", "So Far, So Good... (at " + LocalDateTime.now() + ")");

                new RestTemplate().postForLocation("http://sms.ru/sms/send?api_id={apiId}&to={phoneNumber}&text={text}", null, parameters);
                Thread.sleep(30000);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
