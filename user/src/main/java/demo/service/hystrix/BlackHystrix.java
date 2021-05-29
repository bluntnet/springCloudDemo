package demo.service.hystrix;

import demo.service.EurekaClientFeign;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class BlackHystrix implements EurekaClientFeign {
    @Override
    public List<String> getBlack() {
        String[] a = {"没有找到记录"};
        return Arrays.asList(a);
    }
}
