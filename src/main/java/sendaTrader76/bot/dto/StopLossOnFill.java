package sendaTrader76.bot.dto;

public class StopLossOnFill {
	private String timeInForce;
	private String price;

	public String getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(String timeInForce) {
		this.timeInForce = timeInForce;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
