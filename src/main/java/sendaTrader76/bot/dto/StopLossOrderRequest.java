package sendaTrader76.bot.dto;

import com.oanda.v20.order.TimeInForce;
import com.oanda.v20.pricing_common.PriceValue;
import com.oanda.v20.trade.TradeID;

public class StopLossOrderRequest {
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public com.oanda.v20.order.StopLossOrderRequest toOandaOrderRequest() {
        return new com.oanda.v20.order.StopLossOrderRequest()
                .setPrice(new PriceValue(order.getStopLossOnFill().getPrice()))
                .setTimeInForce(TimeInForce.valueOf(order.getTimeInForce()))
                .setTradeID(new TradeID(order.getTradeId()));
    }
}