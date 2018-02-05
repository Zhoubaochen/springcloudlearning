package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	@Autowired
	GreetingService helloService;

	@Value("${server.port}")
	String port;

	@RequestMapping("/hi")
	public String sayHi(@RequestParam String name) {
		return helloService.hiService(name);
	}
}
