package demo.service;

import demo.config.FeignConfig;
import demo.service.hystrix.BlackHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "eureka-client-sale", configuration = FeignConfig.class,fallback = BlackHystrix.class)
public interface EurekaClientFeign {
    @GetMapping(value = "/black")
    List<String> getBlack();
}
