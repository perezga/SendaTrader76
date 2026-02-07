package sendaTrader76.bot.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import jakarta.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sendaTrader76.bot.dto.Heartbeat;
import sendaTrader76.bot.dto.OrderFillTransaction;
import sendaTrader76.bot.dto.ReplacementOrder;

@Component
public class TransactionStreamService {

	private static final Log logger = LogFactory.getLog(TransactionStreamService.class);

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmssSSS")
			.withZone(ZoneId.of("GMT-5"));

	private RestTemplate restTemplateStream;

	private ObjectMapper objectMapper;

	private SimpMessagingTemplate template;

	private Result result = new Result();

	private BigDecimal price;

	private boolean retry = true;

	@Autowired
	public TransactionStreamService(RestTemplate restTemplateStream, ObjectMapper objectMapper,
			SimpMessagingTemplate template) {
		this.restTemplateStream = restTemplateStream;
		this.objectMapper = objectMapper;
		this.template = template;
	}

	@PreDestroy
	public void shutDownMethod() {
		retry = false;
		logger.info("retry set to false");
	}

	@Async
	public void startTransactionStream(String account, String webSocketTopicPositions) {

		RequestCallback requestCallback = new NoOpRequestCallback();
		ResponseExtractor<?> responseExtractor = new EventResponseExtractor(webSocketTopicPositions);
		while (retry) {
			try {
				logger.info("Starting transaction stream...");
				restTemplateStream.execute(
						"/v3/accounts/" + account + "/transactions/stream",
						HttpMethod.GET, requestCallback, responseExtractor);
				logger.info("Transaction stream stopped...");
			} catch (Exception e) {
				logger.warn("Exception while reading event stream.", e);
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("Price Stream interrpted");
			}
		}
		logger.info("Shutdown TransactionStreamService thread.");
	}

	private class NoOpRequestCallback implements RequestCallback {
		@Override
		public void doWithRequest(ClientHttpRequest request) throws IOException {
		}
	}

	private class EventResponseExtractor implements ResponseExtractor<String> {

		private String webSocketTopicPositions;

		public EventResponseExtractor(String webSocketTopicPositions) {
			this.webSocketTopicPositions = webSocketTopicPositions;
		}

		@Override
		public String extractData(ClientHttpResponse response) throws IOException {
			InputStream inputStream = response.getBody();
			try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream))) {
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					processTransaction(line, webSocketTopicPositions);
				}
			}
			return null;
		}
	}

	public void processTransaction(String line, String webSocketTopicPositions)
			throws IOException, JsonParseException, JsonMappingException {
		if (!line.contains("HEARTBEAT")) {
			Position position = new Position();

			if (line.contains("ORDER_FILL")) {
				OrderFillTransaction orderFillTransaction = objectMapper.readValue(line, OrderFillTransaction.class);
				position.setPrice(orderFillTransaction.getPrice());
				position.setTime(orderFillTransaction.getTime().toInstant().toEpochMilli());

				if (orderFillTransaction.getReason().equals("STOP_LOSS_ORDER")) {
					position.setType(PositionType.STOP_LOSS);

					updateResults(orderFillTransaction);

					template.convertAndSend(webSocketTopicPositions, result);

				} else if (orderFillTransaction.getReason().equals("MARKET_ORDER")) {
					if (orderFillTransaction.getUnits().compareTo(BigDecimal.ZERO) < 0) {
						position.setType(PositionType.SHORT);
					} else {
						position.setType(PositionType.LONG);
					}
					price = orderFillTransaction.getPrice();
				}
			} else if (line.contains("STOP_LOSS_ORDER") && line.contains("REPLACEMENT")) {

				ReplacementOrder replacementOrder = objectMapper.readValue(line, ReplacementOrder.class);

				position.setPrice(replacementOrder.getPrice());
				position.setTime(replacementOrder.getTime().toInstant().toEpochMilli());
				position.setType(PositionType.TRAILLING_STOP);
			}

			logger.info(line);
			template.convertAndSend("/topic/positions", position);
			// eventBus.post(price);
		} else {
			Heartbeat heartbeat = objectMapper.readValue(line, Heartbeat.class);
			logger.debug("HEARTBEAT " + heartbeat);
		}
	}

	private void updateResults(OrderFillTransaction orderFillTransaction) {
		BigDecimal partialPrice;
		if (orderFillTransaction.getUnits().compareTo(BigDecimal.ZERO) < 0) {
			partialPrice = orderFillTransaction.getPrice().subtract(price);
			result.setWinPositions(result.getWinPositions() + 1);
			result.setPartialType(PositionType.LONG);

		} else {
			partialPrice = price.subtract(orderFillTransaction.getPrice());
			result.setLosePositions(result.getLosePositions() + 1);
			result.setPartialType(PositionType.SHORT);
		}

		result.setPartial(partialPrice);
		result.setTotal(result.getTotal().add(partialPrice));
	}
}
