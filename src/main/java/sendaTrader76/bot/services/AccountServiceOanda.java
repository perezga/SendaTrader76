package sendaTrader76.bot.services;

import com.oanda.v20.Context;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.order.*;
import com.oanda.v20.pricing.PricingGetResponse;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.trade.TradeSetDependentOrdersRequest;
import com.oanda.v20.trade.TradeSetDependentOrdersResponse;
import com.oanda.v20.trade.TradeSpecifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sendaTrader76.bot.dto.InstrumentType;
import sendaTrader76.bot.dto.OpenTrades;
import sendaTrader76.bot.dto.OrderRequest;
import sendaTrader76.bot.dto.OrderResponse;
import sendaTrader76.bot.dto.PricesResponse;
import sendaTrader76.bot.dto.StopLossOrderRequest;
import sendaTrader76.bot.dto.TradeOrder;
import sendaTrader76.bot.dto.TradeOrderRequest;
import com.oanda.v20.position.PositionCloseRequest;
import com.oanda.v20.position.PositionCloseResponse;
import com.oanda.v20.trade.TradeListOpenResponse;

import java.util.Collections;

@Component
public class AccountServiceOanda implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceOanda.class);

    @Autowired
    private Context oandaContext;

    @Autowired
    private AccountID oandaAccountId;

    @Override
    public PricesResponse getPrice(String accountId, InstrumentType instrument) {
        try {
            PricingGetResponse response = oandaContext.pricing.get(
                    new AccountID(accountId),
                    Collections.singletonList(new InstrumentName(instrument.name()))
            );
            return new PricesResponse(response.getPrices());
        } catch (RequestException | ExecuteException e) {
            log.error("Error getting price", e);
            return null;
        }
    }

    @Override
    public OrderResponse postOrder(String accountId, OrderRequest orderRequest) {
        try {
            OrderCreateRequest request = new OrderCreateRequest(new AccountID(accountId));
            request.setOrder(orderRequest.toOandaOrderRequest());
            OrderCreateResponse response = oandaContext.order.create(request);
            return new OrderResponse(response.getOrderFillTransaction());
        } catch (RequestException | ExecuteException e) {
            log.error("Error posting order", e);
            return null;
        }
    }

    @Override
    public void postOrder(String accountId, StopLossOrderRequest stopLossOrderRequest) {
        try {
            OrderCreateRequest request = new OrderCreateRequest(new AccountID(accountId));
            request.setOrder(stopLossOrderRequest.toOandaOrderRequest());
            oandaContext.order.create(request);
        } catch (RequestException | ExecuteException e) {
            log.error("Error posting stop loss order", e);
        }
    }

    @Override
    public OrderResponse putOrder(String accountId, StopLossOrderRequest stopLossOrderRequest, String orderId) {
        try {
            OrderReplaceRequest request = new OrderReplaceRequest(new AccountID(accountId), new OrderSpecifier(orderId));
            request.setOrder(stopLossOrderRequest.toOandaOrderRequest());
            OrderReplaceResponse response = oandaContext.order.replace(request);
            return new OrderResponse(response.getOrderFillTransaction());
        } catch (RequestException | ExecuteException e) {
            log.error("Error putting order", e);
            return null;
        }
    }

    @Override
    public TradeOrder postTrade(String accountId, TradeOrderRequest tradeOrderRequest, String traderId) {
        try {
            TradeSetDependentOrdersRequest request = new TradeSetDependentOrdersRequest(new AccountID(accountId), new TradeSpecifier(traderId));
            request.setStopLoss(tradeOrderRequest.getStopLossOnFill().toOandaStopLossDetails());
            TradeSetDependentOrdersResponse response = oandaContext.trade.setDependentOrders(request);
            return new TradeOrder(response.getStopLossOrderTransaction());
        } catch (RequestException | ExecuteException e) {
            log.error("Error posting trade", e);
            return null;
        }
    }

    @Override
    public OrderResponse closeOrder(String accountId, InstrumentType instrumentType, String positionType) {
        try {
            PositionCloseRequest request = new PositionCloseRequest(new AccountID(accountId), new InstrumentName(instrumentType.name()));
            if ("SHORT".equals(positionType)) {
                request.setShortUnits("ALL");
            } else {
                request.setLongUnits("ALL");
            }
            PositionCloseResponse response = oandaContext.position.close(request);
            return new OrderResponse(response.getShortOrderFillTransaction() != null ? response.getShortOrderFillTransaction() : response.getLongOrderFillTransaction());
        } catch (RequestException | ExecuteException e) {
            log.error("Error closing order", e);
            return null;
        }
    }

    @Override
    public OrderResponse getOpenPosition(String accountId) {
        // This method is deprecated in v20. Use getOpenTrades instead.
        return null;
    }

    @Override
    public OpenTrades getOpenTrades(String accountId) {
        try {
            TradeListOpenResponse response = oandaContext.trade.listOpen(new AccountID(accountId));
            return new OpenTrades(response.getTrades());
        } catch (RequestException | ExecuteException e) {
            log.error("Error getting open trades", e);
            return null;
        }
    }
}