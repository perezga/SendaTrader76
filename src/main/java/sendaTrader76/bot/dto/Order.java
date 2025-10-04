package sendaTrader76.bot.dto;

public class Order {
	private String positionFill;

	private String timeInForce;

	private String instrument;

	private String type;

	private String units;

	private StopLossOnFill stopLossOnFill;
	
	//private TrailingStopLossOnFill trailingStopLossOnFill;

	public String getPositionFill() {
		return positionFill;
	}

	public StopLossOnFill getStopLossOnFill() {
		return stopLossOnFill;
	}

	public void setStopLossOnFill(StopLossOnFill stopLossOnFill) {
		this.stopLossOnFill = stopLossOnFill;
	}

	public void setPositionFill(String positionFill) {
		this.positionFill = positionFill;
	}

	public String getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(String timeInForce) {
		this.timeInForce = timeInForce;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return "ClassPojo [positionFill = " + positionFill + ", timeInForce = " + timeInForce + ", instrument = "
				+ instrument + ", type = " + type + ", units = " + units + "]";
	}

	
}
