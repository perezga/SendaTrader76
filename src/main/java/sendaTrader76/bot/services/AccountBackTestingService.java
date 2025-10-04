package sendaTrader76.bot.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import sendaTrader76.bot.dto.InstrumentType;
import sendaTrader76.bot.dto.OpenTrades;
import sendaTrader76.bot.dto.OrderCreateTransaction;
import sendaTrader76.bot.dto.OrderFillTransaction;
import sendaTrader76.bot.dto.OrderRequest;
import sendaTrader76.bot.dto.OrderResponse;
import sendaTrader76.bot.dto.PricesResponse;
import sendaTrader76.bot.dto.StopLossOrderRequest;
import sendaTrader76.bot.dto.TradeOrder;
import sendaTrader76.bot.dto.TradeOrderRequest;
import sendaTrader76.bot.dto.Trades;

@Component
public class AccountBackTestingService implements AccountService {

	private static final Logger log = LoggerFactory.getLogger(AccountBackTestingService.class);

	private BigDecimal openPrice = BigDecimal.ZERO;
	private BigDecimal totalPrice = BigDecimal.ZERO;
	private BigDecimal partialPrice = BigDecimal.ZERO;
	private double askPrice = 0;
	private double bidPrice = 0;
	private boolean longPosition = false;
	private boolean shortPosition = false;
	private BigDecimal stopLossPrice = BigDecimal.ZERO;
	private ZonedDateTime date;

	private List<Trades> trades = new ArrayList<Trades>();

	@Autowired
	private TransactionStreamService transactionStreamService;
	
	@Value("${strader76.websocket.test.topic.positions}")
	private String webSocketTestTopicPositions;

	private static final String MARKET_ORDER_CLIENT = "{\"accountID\":\"101-004-4267188-001\",\"batchID\":\"7399\",\"id\":\"7399\",\"instrument\":\"EUR_USD\",\"positionFill\":\"DEFAULT\",\"reason\":\"CLIENT_ORDER\",\"stopLossOnFill\":{\"price\":\"1.06287\",\"timeInForce\":\"GTC\"},\"time\":\"2016-11-17T22:40:31.845249548Z\",\"timeInForce\":\"FOK\",\"type\":\"MARKET_ORDER\",\"units\":\"-10000\",\"userID\":4267188}";
	private static final String MARKET_ORDER_OPEN_TRADE = "{\"accountBalance\":\"742.5588\",\"accountID\":\"101-004-4267188-001\",\"batchID\":\"7399\",\"financing\":\"0.0000\",\"id\":\"7400\",\"instrument\":\"EUR_USD\",\"orderID\":\"7399\",\"pl\":\"0.0000\",\"price\":\"{price}\",\"reason\":\"MARKET_ORDER\",\"time\":\"{time}\",\"tradeOpened\":{\"tradeID\":\"7400\",\"units\":\"10000\"},\"type\":\"ORDER_FILL\",\"units\":\"{units}\",\"userID\":4267188}";
	private static final String MARKET_ORDER_STOPLOSS = "{\"accountID\":\"101-004-4267188-001\",\"batchID\":\"7399\",\"id\":\"7401\",\"price\":\"1.06287\",\"reason\":\"ON_FILL\",\"time\":\"2016-11-17T22:40:31.845249548Z\",\"timeInForce\":\"GTC\",\"tradeID\":\"7400\",\"triggerCondition\":\"TRIGGER_DEFAULT\",\"type\":\"STOP_LOSS_ORDER\",\"userID\":4267188}";

	private static final String STOPLOSS_TRIGERED = "{\"accountBalance\":\"739.4512\",\"accountID\":\"101-004-4267188-001\",\"batchID\":\"7402\",\"financing\":\"-0.0024\",\"id\":\"7402\",\"instrument\":\"EUR_USD\",\"orderID\":\"7401\",\"pl\":\"-3.1052\",\"price\":\"{price}\",\"reason\":\"STOP_LOSS_ORDER\",\"time\":\"{time}\",\"tradesClosed\":[{\"financing\":\"-0.0024\",\"realizedPL\":\"-3.1052\",\"tradeID\":\"7400\",\"units\":\"10000\"}],\"type\":\"ORDER_FILL\",\"units\":\"{units}\",\"userID\":0}";

	private static final String TRAILLING_STOP_CANCEL = "{\"accountID\":\"101-004-4267188-001\",\"batchID\":\"7406\",\"id\":\"7406\",\"orderID\":\"7405\",\"reason\":\"CLIENT_REQUEST_REPLACED\",\"replacedByOrderID\":\"7407\",\"time\":\"2016-11-17T23:48:21.214550575Z\",\"type\":\"ORDER_CANCEL\",\"userID\":4267188}";
	private static final String TRAILLING_STOP_REPLACEMENT = "{\"accountID\":\"101-004-4267188-001\",\"batchID\":\"7406\",\"cancellingTransactionID\":\"7406\",\"id\":\"7407\",\"price\":\"{price}\",\"reason\":\"REPLACEMENT\",\"replacesOrderID\":\"7405\",\"time\":\"{time}\",\"timeInForce\":\"GTC\",\"tradeID\":\"7404\",\"triggerCondition\":\"TRIGGER_DEFAULT\",\"type\":\"STOP_LOSS_ORDER\",\"userID\":4267188}";

