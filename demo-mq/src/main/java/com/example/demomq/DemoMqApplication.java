package com.example.demomq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;


@SpringBootApplication
@RestController
@EnableJms
public class DemoMqApplication {

        @Autowired
        private JmsTemplate jmsTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DemoMqApplication.class, args);
	}

        @GetMapping("send")
String send(){
    try{
        jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello World!");
        return "OK";
    }catch(JmsException ex){
        ex.printStackTrace();
        return "FAIL";
    }
}


@GetMapping("recv")
String recv(){
    try{
        return jmsTemplate.receiveAndConvert("DEV.QUEUE.1").toString();
    }catch(JmsException ex){
        ex.printStackTrace();
        return "FAIL";
    }
}


@GetMapping("recv-rollback")
String recvrollback() throws Exception{
    try{
        String msg = jmsTemplate.receiveAndConvert("DEV.QUEUE.1").toString();
        System.out.println("Received Message:" + msg);
        Thread.sleep(5000);
        System.out.println("Rolling back....");
        throw new Exception();
    }catch(JmsException ex){
        ex.printStackTrace();
        return "FAIL";
    }
}





}
