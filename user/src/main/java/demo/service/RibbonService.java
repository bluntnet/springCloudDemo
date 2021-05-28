package demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RibbonService {
    @Autowired
    RestTemplate restTemplate;

    public List<String> getUser() {
        return restTemplate.getForObject("http://eureka-client-sale/black", List.class);
    }
}
