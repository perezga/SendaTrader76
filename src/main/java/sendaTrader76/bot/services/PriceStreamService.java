package sendaTrader76.bot.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;

import sendaTrader76.bot.dto.InstrumentType;
import sendaTrader76.bot.dto.Price;

@Component
public class PriceStreamService {

	private static final Log logger = LogFactory.getLog(PriceStreamService.class);

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmssSSS")
			.withZone(ZoneId.of("GMT-5"));

	private RestTemplate restTemplateStream;

	private EventBus eventBus;

	private ObjectMapper objectMapper;

	private boolean retry = true;

	@Autowired
	public PriceStreamService(RestTemplate restTemplateStream, @Qualifier(value = "PriceEURUSD") EventBus eventBus,
			ObjectMapper objectMapper) {
		this.restTemplateStream = restTemplateStream;
		this.eventBus = eventBus;
		this.objectMapper = objectMapper;
	}

	@PreDestroy
	public void shutDownMethod() {
		retry = false;
		logger.info("retry set to false");
	}

	@Async
	public void startPriceService(InstrumentType instrument, String account, List<CandleChart> candleChartList) {

		RequestCallback requestCallback = new NoOpRequestCallback();
		ResponseExtractor<?> responseExtractor = new EventResponseExtractor(candleChartList);
		while (retry) {
			try {
				logger.info("Starting price stream...");
				restTemplateStream.execute(
						"/v3/accounts/" + account + "/pricing/stream?instruments=" + instrument,
						HttpMethod.GET, requestCallback, responseExtractor);
				logger.info("Price stream stopped...");
			} catch (Exception e) {
				logger.warn("Exception while reading event stream.", e);
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("Price Stream interrpted");
			}
		}
		logger.info("Shutdown PriceService thread.");
	}

	private static class NoOpRequestCallback implements RequestCallback {

		@Override
		public void doWithRequest(ClientHttpRequest request) throws IOException {
		}
	}

	private class EventResponseExtractor implements ResponseExtractor<String> {

		private List<CandleChart> candleChartList;

		public EventResponseExtractor(List<CandleChart> candleChartList) {
			this.candleChartList = candleChartList;
		}

		@Override
		public String extractData(ClientHttpResponse response) throws IOException {
			InputStream inputStream = response.getBody();
			try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream));
					BufferedWriter bw = Files.newBufferedWriter(Paths.get(LocalDateTime.now() + "execution.csv"),
							StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW)) {
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					if (!line.contains("HEARTBEAT")) {
						Price price = objectMapper.readValue(line, Price.class);

						logger.debug(line);
						savePriceToFile(bw, price);

						candleChartList.forEach(candleChart -> candleChart.addCandle(price));

						eventBus.post(price);
					} else {
						logger.debug("HEARTBEAT " + line);
					}
				}
			}
			return null;
		}
	}

	private void savePriceToFile(BufferedWriter bw, Price price) throws IOException {
		bw.append(String.format("%s,%s,%s,%s",
				formatter.format(price.getTime()),
				price.getAsks()[price.getAsks().length - 1].getPrice(),
				price.getBids()[0].getPrice(),
				0));
		bw.newLine();
		bw.flush();
	}
}
