package sendaTrader76.bot.dto;

public class TradeOrderRequest {
    private StopLossOnFill stopLossOnFill;

    public StopLossOnFill getStopLossOnFill() {
        return stopLossOnFill;
    }

    public void setStopLossOnFill(StopLossOnFill stopLossOnFill) {
        this.stopLossOnFill = stopLossOnFill;
    }
}