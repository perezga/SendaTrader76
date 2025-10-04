package sendaTrader76.bot.dto;

import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderPositionFill;
import com.oanda.v20.order.TimeInForce;
import com.oanda.v20.primitives.DecimalNumber;
import com.oanda.v20.primitives.InstrumentName;

public class OrderRequest {
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public com.oanda.v20.order.OrderRequest toOandaOrderRequest() {
        return new MarketOrderRequest()
                .setInstrument(new InstrumentName(order.getInstrument()))
                .setUnits(new DecimalNumber(order.getUnits()))
                .setTimeInForce(TimeInForce.valueOf(order.getTimeInForce()))
                .setPositionFill(OrderPositionFill.DEFAULT);
    }
}