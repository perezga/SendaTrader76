package sendaTrader76.bot.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import com.google.common.eventbus.EventBus;

@Configuration
@EnableAsync
@ComponentScan(basePackages = { "sendaTrader76.bot.indicators", "sendaTrader76.bot.strategies", "sendaTrader76.bot.services" })
public class AppConfig {

	@Value("${oanda.rest.header.authentication}")
	private String authenticationHeader;

	@Value("${oanda.account.id}")
	private String accountId;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

		return restTemplateBuilder.rootUri("https://api-fxpractice.oanda.com")
				.interceptors(new OandaHTTPHeaderInterceptor()).build();

	}

	@Bean
	public RestTemplate restTemplateStream(RestTemplateBuilder restTemplateBuilder) {

		return restTemplateBuilder.rootUri("https://stream-fxpractice.oanda.com")
				.interceptors(new OandaHTTPHeaderInterceptor()).build();

	}

	@Bean(name="PriceEURUSD")
	public EventBus eventBus() {		
		
		EventBus eventBus = new EventBus();		

		return eventBus;
	}

	private final class OandaHTTPHeaderInterceptor implements ClientHttpRequestInterceptor {
		@Override
		public ClientHttpResponse intercept(HttpRequest request,
				byte[] body, ClientHttpRequestExecution execution) throws IOException {
			HttpHeaders headers = request.getHeaders();
			headers.set("Authorization", authenticationHeader);
			headers.set("Content-Type", "application/json");
			return execution.execute(request, body);
		}
	}
}