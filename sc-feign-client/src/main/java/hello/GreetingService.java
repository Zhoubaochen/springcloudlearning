package hello;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-hi",fallback = GreetingServiceHiHystric.class)
public interface GreetingService {

	@RequestMapping(value = "/hi",method = RequestMethod.GET)
    String hiService(@RequestParam(value = "name") String name);

}
