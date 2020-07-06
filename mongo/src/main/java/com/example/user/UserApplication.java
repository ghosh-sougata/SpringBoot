package com.example.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;


@EnableDiscoveryClient
@SpringBootApplication
public class UserApplication {

        public static void main(String[] args) {
                SpringApplication.run(UserApplication.class, args);
        }
  //@LoadBalanced
  @Bean
  RestTemplate restTemplate() {
      return new RestTemplate();
  }

}
