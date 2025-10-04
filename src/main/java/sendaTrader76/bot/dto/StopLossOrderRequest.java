package sendaTrader76.bot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StopLossOrderRequest {

	private StopLossOrder order;

	public StopLossOrder getOrder() {
		return order;
	}

	public void setOrder(StopLossOrder order) {
		this.order = order;
	}

}
