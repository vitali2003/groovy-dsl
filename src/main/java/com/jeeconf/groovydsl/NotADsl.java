package com.jeeconf.groovydsl;

import org.apache.commons.io.IOUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class NotADsl {

    public static void main(String[] args) {
        String phoneNumber = "380934902436";
        long period = 30000;

        try {
            String apiKey = IOUtils.toString(NotADsl.class.getResourceAsStream("/api.key"));

            while (true) {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("apiId", apiKey);
                parameters.put("phoneNumber", phoneNumber);
                parameters.put("text", "So Far, So Good... (at " + LocalDateTime.now() + ")");

                new RestTemplate().postForLocation("http://sms.ru/sms/send?api_id={apiId}&to={phoneNumber}&text={text}", null, parameters);
                Thread.sleep(period);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
