package sendaTrader76.bot.stubs;

import com.oanda.v20.pricing_common.PriceValue;
import com.oanda.v20.trade.Trade;
import com.oanda.v20.transaction.OrderFillTransaction;
import com.oanda.v20.transaction.TransactionID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sendaTrader76.bot.dto.OpenTrades;
import sendaTrader76.bot.dto.OrderRequest;
import sendaTrader76.bot.dto.OrderResponse;
import sendaTrader76.bot.dto.TradeOrder;
import sendaTrader76.bot.dto.TradeOrderRequest;
import sendaTrader76.bot.services.AccountServiceOanda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountServiceStub extends AccountServiceOanda {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceStub.class);

    private double askPrice = 0;
    private double bidPrice = 0;
    private boolean longPosition = false;
    private boolean shortPosition = false;
    private BigDecimal stopLossPrice = BigDecimal.ZERO;

    private List<Trade> trades = new ArrayList<>();

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
        if (this.longPosition && this.bidPrice < this.stopLossPrice.doubleValue()) {
            this.trades.remove(0);
            this.longPosition = false;
            log.info("close at " + bidPrice + "  stopLoss " + stopLossPrice);
        }
    }

    @Override
    public OrderResponse postOrder(String accountId, OrderRequest orderRequest) {
        stopLossPrice = new BigDecimal(orderRequest.getOrder().getStopLossOnFill().getPrice());

        OrderFillTransaction orderFillTransaction = new OrderFillTransaction();
        orderFillTransaction.setId(new TransactionID("1"));
        if (orderRequest.getOrder().getUnits().equals("10000")) {
            orderFillTransaction.setPrice(new PriceValue(String.valueOf(askPrice)));
            longPosition = true;
        } else {
            orderFillTransaction.setPrice(new PriceValue(String.valueOf(bidPrice)));
            shortPosition = true;
        }

        Trade trade = new Trade();
        trade.setPrice(orderFillTransaction.getPrice());
        this.trades.add(trade);

        return new OrderResponse(orderFillTransaction);
    }

    @Override
    public TradeOrder postTrade(String accountId, TradeOrderRequest tradeOrderRequest, String traderId) {
        stopLossPrice = new BigDecimal(tradeOrderRequest.getStopLossOnFill().getPrice());
        return null;
    }

    @Override
    public OpenTrades getOpenTrades(String accountId) {
        return new OpenTrades(trades);
    }
}