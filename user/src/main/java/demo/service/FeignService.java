package demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FeignService {

    @Autowired
    EurekaClientFeign eurekaClientFeign;

    public List<String> getUser() {
        return eurekaClientFeign.getBlack();
    }
}
