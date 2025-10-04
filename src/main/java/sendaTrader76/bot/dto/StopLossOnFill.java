package sendaTrader76.bot.dto;

import com.oanda.v20.order.TimeInForce;
import com.oanda.v20.pricing_common.PriceValue;
import com.oanda.v20.transaction.StopLossDetails;

public class StopLossOnFill {
    private String price;
    private String timeInForce;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public StopLossDetails toOandaStopLossDetails() {
        return new StopLossDetails()
                .setPrice(new PriceValue(price))
                .setTimeInForce(TimeInForce.valueOf(timeInForce));
    }
}