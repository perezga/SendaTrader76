package sendaTrader76.bot.stubs;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import sendaTrader76.bot.services.AccountServiceOanda;

public class AccountServiceStub extends AccountServiceOanda {

	private static final Logger log = LoggerFactory.getLogger(AccountServiceStub.class);

	private double askPrice = 0;
	private double bidPrice = 0;
	private boolean longPosition = false;
	private boolean shortPosition = false;
	private BigDecimal stopLossPrice = BigDecimal.ZERO;

	private List<Trades> trades = new ArrayList<Trades>();

	public void setAskPrice(double askPrice) {
		this.askPrice = askPrice;
		if (this.shortPosition && askPrice > stopLossPrice.doubleValue()) {
			trades.remove(0);
			this.shortPosition = false;

			log.info("close at " + askPrice + "  stopLoss " + stopLossPrice);
		}
	}

	public void setBidPrice(double bidPrice) {
		this.bidPrice = bidPrice;
		if (this.longPosition && this.bidPrice > this.stopLossPrice.doubleValue()) {
			this.trades.remove(0);
			this.longPosition = false;

			log.info("close at " + bidPrice + "  stopLoss " + stopLossPrice);
		}
	}

	public PricesResponse getPrice(String accountId, InstrumentType instrument) {
		log.info("");
		return null;
	}

	public TradeOrder postTrade(String accountId, TradeOrderRequest tradeOrderRequest, String traderId) {

		stopLossPrice = new BigDecimal(tradeOrderRequest.getStopLoss().getPrice());

		return null;
	}

	public OrderResponse postOrder(String accountId, OrderRequest orderRequest) {

		stopLossPrice = new BigDecimal(orderRequest.getOrder().getStopLossOnFill().getPrice());

		OrderFillTransaction orderFillTransaction = new OrderFillTransaction();
		orderFillTransaction.setId("1");
		if (orderRequest.getOrder().getUnits() == "10000") {
			orderFillTransaction.setPrice(BigDecimal.valueOf(askPrice));
			longPosition = true;
		} else {
			orderFillTransaction.setPrice(BigDecimal.valueOf(bidPrice));
			shortPosition = true;
		}

		Trades trade = new Trades();
		trade.setPrice(orderFillTransaction.getPrice().toString());
		this.trades.add(trade);

		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setOrderFillTransaction(orderFillTransaction);

		OrderCreateTransaction oct = new OrderCreateTransaction();
		oct.setId("2");
		orderResponse.setOrderCreateTransaction(oct);

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
}