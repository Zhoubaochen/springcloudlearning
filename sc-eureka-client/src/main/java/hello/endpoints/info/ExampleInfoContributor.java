package hello.endpoints.info;

import java.util.Collections;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
@Component
public class ExampleInfoContributor implements InfoContributor{

	@Override
	public void contribute(Builder builder) {
		// TODO Auto-generated method stub
		builder.withDetail("example",
                Collections.singletonMap("key1", "value1"));
	}

}
