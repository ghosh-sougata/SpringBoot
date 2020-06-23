package com.example.company;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.location.Location;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("company")
public class CompanyController {
	
	@Value("${location.service.url}")
    private String locationSvcUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{id}")
    public @ResponseBody Company getCompany(@PathVariable int id) {
        return findCompanyById(id);
    }

    private Company findCompanyById(int id) {
		System.out.println("----------------------" + locationSvcUrl);
		
        Location l = restTemplate.getForObject(locationSvcUrl + "/1", Location.class);

        Company c = new Company();
                c.setId(1);
                c.setName("Accenture");
                c.setLocation(l.getName());
                return c;
    }
}

