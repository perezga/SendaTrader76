package sendaTrader76.bot.dto;

import com.oanda.v20.transaction.OrderFillTransaction;
import com.oanda.v20.transaction.Transaction;

public class OrderResponse {
    private OrderFillTransaction orderFillTransaction;

    public OrderResponse(Transaction orderFillTransaction) {
        if (orderFillTransaction instanceof OrderFillTransaction) {
            this.orderFillTransaction = (OrderFillTransaction) orderFillTransaction;
        }
    }

    public OrderFillTransaction getOrderFillTransaction() {
        return orderFillTransaction;
    }

    public void setOrderFillTransaction(OrderFillTransaction orderFillTransaction) {
        this.orderFillTransaction = orderFillTransaction;
    }
}