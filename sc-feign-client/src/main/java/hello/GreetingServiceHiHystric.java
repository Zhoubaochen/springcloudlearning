package hello;

import org.springframework.stereotype.Component;

@Component
public class GreetingServiceHiHystric implements GreetingService {

	@Override
	public String hiService(String name) {
		return "sorry "+name;
	}

}
