package com.example.demoibmmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;

import java.util.Collections;
import java.util.Optional;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.wmq.WMQConstants;

@SpringBootApplication
@RestController
//@EnableJms
public class DemoIbmMqApplication {

	@Autowired
	private JmsTemplate jmsTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DemoIbmMqApplication.class, args);
	}

	@GetMapping("send")
	String send() {
		try {
			jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello World!");
			return "OK";
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@GetMapping("recv")
	String recv() {
		try {
			return jmsTemplate.receiveAndConvert("DEV.QUEUE.1").toString();
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@GetMapping("recv-rollback")
	String recvrollback() throws Exception {
		try {
			
			final MQQueue queue = new MQQueue("DEV.QUEUE.1");
			MQConnectionFactory mqConnFactory = createMQConnectionFactory();
			JMSContext jmsContext = createJMSContext(mqConnFactory, "admin", "passw0rd");
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			String messageAsString = "";
			Message message = consumer.receiveNoWait();
			int dc = message.getIntProperty("JMSXDeliveryCount");
			System.out.println("JMSXDeliveryCount=" + dc);
			if (message instanceof TextMessage) {
                messageAsString = message.getBody(String.class);
            }
			
			System.out.println("Received Message:" + messageAsString);
			Thread.sleep(5000);
			System.out.println("Rolling back....");
			jmsContext.rollback();
			return "ROLLBACK::JMSXDeliveryCount-" + dc;
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@GetMapping("recv1")
	String recv1() throws Exception {
		try {
			
			final MQQueue queue = new MQQueue("DEV.QUEUE.1");
			MQConnectionFactory mqConnFactory = createMQConnectionFactory();
			JMSContext jmsContext = createJMSContext(mqConnFactory, "admin", "passw0rd");
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			String messageAsString = "";
			Optional<Message> optMessage = Optional.ofNullable(consumer.receiveNoWait());
			if(optMessage.isPresent()) {
				Message message = optMessage.get();
				System.out.println("JMSXDeliveryCount=" + message.getIntProperty("JMSXDeliveryCount"));
				if (message instanceof TextMessage) {
	                messageAsString = message.getBody(String.class);
	                System.out.println("Received Message:" + messageAsString);
	            }
				
			}else {
				messageAsString = "NO-MSG";
			}
			jmsContext.commit();
			
			return messageAsString;
			
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@GetMapping("exit")
	String exit() throws Exception {
		System.exit(1);
		return "EXIT";
	}
	
	private MQConnectionFactory createMQConnectionFactory() throws JMSException {
		final MQConnectionFactory mqConnFactory = new MQConnectionFactory();
		mqConnFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
		mqConnFactory.setQueueManager("QM1");
		mqConnFactory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
		mqConnFactory.setConnectionNameList("130.61.220.240(80)");
		mqConnFactory.setChannel("DEV.ADMIN.SVRCONN");

		return mqConnFactory;
	}
	
	private JMSContext createJMSContext(final MQConnectionFactory mqConnFactory
            , final String username
            , final String password) {
        if (username == null || password == null) {
            return mqConnFactory.createContext(JMSContext.SESSION_TRANSACTED);
        } else {
            return mqConnFactory.createContext(username, password, JMSContext.SESSION_TRANSACTED);
        }
    }
}
