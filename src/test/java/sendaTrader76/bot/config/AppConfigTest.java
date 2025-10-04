package sendaTrader76.bot.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = { "sendaTrader76.bot.indicators" })
public class AppConfigTest {

	@Bean
	public RestTemplate restTemplateBuilder() {
		return new RestTemplateBuilder().build();
	}
}