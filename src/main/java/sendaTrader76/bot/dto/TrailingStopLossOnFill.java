package sendaTrader76.bot.dto;

public class TrailingStopLossOnFill {

	// #
	// # The distance (in price units) from the Trade’s fill price that the
	// # Trailing Stop Loss Order will be triggered at.
	// #
	private String distance;

	// #
	// # The time in force for the created Trailing Stop Loss Order. This may
	// only
	// # be GTC, GTD or GFD.
	// #
	private String timeInForce;

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(String timeInForce) {
		this.timeInForce = timeInForce;
	}

}
