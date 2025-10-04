package sendaTrader76.bot.dto;

import com.oanda.v20.transaction.StopLossOrderTransaction;
import com.oanda.v20.transaction.Transaction;

public class TradeOrder {
    private StopLossOrderTransaction stopLossOrderTransaction;

    public TradeOrder(Transaction stopLossOrderTransaction) {
        if (stopLossOrderTransaction instanceof StopLossOrderTransaction) {
            this.stopLossOrderTransaction = (StopLossOrderTransaction) stopLossOrderTransaction;
        }
    }

    public StopLossOrderTransaction getStopLossOrderTransaction() {
        return stopLossOrderTransaction;
    }

    public void setStopLossOrderTransaction(StopLossOrderTransaction stopLossOrderTransaction) {
        this.stopLossOrderTransaction = stopLossOrderTransaction;
    }
}