package hello;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@EnableEurekaClient
@SpringBootApplication(exclude={},excludeName={})
public class Application {

    public static void main(String[] args) {
    	SpringApplication springApplication=new SpringApplication(Application.class);
    	springApplication.setBannerMode(Banner.Mode.OFF);
    	springApplication.run(args);
    }
    
}
