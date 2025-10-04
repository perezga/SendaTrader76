package sendaTrader76.bot.dto;

public class StopLossOrder {
	private String type;
	private String tradeID;
	private String price;
	private String timeInForce;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTradeID() {
		return tradeID;
	}
	public void setTradeID(String tradeID) {
		this.tradeID = tradeID;
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