	public void setAskPrice(double askPrice, ZonedDateTime priceDate) throws JsonParseException, JsonMappingException, IOException {
		this.askPrice = askPrice;
		this.date = priceDate;
		if (this.shortPosition && askPrice > this.stopLossPrice.doubleValue()) {
			this.trades.remove(0);
			this.shortPosition = false;

			this.partialPrice = this.openPrice.subtract(BigDecimal.valueOf(askPrice));
			this.totalPrice = this.totalPrice.add(this.partialPrice);

			log.info("close at " + askPrice + "  stopLoss " + this.stopLossPrice);
			// template.convertAndSend("/topic/positions",
			// STOPLOSS_TRIGERED.replace("{price}", "" +
			// askPrice).replace("{time}",
			// ""+this.date.truncatedTo(ChronoUnit.MINUTES).toInstant().toEpochMilli()));
			// template.convertAndSend("/topic/positions", "{\"partial\":\"" +
			// this.partialPrice.toString() + "\", \"total\":\"" +
			// this.totalPrice.toString() + "\"}");

			String transaction = STOPLOSS_TRIGERED.replace("{price}", "" + askPrice).replace("{time}", "" + this.date).replace("{units}", "-10000");

			transactionStreamService.processTransaction(transaction, webSocketTestTopicPositions);
		}
	}

	public void setBidPrice(double bidPrice, ZonedDateTime priceDate) throws JsonParseException, JsonMappingException, IOException {
		this.bidPrice = bidPrice;
		this.date = priceDate;
		if (this.longPosition && bidPrice < this.stopLossPrice.doubleValue()) {
			this.trades.remove(0);
			this.longPosition = false;

			this.partialPrice = BigDecimal.valueOf(askPrice).subtract(this.openPrice);
			this.totalPrice = this.totalPrice.add(this.partialPrice);

			log.info("close at " + bidPrice + "  stopLoss " + this.stopLossPrice);
			// template.convertAndSend("/topic/positions",
			// STOPLOSS_TRIGERED.replace("{price}", "" +
			// bidPrice).replace("{time}",
			// ""+this.date.truncatedTo(ChronoUnit.MINUTES).toInstant().toEpochMilli()));
			// template.convertAndSend("/topic/positions", "{\"partial\":\"" +
			// this.partialPrice.toString() + "\", \"total\":\"" +
			// this.totalPrice.toString() + "\"}");

			String transaction = STOPLOSS_TRIGERED.replace("{price}", "" + bidPrice).replace("{time}", "" + this.date).replace("{units}", "10000");
			;

			transactionStreamService.processTransaction(transaction, webSocketTestTopicPositions);
		}
	}

	public PricesResponse getPrice(String accountId, InstrumentType instrument) {
		log.info("");
		return null;
	}

	public TradeOrder postTrade(String accountId, TradeOrderRequest tradeOrderRequest, String traderId) {

		this.stopLossPrice = new BigDecimal(tradeOrderRequest.getStopLoss().getPrice());

		// template.convertAndSend("/topic/positions", TRAILLING_STOP_CANCEL);
		// template.convertAndSend("/topic/positions",
		// TRAILLING_STOP_REPLACEMENT.replace("{price}", "" +
		// this.stopLossPrice).replace("{time}",
		// ""+this.date.truncatedTo(ChronoUnit.MINUTES).toInstant().toEpochMilli()));
		String transaction = TRAILLING_STOP_REPLACEMENT.replace("{price}", "" + this.stopLossPrice).replace("{time}", "" + this.date);
		try {
			transactionStreamService.processTransaction(transaction, webSocketTestTopicPositions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public OrderResponse postOrder(String accountId, OrderRequest orderRequest) {

		this.stopLossPrice = new BigDecimal(orderRequest.getOrder().getStopLossOnFill().getPrice());

		OrderFillTransaction orderFillTransaction = new OrderFillTransaction();
		orderFillTransaction.setId("1");
		if (orderRequest.getOrder().getUnits() == "10000") {
			orderFillTransaction.setPrice(BigDecimal.valueOf(askPrice));
			this.longPosition = true;
		} else {
			orderFillTransaction.setPrice(BigDecimal.valueOf(bidPrice));
			this.shortPosition = true;
		}

		openPrice = orderFillTransaction.getPrice();

		Trades trade = new Trades();
		trade.setPrice(orderFillTransaction.getPrice().toString());
		this.trades.add(trade);

		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setOrderFillTransaction(orderFillTransaction);

		OrderCreateTransaction oct = new OrderCreateTransaction();
		oct.setId("2");
		orderResponse.setOrderCreateTransaction(oct);

		// template.convertAndSend("/topic/positions",
		// MARKET_ORDER_OPEN_TRADE.replace("{units}",
		// orderRequest.getOrder().getUnits()).replace("{price}",
		// orderFillTransaction.getPrice().toString()).replace("{time}",
		// ""+this.date.truncatedTo(ChronoUnit.MINUTES).toInstant().toEpochMilli()));
		String transaction = MARKET_ORDER_OPEN_TRADE.replace("{units}", orderRequest.getOrder().getUnits())
				.replace("{price}", orderFillTransaction.getPrice().toString()).replace("{time}", "" + this.date);
		try {
			transactionStreamService.processTransaction(transaction, webSocketTestTopicPositions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return orderResponse;
	}

	public OpenTrades getOpenTrades(String accountId) {

		OpenTrades openTrades = new OpenTrades();
		openTrades.setTrades(trades);

		return openTrades;
	}

	public void postOrder(String accountId, StopLossOrderRequest stopLossOrderRequest) {

		log.info("");
	}

	public OrderResponse closeOrder(String accountId, InstrumentType instrumentType, String positionType) {

		return null;
	}

	@Override
	public OrderResponse putOrder(String accountId, StopLossOrderRequest stopLossOrderRequest, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderResponse getOpenPosition(String accountId) {
		// TODO Auto-generated method stub
		return null;
	}
}