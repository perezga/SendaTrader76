package sendaTrader76.bot.services;

import sendaTrader76.bot.dto.InstrumentType;
import sendaTrader76.bot.dto.OpenTrades;
import sendaTrader76.bot.dto.OrderRequest;
import sendaTrader76.bot.dto.OrderResponse;
import sendaTrader76.bot.dto.PricesResponse;
import sendaTrader76.bot.dto.StopLossOrderRequest;
import sendaTrader76.bot.dto.TradeOrder;
import sendaTrader76.bot.dto.TradeOrderRequest;

public interface AccountService {

	PricesResponse getPrice(String accountId, InstrumentType instrument);

	OrderResponse postOrder(String accountId, OrderRequest orderRequest);

	void postOrder(String accountId, StopLossOrderRequest stopLossOrderRequest);

	OrderResponse putOrder(String accountId, StopLossOrderRequest stopLossOrderRequest, String orderId);

	TradeOrder postTrade(String accountId, TradeOrderRequest tradeOrderRequest, String traderId);

	OrderResponse closeOrder(String accountId, InstrumentType instrumentType, String positionType);

	OrderResponse getOpenPosition(String accountId);

	OpenTrades getOpenTrades(String accountId);

}