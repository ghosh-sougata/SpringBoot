package com.example.location;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
@RequestMapping("location")
public class LocationController {
 
    @GetMapping("/{id}")
    public @ResponseBody Location getLocation(@PathVariable int id) {
        return findLocationById(id);
    }
 
    private Location findLocationById(int id) {
        Location c = new Location();
		c.setId(1);
		c.setName("Bangalore");
		return c;
    }
}
