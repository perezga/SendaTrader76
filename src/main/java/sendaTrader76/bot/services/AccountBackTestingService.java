package sendaTrader76.bot.services;

import com.oanda.v20.transaction.OrderFillTransaction;
import com.oanda.v20.transaction.TransactionID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sendaTrader76.bot.dto.*;
import com.oanda.v20.pricing_common.PriceValue;
import com.oanda.v20.trade.Trade;
import com.oanda.v20.trade.TradeID;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private List<Trade> trades = new ArrayList<>();

    @Autowired
    private TransactionStreamService transactionStreamService;

    @Value("${strader76.websocket.test.topic.positions}")
    private String webSocketTestTopicPositions;

    private static final String STOPLOSS_TRIGERED = "{\"accountBalance\":\"739.4512\",\"accountID\":\"101-004-4267188-001\",\"batchID\":\"7402\",\"financing\":\"-0.0024\",\"id\":\"7402\",\"instrument\":\"EUR_USD\",\"orderID\":\"7401\",\"pl\":\"-3.1052\",\"price\":\"{price}\",\"reason\":\"STOP_LOSS_ORDER\",\"time\":\"{time}\",\"tradesClosed\":[{\"financing\":\"-0.0024\",\"realizedPL\":\"-3.1052\",\"tradeID\":\"7400\",\"units\":\"10000\"}],\"type\":\"ORDER_FILL\",\"units\":\"{units}\",\"userID\":0}";
    private static final String TRAILLING_STOP_REPLACEMENT = "{\"accountID\":\"101-004-4267188-001\",\"batchID\":\"7406\",\"cancellingTransactionID\":\"7406\",\"id\":\"7407\",\"price\":\"{price}\",\"reason\":\"REPLACEMENT\",\"replacesOrderID\":\"7405\",\"time\":\"{time}\",\"timeInForce\":\"GTC\",\"tradeID\":\"7404\",\"triggerCondition\":\"TRIGGER_DEFAULT\",\"type\":\"STOP_LOSS_ORDER\",\"userID\":4267188}";

    public void setAskPrice(double askPrice, ZonedDateTime priceDate) throws IOException {
        this.askPrice = askPrice;
        this.date = priceDate;
        if (this.shortPosition && askPrice > this.stopLossPrice.doubleValue()) {
            this.trades.remove(0);
            this.shortPosition = false;

            this.partialPrice = this.openPrice.subtract(BigDecimal.valueOf(askPrice));
            this.totalPrice = this.totalPrice.add(this.partialPrice);

            log.info("close at " + askPrice + "  stopLoss " + this.stopLossPrice);
            String transaction = STOPLOSS_TRIGERED.replace("{price}", "" + askPrice).replace("{time}", "" + this.date).replace("{units}", "-10000");
            transactionStreamService.processTransaction(transaction, webSocketTestTopicPositions);
        }
    }

    public void setBidPrice(double bidPrice, ZonedDateTime priceDate) throws IOException {
        this.bidPrice = bidPrice;
        this.date = priceDate;
        if (this.longPosition && bidPrice < this.stopLossPrice.doubleValue()) {
            this.trades.remove(0);
            this.longPosition = false;

            this.partialPrice = BigDecimal.valueOf(askPrice).subtract(this.openPrice);
            this.totalPrice = this.totalPrice.add(this.partialPrice);

            log.info("close at " + bidPrice + "  stopLoss " + this.stopLossPrice);
            String transaction = STOPLOSS_TRIGERED.replace("{price}", "" + bidPrice).replace("{time}", "" + this.date).replace("{units}", "10000");
            transactionStreamService.processTransaction(transaction, webSocketTestTopicPositions);
        }
    }

    public PricesResponse getPrice(String accountId, InstrumentType instrument) {
        log.info("");
        return null;
    }

    public TradeOrder postTrade(String accountId, TradeOrderRequest tradeOrderRequest, String traderId) {
        this.stopLossPrice = new BigDecimal(tradeOrderRequest.getStopLossOnFill().getPrice());
        String transaction = TRAILLING_STOP_REPLACEMENT.replace("{price}", "" + this.stopLossPrice).replace("{time}", "" + this.date);
        try {
            transactionStreamService.processTransaction(transaction, webSocketTestTopicPositions);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public OrderResponse postOrder(String accountId, OrderRequest orderRequest) {
        this.stopLossPrice = new BigDecimal(orderRequest.getOrder().getStopLossOnFill().getPrice());

        OrderFillTransaction orderFillTransaction = new OrderFillTransaction();
        orderFillTransaction.setId(new TransactionID("1"));
        if (orderRequest.getOrder().getUnits().equals("10000")) {
            orderFillTransaction.setPrice(new PriceValue(String.valueOf(askPrice)));
            this.longPosition = true;
        } else {
            orderFillTransaction.setPrice(new PriceValue(String.valueOf(bidPrice)));
            this.shortPosition = true;
        }

        openPrice = orderFillTransaction.getPrice().bigDecimalValue();
        Trade trade = new Trade();
        trade.setPrice(orderFillTransaction.getPrice());
        this.trades.add(trade);

        return new OrderResponse(orderFillTransaction);
    }

    public OpenTrades getOpenTrades(String accountId) {
        return new OpenTrades(trades);
    }

    public void postOrder(String accountId, StopLossOrderRequest stopLossOrderRequest) {
        log.info("");
    }

    public OrderResponse closeOrder(String accountId, InstrumentType instrumentType, String positionType) {
        return null;
    }

    @Override
    public OrderResponse putOrder(String accountId, StopLossOrderRequest stopLossOrderRequest, String orderId) {
        return null;
    }

    @Override
    public OrderResponse getOpenPosition(String accountId) {
        return null;
    }
}