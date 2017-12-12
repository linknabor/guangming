package com.yumu.hexie.service.jms;

import javax.inject.Inject;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JMSProducer {
	@Inject
    private JmsTemplate jmsTemplate;

	public void sendMessage(String destinationName, String message) {
    	
        jmsTemplate.convertAndSend(destinationName, message);
    }
    
}