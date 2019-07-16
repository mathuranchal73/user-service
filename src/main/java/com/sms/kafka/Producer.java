package com.sms.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Producer {

	 private static final Logger logger = LoggerFactory.getLogger(Producer.class);
	 

	
	 @Value("${tpd.topic-name}")
	 private static String TOPIC="Welcome_Email";

	    @Autowired
	    private KafkaTemplate<String, String> kafkaTemplate;
	    
	   
	    

	    public void sendMessage(String message) {
	        logger.info(String.format("#### -> Producing message -> %s", message));
	        this.kafkaTemplate.send(TOPIC, message);
	    }
}
