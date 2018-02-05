package hello;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
//controller where every method returns a domain object instead of a view.
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @RequestMapping("/aa")
    public Greeting greeting(@RequestParam(value="n", defaultValue="Wo333ld") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @Value("${server.port}")
    String port;
    @RequestMapping("/hi")
    public String sayHi(@RequestParam String name) {
        return "hi "+name+",i am from port:" +port;
    }
}
