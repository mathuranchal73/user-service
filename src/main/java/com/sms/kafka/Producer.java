package com.sms.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.model.User;

@Service
public class Producer {

	 private static final Logger logger = LoggerFactory.getLogger(Producer.class);
	 

	
	 @Value("${tpd.topic-name}")
	 private static String TOPIC="Welcome_Email";

	   
	    
	   @Autowired
	   KafkaTemplate kafkaTemplate;
	    
	   
	    

	    public void sendMessage(String message) {
	    	/**ObjectMapper objectMapper = new ObjectMapper();
	    	
            JsonNode  jsonNode = objectMapper.valueToTree(user);**/
            ProducerRecord<String, String> rec = new ProducerRecord<String, String>(TOPIC,message);

	        logger.info(String.format("#### -> Producing message -> %s", message));
	        this.kafkaTemplate.send(rec);
	     
	    }
}
