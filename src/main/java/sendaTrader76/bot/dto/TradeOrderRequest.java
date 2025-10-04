package sendaTrader76.bot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TradeOrderRequest {

	private StopLossDetail stopLoss;

	public StopLossDetail getStopLoss() {
		return stopLoss;
	}

	public void setStopLoss(StopLossDetail stopLoss) {
		this.stopLoss = stopLoss;
	}

}
