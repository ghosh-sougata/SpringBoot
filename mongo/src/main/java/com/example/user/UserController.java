package com.example.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
    
    //@Autowired
    private final UserRepository userRepository;	
   
    @Value("${location.service.url}")
    private String locationSvcUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/ping")
    public @ResponseBody String ping() {
        return "ping ok....";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> getAllUsers() {
	LOG.info("Getting all users.");
	return userRepository.findAll();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable String userId) {
	LOG.info("Getting user with ID: {}.", userId);
	return userRepository.findById(userId).orElse(null);
    }  


    @RequestMapping(method = RequestMethod.POST)
    public User addNewUsers(@RequestBody User user) {
	LOG.info("Saving user.");
	return userRepository.save(user);
    } 
}
