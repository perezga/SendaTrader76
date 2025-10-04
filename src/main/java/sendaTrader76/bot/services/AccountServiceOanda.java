package sendaTrader76.bot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import sendaTrader76.bot.dto.InstrumentType;
import sendaTrader76.bot.dto.OpenTrades;
import sendaTrader76.bot.dto.OrderClose;
import sendaTrader76.bot.dto.OrderFillTransaction;
import sendaTrader76.bot.dto.OrderRequest;
import sendaTrader76.bot.dto.OrderResponse;
import sendaTrader76.bot.dto.PricesResponse;
import sendaTrader76.bot.dto.StopLossOrderRequest;
import sendaTrader76.bot.dto.TradeOrder;
import sendaTrader76.bot.dto.TradeOrderRequest;

@Component
public class AccountServiceOanda implements AccountService {

	private static final Logger log = LoggerFactory.getLogger(AccountServiceOanda.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	CandleService candleService;
	

	@Autowired
	private ObjectMapper objectMapper;

	/* (non-Javadoc)
	 * @see sendaTrader76.bot.services.AccountService#getPrice(java.lang.String, sendaTrader76.bot.dto.InstrumentType)
	 */
	@Override
	public PricesResponse getPrice(String accountId, InstrumentType instrument) {
		PricesResponse pricesResponse = restTemplate.getForEntity("/v3/accounts/" + accountId + "/pricing?instruments=" + instrument,
				PricesResponse.class).getBody();
		return pricesResponse;
	}

	/* (non-Javadoc)
	 * @see sendaTrader76.bot.services.AccountService#postOrder(java.lang.String, sendaTrader76.bot.dto.OrderRequest)
	 */
	@Override
	public OrderResponse postOrder(String accountId, OrderRequest orderRequest) {

		// System.out.println(gson.toJson(orderRequest));

		HttpEntity<OrderRequest> requestEntity = new HttpEntity<OrderRequest>(orderRequest);

		HttpEntity<OrderResponse> response = restTemplate.exchange(
				"/v3/accounts/{accountId}/orders", HttpMethod.POST, requestEntity,
				OrderResponse.class, accountId);

		return response.getBody();
	}

	/* (non-Javadoc)
	 * @see sendaTrader76.bot.services.AccountService#postOrder(java.lang.String, sendaTrader76.bot.dto.StopLossOrderRequest)
	 */
	@Override
	public void postOrder(String accountId, StopLossOrderRequest stopLossOrderRequest) {

		// Gson gson;

		HttpEntity<StopLossOrderRequest> requestEntity = new HttpEntity<StopLossOrderRequest>(stopLossOrderRequest);

		HttpEntity<OrderResponse> response = restTemplate.exchange(
				"/v3/accounts/{accountId}/orders", HttpMethod.POST, requestEntity,
				OrderResponse.class, accountId);
	}

	/* (non-Javadoc)
	 * @see sendaTrader76.bot.services.AccountService#putOrder(java.lang.String, sendaTrader76.bot.dto.StopLossOrderRequest, java.lang.String)
	 */
	@Override
	public OrderResponse putOrder(String accountId, StopLossOrderRequest stopLossOrderRequest, String orderId) {

		// log.info("OrderId " + orderId);
		// log.info(gson.toJson(stopLossOrderRequest));

		HttpEntity<StopLossOrderRequest> requestEntity = new HttpEntity<StopLossOrderRequest>(stopLossOrderRequest);

		HttpEntity<OrderResponse> response = restTemplate.exchange(
				"/v3/accounts/{accountId}/orders/{orderId}", HttpMethod.PUT, requestEntity,
				OrderResponse.class, accountId, orderId);

		return response.getBody();
	}

	/* (non-Javadoc)
	 * @see sendaTrader76.bot.services.AccountService#postTrade(java.lang.String, sendaTrader76.bot.dto.TradeOrderRequest, java.lang.String)
	 */
	@Override
	public TradeOrder postTrade(String accountId, TradeOrderRequest tradeOrderRequest, String traderId) {
		
		// log.info("OrderId " + orderId);
		try {
			log.info(objectMapper.writeValueAsString(tradeOrderRequest));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpEntity<TradeOrderRequest> requestEntity = new HttpEntity<TradeOrderRequest>(tradeOrderRequest);

		HttpEntity<TradeOrder> response = restTemplate.exchange(
				"/v3/accounts/{accountId}/trades/{orderId}/orders", HttpMethod.PUT, requestEntity,
				TradeOrder.class, accountId, traderId);

		return response.getBody();
	}

	/* (non-Javadoc)
	 * @see sendaTrader76.bot.services.AccountService#closeOrder(java.lang.String, sendaTrader76.bot.dto.InstrumentType, java.lang.String)
	 */
	@Override
	public OrderResponse closeOrder(String accountId, InstrumentType instrumentType, String positionType) {

		OrderClose orderClose = new OrderClose();
		if ("SHORT".equals(positionType)) {
			orderClose.setShortUnits("ALL");
		} else {
			orderClose.setLongUnits("ALL");
		}

		// log.info(gson.toJson(orderClose));

		HttpEntity<OrderClose> requestEntity = new HttpEntity<OrderClose>(orderClose);

		HttpEntity<OrderResponse> response = restTemplate.exchange(
				"/v3/accounts/{accountId}/positions/{instrumentType}/close",
				HttpMethod.PUT, requestEntity, OrderResponse.class, accountId, instrumentType);

		return response.getBody();
	}

	/* (non-Javadoc)
	 * @see sendaTrader76.bot.services.AccountService#getOpenPosition(java.lang.String)
	 */
	@Override
	public OrderResponse getOpenPosition(String accountId) {

		HttpEntity<OrderResponse> response = restTemplate.getForEntity("/v3/accounts/{accountId}/openPositions", OrderResponse.class, accountId);

		return response.getBody();
	}
	
	/* (non-Javadoc)
	 * @see sendaTrader76.bot.services.AccountService#getOpenTrades(java.lang.String)
	 */
	@Override
	public OpenTrades getOpenTrades(String accountId) {

		HttpEntity<OpenTrades> response = restTemplate.getForEntity("/v3/accounts/{accountId}/openTrades", OpenTrades.class, accountId);

		return response.getBody();
	}
}