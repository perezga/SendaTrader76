package sendaTrader76.bot.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StopLossDetail {
	private String clientExtensions;
	private String gdtTime;
	private String price;
	private String timeInForce;
	public String getClientExtensions() {
		return clientExtensions;
	}
	public void setClientExtensions(String clientExtensions) {
		this.clientExtensions = clientExtensions;
	}
	public String getGdtTime() {
		return gdtTime;
	}
	public void setGdtTime(String gdtTime) {
		this.gdtTime = gdtTime;
	}
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
}
