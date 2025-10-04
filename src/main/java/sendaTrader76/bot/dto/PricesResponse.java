package sendaTrader76.bot.dto;

import com.oanda.v20.pricing.ClientPrice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PricesResponse {
    private List<Price> prices;

    public PricesResponse(List<ClientPrice> clientPrices) {
        if (clientPrices != null) {
            this.prices = clientPrices.stream().map(Price::new).collect(Collectors.toList());
        } else {
            this.prices = Collections.emptyList();
        }
    }

    public PricesResponse(java.util.Collection<com.oanda.v20.pricing.ClientPrice> clientPrices) {
        if (clientPrices != null) {
            this.prices = clientPrices.stream().map(Price::new).collect(Collectors.toList());
        } else {
            this.prices = Collections.emptyList();
        }
    }

    public PricesResponse() {
        this.prices = Collections.emptyList();
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    @Override
    public String toString() {
        return "ClassPojo [prices = " + prices + "]";
    }
}