try {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("apiId", apiId);
    parameters.put("phoneNumber", smsMessage.getToPhoneNumber());
    parameters.put("text", smsMessage.getText());
            
    ensureRestTemplateWasInjected();
            
    restTemplate.postForLocation(SMS_SEND_URL, null, parameters);
            
    log.info("Send SMS message: " + smsMessage);
} catch (RestClientException e) {
    log.error("Can't send SMS message", e);
    throw new SmsSenderException("Can't send SMS message", e);
}