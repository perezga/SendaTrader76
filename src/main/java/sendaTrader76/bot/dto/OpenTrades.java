package sendaTrader76.bot.dto;

import com.oanda.v20.trade.Trade;
import java.util.List;

public class OpenTrades {
    private List<Trade> trades;

    public OpenTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
}